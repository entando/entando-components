#CAS Client Plugin

**_Installation and configuration of the Entando jpcasclient plugin_**

**_Table of Contents_**

Introduction
Target Audience
Prerequisites
Overview
Technical specification
Packages
Installation in a development environment
Installation in a production environment
Configuration
Login widget
Introduction

The purpose of this guide is to provide a complete description of the Entando CAS Clientplugin whose code is jpcasclient for the Entando platform.

Target Audience

This guide is intended for both administrators and developers who wish to explore the capabilities of the Entando Plugin CAS Client and are considering a possible integration into a running production environment or in a Development Environment.

Prerequisites

In order to take maximum advantage from the present guide, it is necessary to have basic knowledge of the Java platform, the servlet engine Apache Tomcat, PostgreSQL (or MySQL) DBMS and the Entando platform.

Moreover, it's necessary to have read the Plugin Pattern for the installation procedure and an explanation of the standard directory layout.

top

Overview

The CAS Client plugin provides authentication against an external CAS authentication server (a quickstart tutorial can be found here). The authentication process is changed in that after the user has been validated by the CAS server the Entando user with the same username will be loaded (along with his permissions) otherwise it will be created on the fly with no permissions. This behaviour is different from the previous version of the plugin which required the same user to exist in both the CAS server and the Endando portal (however passwords were not required to match).

This plugin adds a new widget used to authenticate to an external CAS server. This widget can coexist with the usual login widget which obviously does not authenticate against CAS.

On every logout the current user is purged from both CAS and Entando session.

Though the plugin installation is not difficult at all, we are going to modify the system tables, so a backup of your database is highly recommended. Furthermore, you may be required to customize the scripts to your needs before installation.

For the purpose of the current guide, a few Maven and Ant commands are shown: your IDE has probably the ability to execute those commands for you in background.

Technical specification

jpcasclient is a modification plugin, because we have to change the web.xml of the application.

Packages

jpcasclient directories are organized following the Maven Standard Directory Layout as shown in the Plugin Pattern.

Installation in a development environment

It is worth noting that the plugin installation is greatly changed from the previous releases (thank you, Maven!).

As always when it comes down to install new things, stop your servlet container before moving on.

Open the src/main/config/web.xml and add the following filter declarations:

       <!-- CAS logout // start -->
        <filter>
            <filter-name>CAS Single Sign Out Filter</filter-name>
            <filter-class>
                com.agiletec.plugins.jpcasclient.aps.filter.CasSingleSignOutFilter
            </filter-class>
        </filter>
        <filter-mapping>
            <filter-name>CAS Single Sign Out Filter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>
        <!-- CAS logout // end -->
Open the pom.xml of your project: locate the <dependencies> tag toward the end of the file, after the <build> tag; if the dependencies tag does not exist just create a new one just after the closure of the build tag.

Add the following snippet inside the dependencies:

    <dependency>
        <groupId>org.entando.entando.plugins</groupId>
        <artifactId>entando-plugin-jpcasclient</artifactId>
        <version>${entando.version}</version><!-- version. Don't remove this comment. -->
        <type>war</type>
    </dependency>
You are done! You can verify the correct installation of the plugin going to the administration area and checking for the new item in the Plugins menu.

top

Installation in a production environment

From now we will use the name myportal when referring to your deployed Entando application or, in other words, to the artifact ID of the deployed portal.

All Entando plugins can be downloaded from the Maven Central repository, just filter by code and by version.

To install jpcasclient in a production environment the file entando-plugin-jpcasclient-3.2.0.war is needed; we will refer to this file as WAR package.

The WAR package might contain the dependencies of other plugins; when performing copy operations you may accidentally overwrite your previous customizations of the JSP files, so you are warmly recommended to create a backup of your installation.

Note: that the integration activity must be performed after the servlet container has been stopped.

copy the content of WEB-INF/lib directory of the WAR package, to myportal/WEB-INF/lib/ directory

create the directory myportal/WEB-INF/plugins/ if it does not exist. Copy the content of *WEB-INF/plugins/*directory of the WAR package, to myportal/WEB-INF/plugins/

copy the content of resources/plugins directory of the WAR package to myportal/resources/plugins/ directory

top

Configuration

The configuration of the plugin is accessed form the left panel in the administration area Plugins → CAS Client → Configuration



The Active checkbox is the Status section is used to toggle authentication on/off.

In the Configuration section:

CAS Server Login URL is the URL of the login page of the CAS server. The system will use this address every time users must insert their credentials.

A possible example of CAS server login page: https://192.168.0.115:8443/cas/login

CAS Server Logout URLis the URL of the logout page of the CAS server. The system will use this address to perform the logout from both the CAS and Entando portal.

A possible example of CAS server logout page: https://192.168.0.115:8443/cas/logout

CAS Server Validate URLis the URL of the CAS page used for validation

A possible example of CAS server validation page: https://192.168.0.115:8443/cas/validate

Portalbase URL is the base URL of the Entando portal to be used for building CAS links

A possible address would be http://192.168.1.115:8080

Page for non authorized users is the code of the page for those users who have successfully authenticated to the CAS server but are not yet permitted to access.

Realm the value specified is given to CAS authenticated users whose principal have no domain extension assigned by the CAS server

Login widget

Administrators place the Login – with CAS widget in a page of choice or swapped with the standard login



The login widget is used to enforce CAS authentication and requires no configuration: if the portal is going to support CAS authentication only, the standard login widget should not be not used.

The administration area login page has a link to the CAS server login page, as shown in the picture below:



The login process is as follows: the Login with CAS widget has a link to the CAS server: at this point users are required to enter their CAS credentials. Upon a successful login users are redirected back to the Entando portal with a welcome message and some login information: in this respect the widget behaves the same of the standard login widget.

top
