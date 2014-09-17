-- Serv Production Sript
-- required Mysql 5.5 or later

CREATE TABLE jpwtt_tickets
(
	code character varying(16) NOT NULL,
	creationdate timestamp NOT NULL DEFAULT 0,
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
	message longtext,
	author character varying(40),
	user_interventiontype smallint,
	op_interventiontype smallint,
	priority smallint,
	wttrole character varying(20),
	wttoperator character varying(40),
	status smallint,
	closingdate timestamp DEFAULT 0,
	resolved smallint,
	CONSTRAINT jpwtt_tickets_pkey PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE jpwtt_ticketoperations
(
	id integer NOT NULL,
	ticketcode character varying(16) NOT NULL,
	operator character varying(40) NOT NULL,
	operationcode smallint NOT NULL,
	interventiontype smallint,
	priority smallint,
	wttrole character varying(20),
	note longtext,
	date timestamp DEFAULT 0,
	CONSTRAINT jpwtt_ticketoperations_pkey PRIMARY KEY (id),
	CONSTRAINT jpwtt_ticketoperations_ticketcode_fkey FOREIGN KEY (ticketcode)
		REFERENCES jpwtt_tickets (code) MATCH SIMPLE
		ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO authpermissions ( permissionname, descr ) VALUES ( 'jpwttAdmin', 'WTT Administration' );
INSERT INTO authpermissions ( permissionname, descr ) VALUES ( 'jpwttOperator', 'WTT Operations' );

