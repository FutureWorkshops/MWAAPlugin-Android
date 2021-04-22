# Instructions to add the AA plugin to the exported project:

### Export the project

1. Navigate to your app on [https://www.mobileworkflow.io/apps](https://www.mobileworkflow.io/apps)
2. In the `Distribution` section, click on `Export Android Project`
3. Upload your app icon by using the `Choose File` button under "Android square icon"
4. Enter you app id on "Android Application ID" matching the ID whitelisted on AA SDK
5. Export the project by clicking on `Export Project`

### Add the AA plugin code

To add the AA plugin code to your project by following these steps:

1. Open the terminal app
2. Navigate to the exported project:

```sh
$ cd /path/to/exported/project
```

3. Add the AA plugin repository to your project by running the following git command:

```sh
$ git clone git@github.com:FutureWorkshops/MWAAPlugin-Android.git
```

4. Contact AA to be get the Print Scanner Android SDK. The provided SDK will be an AAR file named, for example, `aaprintscannerandroid-release.aar`

5. Copy the `aaprintscannerandroid-release.aar` file into the `MWAAPlugin-Android/libs` folder

6. On the exported project, edit your `settings.gradle` to include the AA plugin by adding the following line:

```groovy
include ':MWAAPlugin-Android:aa_plugin'
```

7. On the exported project, edit your `app/build.gradle.kts` to link the AA plugin by adding the following line to your `dependencies` block:

```kotlin
implementation(fileTree(mapOf("dir" to "../MWAAPlugin-Android/libs", "include" to listOf("*.jar", "*.aar"))))
implementation(project(":MWAAPlugin-Android:aa_plugin"))
```

8.  On the exported project, edit your `app/build.gradle.kts` to have the `minSdkVersion` as 25:

```kotlin
minSdkVersion(25)
```

9. Sync your project by running (in the terminal):

```sh
$ ./gradlew clean
```

10. You project is now ready to be run using the command (in the terminal):

```sh
$ ./gradlew assembleDebug installDebug
```
