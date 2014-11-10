INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpuserreg_Config', 
'Configurazione servizio registrazione iscrizione utenti', 
'<userRegConfig>
	<!-- Token validity in minutes - Activation page name -->
	<tokenValidity minutes="60"/>
	
	<!-- Sender code, as in mailConfig -->
	<sender code="CODE1" />        

	<!-- Authorities to load on user request profile -->
	<userAuthDefaults>
		<authotization group="coach" role="editor" />
		<authotization group="customers" role="supervisor" />
	</userAuthDefaults>
	
	<!-- Activation page name -->
	<activation pageCode="activation">
		<template lang="it">
			<subject><![CDATA[[Entando]: Attivazione account]]></subject>
			<body><![CDATA[
			Gentile {name} {surname}, 
			grazie per esserti registrato.
			Per attivare il tuo account è necessario seguire il seguente link: 
			{link}
			Cordiali Saluti.
			]]></body>
		</template>
		<template lang="en">
			<subject><![CDATA[[Entando]: Account activation]]></subject>
			<body><![CDATA[
			Dear {name} {surname}, 
			thank you to register.
			To activate your account you need to click on the link: 
			{link}
			Best regards.
			]]></body>
		</template>
	</activation>
	
	<!-- Reactivation page name -->
	<reactivation pageCode="riattivazione">
		<template lang="it">
			<subject><![CDATA[[Entando]: Ripristino password]]></subject>
			<body><![CDATA[
			Gentile {name} {surname}, 
			il tuo userName è {userName}.
			Per riattivare il tuo account è necessario seguire il seguente link: 
			{link}
			Cordiali Saluti.
			]]></body>
		</template>
		<template lang="en">
			<subject><![CDATA[[Entando]: Password recover]]></subject>
			<body><![CDATA[
			Dear {name} {surname}, 
			your username is {userName}.
			To recover your password you need to click on the link: 
			{link}
			Best regards.
			]]></body>
		</template>
	</reactivation>
</userRegConfig>');

