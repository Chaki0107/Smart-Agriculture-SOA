from fastapi import FastAPI, HTTPException
from services.weather_service import get_weather
from pathlib import Path
from pydantic import BaseModel

import joblib
import pandas as pd


app = FastAPI(
    title="Smart Agriculture - ML Prediction Service",
    description="Service REST de prediction agricole",
    version="1.0.0"
)


# ==================================================
# 1. Charger les modèles entraînés
# ==================================================

BASE_DIR = Path(__file__).resolve().parent
MODEL_DIR = BASE_DIR / "ml" / "models"

try:
    yield_artifact = joblib.load(
        MODEL_DIR / "yield_model.joblib"
    )
    disease_artifact = joblib.load(
        MODEL_DIR / "disease_model.joblib"
    )

    yield_model = yield_artifact["model"]
    yield_features = yield_artifact["features"]

    disease_model = disease_artifact["model"]
    disease_features = disease_artifact["features"]

except FileNotFoundError as exc:
    raise RuntimeError(
        f"Modèle introuvable : {exc}. "
        "Vérifie le dossier ml/models/."
    ) from exc


# ==================================================
# 2. Définir les données d'entrée
# ==================================================

class AgriculturalData(BaseModel):
    crop_type: str | None = None
    growth_stage: str | None = None
    field_zone: str | None = None

    air_temperature_c: float | None = None
    relative_humidity_pct: float | None = None
    soil_temperature_c: float | None = None
    soil_moisture_pct: float | None = None
    soil_ph: float | None = None
    soil_ec_ds_m: float | None = None

    nitrogen_mg_kg: float | None = None
    phosphorus_mg_kg: float | None = None
    potassium_mg_kg: float | None = None

    rainfall_mm: float | None = None
    solar_radiation_w_m2: float | None = None
    wind_speed_m_s: float | None = None
    leaf_wetness_pct: float | None = None
    evapotranspiration_mm: float | None = None


# ==================================================
# 3. Page d'accueil
# ==================================================

@app.get("/")
def home():
    return {
        "service": "ML Prediction Service",
        "status": "running",
        "description": "Service de prediction agricole",
        "endpoints": [
            "/health",
            "/weather",
            "/predict/yield",
            "/predict/disease",
            "/docs"
        ]
    }


# ==================================================
# 4. Vérifier que le service fonctionne
# ==================================================

@app.get("/health")
def health():
    return {
        "status": "ok",
        "models_loaded": True
    }


# ==================================================
# 5. Service météo existant
# ==================================================

@app.get("/weather")
def weather(
        latitude: float = 36.8065,
        longitude: float = 10.1815
):
    try:
        return get_weather(latitude, longitude)
    except RuntimeError as error:
        raise HTTPException(
            status_code=502,
            detail=str(error)
        ) from error


# ==================================================
# 6. Prédiction du rendement agricole
# ==================================================

@app.post("/predict/yield")
def predict_yield(data: AgriculturalData):
    try:
        input_data = data.model_dump()

        X = pd.DataFrame(
            [{
                feature: input_data.get(feature)
                for feature in yield_features
            }],
            columns=yield_features
        )

        prediction = float(yield_model.predict(X)[0])

        return {
            "model": "RandomForestRegressor",
            "target": "yield_t_ha",
            "predicted_yield_t_ha": round(prediction, 3)
        }

    except Exception as exc:
        raise HTTPException(
            status_code=500,
            detail=f"Erreur de prédiction du rendement : {str(exc)}"
        ) from exc


# ==================================================
# 7. Prédiction du risque de maladie
# ==================================================

@app.post("/predict/disease")
def predict_disease(data: AgriculturalData):
    try:
        input_data = data.model_dump()

        X = pd.DataFrame(
            [{
                feature: input_data.get(feature)
                for feature in disease_features
            }],
            columns=disease_features
        )

        predicted_class = int(disease_model.predict(X)[0])

        result = {
            "model": "RandomForestClassifier",
            "target": "disease_risk",
            "predicted_class": predicted_class
        }

        if hasattr(disease_model, "predict_proba"):
            probabilities = disease_model.predict_proba(X)[0]
            classes = list(
                disease_model.named_steps["classifier"].classes_
            )

            if 1 in classes:
                index = classes.index(1)
                result["class_1_probability"] = round(
                    float(probabilities[index]), 4
                )

        return result

    except Exception as exc:
        raise HTTPException(
            status_code=500,
            detail=f"Erreur de prédiction du risque : {str(exc)}"
        ) from exc
