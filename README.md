# MyHealthPassport 

MyHealthPassport is a cutting-edge Android application built with Jetpack Compose that securely stores, manages, and analyzes patients' medical data. It integrates AI agents, voice-based interaction, health analytics, and emergency assistance to provide users with a comprehensive digital health assistant.

![app_image](https://github.com/user-attachments/assets/9d781833-217e-4492-8fd1-b94b44f91119)

<p align="left">
  <a href="https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport">
    <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"/>
  </a>
  <a href="https://github.com/anuragkanojiya1/MyHealthPassport/releases">
    <img alt="Get it on GitHub" src="https://img.shields.io/badge/GET_IT_ON-GitHub_Releases-101010?style=for-the-badge&logo=github&logoColor=white&labelColor=101010" height="70"/>
  </a>
</p>

[![Google Play](https://img.shields.io/badge/Google_Play-Download-3DDC84?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport)
[![GitHub Releases](https://img.shields.io/badge/GitHub-Download_APK-101010?style=for-the-badge&logo=github&logoColor=white&labelColor=101010)](https://github.com/anuragkanojiya1/MyHealthPassport/releases)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

---

## 📋 Table of Contents

- [📲 Download & Releases](#-download--releases)
- [🚀 Key Features](#-key-features)
- [🛠 Tech Stack](#-tech-stack)
- [🏁 Getting Started](#-getting-started)
- [📄 License](#-license)

---

## 📲 Download & Releases

Download **MyHealthPassport** on your Android device directly from the Google Play Store or get the APK from GitHub Releases:

<p align="left">
  <a href="https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport">
    <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"/>
  </a>
  <a href="https://github.com/anuragkanojiya1/MyHealthPassport/releases">
    <img alt="Get it on GitHub" src="https://img.shields.io/badge/GET_IT_ON-GitHub_Releases-101010?style=for-the-badge&logo=github&logoColor=white&labelColor=101010" height="70"/>
  </a>
</p>

* 🛒 **Google Play Store Link:** [https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport](https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport)
* 📦 **GitHub Releases (APK):** [https://github.com/anuragkanojiya1/MyHealthPassport/releases](https://github.com/anuragkanojiya1/MyHealthPassport/releases)

---

## 🚀 Key Features

### 🤖 Intelligent AI Assistants
*   **Medical Report Analyzer:** Parses medical documents (Images) using **Gemini AI** to analyse or extract vital health metrics and automatically update your digital Medical ID.
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

- Android Device running **Android 8.0 (API level 26)** or higher, or an Android Emulator.
- Android Studio Ladybug (or newer) for building from source.

### Installation Options

- 🛒 **Download from Google Play Store:** [MyHealthPassport on Google Play](https://play.google.com/store/apps/details?id=com.anuragkanojiya.myhealthpassport)
- 📦 **Download APK from GitHub Releases:** [MyHealthPassport Releases](https://github.com/anuragkanojiya1/MyHealthPassport/releases)

### 🛠️ Building From Source

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
