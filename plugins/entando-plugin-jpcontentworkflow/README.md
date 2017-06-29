**Content Workflow**

**Code**: ```jpcontentworkflow```

**Description**

The Content Workflow Plugin let administrators create a precise process to follow, in order to write and approve a new content.

**Installation**

In order to install the Content Workflow Plugin, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpcontentworkflow</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**Configuration**

Using a suitable interface present in the administration area of your project, the Content Workflow Plugin allows to create and configure a custom workflow for each type of content defined within a portal.

Through the Workflow configuration page (in the Back Office), you can configure the intermediate steps to manage approval of the contents and assign an user’s role to each step.

Through the Notifier Configuration page (in the Back Office), you can notifier the content’s updates. 
