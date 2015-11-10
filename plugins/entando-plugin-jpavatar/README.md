**Avatar App**

Code: ```jpavatar```

**Description**

Avatar is an Entando App that allows to registered users to manage in the Back Office pictures associated to their profile. 


**Installation**

In order to install the Avatar App, you should to insert the following dependency in the pom.xml file of your project:

```
<dependency>
      <groupId>org.entando.entando.apps</groupId>
      <artifactId>entando-app-avatar</artifactId>
      <version>${entando.version}</version>
      <type>war</type>
 </dependency>
```

**Configuration**

The Avatar App is composed by two items:

1. Avatar
2. Configuration

The Avatar item allows user to choose a profile’s image; you can find it in the section “My profile” of the Admin area.
The Configuration item allows user to choose the configuration’s modality of the user’s avatar. User has three possibilities:

1. Default system configuration allows to choose an image from
2. Local images allows to choose an image from local file system
3. Gravatar allows to choose an image by Gravatar integrated in Entando. 

In the back-office’s Setting, user can enable Gravatar integration. If the Gravatar is enabled, the user’s profile associated with the image derived from Gravatar.
Note: You should choose:
* an image’s format as png or jpg.
* an image’s dimension equal to 56x56 px.
