@echo off
echo 安装项目相关依赖包

call mvn install:install-file -DgroupId=com.jspsmart.upload -DartifactId=jspsmartupload -Dversion=1.0 -Dpackaging=jar -Dfile=jspsmartupload.jar

pause