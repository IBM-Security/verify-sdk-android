# Configuring your environment

## Android Studio setup

1\. After downloading the SDK, extract the contents to a folder that is easily located.

2\. Start Android Studio and create a new project by accepting the default settings.

3\. Go to `File` -> `Project Structure...` and add a new module by click on the `+` symbol in the upper left corner.

4\. Select `Import .JAR/.AAR package` and click `Next`.

5\. Click on the Browse button for the `File name` and select the `VerifySdk.aar` on
your file system. Click `Finish`.

6\. In the left side bar, select your app and then in the tab view, go to the `Dependencies` tab.

7\. Add a new dependency by clicking on the `+` symbol at the bottom. Select `Module dependency`.

8\. Select the `VerifySdk` module in the dialog.

9\. Close the dialogs.

10\. Click on the `Sync your project with Gradle files` icon.


**You should now have the SDK properly linked! Let's test it.**

11\. In your `MainActivity.java`, inside `onCreate()`, add these lines:
```java
HotpGeneratorContext hotpGeneratorContext = new HotpGeneratorContext("AB4C", 6, HmacAlgorithm.SHA1, 0);
Log.i("SDK Demo", "HOTP: " + hotpGeneratorContext.create());
```

13\. Launch the app in an emulator. Look in Android Monitor, ensuring that your log level is set to "Info":

You'll see a line like this:

    SDK Demo: HOTP: 630496


You can find a more detailed description of how to get started with the SDK at [https://www.ibm.com/blogs/security-identity-access/getting-started-with-the-ibm-verify-sdk/](https://www.ibm.com/blogs/security-identity-access/getting-started-with-the-ibm-verify-sdk/).
