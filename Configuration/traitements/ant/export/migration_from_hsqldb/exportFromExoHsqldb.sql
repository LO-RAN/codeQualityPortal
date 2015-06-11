select u.username, u.lastname, u.firstname, u.email, u.password, u.createddate, u.lastlogintime
, replace(replace(replace(replace(replace(replace(upper(m.groupid),'/CAQS/',''),'DEVELOPPEUR','DEVELOPER'),'ADMINISTRATEUR','ADMINISTRATOR'),'CHEFDEPROJET','PROJECTMANAGER'),'QUALITICIEN','QUALITYEXPERT'),'ARCHITECTE','ARCHITECT')
from exo_user u, exo_membership m where u.username=m.username and m.groupid like '/Caqs/%' and m.groupid <> '/Caqs/ROLE_USER'
order by u.username ;

