INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpnewsletter_config', 'Configurazione servizio Newsletter', '<?xml version="1.0" encoding="UTF-8"?>
<newsletterConfig>
	<scheduler active="false" onlyOwner="false" delayHours="24" start="27/08/2009 11:00" />
	<subscriptions allAttributeName="NewsletterAllContents" />
	<contentTypes />
	<mail alsoHtml="true" senderCode="CODE1" unsubscriptionPage="unsubscription_page">
		<subject><![CDATA[Newsletter]]></subject>
		<htmlHeader><![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head></head>
<body><h1>Newsletter</h1><p>Hi, <br />The latest news are:</p>]]></htmlHeader>
		<htmlFooter><![CDATA[<br /><br />Enjoy the reading<br /></body></html>]]></htmlFooter>
		<htmlSeparator><![CDATA[<br /><br /><hr /><br /><br />]]></htmlSeparator>
		<textHeader><![CDATA[The latest news are: 

###########################################################################]]></textHeader>
		<textFooter><![CDATA[###########################################################################

Buona lettura.]]></textFooter>
		<textSeparator><![CDATA[###########################################################################]]></textSeparator>
		<subscriberHtmlFooter><![CDATA[<p>Follow this <a href="{unsubscribeLink}">link</a> to unsubscribe, or copyAndPast this url:</p>
<p>{unsubscribeLink}</p>]]></subscriberHtmlFooter>
		<subscriberTextFooter><![CDATA[Follow this <a href="{unsubscribeLink}">link</a> to unsubscribe, or copyAndPast this url 
{unsubscribeLink}]]></subscriberTextFooter>
		<subscription pageCode="subscription_page" tokenValidityDays="1">
			<subject><![CDATA[Newsletter Subscription]]></subject>
			<htmlBody><![CDATA[<p>Follow this <a href="{subscribeLink}">link</a> to subscribe newsletter, or copyAndPast this url:</p>
<p>{subscribeLink}</p>]]></htmlBody>
			<textBody><![CDATA[Follow this <a href="{subscribeLink}">link</a> to subscribe newsletter, or copyAndPast this url 
{subscribeLink}]]></textBody>
		</subscription>
	</mail>
</newsletterConfig>');

