DELETE FROM guifragment WHERE plugincode = 'jpwebdynamicform';

DELETE FROM widgetconfig WHERE widgetcode = 'jpwebdynamicform_message_form';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpwebdynamicform_message_form');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jpwebdynamicform_message_form';
DELETE FROM widgetcatalog WHERE code = 'jpwebdynamicform_message_form';

DELETE FROM widgetconfig WHERE widgetcode = 'jpwebdynamicform_message_choice';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpwebdynamicform_message_choice');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jpwebdynamicform_message_choice';
DELETE FROM widgetcatalog WHERE code = 'jpwebdynamicform_message_choice';

DELETE FROM localstrings WHERE keycode LIKE 'jpwebdynamicform_%';
