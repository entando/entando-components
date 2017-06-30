**Newsletter**

**Code**: ```jpnewsletter```

**Description**

The _Newsletter_ plugin enable the administrator to send contents via email to specific subscribers. Subscribers can be both registered who agreed to the service or visitors that subscribed the newsletter providing their email address. The newsletter delivery can be scheduled or activated in any moment by the administrator.

The content sent by the newsletter service can be associated to one or more categories or to no categories at all. Registered users can subscribe the newsletter and choose the categories of interest (or none to receive the newsletter in its entirety) depending on the profile configuration created by the administrator. It is responsibility of the administrator to create a profile with at least a boolean attribute to toggle the activation of the service.

Portal visitors (or guests), without an associated Entando user, have the option to provide their email address through a registration form. The system sends an email containing a link to confirm the registration. Such users, being unable to select the categories will receive only public contents or, in other words, those contents associated to the free group: categories are not taken into account.

**Installation**

In order to install the Newsletter plugin, you should insert the following dependency in the pom.xml file of your project:

```  
<dependency>
        <groupId>org.entando.entando.bundles.app-view</groupId>
        <artifactId>entando-app-view-newsletter-default</artifactId>
        <version>${entando.version}</version>
        <type>war</type>
</dependency>
```

**How to use**

This plugin offers many management interfaces, you have the following sections:

1. Configure;
2. Delivery Queue;
3. Contents;
4. Guest Subscribers.

All of these menu are easily accessed via the Plugins → Newsletter menu:

* The Configure section enables the administrator to:
    * specify the content type eligible for delivery (and the relative content models);
    * activate the scheduler and set the delivery time;
    * associate the categories to a specific profile attribute;
    * compose the HTML and plain text templates of the mail of the newsletter;
    * compose the templates of the mail used for the subscription of the occasional user.

* The Contents section enable the administrator to:
    * decide which contents will be included in the next delivery.

* The Delivery Queue section enable the administrator to:
    * remove anytime contents from the delivery list anytime by clicking on the removal icon.

* The Guest Subscribers section:
    * gives a list of the visitors who have subscribed the newsletter service.
    * allow the administrator to delete those users anytime from the list so removing them from the receivers: the removal, carried out by clicking on the remove icon, is permanent and cannot be undone.

Finally, the administrator must choose the page where the Widget Newsletter - Subscription is placed; then after clicking in the confirmation link received in the mail, the user receives confirmation from the Newsletter – Subscription ConfirmationWidget.  Clicking on the removal link of the newsletter received, the user lands where the Newsletter – Unsubscription widgetis placed.

Note

Due to its nature, Newsletter app requires as a prerequisite the installation of _jpmail_ plugin.