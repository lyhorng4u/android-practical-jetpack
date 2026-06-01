# Android Development Guidelines - SampleApp

This document outlines the architecture and coding standards for the SampleApp project.

## 1. Architecture: MVVM Pattern

We follow the Model-View-ViewModel (MVVM) architecture pattern.

### View (UI Layer)
- **Location:** `com.lyhorng.sampleapp.ui.*`
- **Rules:**
    - Use **ViewBinding** for all layouts.
    - Keep logic minimal; only handle UI updates and user input.
    - Observe ViewModels for data changes. Do not hold business logic.
    - Always handle `Resource.Loading`, `Resource.Success`, and `Resource.Error` states.

### ViewModel
- **Location:** `com.lyhorng.sampleapp.viewmodel.*`
- **Rules:**
    - Extend `androidx.lifecycle.ViewModel`.
    - Use `MutableLiveData` (private) and `LiveData` (public) to expose data.
    - Launch coroutines using `viewModelScope.launch`.
    - Communication with Repositories should always return a `Resource` type or equivalent state.

### Repository
- **Location:** `com.lyhorng.sampleapp.data.repository.*`
- **Rules:**
    - Acts as the single source of truth for data.
    - Mediates between `ApiService` and ViewModels.
    - Use `BaseService.safeApiCall` for network requests.

## 2. Network & Data handling

### API Requests
- Define endpoints in `ApiService.kt`.
- Use `BaseService.safeApiCall { ... }` in Repositories.
- Use `Resource<T>` to wrap data in the UI layer:
  ```kotlin
  viewModel.data.observe(this) { resource ->
      when(resource) {
          is Resource.Loading -> // Show Progress
          is Resource.Success -> // Show Data
          is Resource.Error -> // Show Error Toast
      }
  }
  ```

### Local Storage
- Use `PreferenceManager` for small data like Auth Tokens and User Profile snippets.
- Use `commit()` for critical path writes (like Login token) to ensure data is available for immediate subsequent reads.

## 3. Best Practices

- **Input Validation:** Perform basic UI validation (email format, empty fields) in the Activity/Fragment before calling the ViewModel.
- **String Constants:** Keep API keys, preference keys, and fixed values in `Constants.kt`.
- **Naming Convention:** 
    - Activities: `FeatureActivity`
    - ViewModels: `FeatureViewModel`
    - Layouts: `activity_feature.xml`
- **Navigation:** When switching between Auth state and Main state, clear the activity stack:
  ```kotlin
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  ```
