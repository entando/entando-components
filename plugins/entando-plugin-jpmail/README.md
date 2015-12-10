**Mail Plugin**

**Code**: ```jpmail```

**Description**

Mail Plugin is a component that let users configure SMTP server and create new senders. 
This plugin supports other Entando components as ```jpuserreg``` or ```jpnewsletter```.

**Installation**

In order to install the Mail plugin, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpmail</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
````


**Configuration**

From Entando’s back office, you have to:

	1. configure the SMTP server 
	2. manage sender users 
