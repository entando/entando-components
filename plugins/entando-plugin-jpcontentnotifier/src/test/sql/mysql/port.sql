

INSERT INTO sysconfig( version, item, descr, config )
    VALUES ('test', 'jpcontentnotifier_config', 'Configurazione Notificatore Contenuti', '<?xml version="1.0" encoding="UTF-8"?>
<notifierConfig>
	<scheduler active="true" onlyOwner="true" delayHours="24" start="03/04/2009 18:25" />
	<mail senderCode="CODE1" mailAttrName="email" html="true" >
		<subject><![CDATA[Oggetto della mail di notifica]]></subject>
		<header><![CDATA[Inizio Mail (testata)<br/>]]></header>
		<templateInsert><![CDATA[<br />Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />]]></templateInsert>
		<templateUpdate><![CDATA[<br />Aggiornamento Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />]]></templateUpdate>
		<templateRemove><![CDATA[<br />Rimozione Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br />]]></templateRemove>
		<footer><![CDATA[<br />Fine Mail (footer)]]></footer>
	</mail>
</notifierConfig>');
