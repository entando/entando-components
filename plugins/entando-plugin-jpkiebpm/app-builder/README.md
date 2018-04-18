
# Entando ui-component-sample

This is a sample Entando plugin, it can be cloned and used as a starting point to create a new Entando plugin.

## Entando plugin requirements

A valid Entando plugin must match the following constraints:

- it must be a valid npm module
- it must be compiled with ```npm run build-plugin``` command
- the compiled bundle has the structure:
```
    PLUGIN_DIR
    └── pluginBuild
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
  - **uiComponent**: a React Component representing the plugin UI
  - **locales**: an array with the following schema:
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
