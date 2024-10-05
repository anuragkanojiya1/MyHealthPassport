# MyHealthPassport

MyHealthPassport is an Android application built using Jetpack Compose that securely stores and manages patients' medical data. The app ensures privacy and easy access through robust authentication, cloud storage, and advanced image analysis features.

## Features

- **Secure Authentication:** Utilizes Firebase Authentication for secure login and registration.
- **Cloud Storage:** Stores medical data on Cloud Firestore, ensuring reliable and scalable data management.
- **PDF Storage:** Utilizes CosmoCloud Object Storage for securely storing user data in PDF format, ensuring easy and safe retrieval of medical files.
- **Medical ID System:** Each patient's medical information is associated with a unique medical ID for easy and secure access.
- **Image Analysis:** Integrates Gemini API to analyze medical certificates and extract data using the Gemini-1.5-flash model.
- **Personal Therapist Chatbot:** The app includes a personal therapist chatbot using Anthropic API, providing users with emotional and mental health support.
- **Mistral Agent for Personalized Diet and Exercise Plan:** Provides personalized diet and exercise recommendations based on users' medical data via Mistral Agent.
- **User-Friendly Interface:** Designed with Jetpack Compose for a modern and intuitive user experience.

## Mistral Agent for Personalized Diet and Exercise Plan

1. **Personalized Diet and Exercise Recommendation:**
   - The app integrates with Mistral Agent to provide users with personalized diet and exercise plans based on their medical data. By entering the unique medical ID, users can receive tailored health recommendations that suit their medical conditions and fitness goals.

2. **Steps to Use Mistral Agent:**
   - **Enter Medical ID:** After logging in, input your unique medical ID to fetch your stored medical data.
   - **Receive Diet and Exercise Plan:** Mistral Agent analyzes your medical data and provides a custom diet and exercise plan designed to fit your health requirements and fitness objectives.
   - **Dynamic Updates:** As medical data is updated, the diet and exercise recommendations are automatically adjusted to ensure the plan remains relevant and effective.
     
## Anthropic API Integration

1. **Set Up Anthropic API:**
   - Sign up for an account on [Anthropic](https://www.anthropic.com).
   - Obtain your API key.
   - Add the Anthropic API key in `Constant.kt` file in `app/src/main/java/com/example/myhealthpassport` folder.

2. **Using Anthropic API for Chatbot:**
   - Implement the chatbot using the Anthropic API key to offer personalized mental health advice.

## Integration with CosmoCloud

1. **Set Up CosmoCloud Object Storage:**
   - Create an account on [CosmoCloud](https://cosmocloud.io/).
   - lick on Object Storage and create bucket by name of `medicalData` and of "Space(in GiBs) = 0.5".
   - Obtain your environmentId and projectId.
   - Put them in a file called Constant.kt in `app/src/main/java/com/example/myhealthpassport` folder.

2. **Uploading Medical Data to CosmoCloud:**
   - Store medical files in PDF format using CosmoCloud’s object storage for safe and organized data management.
     
## App Demo

Youtube Link- https://youtu.be/HWaFyXuHNPQ

## Screenshots

<img src="https://github.com/user-attachments/assets/934dc1b1-a076-4f41-a06c-6f1fb15da848" alt="Screenshot_20240909_225057" width="250"/>
<img src="https://github.com/user-attachments/assets/a8696ebc-9734-4080-a540-d98383a26f2e" alt="Screenshot_20240918_142927" width="250"/>
<img src="https://github.com/user-attachments/assets/9d369237-4792-498e-83e3-9bb36cd1c55a" alt="Screenshot_20240917_182746" width="250"/>
<img src="https://github.com/user-attachments/assets/707f01f9-e9de-4924-813c-bed16e7f9228" alt="Screenshot_20240909_225024" width="250"/>
<img src="https://github.com/user-attachments/assets/ecb61258-eeb9-4a1e-86d5-06a4a41a4547" alt="Screenshot_20240910_014029" width="250"/>
<img src="https://github.com/user-attachments/assets/d7d55295-356a-4e1a-a228-00d8f20dba02" alt="Screenshot_20240917_183226" width="250"/>
<img src="https://github.com/user-attachments/assets/b9180f51-5b18-41bf-9fb8-4a8ef24b295a" alt="Screenshot_20240917_181517" width="250"/>
<img src="https://github.com/user-attachments/assets/dbfce296-0321-4ad3-96de-29149b56adcf" alt="Screenshot_20240625_015445" width="250"/>
<img src="https://github.com/user-attachments/assets/76602f36-e36c-4fe6-a1f8-c319f761c2c7" alt="Screenshot_20240709_200116" width="250"/>
<img src="https://github.com/user-attachments/assets/9be86b16-a920-4b68-b261-19940ddcd44f" alt="Screenshot_20240909_234029" width="250"/>

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/MyHealthPassport.git
   ```
2. **Open the Project:**
   - Open the project in Android Studio.
  
3. **Create a `Constant.kt` file in `app/src/main/java/com/example/myhealthpassport` folder and put below variables in the file with your Api Key and IDs.**

   ```sh
   const val ANTHROPIC_API_KEY = "Enter_your_own_Anthropic_Api_key"
   const val environmentId = "Enter_your_Cosmocloud_Environment_Id"
   const val projectId = "Enter_your_Cosmocloud_Project_Id"
   ```   
     
4. **Build and Run:**
   - Build the project and run it on an emulator or physical device.

## Usage

1. **Register:**
   - Create a new account using your email and password.
2. **Login:**
   - Log in to your account using the registered email and password.
3. **Add Medical Data:**
   - Enter your medical information and save it using your unique medical ID.
4. **Get Medical Data:**
   - Get your medical data by simply entering your unique medical ID.
5. **Upload Medical Certificate:**
   - Upload a bitmap image of your medical certificate from the gallery.
   - The Gemini API will analyze the image and provide a text response with the extracted data.
6. **Personal Therapist Chatbot:**
   - Access your personal AI therapist powered by the Anthropic API.
   - Chat with the therapist for personalized mental health advice and support.
   - Ask for guidance on managing stress, improving mental well-being, or daily health tips.
7. **Personalized Diet and Exercise Recommendation Agent:**
   - Input your medical ID to receive a personalized diet and exercise plan from the Mistral Agent.
   - The plan is tailored to your health conditions and fitness goals.

## Tools/Products

- **Jetpack Compose:** For building the UI.
- **Firebase Authentication:** For user authentication.
- **Cloud Firestore:** For storing medical data.
- **Gemini API:** For analyzing medical certificates.
- **Anthropic API:** For integrating Personal Therapist ChatBot.
- **Mistral Agent:** For integrating Personalized Diet and Exercise Recommendation Agent.

## Project Structure

  - `Health`: Contains the screens and composables for managing health-related data.
  - `ViewModels`: Contains viewmodels.
  - `Navigation`: Contains NavGraph and Screen for navigation through composables.
  - `SignInSignUp`: Contains Signin and Signup page.
  - `Agent:`: Contains files of the Mistral Agent.
  - `Cloud:` Contains files of the Cosmocloud object storage.
  - `Anthropic:` Contains files of Anthropic's Personal Therapist Chatbot.

## Contributions

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries or support, please contact [LinkedIn](https://www.linkedin.com/in/anurag-kanojiya-101312286/).

---
