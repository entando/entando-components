**Calendar**

**Code**: ```jpcalendar```

**Description**

The _Calendar_ plugin lets users to share activities, events, and news through a calendar on a monthly bases. Users can see with the events of the day.

**Installation**

In order to install the _Calendar_ plugin, you should to insert the following dependency in the pom.xml file of your project:

```  
<dependency>
	<groupId>org.entando.entando.bundles.app-view</groupId>
	<artifactId>entando-app-view-calendar-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**How to use**

* Move on the menù _APPS_ / _CMS_ / _Content Type_
* Create or manage a exists Content Type
* Add two Date type attributes and set toggle ("Can be used as a filter in lists") to On
* Move on section _Integrations_ / _Components_ / _Calendar_
* Use content and attributes created before
* For use plugin, go to the widget
