# Test Android App

This is a **Dacadoo Android application** created for demonstration and experimentation purposes.

## ⚙️ Project Structure

For the sake of **simplicity**, this project is built using a **single module setup**.  
In a production environment, I would typically use a **multi-module architecture** and apply **convention plugins** to manage shared Gradle logic across modules.

## 🚀 Getting Started

To build and run the project:

1. Clone the repo:

    ```bash
    git clone https://github.com/PRobi23/PhotoDownloader.git
    ```

2. Add your `API_KEY` to the `gradle.properties` file:

    ```properties
    API_KEY=your_api_key_here
    ```

3. Open the project in Android Studio and click **Run** (or use `./gradlew assembleDebug` from terminal).