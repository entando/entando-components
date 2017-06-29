# entando-components

```entando-components``` is one of the mainstays of the **Entando Platform**. 
It provides all functionality of specific software elements, called **Entando components** or **Entando extensions**, 
useful to extend the standard Entando platform features and/or use them to improve custom applications. 

**Entando** is the lightest, open source Digital Experience Platform (DXP) for Modern Applications. Entando harmonizes customer experience across the omnichannel (UX convergence) applying the techniques of modern software practices to enterprise applications (modern applications). Entando can be used to modernize UI/UX layers on top of existing applications or to build new generation applications aligned to UI/UX best practices, across different industries and use cases.

The Entando components are: 

* **Bundles** extend functionality of portal-ui component and/or views’ functionality of the other Entando components.
* **Plugins** extend functionality of engine, and admin-console components.
* **Apps** extend functionality of engine, admin-console and portal-ui components. Apps are composed of business logic, 
and one or more views. 

The Entando components project, based on Apache Maven, is organized in three modules including 
all Entando extensions ready to use just now:

1. ```apps``` module, including all Apps.

2. ```plugins``` module, including all Plugins.

3. ```ui-bundles``` module, including all Bundles.

To create a new Entando component, for example a new Plugin, you have to use the Entando archetype for plugin: https://github.com/entando/entando-archetypes.

Regarding the App, currently a related archetype is not available. An App is a component that has the pom.xml file where you can set the dependencies with the appropriate Plugin and Bundle for the view.
Shortly, the archetype for App components will be create.


```entando-components``` depends strictly on ```entando-core``` and ```entando-archetypes```.

Entando core and Entando archetypes are Open Source projects; more information about them can be found respectively in 
https://github.com/entando/entando-core and 
https://github.com/entando/entando-archetypes.

For latest updated news, please visit the company websites: http://www.entando.com.

For any support request, please use [Twitter](https://twitter.com/Entando).

Enjoy!

_The Entando Team_


