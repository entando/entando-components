**LDAP Plugin**

**Code**: ```jpldap```

**Description**

The LDAP plugin lets Entando to authenticate users from LDAP server and increase the base of system users. These remote users are handled in the same way local users are; moreover, the plugin may leave the authentication process to the LDAP server without altering the way authorizations are handled: both local and remote users are managed through the standard users management interface.

**Installation**

In order to install the LDAP Plugin, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
     <groupId>org.entando.entando.plugins</groupId>
     <artifactId>entando-plugin-jpldap</artifactId>
     <version>${entando.version}</version>
     <type>war</type>
</dependency>
```

**Configuration**

In the Administration Area, the LDAP configuration settings added should appear in a dedicated section of the General Settings under the Miscellaneous.

You should configure some parameters (not all of the parameters above are mandatory):

* Provider URL
* Security Principal
* Security Credentials
* User Object Class Name
* User Id Attribute Name
* Filter Group
* Group Attribute Name
* Search Result – Max Size
* Active user Editing
* User Base DN
* User object classes
* OU object classes
* User Real attribute
* User password Attribute name
* User password Algorithm
