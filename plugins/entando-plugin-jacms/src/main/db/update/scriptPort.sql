-- EN-1858
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jacms_resourceMetadataMapping', 'Mapping between resource Metadata and resource attribute fields', '<mapping>
    <field key="alt"></field>
    <field key="description"></field>
    <field key="legend"></field>
    <field key="title"></field>
</mapping>');

-- 2019-10-30

ALTER TABLE `resources` ADD COLUMN `owner` varchar(128); -- mysql
ALTER TABLE resources ADD COLUMN owner character varying(128); -- postgres


ALTER TABLE contents ADD COLUMN published character varying(20); -- postgres
ALTER TABLE contents ADD COLUMN sync smallint; -- postgres

-- mysql
ALTER TABLE `contents` 
    ADD COLUMN `published` VARCHAR(20) NULL AFTER `onlinexml`,
    ADD COLUMN `sync` INT NULL AFTER `published`;


UPDATE contents SET sync = 1 WHERE workxml = onlinexml;
UPDATE contents SET sync = 0 WHERE workxml <> onlinexml AND onlinexml is not null;
UPDATE contents SET sync = 0 WHERE onlinexml is null;

UPDATE contents SET published = ExtractValue(onlinexml, '/content/lastModified') WHERE onlinexml is not null; -- mysql
UPDATE contents SET published = (xpath('./lastModified/text()', onlinexml::xml))[1] WHERE onlinexml is not null; -- postgres

CREATE INDEX contents_sync_idx ON contents (sync);
