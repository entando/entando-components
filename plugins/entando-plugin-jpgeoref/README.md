**Georef Plugin**

**Code**: ```jpgeoref```

**Description**

The _Georef_ plugin lets administrator add geographic reference to contents.

**Installation**

In order to install the Georef App, you must insert the following dependency in the pom.xml file of your project:

```
<dependency>
      <groupId>org.entando.entando.plugins</groupId>
      <artifactId>entando-plugin-jpgeoref</artifactId>
      <version>${entando.version}</version>
      <type>war</type>
 </dependency>
```

**Configuration**

Your Content type need have coords attribute and role jpgeoref.

For use this go in your page and add the widgets
