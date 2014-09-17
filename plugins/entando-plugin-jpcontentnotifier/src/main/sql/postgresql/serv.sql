CREATE TABLE jpcontentnotifier_contentchangingevents
(
  id integer NOT NULL,
  eventdate timestamp without time zone NOT NULL,
  operationcode integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  groups character varying,
  notified smallint,
  CONSTRAINT jpcontentnotifier_contentchangingevents_pkey PRIMARY KEY (id)
);
