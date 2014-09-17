INSERT INTO sysconfig( version, item, descr, config )
    VALUES ('test', 'jpcontentworkflow_config', 'Content Workflow', '<?xml version="1.0" encoding="UTF-8"?>
<contenttypes><contenttype typecode="RAH" role="contentType_RAH" /><contenttype typecode="ART" role="contentType_ART" /><contenttype typecode="EVN" role="contentType_EVN" /></contenttypes>');


INSERT INTO sysconfig( version, item, descr, config )
    VALUES ('test', 'jpcontentworkflow_notifierConfig', 'Content Workflow - Notifier', '<?xml version="1.0" encoding="UTF-8"?>
<notifierConfig>
	<scheduler>
		<active value="false" />
		<delay value="24" />
		<start value="04/12/2008 16:08" />
	</scheduler>
	<mail senderCode="CODE1" mailAttributeName="email" html="false">
		<subject><![CDATA[[My Own Portal]: A content changed]]></subject>
		<header><![CDATA[Hi {user},<br />these contents require your attention<br /><br />]]></header>
		<template><![CDATA[<br />Content {type} - {descr} - Status {status}<br />]]></template>
		<footer><![CDATA[<br />End (footer)]]></footer>
	</mail>
</notifierConfig>');