CREATE TABLE jpaddressbook_contacts
(
  contactkey character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  contactxml character varying NOT NULL,
  contactowner character varying(40) NOT NULL,
  publiccontact smallint NOT NULL,
  CONSTRAINT jpaddressbook_contacts_pkey PRIMARY KEY (contactkey)
)
WITH (OIDS=FALSE);

CREATE TABLE jpaddressbook_contactsearch
(
  contactkey character varying(40) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpaddressbook_contactsearch_contactkey_fkey FOREIGN KEY (contactkey)
      REFERENCES jpaddressbook_contacts (contactkey) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);