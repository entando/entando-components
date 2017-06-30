**User registration**

**Code**: ```jpuserreg```

**Description**

User Registration Plugin lets users register themselves to the portal and later to sign in.

**Installation**

In order to install the User Registration bundle and plugin, you should insert the following dependency in the pom.xml file of your project:

```

<dependency>
    <groupId>org.entando.entando.bundles.app-view</groupId>
    <artifactId>entando-app-view-userreg-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>

```

**Configuration**

In a nutshell, to configure this plugin you have to create a profile type and then configure the Widgets accordingly. You have a number of administrative decisions to take and some email templates to prepare too.

From Entando's back office, you have to:

* configure the SMTP server (jpmail configuration)
* manage sender users (jpmail configuration)
* configure user registration section.

The UserReg plugin is accomplished with the following Widgets that you can find in the Widgets page of the Entando back office:

1. Login Form (with registration management) displays a sign in form with a link to edit the profile of the current logged user.

2. User Activation allows to user lands after having clicked on the verification token received in the mail.

3. User Reactivation allows to user lands after having clicked on the verification token received by mail.

4. User Recover let the user retrieve access credentials by providing the correct username or email address.

5. User Registration let the user start the registration process.

6. User Registration (with profile choice the same as above) let the user start the registration process with the difference that users can choose their profile type among those made available.

7. User Suspension let user suspend their own account.

Now it's time to configure the page and select the frame where to place the Widgets. Then you have to configure the User Registration Widget.
