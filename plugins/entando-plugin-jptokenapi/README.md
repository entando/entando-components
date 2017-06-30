**Token API**

**Code**: ```jptokenapi```

**Description**

The Token API plugin lets users access Entando platform REST APIs through a security token.

**Installation**

Open the pom.xml of your project: locate the tag toward the end of the file, after the tag; if the dependencies tag does not exist just create a new one just after the closure of the build tag.

Add the following snippet inside the dependencies:

```

<dependency>
    <groupId>org.entando.entando.bundles.app-view</groupId>
    <artifactId>entando-app-view-tokenapi-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>

```

**Configuration**

By installing the bundle and the Token API plugin, a single widget is installed in the administration area; if you configure a page with the widget, in the page you can see the token corrisponding to your username, if you are logged in.
