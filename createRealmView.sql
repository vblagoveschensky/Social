CREATE VIEW "REALM" AS SELECT login, password, Usergroup.name AS usergroup
FROM Person_Usergroup
INNER JOIN Person ON Person.id = Person_Usergroup.members_id
INNER JOIN Usergroup ON Usergroup.id =  Person_Usergroup.groups_id