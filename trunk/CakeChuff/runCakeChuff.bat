@echo off
:: PATH=$PATH
:: PATH=C:\Archivos de programa\Java\jre6\bin;$PATH
:: Java Path
:: JAVA_HOME=C:\Archivos de programa\Java\jre6\
:: ClassPath
set CLASSPATH=bin;lib\jME_2.0.jar;lib\iText-2.1.5.jar;lib\MD5Importer-jME2.0-v1.3.jar;lib\jdom.jar
java -Djava.library.path="lib/lib/natives" -Djava.util.logging.config.file="logging.properties" cc.CakeChuff
