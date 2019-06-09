# omdbapi-client

## Getting Started
The following instruction considers that you have all of the tools required for the Android application development installed.
To build omdbapi Android client you need to do the following steps:
1. Clone this repository;
2. Get omdbapi API Key from http://www.omdbapi.com/apikey.aspx;
3. Create ```secured.properties``` file in the root folder of the repository;
4. Put your API Key into that file in the following format ```omdbapi.apiKey="your_api_key"```;
5. Now you can build and run the project;

## Used technologies
Omdbapi client uses the following tehnologies:
1. Room
2. Retrofit
3. Lifecycle: ViewModel, LiveData, Paging, ConstraintLayout, RecyclerView
4. Dagger 2
5. RxJava 2
6. Glide
7. Kotlin

## Architecture
1. Application partly utilizes principles of clean architecture;
2. Application uses MVVM as presentation layer pattern.
