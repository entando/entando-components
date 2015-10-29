INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jprssaggregator', 'mapping per la conversione dei feed rss in contenuti', '<?xml version="1.0" encoding="UTF-8"?>
<mappings>
	<mapping contentType="RSS" insertOnLine="true">
		<attributes>
			<element source="title" dest="Title" default="jprssaggregator_NULL_TITLE" />
			<element source="link" dest="Link"  />
			<element source="description.value" dest="TextBody" default="jprssaggregator_NULL_BODY" />
		</attributes>
		<descr type="expression" maxLength="100" default="jprssaggregator_NULL_DESCR">title</descr>
		<groups main="free">
			<!-- <group name="administrators" /> -->
		</groups>
		<apsAggregatorItem>
			<bean source="descr" dest="Source" />
		</apsAggregatorItem>
	</mapping>
</mappings>');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_TITLE', 'it', 'Titolo non disponibile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_TITLE', 'en', 'Title unavailable');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_BODY', 'it', 'Corpo testo non disponibile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_BODY', 'en', 'Body unavailable');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_DESCR', 'it', 'Descrizione non disponibile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_NULL_DESCR', 'en', 'Description unavailable');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_RSS_READ_MORE', 'en', 'View details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprssaggregator_RSS_READ_MORE', 'it', 'Continua');

