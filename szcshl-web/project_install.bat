@echo off
echo 合并压缩业务脚本文件
call gulp default

echo 编译打包项目
call mvn clean install
pause