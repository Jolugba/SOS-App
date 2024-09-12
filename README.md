# SOS Android App

## Overview

The **SOS Android App** is a personal safety application designed to help users quickly alert their emergency contacts in distress situations. The app allows users to capture an image, acquire their real-time location, and send both—along with preconfigured emergency contact numbers—to a secure REST API endpoint. This provides family members, friends, or authorities with critical information during emergencies.

## Features

- **Image Capture**: Users can take a picture of their surroundings using the device's camera, which is then encoded to a Base64 string.
- **Location Tracking**: On image capture, the app automatically acquires the user's real-time location using the Google Location API (latitude and longitude).
- **SOS Alert Transmission**: The app sends the captured image, location, and emergency contact numbers to a preconfigured REST API endpoint.

## Technologies Used

- **Kotlin**: The main programming language for Android development.
- **CameraView by Otali Studio**: A third-party camera library used for capturing images.
- **Google Location API**: For acquiring real-time location data.
- **Retrofit**: For making network requests and sending the data to a REST API.
- **FusedLocationProviderClient**: For precise location tracking and management.

## Setup and Installation

### Prerequisites
- Android Studio 4.1 or higher
- Android SDK 29 or higher
- A physical device running Android 7.0 (Nougat) or later for testing

### Installation Steps
1. **Clone the Repository**:
    ```bash
    git clone https://github.com/jolugba/sos-app.git
    ```
2. **Open the Project in Android Studio**.
3. **Sync Gradle**: Ensure that all dependencies are downloaded and configured properly.
4. **Build and Run the App**: Run the app on a physical Android device.

## How It Works

1. **Image Capture**: The app uses the CameraView library to capture an image, which is encoded into a Base64 string.
2. **Location Tracking**: When the image is captured, the app uses the FusedLocationProviderClient to obtain the user's current location (latitude and longitude).
3. **Sending SOS Alert**: A POST request is made to the provided REST API endpoint with the phone numbers, image, and location data in the body.

## Testing

1. **Permissions**: Ensure that camera and location permissions are enabled on the device.
2. **Image Capture**: Test the image capture functionality using various lighting conditions and check that the image is encoded as expected.

## Future Improvements
- **UI/UX Enhancements**: Improve the user interface to make it more intuitive and visually appealing.
  
