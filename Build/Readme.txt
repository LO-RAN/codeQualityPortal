updateAndCompile.bat:
	- Met à jour les sources et les fichiers de configurations présentes dans les répertoires ../Src, ../config, ../DBMS, ../traitements, ../audit, ../def à partir de CVS.
	- Compile les sources présentes dans le répertoire ../Src dans le répertoire ./bin.
	- Le répertoire ./Build/Base/carscode est à copier dans Tomcat/webapps/
	
compile.bat:
	- Compile les sources présentes dans le répertoire ../Src dans le répertoire ./bin.
	- Le répertoire ./Build/Base/carscode est à copier dans Tomcat/webapps/

checkoutAndCompile.bat: doit être modifié afin de spécifier la version des sources à récupérer
	- Modifier "-Dsrc.version.tag=V2-0-5" dans le fichier .bat afin de remplacer V2-0-5 par la version à récuérer.
	- Récupère les sources et les fichiers de configurations présentes dans les répertoires ../Src, ../config, ../DBMS, ../traitements, ../audit, ../def à partir de CVS.
	- Compile les sources présentes dans le répertoire ../Src dans le répertoire ./bin.
	- Le répertoire ./Build/Base/carscode est à copier dans Tomcat/webapps/
