**MyPortalPlus App**

Code: ```jpportalplus```

**Description**

MyPortalPlus is an App gives the possibility to add two new features to:

1. ‘Drag & drop’ functionality makes the widget dynamic within the grids as it can be selected and moved around page at will.
2. Swappable widget can be reduced in size, iconized at will by the user.

**Installation**

In order to install the MyPortalPlus plugin, open the pom.xml file of your project and insert the following dependency:

``` 
<dependency>
       <groupId>org.entando.entando.plugins</groupId>
       <artifactId>entando-plugin-jpmyportalplus</artifactId>
       <version>${entando.version}</version>
       <type>war</type>
</dependency> 
```

**Configuration**

The configuration and the use of MyPortalPlus app is quite user-friendly;  the user is only required to choose one or more widget among available ones and to create a list of widgets that then will be set as swappable. 
Keep in mind that, in order to use MyPortalPlus correctly, user is required to use the associated page model called “My Portal”. If any another existing model will be selected user won’t be able to use the app’s functionality.
