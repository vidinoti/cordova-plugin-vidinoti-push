#!/usr/bin/env node

'use strict';

var fs = require('fs');

fs.ensureDirSync = function (dir) {
    if (!fs.existsSync(dir)) {
        dir.split(path.sep).reduce(function (currentPath, folder) {
            currentPath += folder + path.sep;
            if (!fs.existsSync(currentPath)) {
                fs.mkdirSync(currentPath);
            }
            return currentPath;
        }, '');
    }
};

function fileExists(path) {
    try {
        return fs.statSync(path).isFile();
    } catch (e) {
        return false;
    }
}

function updateStringsXml(contents) {
    var json = JSON.parse(contents);
    var stringsXml = fileExists('platforms/android/app/src/main/res/values/strings.xml') ? 'platforms/android/app/src/main/res/values/strings.xml' : 'platforms/android/res/values/strings.xml';
    var strings = fs.readFileSync(stringsXml).toString();

    // strip non-default value
    strings = strings.replace(new RegExp('<string name="google_app_id">([^\@<]+?)</string>', 'i'), '');

    // strip non-default value
    strings = strings.replace(new RegExp('<string name="google_api_key">([^\@<]+?)</string>', 'i'), '');

    // strip empty lines
    strings = strings.replace(new RegExp('(\r\n|\n|\r)[ \t]*(\r\n|\n|\r)', 'gm'), '$1');

    // replace the default value
    strings = strings.replace(new RegExp('<string name="google_app_id">([^<]+?)</string>', 'i'), '<string name="google_app_id">' + json.client[0].client_info.mobilesdk_app_id + '</string>');

    // replace the default value
    strings = strings.replace(new RegExp('<string name="google_api_key">([^<]+?)</string>', 'i'), '<string name="google_api_key">' + json.client[0].api_key[0].current_key + '</string>');

    fs.writeFileSync(stringsXml, strings);
}

module.exports = function (context) {
    // Copy the file google-services.json from the project folder to the android platform folder
    var contents = fs.readFileSync('google-services.json').toString();
    fs.ensureDirSync('platforms/android');
    fs.writeFileSync('platforms/android/google-services.json', contents);
    updateStringsXml(contents);
};