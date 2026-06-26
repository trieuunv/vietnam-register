# Vietnam Register (VNR) - Vehicle Inspection Management System

A modern, comprehensive mono-repo solution for motor vehicle inspections and registration management in Vietnam. The system streamlines vehicle examination workflows, scheduling, data logging, and certificates issuing. It consists of three primary components: a Laravel-based RESTful API backend, a Vite + React web application for registry staff, and a Kotlin + Jetpack Compose Android application for vehicle owners and field operations.

---

## Technical Stack & Tags

[![Laravel](https://img.shields.io/badge/Laravel-9.19-FF2D20?style=for-the-badge&logo=laravel&logoColor=white)](https://laravel.com)
[![PHP](https://img.shields.io/badge/PHP-%5E8.0.2-777BB4?style=for-the-badge&logo=php&logoColor=white)](https://php.net)
[![React](https://img.shields.io/badge/React-19.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.7-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://typescriptlang.org)
[![Vite](https://img.shields.io/badge/Vite-6.2-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vite.dev)
[![Ant Design](https://img.shields.io/badge/Ant_Design-5.24-0170FE?style=for-the-badge&logo=ant-design&logoColor=white)](https://ant.design)
[![Redux Toolkit](https://img.shields.io/badge/Redux_Toolkit-2.6-764ABC?style=for-the-badge&logo=redux&logoColor=white)](https://redux-toolkit.js.org)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-M3-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/compose)
[![Dagger Hilt](https://img.shields.io/badge/Dagger_Hilt-DI-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/training/dependency-injection/hilt-android)
[![Retrofit](https://img.shields.io/badge/Retrofit-2.10-orange?style=for-the-badge)](https://square.github.io/retrofit/)

`#monorepo` `#vehicle-inspection` `#vietnam-register` `#laravel` `#react` `#typescript` `#kotlin` `#jetpack-compose` `#clean-architecture` `#mvi` `#mvvm` `#redux-toolkit` `#jwt` `#rest-api` `#room-db`

---

## Project Structure

```bash
vietnam-register/
├── laravel-backend/        # RESTful API Backend (Laravel 9 + JWT)
├── staff-frontend/         # Registry Staff Web Portal (React 19 + TypeScript + Vite)
└── android-frontend/       # Mobile App (Kotlin + Jetpack Compose + Clean Architecture)
```

---

## Features

### 1. Registry Staff Web Portal (`staff-frontend`)

- **Inspection Dashboard**: Track ongoing, completed, and canceled vehicle inspections at the registry center.
- **Evaluation Workflow**: Step-by-step assessment of vehicles against predefined inspection criteria (e.g., brakes, lights, emissions, safety).
- **Real-time Logic**: Automatically evaluates inspection results (`Passed`, `Failed`, `Conditional/Warning`) based on criteria checkmarks.
- **Notes & Logs**: Allow inspectors to add details, upload remarks, and edit notes on active certificates.
- **Billing & Fee Calculation**: Calculates fees in VND, registers receipt numbers, and handles certificate updates.

### 2. Mobile Application (`android-frontend`)

- **MVI/MVVM Clean Architecture**: Highly structured presentation, domain, and data layers.
- **Offline Support**: Local caching using Room database for robust offline operations.
- **Secure Storage**: Credentials and settings encrypted using Jetpack Security Crypto and DataStore Preferences.
- **Interactive UI**: Smooth user interface using Jetpack Compose with Material Design 3 and animated navigation bars.
- **API Client**: Clean, reactive networking powered by Retrofit, OkHttp, and Kotlinx Serialization.

### 3. RESTful API Backend (`laravel-backend`)

- **Authentication**: Secure authentication system utilizing JWT (`tymon/jwt-auth`) and Laravel Sanctum.
- **Business Logic**: Flexible service layer design with custom Helpers (e.g., Holiday calculation, files storage helper).
- **Data Validation**: Rigid FormRequests for ensuring incoming payloads match requirements.
- **Resources**: Structured API responses through Eloquent API Resources.

---

## Architecture & Contribution Guidelines

### Mobile Client Feature Workflow (`android-frontend`)

To maintain consistency across developers, follow these architectural steps when implementing a new feature in the Android app:

1.  **DTO Definitions**: Create data class requests and responses in the `dto` directory.
2.  **API Client**: Define endpoints in the `api` (ApiService) directory.
3.  **Data Source**: Implement remote and local data fetching in the `datasource` using the generated DTOs.
4.  **Local Storage**: Persist long-term data in the database (`db`) or user preferences (`pref`) using Android DataStore/Room.
5.  **Domain Model**: Map raw data into domain-level models under `domain.model`.
6.  **Repository Pattern**: Create repositories under `repository` that abstract the sources (local/remote) and expose data to domain logic.
7.  **State Management**: Create view models in `viewmodel`, fetching data from repositories and exposing UI state as `StateFlow`.
8.  **UI Screens**: Design the layout inside the `screen` directory using Compose and link it with the appropriate ViewModel.
9.  **Navigation**: Register the new routes in the `navigation` package.

---

## Setup & Installation

### Prerequisites

Ensure you have the following installed on your machine:

- [PHP >= 8.0.2](https://www.php.net/) & [Composer](https://getcomposer.org/)
- [Node.js >= 18.0](https://nodejs.org/) & [NPM](https://www.npmjs.com/)
- [Android Studio](https://developer.android.com/studio) (Ladybug or newer)
- MySQL or any compatible SQL Database engine

---

### Backend Configuration (`laravel-backend`)

1.  Navigate to the backend directory:
    ```bash
    cd laravel-backend
    ```
2.  Install composer dependencies:
    ```bash
    composer install
    ```
3.  Copy environment configurations and setup database details:
    ```bash
    cp .env.example .env
    ```
4.  Generate application keys & JWT secret key:
    ```bash
    php artisan key:generate
    php artisan jwt:secret
    ```
5.  Run migrations & seed initial registry databases:
    ```bash
    php artisan migrate --seed
    ```
6.  Start the development backend server:
    ```bash
    php artisan serve
    ```

---

### Staff Portal Configuration (`staff-frontend`)

1.  Navigate to the web portal directory:
    ```bash
    cd staff-frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Run the development server:
    ```bash
    npm run dev
    ```
4.  Open `http://localhost:5173` (or the port specified by Vite) in your browser.

---

### Android App Configuration (`android-frontend`)

1.  Open the project in Android Studio.
2.  Wait for Gradle to sync dependencies automatically.
3.  Ensure your Android emulator or physical device is connected (Min SDK: 24, Target SDK: 34).
4.  Configure backend base URL in `gradle.properties` or resource values to point to your backend IP (e.g. `http://10.0.2.2:8000/api` for emulator).
5.  Build and run the project (`app` configuration).

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.
