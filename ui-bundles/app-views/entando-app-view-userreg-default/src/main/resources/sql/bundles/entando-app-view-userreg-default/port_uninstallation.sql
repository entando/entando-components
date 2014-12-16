DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_loginUserReg';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpuserreg_loginUserReg');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpuserreg_loginUserReg';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_loginUserReg';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_Registration';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpuserreg_Registration');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpuserreg_Registration';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_Registration';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_profile_choice';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_profile_choice';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_Activation';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_Activation';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_Recover';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_Recover';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_Reactivation';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_Reactivation';

DELETE FROM widgetconfig WHERE widgetcode = 'jpuserreg_Suspension';
DELETE FROM widgetcatalog WHERE code = 'jpuserreg_Suspension';

DELETE FROM localstrings WHERE keycode LIKE 'jpuserreg_%';
