@echo off
echo ��װ��Ŀ���������

call mvn install:install-file -DgroupId=com.jspsmart.upload -DartifactId=jspsmartupload -Dversion=1.0 -Dpackaging=jar -Dfile=jspsmartupload.jar

pause