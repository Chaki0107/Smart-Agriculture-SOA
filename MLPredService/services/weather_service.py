import requests

WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast"


def get_weather(latitude: float, longitude: float):
    params = {
        "latitude": latitude,
        "longitude": longitude,
        "current": (
            "temperature_2m,"
            "relative_humidity_2m,"
            "precipitation,"
            "wind_speed_10m"
        ),
        "timezone": "Africa/Tunis"
    }

    try:
        response = requests.get(
            WEATHER_API_URL,
            params=params,
            timeout=10
        )

        response.raise_for_status()
        data = response.json()

        return {
            "location": {
                "latitude": latitude,
                "longitude": longitude
            },
            "time": data["current"]["time"],
            "temperature_c": data["current"]["temperature_2m"],
            "humidity_pct": data["current"]["relative_humidity_2m"],
            "precipitation_mm": data["current"]["precipitation"],
            "wind_speed_kmh": data["current"]["wind_speed_10m"]
        }

    except requests.RequestException as error:
        raise RuntimeError(
            f"Erreur lors de la récupération météo : {error}"
        ) from error

    except (KeyError, TypeError) as error:
        raise RuntimeError(
            "La réponse météo ne contient pas les données attendues."
        ) from error
