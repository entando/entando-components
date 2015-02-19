INSERT INTO jpwebform_typeversions (code, typecode, version, modelxml, stepxml) VALUES (1, 'COM', 1, '<?xml version="1.0" encoding="UTF-8"?>
<formtype typecode="COM" typedescr="Company Form" version="1" repeatable="true" ignoreVersionEdit="true" ignoreVersionDisplay="false">
	<attributes>
		<attribute name="Company" attributetype="Monotext" description="company">
			<validations>
				<required>true</required>
			</validations>
		</attribute>
		<attribute name="Address" attributetype="Monotext" description="Address" />
		<attribute name="Email" attributetype="Monotext" description="Email" />
		<attribute name="Note" attributetype="Monotext" description="Note" />
	</attributes>
</formtype>

', '<?xml version="1.0" encoding="UTF-8"?>
<formtype code="COM" confirmGui="false" builtConfirmGui="false" builtEndPointGui="true">
	<step code="1" builtGui="true">
		<attribute name="Company" view="false" />
		<attribute name="Address" view="false" />
		<attribute name="Email" view="false" />
		<attribute name="Note" view="false" />
	</step>
	<step code="2" builtGui="true">
		<attribute name="Company" view="true" />
		<attribute name="Address" view="true" />
		<attribute name="Email" view="true" />
		<attribute name="Note" view="true" />
	</step>
</formtype>

');
INSERT INTO jpwebform_typeversions (code, typecode, version, modelxml, stepxml) VALUES (2, 'PER', 1, '<?xml version="1.0" encoding="UTF-8"?>
<formtype typecode="PER" typedescr="Personal" version="1" repeatable="true" ignoreVersionEdit="true" ignoreVersionDisplay="false">
	<attributes>
		<attribute name="Name" attributetype="Monotext" description="Name" />
		<attribute name="Surname" attributetype="Monotext" description="Surname" />
		<attribute name="Birthdate" attributetype="Date" description="Birthdate" />
	</attributes>
</formtype>

', '<?xml version="1.0" encoding="UTF-8"?>
<formtype code="PER" confirmGui="true" builtConfirmGui="true" builtEndPointGui="true">
	<step code="1" builtGui="true">
		<attribute name="Name" view="false" />
		<attribute name="Surname" view="false" />
		<attribute name="Birthdate" view="false" />
	</step>
	<step code="2" builtGui="true">
		<attribute name="Name" view="true" />
		<attribute name="Surname" view="true" />
		<attribute name="Birthdate" view="true" />
	</step>
</formtype>

');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (1, '2', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Company;type=label#]]
		[[#fieldName=Company;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Company;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Address;type=label#]]
		[[#fieldName=Address;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Address;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Email;type=label#]]
		[[#fieldName=Email;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Email;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Note;type=label#]]
		[[#fieldName=Note;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Note;type=input;edit=false#]]
	</div>
</div>
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (1, '1', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Company;type=label#]]
		[[#fieldName=Company;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Company;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Address;type=label#]]
		[[#fieldName=Address;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Address;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Email;type=label#]]
		[[#fieldName=Email;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Email;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Note;type=label#]]
		[[#fieldName=Note;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Note;type=input#]]
	</div>
</div>
<p class="form-actions">
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (1, 'ending', '<div class="alert alert-success">
	<strong>Success!</strong> Thank You.
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (2, '2', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Name;type=label#]]
		[[#fieldName=Name;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Name;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Surname;type=label#]]
		[[#fieldName=Surname;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Surname;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Birthdate;type=label#]]
		[[#fieldName=Birthdate;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Birthdate;type=input;edit=false#]]
	</div>
</div>
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (2, '1', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Name;type=label#]]
		[[#fieldName=Name;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Name;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Surname;type=label#]]
		[[#fieldName=Surname;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Surname;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Birthdate;type=label#]]
		[[#fieldName=Birthdate;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Birthdate;type=input#]]
	</div>
</div>
<p class="form-actions">
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (2, 'confirm', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_gui (code, stepcode, gui, css) VALUES (2, 'ending', '<div class="alert alert-success">
	<strong>Success!</strong> Thank You.
</div>
', '');


INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('COM', '1', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Company;type=label#]]
		[[#fieldName=Company;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Company;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Address;type=label#]]
		[[#fieldName=Address;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Address;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Email;type=label#]]
		[[#fieldName=Email;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Email;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Note;type=label#]]
		[[#fieldName=Note;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Note;type=input#]]
	</div>
</div>
<p class="form-actions">
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('COM', '2', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Company;type=label#]]
		[[#fieldName=Company;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Company;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Address;type=label#]]
		[[#fieldName=Address;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Address;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Email;type=label#]]
		[[#fieldName=Email;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Email;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Note;type=label#]]
		[[#fieldName=Note;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Note;type=input;edit=false#]]
	</div>
</div>
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('COM', 'ending', '<div class="alert alert-success">
	<strong>Success!</strong> Thank You.
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('PER', '1', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Name;type=label#]]
		[[#fieldName=Name;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Name;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Surname;type=label#]]
		[[#fieldName=Surname;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Surname;type=input#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Birthdate;type=label#]]
		[[#fieldName=Birthdate;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Birthdate;type=input#]]
	</div>
</div>
<p class="form-actions">
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('PER', '2', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Name;type=label#]]
		[[#fieldName=Name;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Name;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Surname;type=label#]]
		[[#fieldName=Surname;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Surname;type=input;edit=false#]]
	</div>
</div>
<div class="control-group">
	<div class="control-label">
		[[#fieldName=Birthdate;type=label#]]
		[[#fieldName=Birthdate;type=info#]]
	</div>
	<div class="controls">
		[[#fieldName=Birthdate;type=input;edit=false#]]
	</div>
</div>
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('PER', 'confirm', '<h2>
	[[#title#]]
</h2>
<div class="form-horizontal">
	[[#form-start#]]
<p class="form-actions">
	[[#form-back#]]
	&#32;
	[[#form-submit#]]
</p>
	[[#form-end#]]
</div>
', '');
INSERT INTO jpwebform_workgui (typecode, stepcode, gui, css) VALUES ('PER', 'ending', '<div class="alert alert-success">
	<strong>Success!</strong> Thank You.
</div>
', '');