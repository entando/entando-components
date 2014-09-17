-- Test Port Script

INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpwttConfig', 'Configurazione servizio Web Trouble Ticketing', '<wttConf>
	<interventionTypes>
		<interventionType id="1" descr="Hardware" />
		<interventionType id="2" descr="Software" />
	</interventionTypes>
	<priorities>
		<priority id="1" descr="High" />
		<priority id="2" descr="Medium" />
		<priority id="3" descr="Low" />
	</priorities>
</wttConf>');


INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpwttMailConfig', 'Configurazione servizio Mail Alert per Web Trouble Ticketing', '<wttMailConfig>
	<mail uniqueMail="true" senderCode="CODE1" mailAttrName="email" >
		<subject>Wtt Alert</subject>
		<templates>
			<template operation="0" descr="OPEN">
				<body type="admin"><![CDATA[Op0: Testo della mail admin]]></body>
				<body type="user"><![CDATA[Op0: Testo della mail user]]></body>
			</template>

			<template operation="5" descr="ANSWER">
				<body type="admin"><![CDATA[Op3: Testo della mail admin]]></body>
				<body type="user"><![CDATA[Op3: Testo della mail user]]></body>
				<body type="allOperators"><![CDATA[Op3: Testo della mail allOperators]]></body>
			</template>

			<template operation="10" descr="SETASSIGNABLE">
				<body type="admin"><![CDATA[Op1: Testo della mail admin]]></body>
				<body type="allOperators"><![CDATA[Op1: Testo della mail allOperators]]></body>
			</template>

			<template operation="15" descr="TAKEINCHARGE">
				<body type="admin"><![CDATA[Op2: Testo della mail admin]]></body>
				<body type="allOperators"><![CDATA[Op2: Testo della mail allOperators]]></body>
			</template>

			<template operation="25" descr="RELEASE">
				<body type="admin"><![CDATA[Op6: Testo della mail admin]]></body>
			</template>

			<template operation="30" descr="CLOSE">
				<body type="admin"><![CDATA[Op5: Testo della mail admin]]></body>
				<body type="user"><![CDATA[Op5: Testo della mail user]]></body>
				<body type="allOperators"><![CDATA[Op5: Testo della mail allOperators]]></body>
			</template>
		</templates>
	</mail>
	<commonAddresses>
		<admin>
			<!-- PUO ESSERE VUOTO -->
			<address>pippo@agiletec.it</address>
			<address>pluto@agiletec.it</address>
		</admin>
		<operator>
			<!-- PUO ESSERE VUOTO -->
			<address>topolino@agiletec.it</address>
		</operator>
	</commonAddresses>
	<interventionTypes>
		<interventionType id="1" >
			<admin>
				<!-- PUO ESSERE VUOTO -->
				<address>paperino@agiletec.it</address>
				<address>paperina@agiletec.it</address>
			</admin>
			<operator>
				<!-- PUO ESSERE VUOTO -->
				<address>qui@agiletec.it</address>
				<address>quo@agiletec.it</address>
				<address>qua@agiletec.it</address>
			</operator>
		</interventionType>
		<interventionType id="2">
		</interventionType>
	</interventionTypes>
</wttMailConfig>');