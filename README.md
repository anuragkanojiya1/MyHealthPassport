# MyHealthPassport

MyHealthPassport is a cutting-edge Android application built with Jetpack Compose that securely stores, manages, and analyzes patients' medical data. It integrates AI agents, voice-based interaction, health analytics, and emergency assistance to provide users with a comprehensive digital health assistant.

![app_image](https://github.com/user-attachments/assets/c7ee00ec-14bb-46a8-a71f-ef0a43645704)

---

## Table of Contents

- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [License](#-license)

## 🚀 Key Features

### 🤖 Intelligent AI Assistants
*   **Medical Report Analyzer:** Parses medical documents (Images) using **Gemini AI** to extract vital health metrics and automatically update your digital Medical ID.
*   **Personalized Health Coach:** A dedicated AI agent that provides custom diet and exercise recommendations based on your unique medical profile.
*   **Health Insights:** Real-time analysis of blood pressure and glucose trends with AI-generated recommendations.

### 🛡️ Privacy & Security
*   **Biometric Authentication:** Native integration with the **Biometric API** (Fingerprint/Face Unlock) to protect sensitive health data.
*   **Advanced Encryption:** Sensitive medical information is encrypted using **AES-256** encryption via `CryptoManager` integrated with the Android Keystore.
*   **Secure Auth:** Robust authentication powered by **Firebase Auth** and Google Identity Services.

### 📊 Health Analytics
*   **Dynamic Visualizations:** Interactive line charts for blood pressure and blood sugar tracking, and pie charts for medication distribution.
*   **Automated Tracking:** Syncs with your Medical ID to maintain a longitudinal history of your vitals.

### 🎙️ Voice & Accessibility
*   **Text-to-Speech (TTS):** Spoken health reports and analysis to make insights more accessible.
*   **Modern UI:** A responsive interface built with **Jetpack Compose (Material 3)** that adapts to different screen sizes.

### 📱 Home Screen Widget
*   **Jetpack Glance:** Stay updated with vital health stats directly from your home screen with beautifully designed widgets.

---

## 🛠 Tech Stack

- **UI:** Jetpack Compose, Material 3, Jetpack Glance (Widgets).
- **Architecture:** Clean Architecture + MVVM + Unidirectional Data Flow.
- **Dependency Injection:** Dagger-Hilt.
- **Networking:** Retrofit, OkHttp.
- **Local Database:** Room.
- **Cloud/Backend:** Firebase (Auth, Firestore, Storage).
- **AI/ML:** Google Gemini AI.
- **Security:** Biometric API, Android Keystore (AES Encryption).
- **Async:** Kotlin Coroutines & Flow.
- **Image Loading:** Coil.

---

## 🏁 Getting Started

### Prerequisites

- Android Studio Ladybug or later.
- An Android device with API 26+ and biometric support.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/anuragkanojiya1/MyHealthPassport.git
   ```
2. **Open the project in Android Studio.**
3. **Configure Firebase:** Add your `google-services.json` to the `app/` folder.
4. **API Keys:** Securely add your Gemini API key in the app settings or via `local.properties`.
5. **Build and Run.**

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

MyHealthPassport is designed to empower users with actionable health insights and personalized AI-driven recommendations. Enjoy managing your health with the power of AI at your fingertips!
