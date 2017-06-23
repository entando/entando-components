**Survey Plugin**

**Code**: ```jpsurvey```

**Description**

The _Survey_ plugin lets administrator create and manage Survey and Questionnaire. From the configuration pages administrators can access the survey and questionnaire lists, edit or create new survey and questionnaire, decide whether publish them and access the results.

**Installation**

In order to install the _Survey_ App, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
    <groupId>org.entando.entando.apps</groupId>
    <artifactId>entando-app-survey</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
        
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpsurvey</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>

```

**Configuration**
In Integrations, Components you can create and manage Survey and Questionnaire.

