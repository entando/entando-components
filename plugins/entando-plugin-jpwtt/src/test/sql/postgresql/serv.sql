-- Test Serv Script

CREATE TABLE jpwtt_tickets
(
	code character varying(16) NOT NULL,
	creationdate timestamp without time zone NOT NULL,
	nome character varying(50),
	cognome character varying(50),
	codfisc character varying(20),
	comune character varying(50),
	localita character varying(100),
	tipoindirizzo character varying(10),
	indirizzo character varying(100),
	numeroindirizzo character varying(8),
	telefono character varying(15),
	email character varying(100),
	message character varying,
	author character varying(40),
	user_interventiontype smallint,
	op_interventiontype smallint,
	priority smallint,
	wttrole character varying(20),
	wttoperator character varying(40),
	status smallint,
	closingdate timestamp without time zone,
	resolved smallint,
	CONSTRAINT jpwtt_tickets_pkey PRIMARY KEY (code)
);

CREATE TABLE jpwtt_ticketoperations
(
	id integer NOT NULL,
	ticketcode character varying(16) NOT NULL,
	operator character varying(40) NOT NULL,
	operationcode smallint NOT NULL,
	interventiontype smallint,
	priority smallint,
	wttrole character varying(20),
	note character varying,
	date timestamp without time zone NOT NULL,
	CONSTRAINT jpwtt_ticketoperations_pkey PRIMARY KEY (id),
	CONSTRAINT jpwtt_ticketoperations_ticketcode_fkey FOREIGN KEY (ticketcode)
		REFERENCES jpwtt_tickets (code) MATCH SIMPLE
		ON UPDATE RESTRICT ON DELETE RESTRICT
);
