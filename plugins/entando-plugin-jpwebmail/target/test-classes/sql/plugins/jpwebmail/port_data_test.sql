INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpwebmail_config', 'Webmail service configuration', '<webmailConfig>
	<certificates>
		<!-- Esegue l''handshake con l''host -->
		<enable>false</enable>
		<!-- Accetta in maniera pedissequa qualsiasi certificato -->
		<lazyCheck>true</lazyCheck>
		<!-- path dove porre la firma dei server fidati -->
		<certPath>/home/utente/jpwebmail</certPath>
		<!-- Mostra in console messaggi di debug -->
		<debugOnConsole>true</debugOnConsole>
	</certificates>
	<localhost>hostname</localhost>
	<domain>miodominio.boh</domain>
	<imap>
		<host>imap.gmail.com</host>
		<protocol>imaps</protocol>
		<port>993</port>
	</imap>
	<smtp debug="false" >
		<host>out.virgilio.it</host>
		<user></user>
		<password></password>
		<port></port>
	</smtp>
	<folder>
		<!-- Nomi completo della cartella di sistema (se esistono comprensivo delle cartelle parenti con il separatore ".") -->
		<trash>Trash</trash>
		<sent>Sent</sent><!-- NON ANCORA ATTIVA -->
	</folder>
</webmailConfig>');
