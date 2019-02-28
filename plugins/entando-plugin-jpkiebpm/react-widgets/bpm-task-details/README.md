This project was bootstrapped with [Create React App](https://github.com/facebookincubator/create-react-app).

Below you will find some information on how to perform common tasks.<br>
You can find the most recent version of this guide [here](https://github.com/facebookincubator/create-react-app/blob/master/packages/react-scripts/template/README.md).

## Table of Contents
- [How to build or update the react widget]
    - [1. Build the widget]
    - [2. Copy the widget to the kie plugin]
    - [3. Rebuild the kie plugin]
    - [4. Reload your application]

## Build the widget
In the widget directory, you can run:

### `npm run build`

If the node_modules folder is not present before build run

### `npm install`

After that the main.js file will be created in the folder `/build/static/js/`

## Copy the react widget to the kie plugin

After theebuild of the new version of the widget you can copy the main.js file
from /build/static/js/ in the folder below:
`entando-components/plugins/entando-plugin-jpkiebpm/src/main/webapp/resources/plugins/jpkiebpm/static/js/react-resources/WIDGET_NAME_FOLDER`
and your version of the react widget is now available

## Rebuild the kie plugin
Rebuild the kie plugin runnning 'mvn clean install' in the kie plugin main folder
`entando-components/plugins/entando-plugin-jpkiebpm/`

## Reload your application
To see the changes on the kie plugin you need to reload your webapp
