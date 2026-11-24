# 🌱 Smart Agriculture – Service-Oriented Architecture (SOA)

## 1. Project Overview

**Smart Agriculture** is a Service-Oriented Architecture (SOA) solution designed to support farmers in monitoring agricultural activities, analyzing field conditions, predicting crop yields, and managing agricultural alerts.

The platform integrates IoT data collection, Machine Learning, GraphQL, and alert management through a centralized **MuleSoft Enterprise Service Bus (ESB)**.

A web-based dashboard provides farmers with a unified interface to interact with the different services.

## 2. System Architecture

The solution follows a service-oriented architecture in which each service provides specific business functionality.

The communication flow is:

**Farmer → React Dashboard → MuleSoft ESB → Backend Services → MuleSoft ESB → Dashboard**

The ESB acts as the central integration layer, routing requests to the appropriate services and aggregating their responses.

## 3. Main Components

| Component | Description | Technology |
|---|---|---|
| IoT Web Service | Collects and manages sensor readings | Java, Spring Boot |
| ML Prediction Service | Predicts agricultural yield and supports crop disease prediction | Python, FastAPI, Scikit-learn |
| Parcels GraphQL Service | Provides GraphQL access to agricultural parcels and related data | Java, Spring Boot, GraphQL |
| Alert Management Service | Manages alert rules, evaluates conditions, and generates alerts | Java, Spring Boot |
| Enterprise Service Bus | Routes and integrates requests between the dashboard and backend services | MuleSoft |
| User Dashboard | Provides an interface for monitoring and interacting with the platform | React, JavaScript, CSS |

## 4. Technologies Used

- **Backend:** Java, Spring Boot, Python, FastAPI
- **Machine Learning:** Scikit-learn, Random Forest
- **API Integration:** REST, GraphQL
- **ESB:** MuleSoft Anypoint Studio
- **Frontend:** React, JavaScript, HTML, CSS
- **Database:** Relational database
- **Testing:** Postman
- **Development Tools:** IntelliJ IDEA, Visual Studio Code, Anypoint Studio

## 5. Repository Structure

```text
Smart-Agriculture-SOA/
│
├── IoTWebService/
│   ├── src/
│   └── pom.xml
│
├── ParcellesGraphQLService/
│   ├── src/
│   └── pom.xml
│
├── MLPredService/
│   ├── main.py
│   ├── requirements.txt
│   ├── ml/
│   │   ├── train_yield.py
│   │   └── train_disease.py
│   └── services/
│       └── weather_service.py
│
├── alerteservice/
│   ├── src/
│   └── pom.xml
│
├── Smart-agri-dashboard/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── src/
│   └── main/
│       └── mule/
│           └── agriculture-api.xml
│
├── exchange-docs/
├── pom.xml
└── README.md
```

## 6. Installation and Execution

### Prerequisites

Make sure the following tools are installed:

- Java JDK
- Maven
- Python 3
- Node.js and npm
- Anypoint Studio
- A configured database

### 6.1. IoT Web Service

Navigate to the service directory:

```bash
cd IoTWebService
```

Run the application:

```bash
./mvnw spring-boot:run
```

Default port: `8081`

### 6.2. Machine Learning Prediction Service

Navigate to the ML service directory:

```bash
cd MLPredService
```

Create and activate a virtual environment:

```bash
python3 -m venv venv
source venv/bin/activate
```

Install dependencies:

```bash
pip install -r requirements.txt
```

Start the FastAPI application:

```bash
uvicorn main:app --reload --port 8000
```

Default port: `8000`

API documentation:

`http://localhost:8000/docs`

**Note:** Required datasets and trained model files must be available at the paths expected by the Python scripts. If model files are not included in the repository, generate them using the training scripts before running predictions.

### 6.3. Parcels GraphQL Service

Navigate to:

```bash
cd ParcellesGraphQLService
```

Run:

```bash
./mvnw spring-boot:run
```

### 6.4. Alert Management Service

Navigate to:

```bash
cd alerteservice
```

Run:

```bash
./mvnw spring-boot:run
```

### 6.5. React Dashboard

Navigate to:

```bash
cd Smart-agri-dashboard
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Open the local URL displayed in the terminal.

### 6.6. MuleSoft ESB

1. Open the Mule project in Anypoint Studio.
2. Configure the required backend service URLs and database connections.
3. Verify that the backend services are running.
4. Run the Mule application.
5. Send requests through the ESB endpoints rather than directly accessing backend services from the dashboard.

## 7. Communication Workflow

The platform follows this communication sequence:

1. The farmer interacts with the React dashboard.
2. The dashboard sends an HTTP request to the MuleSoft ESB.
3. The ESB identifies the appropriate backend service.
4. The selected service processes the request.
5. The service returns its response to the ESB.
6. The ESB forwards the response to the dashboard.
7. The dashboard displays the result to the farmer.

This architecture promotes modularity, interoperability, and separation of concerns.

## 8. Key Functionalities

- Agricultural sensor data collection and monitoring.
- Crop yield prediction using Machine Learning.
- Crop disease prediction.
- Agricultural parcel data access through GraphQL.
- Alert rule configuration and management.
- Automatic evaluation of agricultural conditions.
- Centralized service integration through MuleSoft ESB.
- Unified monitoring dashboard for farmers.

## 9. Future Improvements

- Integration of additional IoT sensors.
- Real-time notifications for critical agricultural conditions.
- Enhanced Machine Learning models.
- Deployment of the platform to a cloud environment.
- Advanced analytics and historical data visualization.

## 10. Academic Project

**Project:** Smart Agriculture – Service-Oriented Architecture

**Author:** Chahnez Naccache

**Academic Context:** Master's Degree – Data Science

This project demonstrates the implementation of a distributed agricultural information system using SOA principles, REST and GraphQL APIs, Machine Learning, and Enterprise Service Bus integration.