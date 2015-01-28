# entando-components

```entando-components``` is one of the mainstays of the **Entando Platform**. 
It provides all functionality of specific software elements, called **Entando components** or **Entando extensions**, 
useful to extend the standard Entando platform features and/or use them to improve custom applications. 

The Entando components are: 

* **Bundles** extend functionality of portal-ui core component and/or views’ functionality of the other Entando components.
* **Plugins** extend functionality of engine, and admin-console core components.
* **Apps** extend functionality of engine, admin-console and portal-ui core components. Apps are composed of business logic, 
and one or more views. A Plugin and one or more Bundles build an Entando App.

The Entando components project, based on Apache Maven, is organized in three modules including 
all Entando extensions ready to use just now:

1. ```apps``` module, including all Apps.

2. ```plugins``` module, including all Plugins.

3. ```ui-bundles``` module, including all Bundles.

To create a new Entando component, for example a new Plugin, you have to use the Entando archetype for plugin. 

```entando-components``` depends strictly on ```entando-core``` and ```entando-archetypes```.

Entando core and Entando archetypes are Open Source projects; more information about them can be found respectively in https://github.com/entando/entando-core and 
https://github.com/entando/entando-archetypes.

For latest updated news, please visit the company websites: http://www.entando.com.

For any support request, please use [Twitter] (https://twitter.com/Entando).

Enjoy!

_The Entando Team_


