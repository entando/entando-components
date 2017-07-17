**RSS**

**Code**: ```jprss```

**Description**

The jprss plugin allows to serve the portal contents through RSS channels.

**Installation**

In order to install the RSS plugin, you should to insert the following dependency in the pom.xml file of your project:

```  
<dependency>
    <groupId>org.entando.entando.bundles.app-view</groupId>
    <artifactId>entando-app-view-rss-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**How to use**

This plugin offers:

* An back-end interface used by the administrator to create and manage the contents served through RSS channel. This interface adheres strictly to the pre-existing back-end graphics and settings.

* The RSS Channels Widget for the front-end designed to serve the list of the links of the active channels. The link to RSS contents served in the front-end follows this pattern: ( http://ipaddress:8080/myportal/do/jprss/Rss/Feed/show.action?id ) where ipaddress is the ip of the myPortal portal and the id is the ID of the served content.