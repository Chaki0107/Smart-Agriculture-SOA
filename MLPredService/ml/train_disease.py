import sys
# train_disease.py

from pathlib import Path

import joblib
import pandas as pd

from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.impute import SimpleImputer
from sklearn.metrics import (
    accuracy_score,
    classification_report,
    confusion_matrix,
    roc_auc_score,
)
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

TARGET = "disease_risk"

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
    "leaf_wetness_pct",
    "evapotranspiration_mm",
]

CATEGORICAL_FEATURES = [
    "crop_type",
    "growth_stage",
    "field_zone",
]

FEATURES = NUMERIC_FEATURES + CATEGORICAL_FEATURES


# 2. Charger les données depuis PostgreSQL
query = f"""
SELECT {", ".join(FEATURES)}, {TARGET}
FROM sensor_data
WHERE {TARGET} IS NOT NULL
"""

with engine.connect() as connection:
    df = pd.read_sql_query(query, connection)

print("Nombre de lignes avec risque connu :", len(df))

if df.empty:
    raise ValueError(
        "Aucune étiquette de maladie disponible. "
        "Il faut renseigner disease_risk à partir d'observations fiables."
    )

# 3. Nettoyer les données
df[TARGET] = pd.to_numeric(df[TARGET], errors="coerce")
df = df.dropna(subset=[TARGET])

for col in NUMERIC_FEATURES:
    df[col] = pd.to_numeric(df[col], errors="coerce")

labels = set(df[TARGET].unique())

if not labels.issubset({0, 1}):
    raise ValueError(
        f"disease_risk contient les valeurs {sorted(labels)}. "
        "Ce script attend uniquement 0 et 1. "
        "Si c'est un score continu, utilise une régression."
    )

if len(df) < 10:
    raise ValueError(
        "Moins de 10 observations étiquetées : "
        "collecte davantage de données avant l'entraînement."
    )

if df[TARGET].nunique() < 2:
    raise ValueError(
        "Une seule classe est présente. "
        "Il faut des exemples des deux classes (0 et 1)."
    )

# Retirer les variables entièrement vides
numeric_features = [
    col for col in NUMERIC_FEATURES if df[col].notna().any()
]
categorical_features = [
    col for col in CATEGORICAL_FEATURES if df[col].notna().any()
]
features = numeric_features + categorical_features

X = df[features].copy()
y = df[TARGET].astype(int)

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

# 5. Séparer entraînement et test en conservant les proportions des classes
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.20,
    random_state=42,
    stratify=y,
)

# 6. Construire et entraîner le modèle
model = Pipeline([
    ("preprocessor", preprocessor),
    ("classifier", RandomForestClassifier(
        n_estimators=300,
        min_samples_leaf=2,
        class_weight="balanced",
        random_state=42,
        n_jobs=-1,
    )),
])

model.fit(X_train, y_train)

# 7. Évaluer
predictions = model.predict(X_test)

print("\n--- Évaluation du risque de maladie ---")
print(f"Accuracy : {accuracy_score(y_test, predictions):.3f}")
print("\nMatrice de confusion :")
print(confusion_matrix(y_test, predictions, labels=[0, 1]))
print("\nRapport de classification :")
print(classification_report(
    y_test,
    predictions,
    labels=[0, 1],
    zero_division=0,
))

if len(set(y_test)) == 2:
    probabilities = model.predict_proba(X_test)[:, 1]
    print(f"ROC-AUC : {roc_auc_score(y_test, probabilities):.3f}")

# 8. Sauvegarder
artifact = {
    "model": model,
    "features": features,
    "target": TARGET,
    "classes": model.named_steps["classifier"].classes_.tolist(),
}

joblib.dump(artifact, MODEL_DIR / "disease_model.joblib")
print("\nModèle enregistré dans models/disease_model.joblib")
