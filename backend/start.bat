@echo off
chcp 65001 >nul
echo ============================================
echo   教师招聘报名和资格审核系统 - 后端启动脚本
echo ============================================
echo.

set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "MAVEN_HOME=C:\Program Files\Apache\maven-3.9.6"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo 正在检查Java环境...
java -version 2>&1 | findstr /i "version"
if errorlevel 1 (
    echo ERROR: Java 21 未安装或未配置正确
    echo 请访问 https://adoptium.net/temurin/releases/ 下载并安装 JDK 21
    pause
    exit /b 1
)

echo.
echo 正在检查Maven环境...
mvn -version 2>&1 | findstr /i "Apache Maven"
if errorlevel 1 (
    echo ERROR: Maven 未安装或未配置正确
    echo 请访问 https://maven.apache.org/download.cgi 下载并安装 Maven
    pause
    exit /b 1
)

echo.
echo 正在创建数据目录...
if not exist "data" mkdir data
if not exist "uploads" mkdir uploads

echo.
echo 正在编译项目...
mvn clean compile -q
if errorlevel 1 (
    echo ERROR: 项目编译失败
    pause
    exit /b 1
)

echo.
echo 编译成功，正在启动服务...
echo 服务地址: http://localhost:8080
echo 管理员账号: admin / admin
echo.
mvn spring-boot:run

pause
