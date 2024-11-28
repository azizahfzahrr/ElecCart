# ElecCart 

![image eleccart](https://github.com/user-attachments/assets/e09ce982-ec5f-462e-b585-c6640fd38617)


ElecCart is your go-to app for exploring and purchasing a wide range of electronic products, from the latest smartphones and laptops to home appliances and accessories. Shop effortlessly with a secure transaction experience, complete with real-time payment status updates. ElecCart brings convenience and cutting-edge technology to your fingertips.


## Table of Contents

- [ElecCart](#eleccart)
- [Demo](#demo)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Library](#library)
- [Project Modules](#project-modules)
- [Design Pattern](#design-pattern)
- [Dependency Injection](#dependency-injection)


## Demo 


## Tech Stack

![tech stack](https://github.com/user-attachments/assets/83041abb-d7e4-4d00-9e6e-40a64b9ebbdc)


## Features 

- **Splashscreen** : The introductory screen that appears when launching the app, often used to display branding or logo before the main content loads.
- **Onboard** : A series of introductory screens that guide new users through the key features and functions of the app.
- **Login with Google Authentication** : This feature allows users to sign in quickly using their Google account credentials, providing a seamless login experience.
- **List Product** : A feature that allows users to browse and discover a wide range of products, organized by categories to help users find what they need.
- **Category** : A feature that organizes products into distinct groups based on their type or function, helping users easily navigate and find items that match their needs. Categories allow users to filter and explore products more efficiently.
- **Search** : A function that enables users to quickly find products by entering keywords or filtering based on specific criteria.
- **Share** : A feature that allows users to share product details or app content with others via social media, messaging apps, or direct links.
- **Carts** : A feature where users can add products they wish to purchase, acting as a shopping basket for later checkout.
- **Wishlist** :  A feature that lets users save products they are interested in for future purchase or reference.
- **Checkout Product** : The process of reviewing the products in the cart, confirming their details, and finalizing the purchase.
- **Payment Method** : After confirming the order, users can select from various payment options to complete the transaction securely.
- **Transaction Order History** : A feature that enables users to view a log of all their previous orders, with details of the status and payment completion.
- **Logout** : This feature allows users to exit their account session and securely log out of the app, preventing unauthorized access.


## Library

- **Firebase Authentication** : Simplifies user login and ensures secure authentication with token auto-refresh.
- **Firebase Crashlytics** : Monitors and reports app crashes for quick issue resolution.
- **Local Database** : Uses Room Database to securely store and manage data locally on the device
- **Preferences Datastore** : Efficiently saves and retrieves user preferences with modern technology.
- **API Logging** : Tracks network activities and API requests using OkHttp’s Logging Interceptor.
- **Network** : Handles API communication effectively using Retrofit for seamless data exchange.
- **Retrofit** : A tool for making network requests to a web API over HTTP, requiring an internet connection.
- **Dagger Hilt** : A library that simplifies the process of implementing dependency injection (DI) in Android applications, making it more organized and effective.
- **Material 3 Design** : A design system that provides the core components and guidelines for creating the user interface of an app.
- **Smooth Loading** : Enhances user experience with engaging Shimmer animations while loading content.
- **Image Loader** : Loads images efficiently from the internet with Glide, ensuring fast and smooth performance.


## Project Layers

ElecCart is designed with a structured architecture, dividing the application into three layers.

- **Presentation** : Manages the app's visual presentation, including screens, ViewModels, and the startup process. It is responsible for all user interactions and the overall interface design.
- **Domain** : Hosts the core business logic, including domain models, repository contracts, and use case implementations. This module is entirely independent, ensuring it remains stable and unaffected by external dependencies.
- **Data** : Oversees data handling tasks such as API calls and local storage operations. It contains response models, entity models, and repository implementations, ensuring seamless data flow between the server, database, and app.

![clean arch](https://github.com/user-attachments/assets/2ed905b3-382a-4eed-9b91-0e267e93dbcf)


## Design Pattern 

MVVM (Model-View-ViewModel) architecture is implemented in Shutter Shop to ensure a well-organized codebase. This pattern is widely adopted for Android applications as it promotes a clear separation of responsibilities, simplifying app maintenance and testing.

- **Model** : Represents the application's data layer, including data structures, data operations, and core business logic. It interacts with external sources such as APIs or local databases, acting as the single source of truth for the application's data.
- **View** : The user interface layer responsible for presenting data to the user and capturing user interactions. It communicates with the ViewModel to request or update data.
- **ViewModel** : Acts as the link between the View and the Model. It processes user input, prepares data for the View, and manages the UI state. By leveraging observable properties like LiveData or StateFlow, the ViewModel allows the View to respond to data changes dynamically, without directly accessing the Model.
  
![mvvm](https://github.com/user-attachments/assets/276f27ab-33ac-4cd2-94cd-8ed0967ce664)


## Dependency Injection 

This project utilizes Dagger Hilt for dependency injection. Dependency injection plays a vital role in MVVM and modular architecture by efficiently managing dependencies between different components. It ensures loose coupling, simplifies maintenance, and improves the testability of the codebase.


## Prerequisites to Run This Project
To build and run the project locally, ensure you have the following installed:

- *Java 8*
- *Gradle 7.8*
- *Android Studio*

### Clone Project
```bash
gh repo clone azizahfzahrr/ElecCart


*Build the Debug APK*
bash
./gradlew assembleDebug


*Important Notes*
- Using Android Studio to build the project is highly encouraged for an optimal development experience. Before creating the APK, ensure you have configured your Firebase project and included the `google-services.json` file, as Firebase integration is essential for this project. Additionally, remember to set up the SHA-1 key for proper configuration.

## Installation
The minimum device requirements to run this application are Android 10 (API 29).
