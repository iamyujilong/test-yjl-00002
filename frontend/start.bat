@echo off
chcp 65001 >nul
echo ============================================
echo   教师招聘报名和资格审核系统 - 前端启动脚本
echo ============================================
echo.

echo 正在检查Node.js环境...
node --version 2>&1 | findstr /i "v20"
if errorlevel 1 (
    echo ERROR: Node.js 20 未安装或未配置正确
    echo 请访问 https://nodejs.org/download/release/v20.11.0/ 下载并安装 Node.js 20
    pause
    exit /b 1
)

echo.
echo 正在检查npm环境...
npm --version
if errorlevel 1 (
    echo ERROR: npm 未安装
    pause
    exit /b 1
)

echo.
echo 正在安装依赖...
if not exist "node_modules" (
    npm install
    if errorlevel 1 (
        echo ERROR: 依赖安装失败
        pause
        exit /b 1
    )
)

echo.
echo 依赖安装成功，正在启动开发服务器...
echo 访问地址: http://localhost:5173
echo.
npm run dev

pause
