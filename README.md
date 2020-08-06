<h1 align="center">Bluffpost</h1>

<p align="center">
  <a href="https://developer.android.com/studio/releases/platforms#5.1"><img alt="API 19" src="https://img.shields.io/badge/API-19%2B-brightgreen"/></a>
  <a href="http://www.apache.org/licenses/LICENSE-2.0"><img alt="Apache License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
</p>


<p align="center">  
Bluffpost is a demo news application based on Android's MVVM architecture.
This project is a playground for multi-module Android app with Dagger2 dependency injection and Jetpack Navigation library. It also uses Paging2 library for infinite scrolling.
</p>
</br>

<img src="assets/demo.gif" align="right" width="32%"/>

## Built with
- Minimum SDK level 19
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous work
- [Dagger2](https://github.com/google/dagger) for dependency injection
- Android Jetpack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - [Navigation](https://developer.android.com/guide/navigation) - Provides Navigation components
  - [Paging](https://developer.android.com/topic/libraries/architecture/paging) - Load and display small chunks of data at a time
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - abstraction layer over SQLite
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
- [Retrofit](https://github.com/square/retrofit) - De-facto Network library for Android
- [GSON](https://github.com/google/gson) - Java serialization/deserialization library to convert Java Objects into JSON and back
- [Glide](https://github.com/bumptech/glide) - Image downloading and caching library
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView

# License
```xml
Designed and developed by 2020 antarikshc (Antariksh Chavan)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```