DELETE FROM guifragment WHERE plugincode = 'jpfacetnav';

DELETE FROM widgetconfig WHERE widgetcode = 'jpfacetnav_results';
DELETE FROM widgetconfig WHERE widgetcode = 'jpfacetnav_tree';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpfacetnav_results');
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpfacetnav_tree');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jpfacetnav_results';
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpfacetnav_tree';
DELETE FROM widgetcatalog WHERE code = 'jpfacetnav_results';
DELETE FROM widgetcatalog WHERE code = 'jpfacetnav_tree';

DELETE FROM localstrings WHERE keycode = 'jpfacetnav_REMOVE_FILTER';
DELETE FROM localstrings WHERE keycode = 'jpfacetnav_TITLE_TREE';
DELETE FROM localstrings WHERE keycode = 'jpfacetnav_TITLE_FACET_RESULTS';