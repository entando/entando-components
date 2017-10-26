**Action Logger**

**Code**: ```jpactionlogger```

**Description**

The _Action Logger_ plugin tracks the Back-Office operations. 
For each logged action are recorded the _Date_ and _Time_, the _Namespace_, the _User_ and the _Parameters_ involved.
The administrator can also prevent some parameters (e.g. password) from being traced.

**Installation**

In order to install the Action Logger plugin, you must insert the following dependency in the pom.xml file of your project:

```
<dependency>
      <groupId>org.entando.entando.plugins</groupId>
      <artifactId>entando-plugin-jpactionlogger</artifactId>
      <version>${entando.version}</version>
      <type>war</type>
 </dependency>
```

**How to use**

You have to create a new user whose name and password are _newuser_ and _mypassword_, respectively. 

Access interface from Plugins section of the Menù -> Action Logger: you are presented with all the actions tracked.
We see that the _admin_ created a new user named _newuser_. The careful reader might have noticed that though the _mypassword_ parameter was not recorded, the password used for confirmation is clearly displayed, and this is not what we intended: adding the exclusion for the “_passwordConfirm_ parameter solves the problem.
