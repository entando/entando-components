INSERT INTO sysconfig( version, item, descr, config ) VALUES ('production', 'jpcontentworkflow_config', 'Workflow - Configurazione Workflow Contenuti', '<contenttypes/>');

INSERT INTO sysconfig( version, item, descr, config ) VALUES ('production', 'jpcontentworkflow_notifierConfig', 'Workflow - Servizio Notifica Cambio Stato Contenuti', '<?xml version="1.0" encoding="UTF-8"?>
<notifierConfig>
	<scheduler>
		<active value="true" />
		<delay value="24" />
		<start value="20/11/2009 10:08" />
	</scheduler>
	<mail senderCode="CODE1" mailAttributeName="email" html="true">
		<subject><![CDATA[[Subject]: Insert your subject]]></subject>
		<header><![CDATA[<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head><style type="text/css">.body {padding: 1em;} body {color:#333333;font-family:Verdana,Arial,sans-serif;font-size:100.01%;margin: 0 auto;max-width: 1200px;} .header {padding: 1em; background-color: #E5ECFA;color: #0F4780; border-top: 0; border-bottom:2px solid #0F4780; } .footer {background-color:#E5ECFA;border-bottom:2px solid #0F4780;border-top:2px solid #0F4780;padding:0.1em 0.5em; }</style>	</head>
<body>
<div class="header"><h1>Your Company</h1><h2>subject</h2></div>
<div class="body">
<p>Ciao ${user}, di seguito l''elenco dei contenuti per cui è richiesto il tuo intervento
<ul>]]></header>
		<template><![CDATA[<li><h3><a href="{link}">{descr}</a></h3><p>{type}, {status}</p></li>]]></template>
		<footer><![CDATA[</ul></div><div class="footer"></div></body></html>]]></footer>
	</mail>
</notifierConfig>');

