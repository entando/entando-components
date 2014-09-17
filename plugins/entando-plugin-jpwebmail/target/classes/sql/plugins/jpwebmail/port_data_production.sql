INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwebmail_config', 'Configurazione del servizio WebMail', '<webmailConfig>
	<certificates>
		<enable>true</enable>
		<lazyCheck>true</lazyCheck>
		<certPath>/home/agile/workspace/entando/build/temp</certPath>
		<debugOnConsole>true</debugOnConsole>
	</certificates>
	<entandoUserPassword>true</entandoUserPassword>
	<localhost>hostname</localhost>
	<domain>tiscali.it</domain>
	<imap>
		<host>imap.tiscali.it</host>
		<protocol>imap</protocol>
		<port>143</port>
	</imap>
	<smtp debug="true" >
		<host>out.virgilio.it</host>
		<user></user>
		<password></password>
		<port></port>
		<security>std</security>
	</smtp>
	<folder>
		<!-- Nomi completo della cartella di sistema (se esistono comprensivo delle cartelle parenti con il separatore ".") -->
		<trash>Trashcan</trash>
		<sent>Sent</sent>
	</folder>
</webmailConfig>');


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ('jpwebmail_small', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Webmail - Service Intro</property>
<property key="it">Webmail - Intro Servizio</property>
</properties>', NULL, 'jpwebmail', NULL, NULL, 1 );

INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ('jpwebmail_navigation', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Webmail - Navigation</property>
<property key="it">Webmail - Navigazione</property>
</properties>', NULL, 'jpwebmail', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpwebmail/Portal/WebMail/intro</property>
</properties>', 0 );


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_HAVE', 'it', 'Hai' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_HAVE', 'en', 'You have' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_GO_TO', 'it', 'Vai alla' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_GO_TO', 'en', 'Go to' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_EMAIL_ACCOUNT', 'it', 'casella di posta' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_EMAIL_ACCOUNT', 'en', 'web mail' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_IN_ACCOUNT', 'it', 'messaggi in casella' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_IN_ACCOUNT', 'en', 'messages in the box' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NOT_READ', 'it', 'messaggi non letti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NOT_READ', 'en', 'unread messages' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SERVICE_INTRO', 'it', 'Introduzione al servizio Web Mail' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SERVICE_INTRO', 'en', 'Introduction to Web Mail service' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_ACCOUNT_INTRO', 'it', 'Introduzione al servizio Web Mail per utenti registrati privi di casella' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_ACCOUNT_INTRO', 'en', 'Introduction to Web Mail service for registered users without mailbox' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SHOWLET_TITLE', 'it', 'Web Mail' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SHOWLET_TITLE', 'en', 'Web Mail' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MESSAGES', 'it', 'Messaggi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MESSAGES', 'en', 'Messages' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_LIST_CAPTION', 'it', 'Elenco dei messaggi di questa cartella' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_LIST_CAPTION', 'en', 'Message list in this folder' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_LIST_SUMMARY', 'it', 'La tabella riporta la lista dei messaggi presenti nella cartella corrente. Per ciascun messaggio è riportato un numero progressivo, lo status (letto, non letto, et cetera), il mittente, l''oggetto, e la data di ricezione.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_LIST_SUMMARY', 'en', 'This table shows the list of messages in current folder. For each message shows serial number, status (read, unread, etc.), the sender, the subject and the arrival date.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NUMBER', 'it', 'Messaggio Numero' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NUMBER', 'en', 'Message #' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NUMBER_ABBR', 'it', 'N.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NUMBER_ABBR', 'en', '#' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_STATUS', 'it', 'Stato del messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_STATUS', 'en', 'Message status' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_STATUS_ABBR', 'it', 'S.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_STATUS_ABBR', 'en', 'S.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_FROM', 'it', 'Mittente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_FROM', 'en', 'Sender' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_SUBJECT', 'it', 'Oggetto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_SUBJECT', 'en', 'Subject' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_DATE', 'it', 'Data' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TABLE_DATE', 'en', 'Date' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_FROM', 'it', 'Da' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_FROM', 'en', 'From' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SUBJECT', 'it', 'Oggetto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SUBJECT', 'en', 'Subject' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DATE', 'it', 'Data' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DATE', 'en', 'Date' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_RECIPIENT', 'it', 'Nessun Destinatario' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_RECIPIENT', 'en', 'No Recipient' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_RECIPIENTS', 'it', 'Destinatari' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_RECIPIENTS', 'en', 'Recipients' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MORE_RECIPIENTS', 'it', 'Più Destinatari' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MORE_RECIPIENTS', 'en', 'More than one Recipients' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SUBJECT_EMPTY', 'it', 'Nessun Oggetto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SUBJECT_EMPTY', 'en', 'No Subject' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SELECT', 'it', 'Seleziona' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SELECT', 'en', 'Select' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ALL_MSG', 'it', 'Tutti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ALL_MSG', 'en', 'All' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NONE_MSG', 'it', 'Nessuno' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NONE_MSG', 'en', 'None' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_TABLE_EMPTY', 'it', 'Non sono presenti messaggi.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_TABLE_EMPTY', 'en', 'There are no messages.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_RETURN_START_FOLDER', 'it', 'Ritorna alla cartella principale' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_RETURN_START_FOLDER', 'en', 'Back to main folder' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_REFRESH_NOW', 'it', 'Controlla la posta' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_REFRESH_NOW', 'en', 'Check Mail' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NEW', 'it', 'Scrivi un messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_NEW', 'en', 'Write a message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DELETE', 'it', 'Elimina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DELETE', 'en', 'Delete' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_MOVE', 'it', 'Sposta' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_MOVE', 'en', 'Move' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_MOVE_LABEL', 'it', 'Sposta in' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_MOVE_LABEL', 'en', 'Move to' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_FOLDERS', 'it', 'Cartelle' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_FOLDERS', 'en', 'Folders' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CURRENT_FOLDER', 'it', 'Cartella corrente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CURRENT_FOLDER', 'en', 'Current folder' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_REFRESH_LAST', 'it', 'Ultimo controllo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_REFRESH_LAST', 'en', 'Last check' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG', 'it', 'Messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG', 'en', 'Message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_REPLY', 'it', 'Rispondi al mittente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_REPLY', 'en', 'Reply to sender' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_REPLY_ALL', 'it', 'Rispondi a tutti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_REPLY_ALL', 'en', 'Reply All' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_FORWARD', 'it', 'Inoltra' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_FORWARD', 'en', 'Forward' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_GO_NEXT', 'it', 'Vai al messaggio seguente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_GO_NEXT', 'en', 'Go to next message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_GO_PREVIOUS', 'it', 'Vai al messaggio precedente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_GO_PREVIOUS', 'en', 'Go to previous message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TO', 'it', 'A' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TO', 'en', 'TO' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_CC', 'it', 'CC' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_CC', 'en', 'CC' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BCC', 'it', 'CCN' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BCC', 'en', 'BCC' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SEND_DATE', 'it', 'Data di invio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SEND_DATE', 'en', 'Sent' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_RECEIVE_DATE', 'it', 'Data di ricezione' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_RECEIVE_DATE', 'en', 'Received' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BODY', 'it', 'Corpo del messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BODY', 'en', 'Message body' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACHMENTS', 'it', 'Allegati' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACHMENTS', 'en', 'Attachments' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_UNREAD', 'it', 'Non letto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_UNREAD', 'en', 'Unread' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NOTE', 'it', 'Nota' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NOTE', 'en', 'Note' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACTS_NOTE', 'it', 'Puoi aggiungere gli indirizzi dei destinatari scrivendoli a mano separati da una virgola (es. mariorossi@japsportal.org, giuseppeverdi@japsportal.org)' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACTS_NOTE', 'en', 'You can add the addresses of the recipients separated by a comma (ex. johnsmith@japsportal.org, giuseppeverdi@japsportal.org).' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TO_LABEL', 'it', 'A' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_TO_LABEL', 'en', 'TO' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_CC_LABEL', 'it', 'CC' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_CC_LABEL', 'en', 'CC' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BCC_LABEL', 'it', 'CCN' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_BCC_LABEL', 'en', 'BCC' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ATTACH_REMOVE', 'it', 'Rimuovi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ATTACH_REMOVE', 'en', 'Remove' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACH_NEW', 'it', 'Nuovo allegato' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACH_NEW', 'en', 'New attachment' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACH_THIS', 'it', 'Allega' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_ATTACH_THIS', 'en', 'Attach' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SEND', 'it', 'Invia il messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_SEND', 'en', 'Send a message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_BACK', 'it', 'Annulla' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_BACK', 'en', 'Cancel' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DETAILS', 'it', 'Dettagli' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_DETAILS', 'en', 'Details' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_OPEN', 'it', 'Apri' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_OPEN', 'en', 'Open' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ERROR_RECIPIENT', 'it', 'Errore! Recipient sconosciuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ERROR_RECIPIENT', 'en', 'Error! Unknown recipient' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_BOOK_INTRO', 'it', 'Puoi scegliere tra i contatti della rubrica selezionandoli dalla lista o effettuare una ricerca.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_BOOK_INTRO', 'en', 'You can choose from the contacts in the address book selecting them from the list or do a search.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SEARCH', 'it', 'Cerca' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_SEARCH', 'en', 'Search' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_LIST', 'it', 'Lista dei contatti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_LIST', 'en', 'Address book' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_ADD', 'it', 'Aggiungi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_ADD', 'en', 'Add' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_NO_RESULT', 'it', 'Nessun contatto trovato' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_NO_RESULT', 'en', 'No contact found' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACT_LIST_SUMMARY', 'it', 'La tabella riporta l''elenco dei destinatari selezionati mostrando Cognome e Nome, Indirizzo E-Mail e un comando per rimuovere il contatto dalla lista.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACT_LIST_SUMMARY', 'en', 'This table shows the list of recipients showing Name, Surname, E-Mail and a button to remove the address from the list.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACT_LIST_CAPTION', 'it', 'Destinatari selezionati' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONTACT_LIST_CAPTION', 'en', 'Selected recipients' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_USER', 'it', 'Cognome e Nome' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_USER', 'en', 'Surname and Name' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS', 'it', 'Indirizzo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS', 'en', 'Address' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_OPERATIONS', 'it', 'Operazioni' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_OPERATIONS', 'en', 'Actions' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_REMOVE', 'it', 'Rimuovi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ADDRESS_REMOVE', 'en', 'Remove' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CANCEL', 'it', 'Annulla' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CANCEL', 'en', 'Cancel' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONFIRM', 'it', 'Conferma' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_CONFIRM', 'en', 'Confirm' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_READ', 'it', 'Letto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_MSG_READ', 'en', 'Read' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NEW_MSG', 'it', 'Nuovo Messaggio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NEW_MSG', 'en', 'New Message' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ERROR_HAPPENED', 'it', 'Si è verificato un errore di comunicazione con la casella di posta.Prova a ricaricare la pagina.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_ERROR_HAPPENED', 'en', 'Is happened a communication error with the web mail.Refresh the page.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_LOGIN', 'it', 'Devi effettuare il login' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_NO_LOGIN', 'en', 'You must login' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_WEBMAIL_PASSWORD', 'it', 'Password' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpwebmail_WEBMAIL_PASSWORD', 'en', 'Password' );
