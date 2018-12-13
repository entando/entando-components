
# Entando ui-component-sample

This is a sample Entando plugin, it can be cloned and used as a starting point to create a new Entando plugin.

## Quick start:

```
> git clone https://github.com/entando/app-builder
> git clone https://github.com/entando/ui-component-sample
> cd ui-component-sample
> npm install
> cd ../app-builder
> echo "[{ \"name\": \"ui-component-sample\", \"path\": \"../ui-component-sample\" }]" > entando-plugins-config/plugins.json
> npm install
> npm run import-plugins
> npm start
```

In more details, to get the **app-builder** with a sample plugin up and running, **app-builder**
and **ui-component-sample** projects must be cloned, and `npm install` must be launched on both.

After that, the `app-builder/entando-plugins-config/plugins.json` must be edited. This is a JSON array
and its elements have this schema:

- **name**: the plugin unique name (the "name" property in the plugin's `package.json` file)
- **path**: the path to the plugin's root folder

In this case (assuming you cloned the projects in the same directory, and didn't change the directory names),
the `app-builder/entando-plugins-config/plugins.json` should be like:

```
[
  {
    "name": "ui-component-sample",
    "path": "../ui-component-sample"
  }
]
```

After that, running `npm run import-plugins` (on the **app-builder** project) will build the plugin and
import it (it copies the assets and it generates an import file `app-builder/src/entando-plugins.js`).

Then, running `npm start` will start an **app-builder** instance with a custom plugin page (accessible via the Integrations menu)


## Entando plugin requirements

A valid Entando plugin must match the following constraints:

- it must be a valid npm module
- it must be compiled with ```npm run build-plugin``` command
- the compiled bundle has the structure:
```
    PLUGIN_DIR
    └── dist
    |   ├── static
    |   |   ├── js
    |   |   |   ├── main.js
    |   |   |   └── [main.js.map]
    |   |   ├── css
    |   |   |   ├── main.css
    |   |   |   └── [main.css.map]
    |   |   └── [media]
    |   |   |   └── [... plugin imported assets ...]
    |   └── plugin-assets
    |       └── PLUGIN_NAME
    |           └── [... plugin public assets ...]
```
- the ```main.js``` must export:

  - **id**: the plugin id (the "name" property in ```package.json```)
  - **menuItemLabelId**: a message key (defined in **locales** objects) that will be the translated
  text for the plugin menu item
  - **reducer**: a valid Redux reducer, returning the plugin state
  - **uiComponent**: a React Component representing the plugin config page (the page shown when the user clicks on the Integrations menu)
  - **locales**: an array with the following schema:
  - **widgetForms** (optional): an object to export forms for widgets that require a configuration.
    - key: the widget code (as returned from the API)
    - value: the form React Component
```
        [
          {
            locale: 'en',
            messages: {
              welcome: 'Welcome to Entando',
              ... other messages ...
            }
          },
          {
            locale: 'it',
            messages: {
              welcome: 'Benvenuto in Entando',
              ... other messages ...
            }
          },
        ]
```
- all the public assets (if any) must be placed in the `public/plugin-assets/<PLUGIN_NAME>` directory,
where `<PLUGIN_NAME>` is the `name` property in the `package.json` file. The files will be copied in
the main application `public/plugin-assets` directory when importing the plugin.


## Example: adding a form to the plugin

Giving you followed the Quick start guide at the top of this doc file, adding a form and setting it
as the plugin config page is easy:

#### 1. install redux-form

```bash
cd ui-component-sample
npm i redux-form 
```

#### 2. (optional) add redux-form to Webpack externals

To decrease the plugin bundle size and avoid package conflicts, add the redux-form package
to the webpack externals: in the `./config/webpack.config.plugin.js` file:

```javascript
  externals: {
    "react-intl" : {
      commonjs: "react-intl",
      amd: "react-intl",
    },

    // add the new entry for redux-form
    "redux-form" : {
      commonjs: "redux-form",
      amd: "redux-form",
    }
  },

```

#### 3. create the form component

Create a new directory `./src/ui/sample-form/` and create a new file inside it, called `SampleForm.js`
Copy the following text into the file:

```javascript
import React from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';

import { connect } from 'react-redux';


// 1. create the form

const SampleForm = ({ handleSubmit }) => (
  <form
    className="SampleForm form form-horizontal"
    onSubmit={handleSubmit}
  >
    <br />
    <div className="form-group">
      <label htmlFor="number" className="control-label col-xs-2">
        Title
      </label>
      <div className="input-group col-xs-2">
        <Field
          id="title"
          component="input"
          className="form-control"
          name="title"
        />
      </div>
    </div>
    <br />
    <div className="row">
      <div className="col-xs-4">
        <button type="submit" className="pull-right">
          Save
        </button>
      </div>
    </div>
  </form>
);

SampleForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
};


// 2. wrap the component with the redux-form HOC:

const SampleFormWrapped = reduxForm({
  form: 'widgetConfigForm',
})(SampleForm);


// 3. create a react-redux container:

const mapDispatchToProps = () => ({
  onSubmit: data => alert(JSON.stringify(data)),
});
const SampleFormContainer = connect(null, mapDispatchToProps)(SampleFormWrapped);


// 4. finally, export the form component

export default SampleFormContainer;

```

#### 4. Update the index.js to export the new form

```javascript
// ./src/index.js

import uiComponent from 'ui/sample-form/SampleForm'; // update this line with the new form
import reducer from 'state/main/reducer';

import enLocale from 'locales/en';
import itLocale from 'locales/it';

import { name as id } from '../package.json';


import './sass/index.css';


const plugin = {
  id,
  menuItemLabelId: 'menu.itemLabel',
  uiComponent,
  reducer,
  locales: [
    enLocale,
    itLocale,
  ],
};

export default plugin;

```

#### 5. Rebuild the plugin

Cd into ui-component-sample directory and run
```bash
npm run build-plugin
```
