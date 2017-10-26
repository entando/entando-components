**Versioning**

**Code**: ```jpversioning```

**Description**

The Versioning Plugin allows to trace, and restore selectively, the versions of single contents. 
This plugin adds new administration interfaces related to the content tracking; the resources - here intended as attachments and images – receive a slightly different treatment: changes across the same element are not traced, instead a backup of all the deleted resources is kept.
Please also be aware that a resource referenced by older versions of the content cannot be deleted to avoid inconsistency on restore.
Though the plugin installation is not difficult at all, we are going to modify the system tables, so a backup of your database is highly recommended. Furthermore, you may be required to customize the scripts to your needs before installation.

**Installation**

In order to install the Versioning Plugin, you must insert the following dependency in the pom.xml file of your project:
```
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpversioning</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**Configuration**

You can access to Versioning Plugin’s functionality through the usual left menu: _Plugins_ -> _Versioning_ in the Administration Area.
Selecting _Contents_ from the _Plugins_ menu, you can access to the _Contents_ history management.

Clicking on the content description you can access to the history of the selected content.
For each content we are given the possibility to manage various version by clicking on the description as well as some information, 
such as the date of the modification the user which made the changes.

Regarding the version number's rule, the content version is characterized from the pattern ```<major_number>.<minor_number>```, 
a new version of the content always increases the minor number. The major number is incremented (and the minor number is set to zero) only when a content is published.

You can also restore the content to the wanted version by clicking the arrow icon, or delete the record 
(version numbers don't change in this case).

Please note that the restore action when confirmed, loads only a previous version of the content. 
You have to save your changes with the Save button to finalize the restore process. 
Naturally the version replaced is tracked by the plugin.

Regarding Resource (Attachments and Images) are traced only when deleted. That is, if you edit a resource and change the file or the description your changes are not saved.

Resources are placed in a _recycle bin_ so that they can be later restored only when deleted.
The _recycle bin_ can be access from the left menu _Plugins_ → _Versioning_ → _Images_ or _Attachments_.
You can restore and delete permanently the resources using the arrow and the remove icons.


