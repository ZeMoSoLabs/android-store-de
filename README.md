# android-store-de
ZeTarget Store de android demo app

# InAppText Android SDK
## Installation
- Download InAppTextSDK.aar from <link>
- Copy and paste it into libs folder of the application
- Add the following lines in the app level build.gradle file

    ````
    compile 'com.jraska:falcon:1.0.3'
    compile(name:'InAppTextSDK', ext:'aar')
    ````
- Also make sure that you have the following lines in your build.gradle file
    
    ````
    repositories {
      flatDir {
          dirs 'libs'
      }
    }
    ````
    
## Initialization
- Add the following lines in your LAUNCHER/Main Activity's `onResume()` method
  
  ````
  InAppText.enableDebugging();  // Remove this line (or all the three lines) to disable the SDK
  InAppText.enableAudio();      // Call this method if you want audio while recording screen
  InAppText.initializeInAppText(this); // Initializes the sdk
  ````
