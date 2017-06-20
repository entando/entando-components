**Calendar Plugin**

**Code**: ```jpcalendar```

**Description**

The _Calendar plugin lets users to share activities, events, and news through a calendar on a monthly bases. Users can see with the events of the day.

**Installation**

In order to install the _Calendar App, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
    <groupId>org.entando.entando.apps</groupId>
    <artifactId>entando-app-calendar</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
        
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpcalendar</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>

```

**Configuration**

Your Content type need have two dates attribute that must be set to On the toggle "Can be used as a filter in lists". 
