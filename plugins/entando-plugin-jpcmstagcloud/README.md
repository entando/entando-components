**CMS Tag Cloud**

**Code**: ```jpcmstagcloud```

**Description**

The CMS Tag Cloud plugin adds a tag-cloud in the portal which contains the categories of the most-tagged contents.

**Installation**

In order to install the CMS Tag Cloud plugin, you must insert the following dependency in the pom.xml file of your project:

```  
<dependency>
   <groupId>org.entando.entando.bundles.app-view</groupId>
   <artifactId>entando-app-view-cmstagcloud-default</artifactId>
   <version>${entando.version}</version>
   <type>war</type>
</dependency>
```

**How to use**

In the Categories section of the Administration area, you should to create appropriate categories. In the Content section, you should to create a list of contents; each content should be associated with the desired category.

In the Widget section, you can see two new widgets added to the system:

* _Tag Cloud_;
* _Publish a List of Tagged Contents_

You should configure each of the two widgets in a new different page.

The _Tag Cloud_ widget allows you to view, in an appropriate page, the categories in which the contents are been tagged.

The _Publish a List of Tagged Contents_ widget allows you to view the tagged contents list through the _Tag Cloud_ widget configuration.
