-- Port Production Script

INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwttConfig', 'Configurazione servizio Web Trouble Ticketing', '<wttConf>
	<interventionTypes>
		<interventionType id="1" descr="Hardware" />
		<interventionType id="2" descr="Software" />
	</interventionTypes>
	<priorities>
		<priority id="1" descr="Alta" />
		<priority id="2" descr="Media" />
		<priority id="3" descr="Bassa" />
	</priorities>
</wttConf>');


INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwttMailConfig', 'Configurazione servizio Mail Alert per Web Trouble Ticketing', '<wttMailConfig>
	<mail uniqueMail="true" senderCode="CODE1" mailAttrName="email" >
		<subject>[Plugin WTT]: Servizio di segnalazioni</subject>
		<templates>
			<template operation="0" descr="OPEN">
				<body type="admin"><![CDATA[E'' stata ricevuta una nuova richiesta e le è stato aseegnato il codice {code}]]></body>
				<body type="user"><![CDATA[Gentile {nome} {cognome},
la sua segnalazione è stata ricevuta e le è stato assegnato il codice {code}.
I nostri operatori provvederanno al più presto ad inviarle una risposta.

Cordiali Saluti

]]></body>
			</template>
			<template operation="5" descr="ANSWER">
				<body type="admin"><![CDATA[E'' stata inviata la seguente risposta alla segnalazione di codice {code}:

{note}
]]></body>
				<body type="user"><![CDATA[{note}]]></body>
				<body type="allOperators"><![CDATA[E'' stata inviata la seguente risposta alla segnalazione di codice {code}:

{note}
]]></body>
			</template>
			<template operation="10" descr="SETASSIGNABLE">
				<body type="admin"><![CDATA[Ticket {code} assegnato agli operatori di ruolo {wttRole}]]></body>
				<body type="allOperators"><![CDATA[Ticket {code} assegnato agli operatori di ruolo {wttRole}]]></body>
			</template>
			<template operation="15" descr="TAKEINCHARGE">
				<body type="admin"><![CDATA[Ticket {code} preso in carico dall''operatore {operator}]]></body>
				<body type="allOperators"><![CDATA[Ticket {code} preso in carico dall''operatore {operator}]]></body>
			</template>
			<template operation="25" descr="RELEASE">
				<body type="admin"><![CDATA[La segnalazione {code} è stata riportata allo stato di lavorazione.]]></body>
			</template>
			<template operation="30" descr="CLOSE">
				<body type="admin"><![CDATA[La segnalazione {code} è stata chiusa con esito {resolved}.]]></body>
				<body type="user"><![CDATA[Gentile {nome} {cognome},
le comunichiamo che la sua segnalazione di codice {code} è stata chiusa con esito {resolved}.
Cordiali saluti.

]]></body>
				<body type="allOperators"><![CDATA[La segnalazione {code} è stata chiusa con esito {resolved}.]]></body>
			</template>
		</templates>
	</mail>
	<commonAddresses>
		<admin>
			<!-- PUO ESSERE VUOTO -->
			<address>@</address>
			<address>@</address>
		</admin>
		<operator>
		</operator>
	</commonAddresses>
	<interventionTypes>
		<interventionType id="1" >
			<admin>
				<address>@</address>
			</admin>
			<operator>
				<address>@</address>
			</operator>
		</interventionType>
		<interventionType id="2">
			<admin>
				<address>@</address>
			</admin>
			<operator>
				<address>@</address>
			</operator>
		</interventionType>
	</interventionTypes>
</wttMailConfig>');