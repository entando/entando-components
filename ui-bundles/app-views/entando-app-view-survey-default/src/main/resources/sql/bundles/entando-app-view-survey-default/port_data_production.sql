
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_pollArchive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Surveys - Surveys Archive</property>
<property key="it">Sondaggi - Archivio dei Sondaggi</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_pollList', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Surveys - Active List</property>
<property key="it">Sondaggi - Lista Sondaggi Attivi</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_questionnaireArchive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaires - Questionnaires Archive</property>
<property key="it">Questionari - Archivio dei Questionari</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_questionnaireList', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaires - Active List</property>
<property key="it">Questionari - Lista Questionari Attivi</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_detailsSurvey', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaire/Survey - Details</property>
<property key="it">Questionari/Sondaggi - Dettaglio</property>
</properties>', NULL, 'jpsurvey', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsurvey/Front/Survey/entryPoint.action</property>
</properties>', 1);


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_resultsSurvey', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaire/Survey - Show Results</property>
<property key="it">Questionario/Sondaggio - Mostra Risultati</property>
</properties>', NULL, 'jpsurvey', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsurvey/Front/SurveyDetail/entryPoint.action</property>
</properties>', 1);


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ACTIVE_QUESTIONNAIRE', 'en', 'active Questionnaires');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ACTIVE_QUESTIONNAIRE', 'it', 'questionari attivi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_AGE', 'en', 'Age');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_AGE', 'it', 'Età');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ANSWER_NUMBER', 'en', 'response number');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ANSWER_NUMBER', 'it', 'risposta numero');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ANSWERS', 'en', 'Answers');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ANSWERS', 'it', 'Risposte');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ARCHIVE_ENDDAY', 'en', 'and expired');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ARCHIVE_ENDDAY', 'it', 'ed è scaduto');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_BEGIN', 'en', 'Begins');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_BEGIN', 'it', 'Inizia il');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_COUNTRY', 'en', 'City');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_COUNTRY', 'it', 'Città');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_DESCRIPTION', 'en', 'Description');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_DESCRIPTION', 'it', 'Descrizione');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ENDDAY', 'en', 'and expires');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ENDDAY', 'it', 'e scadrà');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_ALREADY_VOTED', 'en', 'Mistake! You have already voted.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_ALREADY_VOTED', 'it', 'Errore! Hai già votato.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_EXPIRED_SURVEY', 'en', 'Mistake! The survey is already expired.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_EXPIRED_SURVEY', 'it', 'Errore! Il sondaggio è scaduto');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_FINALRESULTNOTALLOWED', 'en', 'Mistake! The display of final results is not allowed.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_FINALRESULTNOTALLOWED', 'it', 'Errore! La visualizzazione dei risultati finali non è permessa.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_NOTBEGUNYET', 'en', 'Mistake! You have not yet begun.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_NOTBEGUNYET', 'it', 'Errore! Non hai ancora cominciato.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_NULL_VOTER_RESPONSE', 'en', 'Mistake! The number of responses is zero');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_NULL_VOTER_RESPONSE', 'it', 'Errore! Il numero di risposte è nullo');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_PARTIALRESULTSNOTALLOWED', 'en', 'Mistake! Display of partial results is not allowed.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_PARTIALRESULTSNOTALLOWED', 'it', 'Errore! La visualizzazione dei risultati parziali non è permessa.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_USER_NOT_ALLOWED', 'en', 'Mistake! Unauthorized.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_ERROR_USER_NOT_ALLOWED', 'it', 'Errore! Utente non autorizzato.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_FINAL_RESULTS', 'en', 'Final Results');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_FINAL_RESULTS', 'it', 'Risultati finali');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_FREE_TEXT', 'en', 'Free Text');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_FREE_TEXT', 'it', 'Testo Libero');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO', 'en', 'Send');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO', 'it', 'Invia');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_ACTIVE_POLLS', 'en', 'active Surveys');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_ACTIVE_POLLS', 'it', 'sondaggi attivi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_ACTIVE_POLLS', 'en', 'Go to the list of active surveys');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_ACTIVE_POLLS', 'it', 'Vai alla lista dei sondaggi attivi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_ACTIVE_QUESTIONNAIRE', 'en', 'Go to the list of active questionnaries');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_ACTIVE_QUESTIONNAIRE', 'it', 'Vai alla lista dei questionari attivi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_POLL', 'en', 'Go to');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_POLL', 'it', 'Vai a');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_POLL_ARCHIVE', 'en', 'Go to surveys');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_POLL_ARCHIVE', 'it', 'Vai all''archivio sondaggi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_QUESTIONNAIRE', 'en', 'Vai a');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_QUESTIONNAIRE', 'it', 'Vai a');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_QUESTIONNAIRE_ARCHIVE', 'en', 'Go to questionnaires');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_GO_TO_QUESTIONNAIRE_ARCHIVE', 'it', 'Vai all''archivio questionari');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_INSERT_FREE_TEXT', 'en', 'Enter free text');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_INSERT_FREE_TEXT', 'it', 'Inserisci il testo libero');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_MAX_ANSWERS', 'en', 'Maximum number of responses');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_MAX_ANSWERS', 'it', 'Numero massimo di risposte');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_MIN_ANSWERS', 'en', 'Minimum number of responses');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_MIN_ANSWERS', 'it', 'Numero minimo di risposte');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_OBTAINED', 'en', 'scored');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_OBTAINED', 'it', 'ha ottenuto');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PARTIAL_RESULTS', 'en', 'Subtotals');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PARTIAL_RESULTS', 'it', 'Risultati parziali');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PERSON', 'en', 'people.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PERSON', 'it', 'persone.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL', 'en', 'survey');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL', 'it', 'sondaggio');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL_ARCHIVE', 'en', 'Archive');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL_ARCHIVE', 'it', 'Archivio');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL_STARTDAY', 'en', 'The survey was published');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLL_STARTDAY', 'it', 'Il sondaggio è stato pubblicato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS', 'en', 'Surveys');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS', 'it', 'Sondaggi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_ARCHIVE_INTRO', 'en', 'If you want you can go back to');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_ARCHIVE_INTRO', 'it', 'Se vuoi puoi tornare ai');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_LIST', 'en', 'List active surveys');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_LIST', 'it', 'Lista dei sondaggi attivi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_LIST_INTRO', 'en', 'If you want you can see the results of polls ended in');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_POLLS_LIST_INTRO', 'it', 'Se vuoi puoi vedere i risultati dei sondaggi scaduti nell''');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PROFILE_NEEDED', 'en', 'It takes some of your data. Thank you for your cooperation.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_PROFILE_NEEDED', 'it', 'Sono necessari alcuni tuoi dati anagrafici. Grazie per la collaborazione.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTION', 'en', 'Question');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTION', 'it', 'Domanda');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTION_OF', 'en', 'of');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTION_OF', 'it', 'di');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTIONNAIRE_ARCHIVE', 'en', 'Archive');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTIONNAIRE_ARCHIVE', 'it', 'Archivio');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTIONNAIRE_LIST_INTRO', 'en', 'If you want, you can see the list of expired surveys browsing the');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_QUESTIONNAIRE_LIST_INTRO', 'it', 'Se vuoi puoi vedere i questionari scaduti nell''');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SEX', 'en', 'Gender');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SEX', 'it', 'Sesso');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SURVEY', 'en', 'questionnaire');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SURVEY', 'it', 'questionario');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SURVEY_STARTDAY', 'en', 'The questionnaire was posted');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_SURVEY_STARTDAY', 'it', 'Il questionario è stato pubblicato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_THANKS_FOR', 'en', 'Thank you for cooperating. If you want you can return to the list');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_THANKS_FOR', 'it', 'Grazie per aver collaborato. Se vuoi puoi tornare alla lista dei');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_TITLE', 'en', 'Title');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_TITLE', 'it', 'Titolo');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_VOTED_TOT', 'en', 'All voted');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_VOTED_TOT', 'it', 'Hanno votato in tutto');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_VOTES', 'en', 'votes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_VOTES', 'it', 'voti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_HAVE_NOT_VOTED', 'en', 'You have not voted');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_HAVE_NOT_VOTED', 'it', 'Non hai ancora votato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_HAVE_VOTED', 'en', 'You have already voted');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_HAVE_VOTED', 'it', 'Hai già votato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_NOT_VOTED', 'en', '(You have not voted)');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_NOT_VOTED', 'it', '(Non hai ancora votato)');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_VOTED', 'en', '(You have already voted)');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_YOU_VOTED', 'it', '(Hai già votato)');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsurvey_POLLS_ARCHIVE_LIST_TITLE', 'en', 'Surveys Archive');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsurvey_POLLS_ARCHIVE_LIST_TITLE', 'it', 'Archivio dei Sondaggi');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_DO_LOGIN', 'en', 'Sign in if you want to participate.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('JPSURVEY_DO_LOGIN', 'it', 'Accedi se vuoi partecipare.');
