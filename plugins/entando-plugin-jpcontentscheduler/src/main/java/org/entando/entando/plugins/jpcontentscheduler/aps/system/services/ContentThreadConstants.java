/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services;

public class ContentThreadConstants {

	// Chiave a database nella tabella SYSCONFIG di japsport per recuperare
	// la configurazione
	public static final String CONTENTTHREAD_CONFIG_ITEM = "cthread_config";
	public static final String READ_CONFIG_ERROR = "Item di configurazione non presente: ";

	public static final String INIT_ERROR = "Errore in fase di inizializzazione";

	public static final String GET_CONTENTIDS_ERROR = "Errore nel recupero dei contenuti da pubblicare";

	// Formato data nell'xml del contenuto
	public static final String DATE_PATTERN = "yyyyMMdd";
	// Formato del timestamp per inizio e fine data ora del job
	public static final String PRINT_DATE_PATTERN = "dd-MM-yyyy hh:mm:ss";

	// messaggi di info del thread per il log
	public static final String APP_CTX_ERROR = "Non e' disponibile nessun application contenxt nello scheduler per la chiave ";
	public static final String START_TIME_LOG = "Inizio thread pubblicazione/sospensione automatica contenuti....";
	public static final String END_TIME_LOG = "Fine thread pubblicazione/sospensione automatica contenuti.";

	// messaggi di errore per i contenuti
	public static final String NULL_CONTENT = "Il contentid non esiste.";
	public static final String NULL_CONTENT_REPLACE = " - Contenuto correlato a pagina - Il contentId del contenuto sostitutivo non esiste.";
	public static final String NOTREADYSTATUS = "Il contenuto non e' in stato 'Pronto'.";
	public static final String CONTENTWITHERRORS = "Sono presenti errori nel contenuto.";
	public static final String ERROR_ON_PUBLISH = "Sono avvenuti errori nell'esecuzione del job per pubblicare i contenuti.";
	public static final String UNEXPECTED_ERROR = "Errore generico.";
	public static final String ISALREADYONLINE = "Contenuto gia' on-line.";
	public static final String CONTENT_WITH_REFERENCES = "Contenuto con riferimenti e senza contentId sostitutivo.";
	public static final String CONTENT_WITH_REFERENCE_CONTENT = "Contenuto con riferimenti";
	public static final String CONTENT_WITH_REFERENCES_AND_BROKEN_LINK = "Contenuto con riferimenti depubblicato, possibile presenza di link non validi.";

	public static final String ISALREADYSUSPENDED = "Contenuto gia' sospeso.";
	public static final String ISNOTONLINE = " - Contenuto correlato a pagina - Il contenuto sostitutivo non è online";
	public static final String ISALREADYMOVED = "Contenuto gia' spostato in archivio.";

	// messaggi per le mail
	public static final String MAIL_SENT = "Thread sospensione/pubblicazione automatica: spedita notifica a utente ";
	public static final String ERROR_ON_MAIL = "Errore nella preparazione o spedizione della mail.";
	public static final String USER_IS_NULL = "Thread sospensione/pubblicazione automatica: l'utente non esiste, rivedere i parametri di configurazione - ";
	public static final String PROFILE_IS_NULL = "Thread sospensione/pubblicazione automatica: l'utente esiste ma non esiste il profilo, rivedere i parametri di configurazione - ";
	public static final String SEND_ERROR = "Thread sospensione/pubblicazione automatica: sono avvenuti errori nella spedizione della mail di notifica all'utente ";
	public static final String REPLACE_CONTENT = " sostituzione nella pagina con il contenuto indicato.";

	public static final String PUBLISH_ACTION = "publish";
	public static final String SUSPEND_ACTION = "suspend";
	public static final String MOVE_ACTION = "move";

	public static final String ACTION_SUCCESS = "OK";

	// valore per l'attributo types nell'xml di configurazione che indica "tutti
	// i tipi di contenuto"
	public static final String ALL_TYPES = "*";

}
