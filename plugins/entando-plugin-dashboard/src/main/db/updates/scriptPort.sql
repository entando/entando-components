INSERT INTO public.sysconfig(version, item, descr, config) VALUES ('production', 'jpcms_dashboard_config', 'Definition of entando iot default dashboard and datasource', '<?xml version="1.0" encoding="UTF-8"?> <iot> <datasourceCode>0L</datasourceCode><dashboardId>0</dashboardId></iot>');

UPDATE sysconfig SET config = regexp_replace(config, '</contenttypes>','
	<contenttype typecode="PRK" typedescr="Info_Parking" viewpage="**NULL**" listmodel="**NULL**" defaultmodel="**NULL**">
		<attributes>
			<attribute name="occupati" attributetype="Number" description="occupati" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:occupati</role>
				</roles>
			</attribute>
			<attribute name="nStalli" attributetype="Number" description="nStalli" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:nStalli</role>
				</roles>
			</attribute>
			<attribute name="latitudine" attributetype="Monotext" description="latitudine" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:latitude</role>
				</roles>
			</attribute>
			<attribute name="longitudin" attributetype="Monotext" description="longitudin" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:longitude</role>
				</roles>
			</attribute>
			<attribute name="dsrcCode" attributetype="Monotext" description="datasourceCode" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:datasourceCode</role>
				</roles>
			</attribute>
			<attribute name="dboardId" attributetype="Number" description="dashboardId" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jpcmsiot:dashboardId</role>
				</roles>
			</attribute>
		</attributes>
	</contenttype>
</contenttypes>'
) WHERE item = 'contentTypes' AND config NOT LIKE 'contenttype typecode="PRK"';
