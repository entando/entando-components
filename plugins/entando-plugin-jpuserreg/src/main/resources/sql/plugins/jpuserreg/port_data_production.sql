INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpuserreg_Config', 
'Configuration for the plugin User Registration',
'<userRegConfig>
	<tokenValidity minutes="60"/>
	<sender code="CODE1" />
	<userAuthDefaults />
	<activation pageCode="activation">
		<template lang="it">
			<subject><![CDATA[Il tuo account è stato creato]]></subject>
			<body><![CDATA[
Gentile {name} {surname},
grazie per esserti registrato.
Per attivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.
			]]></body>
		</template>
		<template lang="en">
			<subject><![CDATA[Your account has been created]]></subject>
			<body><![CDATA[
Dear {name} {surname}, 
thank you for registering.
To activate your account you need to click on the link: 
{link}
Best regards.
			]]></body>
		</template>
	</activation>
	<reactivation pageCode="reactivation">
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
