Weather Forecast - Android App
This is an android application, which let users to know about the weather forecast reports for the whole week.

This application is built with these utilities of Android mentioned below :
  1. Fragments - For better use of the activities,so we can use the same fragment for multiple activities and apply the reusability factor of fragments.
  2. Array Adapters - Adapters are used to iterate the same layout for the given list of items.
  3. Intents - Intents are used to perform a particular task with any other activity,I have used intents to pass the data of one activity to another activity to show the detailed forecast for the corresponding date, and opening map in the google map to show the location which is stored in the database.
  4. Settings Activity - The settings activity can be created directly with android studio,you have to define some preferences in the preferences screen.
  5. SQLite database -  I have used SQLite3 database to storedata in this application for the whole week forecast so that it have no need to fetch data every time.
  6. Weather Api - I have used the OPEN WEATHER MAP API to fetch data from this api in the form of JSON and then parse it to use the information given by this info.
  7. Default Shared Preferences : i have used default shared preferences to store data of settings menu items.
  
For running this appliaction Simply follow the steps :
  1. Download Android Studio(You can download it from Developer.android.com)
  2. You should have the latest version of java or download it from WWW.JAVA.COM
  3. set the path of java environment variables or follow this guide to set java path
     http://www.javatpoint.com/how-to-set-path-in-java
  4. Open android sdk and download the support packages.
  5. now download the zip file of this project anywhere and extract it.
  6. open android studio, File -> New -> import project , select the extracted folder.
  7. Now you have imported the project, run the project, and start any virtual device from emulator or download any       device then start the device and run the application.
  8. if you want to run this application on your android device, turn on usb debugging on your device(In case you         don't know how to turn on usb debugging then follow this link               http://www.phonearena.com/news/How-to-enable-USB-debugging-on-Android_id53909 ).
  9. Then click on run button to run application on your device (If your device is not shown in currently running         devices then you must download the usb drivers for your device to become available for the android studio).
  
