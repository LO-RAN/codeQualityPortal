updateAndCompile.bat:
	- Met � jour les sources et les fichiers de configurations pr�sentes dans les r�pertoires ../Src, ../config, ../DBMS, ../traitements, ../audit, ../def � partir de CVS.
	- Compile les sources pr�sentes dans le r�pertoire ../Src dans le r�pertoire ./bin.
	- Le r�pertoire ./Build/Base/carscode est � copier dans Tomcat/webapps/
	
compile.bat:
	- Compile les sources pr�sentes dans le r�pertoire ../Src dans le r�pertoire ./bin.
	- Le r�pertoire ./Build/Base/carscode est � copier dans Tomcat/webapps/

checkoutAndCompile.bat: doit �tre modifi� afin de sp�cifier la version des sources � r�cup�rer
	- Modifier "-Dsrc.version.tag=V2-0-5" dans le fichier .bat afin de remplacer V2-0-5 par la version � r�cu�rer.
	- R�cup�re les sources et les fichiers de configurations pr�sentes dans les r�pertoires ../Src, ../config, ../DBMS, ../traitements, ../audit, ../def � partir de CVS.
	- Compile les sources pr�sentes dans le r�pertoire ../Src dans le r�pertoire ./bin.
	- Le r�pertoire ./Build/Base/carscode est � copier dans Tomcat/webapps/
