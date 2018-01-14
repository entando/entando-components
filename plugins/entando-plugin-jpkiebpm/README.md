**KIE-BPM connector**

**Code**: ```jpkiebpm```

**Description**

This plugin connects to a given KIE server to display the form and human tasks of a process of choice. It is also possible
display the schema of the process as a image.

**Installation**

In order to install the KIE BPM plugin, you must insert the following dependency in the pom.xml file of your project:

```
<dependency>
   <groupId>org.entando.entando.plugins</groupId>
   <artifactId>entando-plugin-jpkiebpm</artifactId>
   <version>${entando.version}</version>
   <type>war</type>
</dependency>
```

The project *must* be generated with the [Inspinia archetype](https://github.com/entando/entando-archetypes)

