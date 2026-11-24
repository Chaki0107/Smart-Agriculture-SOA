import sys
# train_yield.py

from pathlib import Path

import joblib
import pandas as pd

from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestRegressor
from sklearn.impute import SimpleImputer
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder

# Ajouter la racine de MLPredictionService au chemin Python
ROOT_DIR = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT_DIR))

from database import engine


# 1. Configuration
MODEL_DIR = Path("ml/models")
MODEL_DIR.mkdir(parents=True, exist_ok=True)

TARGET = "yield_t_ha"

NUMERIC_FEATURES = [
    "air_temperature_c",
    "relative_humidity_pct",
    "soil_temperature_c",
    "soil_moisture_pct",
    "soil_ph",
    "soil_ec_ds_m",
    "nitrogen_mg_kg",
    "phosphorus_mg_kg",
    "potassium_mg_kg",
    "rainfall_mm",
    "solar_radiation_w_m2",
    "wind_speed_m_s",
    "evapotranspiration_mm",
]

CATEGORICAL_FEATURES = [
    "crop_type",
    "growth_stage",
    "field_zone",
]

FEATURES = NUMERIC_FEATURES + CATEGORICAL_FEATURES


# 2. Charger les données directement depuis PostgreSQL
query = f"""
SELECT {", ".join(FEATURES)}, {TARGET}
FROM sensor_data
WHERE {TARGET} IS NOT NULL
"""

with engine.connect() as connection:
    df = pd.read_sql_query(query, connection)

print("Nombre de lignes avec rendement connu :", len(df))

if df.empty:
    raise ValueError(
        "Aucune valeur de rendement disponible. "
        "Remplis yield_t_ha avec des rendements mesurés avant l'entraînement."
    )

# 3. Préparer les données
df[TARGET] = pd.to_numeric(df[TARGET], errors="coerce")
df = df.dropna(subset=[TARGET])

for col in NUMERIC_FEATURES:
    df[col] = pd.to_numeric(df[col], errors="coerce")

# Retirer les variables entièrement vides
numeric_features = [
    col for col in NUMERIC_FEATURES if df[col].notna().any()
]
categorical_features = [
    col for col in CATEGORICAL_FEATURES if df[col].notna().any()
]
features = numeric_features + categorical_features

if len(df) < 10:
    raise ValueError(
        "Moins de 10 observations étiquetées : "
        "collecte davantage de données avant de valider un modèle."
    )

X = df[features].copy()
y = df[TARGET]

# 4. Prétraitement
numeric_pipeline = Pipeline([
    ("imputer", SimpleImputer(strategy="median")),
])

categorical_pipeline = Pipeline([
    ("imputer", SimpleImputer(strategy="most_frequent")),
    ("encoder", OneHotEncoder(handle_unknown="ignore")),
])

preprocessor = ColumnTransformer([
    ("numeric", numeric_pipeline, numeric_features),
    ("categorical", categorical_pipeline, categorical_features),
])

# 5. Construire le modèle
model = Pipeline([
    ("preprocessor", preprocessor),
    ("regressor", RandomForestRegressor(
        n_estimators=300,
        min_samples_leaf=2,
        random_state=42,
        n_jobs=-1,
    )),
])

# 6. Séparer entraînement et test
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.20, random_state=42
)

# 7. Entraîner
model.fit(X_train, y_train)

# 8. Évaluer sur les données de test
predictions = model.predict(X_test)

mae = mean_absolute_error(y_test, predictions)
rmse = mean_squared_error(y_test, predictions) ** 0.5
r2 = r2_score(y_test, predictions) if len(y_test) >= 2 else float("nan")

print("\n--- Évaluation du modèle de rendement ---")
print(f"Observations d'entraînement : {len(X_train)}")
print(f"Observations de test        : {len(X_test)}")
print(f"MAE                         : {mae:.3f} t/ha")
print(f"RMSE                        : {rmse:.3f} t/ha")
print(f"R²                          : {r2:.3f}")

# 9. Sauvegarder le modèle et ses caractéristiques
artifact = {
    "model": model,
    "features": features,
    "target": TARGET,
}

joblib.dump(artifact, MODEL_DIR / "yield_model.joblib")
print("\nModèle enregistré dans models/yield_model.joblib")
