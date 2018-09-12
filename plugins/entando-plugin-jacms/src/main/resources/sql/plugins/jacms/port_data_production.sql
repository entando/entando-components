INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'contentTypes', 'Definition of the Content Types', '<contenttypes>
</contenttypes>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'imageDimensions', 'Definition of the resized image dimensions', '<Dimensions>
	<Dimension>
		<id>1</id>
		<dimx>90</dimx>
		<dimy>90</dimy>
	</Dimension>
	<Dimension>
		<id>2</id>
		<dimx>130</dimx>
		<dimy>130</dimy>
	</Dimension>
	<Dimension>
		<id>3</id>
		<dimx>150</dimx>
		<dimy>150</dimy>
	</Dimension>
</Dimensions>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jacms_resourceMetadataMapping', 'Mapping between resource Metadata and resource attribute fields', '<mapping>
    <field key="alt"></field>
    <field key="description"></field>
    <field key="legend"></field>
    <field key="title"></field>
</mapping>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'subIndexDir', 'Name of the sub-directory containing content indexing files', 'index');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jacms_BACK_TO_EDIT_CONTENT', 'en', 'Back to edit content');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jacms_BACK_TO_EDIT_CONTENT', 'it', 'Torna alla modifica dei contenuti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jacms_CONTENT_PREVIEW', 'en', 'Content preview');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jacms_CONTENT_PREVIEW', 'it', 'Anteprima contenuto');