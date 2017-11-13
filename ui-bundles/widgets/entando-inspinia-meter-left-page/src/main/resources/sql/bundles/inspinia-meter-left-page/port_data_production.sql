INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-inspinia-meter-left-page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">entando-inspinia-meter-left-page</property>
<property key="it">entando-inspinia-meter-left-page</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked)
VALUES ('entando-inspinia-meter-left-page', 'entando-inspinia-meter-left-page', NULL, NULL, '<div class="ibox float-e-margins">
    <div class="ibox-title">
        <span class="label label-success pull-right">Monthly</span>
        <h5>Mortgage requests</h5>
    </div>
    <div class="ibox-content">
        <h1 class="no-margins">120</h1>
    </div>
</div>', 1);