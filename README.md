# MyHealthPassport

MyHealthPassport is a cutting-edge Android application built with Jetpack Compose that securely stores, manages, and analyzes patients' medical data. It integrates AI agents, voice-based interaction, health analytics, and emergency assistance to provide users with a comprehensive digital health assistant.

![879shots_so](https://github.com/user-attachments/assets/89c5b347-b386-4e7a-b4b4-14353782fade)

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Multi-Agent System**  
  MyHealthPassport offers separate AI agents dedicated to various tasks:
  - **AI Symptom Checker:** Analyzes symptoms using a fine-tuned Gemini model with voice-based interaction.
  - **Medical Report Analyzer:** Extracts details from medical reports to update your Medical ID or provide direct analysis.
  - **Personalized Diet & Exercise Plan:** Leverages the Mistral AI agent to provide custom health recommendations based on your medical data.
  - **Additional Agents:** Other agents are available to support various aspects of health management.

- **AI-Generated Health Reports & Insights**  
  - **Dynamic Visualizations:**  
    - Line charts for tracking blood pressure and blood sugar trends.
    - A pie chart for visualizing medication usage.
  - **Detailed Analysis:** Users can tap on the "Insights" button under each chart to receive an in-depth analysis of trends and recommendations, powered by Gemini AI.
  
- **Voice-Based AI Interaction**  
  Integrated using Google speech services, allowing users to:
  - Interact hands-free with the symptom checker, report analyzer, and other features.
  - Receive spoken health reports and guidance.

- **Medical ID System & Data Management**  
  - **Secure Authentication:** Users can register and log in securely.
  - **Personal Medical Data:** Save and update your health details via a unique Medical ID.
  - **Local Data Storage:** Chat history for the AI symptom checker is maintained locally with Room (with options for deletion).
  - **Emergency Contact Screen:** Quick access to essential emergency contacts.
  - **Data Synchronization:** Easily update or retrieve medical details using your Medical ID.

---

## Technologies Used

- **Jetpack Compose:** For building a modern, responsive UI.
- **Firebase Authentication & Cloud Firestore:** For secure user authentication and cloud data storage.
- **Room Database:** For local storage (e.g., chat history).
- **Gemini AI:** Powers the AI Symptom Checker and generates detailed health insights.
- **Google Speech Services:** Enables voice-based interactions.
- **Mistral AI Agent:** Provides personalized diet and exercise recommendations.

---

## Getting Started

### Prerequisites

- Android Studio.
- An Android device or emulator.

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/anuragkanojiya1/MyHealthPassport.git
   cd MyHealthPassport
   ```

2. **Open the project in Android Studio.**

3. **Build the Project:**
   - Sync Gradle and build the project in Android Studio.

4. **Run on an Emulator or Device:**
   - Deploy the app to an emulator or physical device to start using MyHealthPassport.

---

## Usage

- **Registration & Login:**  
  Users register using their email and password. Secure authentication is managed by Firebase.

- **Medical ID & Data Management:**  
  After login, users create or update their Medical ID to save and retrieve their health details.

- **AI Symptom Checker:**  
  Use voice or text to input symptoms. The Gemini-powered agent analyzes the input and provides insights.

- **Medical Report Analyzer:**  
  Upload a medical report to extract details automatically, with options to update your Medical ID or view a direct analysis.

- **Health Reports & Insights:**  
  View dynamic health reports:
  - **Line Charts:** Track blood pressure and blood sugar trends.
  - **Pie Chart:** Visualize medication usage.
  - Tap "Insights" on any chart for detailed analysis and recommendations.

- **Voice-Based Interaction:**  
  Engage with various features using voice commands powered by Google speech services.

- **Emergency Contacts:**  
  Access essential emergency contacts quickly in critical situations.

---

## Contributing

Contributions are welcome! If you'd like to contribute to MyHealthPassport, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes and push the branch.
4. Open a pull request detailing your changes.

For major changes, please open an issue first to discuss what you would like to change.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

MyHealthPassport is designed to empower users with actionable health insights and personalized AI-driven recommendations. Enjoy managing your health with the power of AI at your fingertips!
