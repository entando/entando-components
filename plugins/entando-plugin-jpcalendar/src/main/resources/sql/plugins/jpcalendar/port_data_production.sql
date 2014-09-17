INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpcalendar_Config', 'Calendar plugin configuration', '<calendarConfig>
	<contentType code="CONTENT_TYPE_ID" />
	<dateAttributes>
		<start name="START_DATE_ATTRIBUTE_NAME" />
		<end name="END_DATE_ATTRIBUTE_NAME" />
	</dateAttributes>
</calendarConfig>');


INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpcalendar_calendar', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Calendar</property>
<property key="it">Calendario</property>
</properties>', NULL, 'jpcalendar', NULL, NULL, 1);


INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpcalendar_dailyEvents', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Events of the Day</property>
<property key="it">Eventi del giorno</property>
</properties>', NULL, 'jpcalendar', NULL, NULL, 1);


INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_SEARCH_GO' , 'en', 'Search');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_SEARCH_GO' , 'it', 'Ricerca');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_PREVIOUS' , 'en', 'Previuos Month');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_PREVIOUS' , 'it', 'Mese Precedente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_NEXT' , 'en', 'Next Month');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_NEXT' , 'it', 'Prossimo Mese');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_CHOOSE' , 'en', 'Select Month');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_CHOOSE' , 'it', 'Seleziona Mese');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JANUARY' , 'en', 'January');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JANUARY' , 'it', 'Gennaio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_FEBRUARY' , 'en', 'February');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_FEBRUARY' , 'it', 'Febbraio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_MARCH' , 'en', 'March');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_MARCH' , 'it', 'Marzo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_APRIL' , 'en', 'April');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_APRIL' , 'it', 'Aprile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_MAY' , 'en', 'May');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_MAY' , 'it', 'Maggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JUNE' , 'en', 'June');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JUNE' , 'it', 'Giugno');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JULY' , 'en', 'July');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_JULY' , 'it', 'Luglio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_AUGUST' , 'en', 'August');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_AUGUST' , 'it', 'Agosto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_SEPTEMBER' , 'en', 'September');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_SEPTEMBER' , 'it', 'Settembre');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_OCTOBER' , 'en', 'October');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_OCTOBER' , 'it', 'Ottobre');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_NOVEMBER' , 'en', 'November');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_NOVEMBER' , 'it', 'Novembre');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_DECEMBER' , 'en', 'December');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_MONTH_DECEMBER' , 'it', 'Dicembre');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_YEAR_CHOOSE' , 'en', 'Select Year');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_YEAR_CHOOSE' , 'it', 'Seleziona Anno');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_SUMMARY' , 'en', 'Event calendar');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_SUMMARY' , 'it', 'Calendario eventi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_CAPTION' , 'en', 'Descr event calendar');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_CAPTION' , 'it', 'Calendario eventi descr');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_NUMBER' , 'en', 'week num');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_NUMBER' , 'it', 'num sett');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_MONDAY' , 'en', 'mon');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_MONDAY' , 'it', 'lun');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_TUESDAY' , 'en', 'tue');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_TUESDAY' , 'it', 'mar');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_WEDNESDAY' , 'en', 'wed');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_WEDNESDAY' , 'it', 'mer');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_THURSDAY' , 'en', 'thu');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_THURSDAY' , 'it', 'gio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_FRIDAY' , 'en', 'fri');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_FRIDAY' , 'it', 'ven');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_SATURDAY' , 'en', 'sat');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_SATURDAY' , 'it', 'sab');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_SUNDAY' , 'en', 'sun');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_WEEK_SUNDAY' , 'it', 'dom');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_TITLE' , 'en', 'Events');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_TITLE' , 'it', 'Eventi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_PREVIOUS' , 'en', 'previous events');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_PREVIOUS' , 'it', 'precedenti eventi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_NEXT' , 'en', 'next events');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpcalendar_EVENTS_NEXT' , 'it', 'prossimi eventi');