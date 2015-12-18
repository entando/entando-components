**Mail Plugin**

**Code**: ```jpmail```

**Description**

Mail Plugin is a component that let Entando users configure SMTP server and create new senders to send email messages.

**Installation**

In order to install the Mail Plugin, you should to insert the following dependency in the pom.xml file of your project:

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

 1. create new **Sender**: you have to create a new Sender with a _Code_ and an _Email_. You can have a List of Senders; the Sender will be choosed on the base of its Code. 
 2. configure the **SMTP server**: you have to active the SMTP server, and then to set _Host_ (mandatory), _Port_, _Security Certification_, _Timeout_ parameter.
 
Please leave Username and Password blank if the SMTP does not require authentication.
 
 
