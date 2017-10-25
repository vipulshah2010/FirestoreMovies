# FirestoreMovies
Simple Movie application showcasing use of Firestore document based database.

This application is built to showcase use of Firestore concepts at Google Devfest'17 Mumbai Android workshop.

## Features

With the app, you can:
* Push set of mock movies on Firestore Database
* Submit / Read reviews about movies
* Submit / View Ratings

## Screens
| Movie Listing  | Movie Details |
| ------------- | ------------- |
| ![](../master/art/movie_list.png)  | ![](../master/art/movie_details.png)  |

| Movie Remixer  | Movie Reviews |
| ------------- | ------------- |
| ![screen](../master/art/movie_remixer.png)  | ![screen](../master/art/movie_reviews.png)  |

| Movie Ratings |
| ------------- |
| ![screen](../master/art/movie_ratings.png)|

## Diagram

![](../master/art/flowchart.png)

## Description

* Root Collection is Movies which Contain multiple Movie documents
* Each Movie document inturn contains two collections -> Ratings and Reviews

## Documents

| Movie          |  
| ---------------|  
| totalRatings   |  
| averageRatings |  
| totalReviews   |  
| title          |
| poster         |
| language       |   
| adult          | 
| overview       |
| releaseDate    |

| Rating  |
| --------|
| email   |
| ratings |
| date    |

| Reviews |
| --------|
| email   |
| review  |
| date    | 

## Libraries

* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Picasso](http://square.github.io/picasso/)
* [Moshi](https://github.com/square/moshi)
* [Remixer](https://github.com/material-foundation/material-remixer-android)
* [Dagger2](https://github.com/google/dagger)

## Steps

* Open the [Firebase Console](https://console.firebase.google.com/u/0/) and click Add project.
* Select the option to Enable Cloud Firestore Beta for this project.
* Click Create Project
* Download google-services.json and keep in root of app module.

Add following firebase dependencies.

| Gradle        | Dependency    |
| ------------- |:-------------:|
| Project Level | classpath 'com.google.gms:google-services:3.1.0'                    |
| App Level     | implementation 'com.google.firebase:firebase-auth:11.4.2'           |
| App Level     | implementation 'com.google.firebase:firebase-firestore:11.4.2'      |
| App Level ( End)     | apply plugin: 'com.google.gms.google-services'      |
