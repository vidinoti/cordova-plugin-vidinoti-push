#!/usr/bin/env node

'use strict';

var fs = require('fs');

function fileExists(path) {
    try {
        return fs.statSync(path).isFile();
    } catch (e) {
        return false;
    }
}

// Resets the string properties to their default value, this way they are correctly removed when uninstalled.
function resetStringsXml() {
    var stringsXml = fileExists('platforms/android/app/src/main/res/values/strings.xml') ? 'platforms/android/app/src/main/res/values/strings.xml' : 'platforms/android/res/values/strings.xml';
    var strings = fs.readFileSync(stringsXml).toString();

    // replace the default value
    strings = strings.replace(new RegExp('<string name="google_app_id">([^<]+?)</string>', 'i'), '<string name="google_app_id">@string/google_app_id</string>');

    // replace the default value
    strings = strings.replace(new RegExp('<string name="google_api_key">([^<]+?)</string>', 'i'), '<string name="google_api_key">@string/google_api_key</string>');

    fs.writeFileSync(stringsXml, strings);
}

// Deletes the file google-services.json if it exists
function deleteGoogleServicesJson() {
    let jsonFile = 'platforms/android/google-services.json';
    if (fileExists(jsonFile)) {
        fs.unlinkSync(jsonFile);
    }
}

module.exports = function (context) {
    resetStringsXml();
    deleteGoogleServicesJson();
};