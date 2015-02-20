INSERT INTO jpcollaboration_ideainstance(code, createdat) VALUES ('default', '2013-01-01 00:00:00');

INSERT INTO jpcollaboration_ideainstance_group(code, groupname)   VALUES ('default', 'free');

INSERT INTO jpcollaboration_idea (id, title, descr, pubdate, username, status, votepositive, votenegative, instancecode) VALUES ('1', 'idea di prova', 'facciamo una prova?', '2011-03-08 10:11:12', 's.puddu', 2, 3, 5, 'default');

INSERT INTO jpcollaboration_idea_comments (id, ideaid, creationdate, commenttext, status, username) VALUES (2, '1', '2011-03-08 11:00:00', 'commento1', 2, 'admin');
INSERT INTO jpcollaboration_idea_comments (id, ideaid, creationdate, commenttext, status, username) VALUES (3, '1', '2011-03-08 12:00:00', 'commento2', 2, 'admin');
INSERT INTO jpcollaboration_idea_comments (id, ideaid, creationdate, commenttext, status, username) VALUES (4, '1', '2011-03-08 13:00:00', 'commento3', 2, 'admin');

INSERT INTO jpcollaboration_idea_tags (ideaid, catcode) VALUES ('1', 'home');
