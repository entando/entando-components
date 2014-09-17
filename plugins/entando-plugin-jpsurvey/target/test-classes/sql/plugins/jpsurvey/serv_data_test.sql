INSERT INTO jpsurvey ( id, description, maingroup, startdate, enddate, active, publicpartialresult, 
	publicresult, questionnaire, gatheruserinfo, title, restrictedaccess, checkcookie, 
	checkipaddress, checkusername, imageid, imagedescr ) VALUES ( 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Description-1</property>
<property key="it">Descrizione-1</property>
</properties>', 'ignored', '2009-03-16 00:00:00', NULL, 1, 0, 1, 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Title-1</property>
<property key="it">Titolo-1</property>
</properties>', 0, 0, 0, 0, 'IMG001', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Barrali by night</property>
<property key="it">Barrali di notte</property>
</properties>' );

INSERT INTO jpsurvey ( id, description, maingroup, startdate, enddate, active, publicpartialresult, 
	publicresult, questionnaire, gatheruserinfo, title, restrictedaccess, checkcookie, 
	checkipaddress, checkusername,imageid, imagedescr ) VALUES ( 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Descrizione-2</property>
	<property key="en">Description-2</property>
</properties>', 'ignoredToo', '2008-02-06 00:00:00', NULL, 0, 0, 0, 0, 0, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Titolo-2</property>
	<property key="en">Title-2</property>
</properties>', 1, 0, 0, 0, NULL, NULL );

INSERT INTO jpsurvey_questions ( id, surveyid, question, pos, singlechoice, minresponsenumber, maxresponsenumber ) VALUES 
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-1</property>
	<property key="en">Question 1-1</property>
</properties>', 2, 1, 1, 1 );
INSERT INTO jpsurvey_questions ( id, surveyid, question, pos, singlechoice, minresponsenumber, maxresponsenumber ) VALUES 
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-2</property>
	<property key="en">Question 1-2</property>
</properties>', 1, 0, 1, 2 );

INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-1</property>
	<property key="en">Option 1-1-1</property>
</properties>', 1, 0 );
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-2</property>
	<property key="en">Option 1-1-2</property>
</properties>', 3, 0 ); 
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 3, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-3</property>
	<property key="en">Option 1-1-3</property>
</properties>', 2, 0 );
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 4, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-2</property>
	<property key="en">Option 1-2-2</property>
</properties>', 2, 0 );
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 5, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-1</property>
	<property key="en">Option 1-2-1</property>
</properties>', 1, 0 );
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 6, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione TESTO LIBERO</property>
	<property key="en">Option FREE TEXT</property>
</properties>', 4, 1 );
INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 7, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-3</property>
	<property key="en">Option 1-2-3</property>
</properties>', 3, 0 );

INSERT INTO jpsurvey_voters ( id, age, country, sex, votedate, surveyid, username, ipaddress ) VALUES 
	( 1, 99, 'ir', 'M', '2008-04-07 00:00:00', 2, 'guest', '192.168.10.1' );


INSERT INTO jpsurvey_responses ( voterid, questionid, choiceid, freetext ) VALUES
	( 1, 2, 6, 'lorem ipsum dolor' );
