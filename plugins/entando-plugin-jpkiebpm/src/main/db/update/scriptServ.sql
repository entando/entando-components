-- Table: public.jpkiebpm_kieformoverride

-- DROP TABLE public.jpkiebpm_kieformoverride;

CREATE TABLE public.jpkiebpm_kieformoverride
(
  id integer NOT NULL,
  date date,
  field text,
  containerid text,
  processid text,
  override text,
  CONSTRAINT id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.jpkiebpm_kieformoverride
  OWNER TO agile;

-- EN-2205
ALTER TABLE public.jpkiebpm_kieformoverride ADD COLUMN sourceid text;
ALTER TABLE public.jpkiebpm_kieformoverride ADD COLUMN widgetinfoid integer;
ALTER TABLE public.jpkiebpm_kieformoverride ADD COLUMN active boolean;