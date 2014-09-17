INSERT INTO authroles (rolename, descr) VALUES ('contentType_ART', 'ART Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_EVN', 'EVN Content Type Management');
INSERT INTO authroles (rolename, descr) VALUES ('contentType_RAH', 'RAH Content Type Management');

INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_ART', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('contentType_EVN', 'enterBackend');

INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_RAH');
INSERT INTO authuserroles (username, rolename) VALUES ('editorCoach', 'contentType_ART');