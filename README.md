# Cordova plugin for Vidinoti Push integration with Android

This plugin can be used together with the [cordova-plugin-pixlive](https://github.com/vidinoti/cordova-plugin-PixLive).
It adds the support for the Android push notifications. Previously, it was directly integrated within the `cordova-plugin-pixlive` but has been decoupled.

This plugin requires the Vidinoti SDK version >= 6.4.0

## How to use

1. Install and follow the instructions for the [cordova-plugin-pixlive](https://github.com/vidinoti/cordova-plugin-PixLive).
2. Connect to the [Firebase Console](https://console.firebase.google.com/). Create a new Firebase project and download the `google-services.json` file.
3. Place the `google-services.json` file in the root folder of your cordova project.
4. Install the plugin

        cordova plugin add cordova-plugin-vidinoti-push

## What does the plugin

1. Add required dependencies in gradle files
2. Copy Java classes (Android services) `VidinotiFcmListenerService`
3. Add the Android services to the `AndroidManifest.xml` file
4. In the "after prepare" hook scripts, it takes the `google-services.json` file and copy it to `platforms/android`
5. In the "after prepare" hook scripts, it takes the `google-services.json` file and extracts the Google app ID and key. It then writes them in the Android `strings.xml` file

## How to contribute and create a new release

* Edit the plugin as needed
* Update the version number in `plugin.xml` and `package.json`
* Commit and push the changes to GitHub
* Create a new release from GitHub interface (releases > Draft a new release)
* Publish the release to npm registry (if necessary `npm login`): `npm publish`

## Release note

### Version 0.2.0 - 19 October 2022

Fix for Android 12

### Version 0.1.2 - 27 February 2020

Set firebase dependencies version

### Version 0.1.1 - 18 April 2018

Fix the plugin such that it can be correctly removed.

### Version 0.1.0 - 25 June 2018

Initial release

