**Fast Content Edit Plugin**

**Code**: ```jpfastcontentedit```

**Description**

Fast Content Edit Plugin allows administrator to edit contents directly from the portal pages.

**Installation**

In order to install Fast Content Edit Plugin, you must insert the following dependency in the pom.xml file of your project:

````
<dependency>
      <groupId>org.entando.entando.plugins</groupId>
      <artifactId>entando-plugin-jpfastcontentedit</artifactId>
      <version>${entando.version}</version>
      <type>war</type>
 </dependency>
```
**Configuration**

In the Widgets page of the Admin area of your project, you can see two widgets related to this plugin:

1. _Contents - Publish List of Editable Content_
2. _Content Editing Form_

You can publish the _Contents-Publish List of Editable Content_ Widget in a dedicated page 
in order to show the contents's list that you are already published.
In an other page, you can publish the _Content Editing Form_ widget through which you can have forms to edit your published contents.


