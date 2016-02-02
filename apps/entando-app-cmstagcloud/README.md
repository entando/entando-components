**CMS Tag Cloud App**

**Code**: ```cmstagcloud```

**Description**

The App adds a tag-cloud in the portal which contains the categories of the most-tagged contents.

**Installation**

In order to install the _CMS Tag Cloud_ App, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
   <groupId>org.entando.entando.apps</groupId>
   <artifactId>entando-app-cmstagcloud</artifactId>
   <version>${entando.version}</version>
   <type>war</type>
</dependency> 
```

The app requires as pre-requisite the dependency of the related bundle in the pom.xml file:

```
<dependency>
   <groupId>org.entando.entando.bundles.app-view</groupId>
   <artifactId>entando-app-view-cmstagcloud-default</artifactId>
   <version>${entando.version}</version>
   <type>war</type>
</dependency>
```

**Configuration**

In the _Categories_ section of the Administration area,  you should to create appropriate categories.
In the _Content_ section, you should to create a list of contents; each content should be associated with the desired category.

In the _Widget_ section, you can see two new widgets added to the system:

1. _tag Cloud_; 

2. _Publish a List of Tagged Contents_

You should configure each of the two widgets in a new different page.

The _tag Cloud_ widget allows you to view, in an appropriate page, the categories in which the contents are been tagged.

The _Publish a List of Tagged Contents_ widget allows you to view the tagged contents list through the tag Cloud widget configuration.
