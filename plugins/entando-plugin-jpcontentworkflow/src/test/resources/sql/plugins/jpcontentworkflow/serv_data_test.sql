INSERT INTO authroles (rolename, descr) VALUES ('contentType_ART', 'ART Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_EVN', 'EVN Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_RAH', 'RAH Content Type Management');

INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'enterBackend');

INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'coach', 'contentType_RAH');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'coach', 'contentType_ART');
