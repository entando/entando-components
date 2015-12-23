**RSS Aggregator Plugin**

**Code**: ```jprssaggregator```

**Description**

The Rss Aggregator plugin converts the feeds of a specified URL into standard Entando contents. The RSS sources are organized in lists: each of the sources is periodically updated so to have the corresponding contents always synchronized. Once imported, the RSS feeds are Entando contents in all respects and are treated in the same way of the others. Deleting the RSS channel does not result in the deletion of the imported contents.
Though the plugin installation is not difficult at all, we are going to modify the system tables, so a backup of your database is highly recommended. Furthermore, you may be required to customize the scripts to your needs before installation.

**Installation**

```
<dependency>
	<groupId>org.entando.entando.plugins</groupId>
	<artifactId>entando-plugin-jprssaggregator</artifactId>
	<version>${entando.version}</version>
	<type>war</type>
</dependency>
```

**Configuration**

In the portal administration you can create, modify or eliminate RSS sources.
A new source is characterized by:

	* Content Type that shows only all those content types suitable to be used by the RSS aggregator
	* Description
	* Url
	* Refresh Delay
	* Categories Management where you can optionally add the category whose retrieved contents
