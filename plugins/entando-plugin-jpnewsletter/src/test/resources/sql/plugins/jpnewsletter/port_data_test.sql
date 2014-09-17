INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpnewsletter_config', 'Configurazione servizio Newsletter', '<newsletterConfig>
	<scheduler active="false" onlyOwner="false" delayHours="24" start="01/03/2009 11:08" />
	<subscriptions allAttributeName="" >
		<descr>
			mappa delle corrispondenze tra attributo buleano 
			"sottoscrizione categoria newsletter" di profilo utente 
			e categoria di contenuto/tematismo-newsletter
		</descr>
		<subscription categoryCode="cat1" attributeName="boolean1" />
		<subscription categoryCode="evento" attributeName="boolean2" />
	</subscriptions>
	<contentTypes>
		<contentType code="ART" defaultModel="2" htmlModel="3" />
	</contentTypes>
	<mail alsoHtml="true" senderCode="CODE1" mailAttrName="email" unsubscriptionPage="newsletter_unsubscribe" >
		<subject><![CDATA[Oggetto della mail]]></subject>
		<htmlHeader><![CDATA[<strong>Header html della mail</strong>]]></htmlHeader>
		<htmlFooter><![CDATA[<strong>Footer html della mail</strong>]]></htmlFooter>
		<htmlSeparator><![CDATA[Separatore html della mail]]></htmlSeparator>
		<textHeader><![CDATA[Header text della mail]]></textHeader>
		<textFooter><![CDATA[Footer text della mail]]></textFooter>
		<textSeparator><![CDATA[Separatore text della mail]]></textSeparator>
		
		<subscriberHtmlFooter><![CDATA[Clicca sul link per cancellare la sottoscrizione <a href="{unsubscribeLink}" >CONFERMA</a></body></html>]]></subscriberHtmlFooter>
		<subscriberTextFooter><![CDATA[Clicca sul link {unsubscribeLink} per cancellare la sottoscrizione]]></subscriberTextFooter>
		
		<subscription pageCode="newsletter_terminatereg" tokenValidityDays="90" >
			<subject><![CDATA[Conferma la sottoscrizione al servizio di Newsletter]]></subject>
			<htmlBody><![CDATA[<p>Clicca sul link per confermare <a href="{subscribeLink}" >***CONFERMA***</a></p>]]></htmlBody>
			<textBody><![CDATA[Clicca sul link {subscribeLink} per confermare]]></textBody>
		</subscription>
	</mail>
</newsletterConfig>');