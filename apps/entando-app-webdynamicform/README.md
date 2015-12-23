**Web Dinamic Form App**

**Code**: ```jpwebdynamicform```

**Description**

The Web Dinamic Form App  enables the guests of the portal to initiate a two-ways communication with the managers of the portal via notifications sent by email: in this respect we can consider the Dynamic Form in term of a message sent from the user to the administrator. By guests we intend unregistered portal users.
Please be aware that this app wasn't conceived as a message exchange tool: the administrator won't see any response from the visitor anywhere in the backend; the communication is intended to continue via email.
The administrator might choose to write back to the user by email or to use the backend interface provided: in this case all the responses toward the visitor will be recorded (and optionally are made available for use by other plugins).
The App provides an interface in the portal through which the visitors submit a form to the administrator: this interface is capable of delivering dynamic forms, customizable at will thanks to the Entando entity engine.
Administrators have a number of configuration decisions to take through the management interface in the backend:
create the proper entity types for the forms
decide how the message will be handled by the system that is, whether it will be stored in the database or notify a new message to a list of specific users configure the page(s) where the form(s) will be published

**Dependency**

```jpwebdynamicform``` requires as a prerequisite the _Email Sender_ plugin jpmail.

**Installation**

In order to install the Web Dinamic Form App, you should to 

```
<dependency>
      <groupId>org.entando.entando.apps</groupId>
      <artifactId>entando-app-avatar</artifactId>
      <version>${entando.version}</version>
      <type>war</type>
 </dependency>
```

**Configuration**

In order to configure the app, you should create at least one entity message for the form. This is done accessing the Message Types item in the menu of the plugin. A new Message is characterized by: 

* Key, three letters name which identifies uniquely the entity;
* Description, a human readable string;
* Attribute (Type), the administrator builds the entity adding the attributes desired using the Add button and then completing the configuration relative to that attribute. the Save button saves the entity and returns to the entity list.

Once you have chosen the desired Message Type click the go to the Configuration.

* The Local Message Storage checkbox makes the messages list available to third part software for import.
* The Sender represents the email used by the system for communication.
* The Email Attribute from Message is used to select the attribute of the entity (Dynamic Form) which holds the email address of the user.
* Object and Template Body are used to define the email template. Is it important to notice that the body of the email is a Velocity template;  you have two tokens,  $i18n and $message that will be respectively expanded with the appropriate label and with the various fields of the message.

For example, a given a Dynamic Form named “Company form”, composed with the following MonoText attributes:

  * Company
  * Address
  * email
  * Note

In the Automatic Notification the administrator inserts the email addresses of the people which are going to be notified whenever a new form is submitted.
The Active checkbox toggles the notification service: every time a visitor submits a form the people in the list of the recipients will receive a notification.

The Messages List is a management interface where we have the list of the forms/messages submitted by the visitors of the portal; you may Search by Type and, in the Base Search section, a submission period (From, To) and the Status (Answered / Not Answered or both) can be imposed as well.
