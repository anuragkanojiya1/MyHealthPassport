
# MyHealthPassport

MyHealthPassport is an Android application built using Jetpack Compose that securely stores and manages patients' medical data. The app ensures privacy and easy access through robust authentication, cloud storage, and advanced image analysis features.

## Features

- **Secure Authentication:** Utilizes Firebase Authentication for secure login and registration.
- **Cloud Storage:** Stores medical data on Cloud Firestore, ensuring reliable and scalable data management.
- **PDF Storage:** Utilizes CosmoCloud Object Storage for securely storing user data in PDF format, ensuring easy and safe retrieval of medical files.
- **Medical ID System:** Each patient's medical information is associated with a unique medical ID for easy and secure access.
- **Image Analysis:** Integrates Gemini API to analyze medical certificates and extract data using the Gemini-1.5-flash model.
- **Personal Therapist Chatbot:** The app includes a personal therapist chatbot using Anthropic API, providing users with emotional and mental health support.
- **User-Friendly Interface:** Designed with Jetpack Compose for a modern and intuitive user experience.

## Integration with CosmoCloud

1. **Set Up CosmoCloud Object Storage:**
   - Create an account on [CosmoCloud](https://cosmocloud.io/).
   - Obtain your environmentId and projectId.
   - Put them in a file called Constant.kt in com.example.myhealthpassport folder.

2. **Uploading Medical Data to CosmoCloud:**
   - Store medical files in PDF format using CosmoCloud’s object storage for safe and organized data management.
  
## Anthropic API Integration

1. **Set Up Anthropic API:**
   - Sign up for an account on [Anthropic](https://www.anthropic.com).
   - Obtain your API key and Base Url.
   - Add the Anthropic API key and Base Url in Constant.kt file in com.example.myhealthpassport folder.
   
2. **Using Anthropic API for Chatbot:**
   - Implement the chatbot using the Anthropic API key to offer personalized mental health advice.
     
## App Demo

Youtube Link- https://youtu.be/cY1sU-AKsRY

## Screenshots

<img src="https://github.com/user-attachments/assets/934dc1b1-a076-4f41-a06c-6f1fb15da848" alt="Screenshot_20240909_225057" width="300"/>
<img src="https://github.com/user-attachments/assets/b95d6e53-f969-4cdd-a739-b8b32986ee6f" alt="Screenshot_20240909_234008" width="300"/>
<img src="https://github.com/user-attachments/assets/9be86b16-a920-4b68-b261-19940ddcd44f" alt="Screenshot_20240909_234029" width="300"/>
<img src="https://github.com/user-attachments/assets/707f01f9-e9de-4924-813c-bed16e7f9228" alt="Screenshot_20240909_225024" width="300"/>
<img src="https://github.com/user-attachments/assets/ecb61258-eeb9-4a1e-86d5-06a4a41a4547" alt="Screenshot_20240910_014029" width="300"/>
<img src="https://github.com/user-attachments/assets/54e7ead8-0070-4b04-992b-6c1ae2ac2176" alt="Screenshot_20240910_014428" width="300"/>
<img src="https://github.com/user-attachments/assets/19502c66-faa7-47a3-a10a-ab892dd0fe02" alt="Screenshot_20240807_020345" width="300"/>
<img src="https://github.com/user-attachments/assets/dbfce296-0321-4ad3-96de-29149b56adcf" alt="Screenshot_20240625_015445" width="300"/>
<img src="https://github.com/user-attachments/assets/76602f36-e36c-4fe6-a1f8-c319f761c2c7" alt="Screenshot_20240709_200116" width="300"/>

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/MyHealthPassport.git
   ```
2. **Open the Project:**
   - Open the project in Android Studio.

3. **Set Up Firebase:**
   - Add your `google-services.json` file to the `app` directory.
   - Ensure Firebase Authentication and Cloud Firestore are set up in your Firebase project.

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

## Tools/Products

- **Jetpack Compose:** For building the UI.
- **Firebase Authentication:** For user authentication.
- **Cloud Firestore:** For storing medical data.
- **Gemini API:** For analyzing medical certificates.
- **Anthropic API:** For integrating Personal Therapist ChatBot.

## Project Structure

  - `Health`: Contains the screens and composables for managing health-related data.
  - `ViewModels`: Contains viewmodels.
  - `Navigation`: Contains NavGraph and Screen for navigation through composables.
  - `SignInSignUp`: Contains Signin and Signup page.

## Contributions

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries or support, please contact [LinkedIn](https://www.linkedin.com/in/anurag-kanojiya-101312286/).

---
