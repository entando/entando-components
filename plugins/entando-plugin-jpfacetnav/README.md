**Faceted Navigation**

**Code**: ```jpfacetnav```

**Description**

The Faceted Navigation plugin enables faceted navigation of the portal contents organized by categories.

**Installation**

In order to install the Faceted Navigation plugin, you must insert the following dependency in the pom.xml file of your project:

```  
<dependency>
    <groupId>org.entando.entando.bundles.app-view</groupId>
    <artifactId>entando-app-view-facetnav-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**How to use**

Configure a page with the two widgets - "Facets Tree" and "Search Result" - and check it out, but pay attention to one thing: put the search result widget in a successive frame compared to the one with facets tree widget.
