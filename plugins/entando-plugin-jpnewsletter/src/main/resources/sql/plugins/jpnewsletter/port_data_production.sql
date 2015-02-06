INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpnewsletter_config', 'Configurazione servizio Newsletter', '<newsletterConfig>
	<scheduler active="false" onlyOwner="false" delayHours="24" start="27/08/2009 11:08" />
	<subscriptions allAttributeName="NewsletterAllContents" >
		<subscription categoryCode="diritto_civile" attributeName="NewsletterDirittoCivile" />
		<subscription categoryCode="diritto_penale" attributeName="NewsletterDirittoPenale" />
	</subscriptions>
	<contentTypes />
	<mail alsoHtml="true" senderCode="CODE1" >
		<subject><![CDATA[Newsletter]]></subject>
		<htmlHeader><![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head></head>
<body><h1>Newsletter</h1><p>Hi, <br />The latest news are:</p>]]></htmlHeader>
		<htmlFooter><![CDATA[<br /><br />Enjoy the reading<br /></body></html>]]></htmlFooter>
		<htmlSeparator><![CDATA[<br /><br /><hr /><br /><br />]]></htmlSeparator>
		<textHeader><![CDATA[The latest news are: 

###########################################################################

]]></textHeader><textFooter><![CDATA[

###########################################################################

Buona lettura.]]></textFooter>
		<textSeparator><![CDATA[

###########################################################################

]]></textSeparator>
	</mail>
</newsletterConfig>');

