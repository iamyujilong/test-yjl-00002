# 教师招聘报名和资格审核系统

面向教育局和学校的教师招聘报名和资格审核系统，实现用户注册登录、考试报名、信息填报、证书上传、后台审核筛选等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.x
- **ORM**: MyBatis-Plus 3.5.5
- **数据库**: SQLite (嵌入式文件数据库，无需独立部署)
- **安全**: Spring Security + JWT
- **文件上传**: Spring Multipart

### 前端
- **框架**: Vue 3 + TypeScript
- **UI组件**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **构建工具**: Vite 5

## 功能特性

### 用户端功能
1. **用户注册登录**
   - 使用身份证号、姓名、手机号注册
   - 支持身份证号或手机号登录

2. **考试报名**
   - 查看正在开放的考试列表
   - 选择一场或多场考试进行报名

3. **信息填报**
   - 基本信息：姓名、性别、出生年月、身份证号、学历、专业
   - 职称选择：无职称、中小学各级职称、大学各级职称
   - 教师资格证：学段（初级/中级/高级）+ 科目

4. **证书上传**
   - 中小学教师：省级或市级荣誉称号、公开课赛课证书、毕业年级证明
   - 大学教师：三项市级荣誉或五项国家级荣誉
   - 每项证书可上传多张图片
   - 追加式上传，不覆盖之前文件
   - 支持JPG/PNG/PDF格式，单文件不超过5MB

5. **报名提交**
   - 提交前显示明确提示框
   - 提交后不可修改、不可补材料
   - 可查看报名状态和审核结果

### 管理端功能
1. **考试管理**
   - 创建、编辑、删除考试
   - 设置考试名称、描述、教师类型、报名时间
   - 控制考试状态（未开始、开放中、已结束）

2. **报名审核**
   - 批量筛选报名信息（按年龄、状态、教师类型等）
   - 查看报名详情和证书材料
   - 审核通过或不通过
   - 不通过可添加备注说明原因

3. **审核记录**
   - 记录每次审核操作
   - 可查看审核历史

## 项目结构

```
teacher-recruitment/
├── backend/                    # 后端项目
│   ├── src/main/java/com/example/teacher/recruitment/
│   │   ├── common/            # 公共类（响应封装、全局异常处理）
│   │   ├── config/            # 配置类（MyBatis-Plus、Security、Web）
│   │   ├── controller/        # 控制器（Auth、Exam、Registration、Certificate、Review）
│   │   ├── dto/               # 数据传输对象（LoginDTO、RegisterDTO）
│   │   ├── entity/            # 实体类（User、Exam、Registration、CertificateItem等）
│   │   ├── filter/            # 过滤器（JWT认证过滤器）
│   │   ├── mapper/            # Mapper接口
│   │   ├── service/           # 服务层接口
│   │   ├── service/impl/      # 服务层实现
│   │   ├── util/              # 工具类（JWT工具）
│   │   └── TeacherRecruitmentApplication.java  # 启动类
│   ├── src/main/resources/
│   │   ├── application.yml    # 应用配置
│   │   └── schema.sql         # 数据库初始化脚本
│   ├── start.bat              # Windows启动脚本
│   └── pom.xml                # Maven依赖
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/               # API接口定义
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # Pinia状态管理
│   │   ├── utils/             # 工具类（Axios封装）
│   │   ├── views/             # 页面组件
│   │   │   ├── admin/         # 管理端页面
│   │   │   ├── Home.vue       # 首页
│   │   │   ├── Login.vue      # 登录页
│   │   │   ├── Register.vue   # 注册页
│   │   │   ├── ExamList.vue   # 考试列表页
│   │   │   ├── Registration.vue # 报名页面
│   │   │   └── MyRegistrations.vue # 我的报名页
│   │   ├── App.vue            # 根组件
│   │   └── main.ts            # 入口文件
│   ├── start.bat              # Windows启动脚本
│   ├── package.json           # 依赖配置
│   ├── vite.config.ts         # Vite配置
│   └── tsconfig.json          # TypeScript配置
└── README.md                   # 项目说明文档
```

## 环境要求

### 后端环境
- **JDK**: 21
- **Maven**: 3.9+

### 前端环境
- **Node.js**: 20+
- **npm**: 10+

## 快速开始

### 1. 安装依赖

#### 后端依赖（JDK 21）
```bash
# Windows使用winget安装
winget install Microsoft.OpenJDK.21

# 或者从官网下载安装
# https://adoptium.net/temurin/releases/?version=21
```

#### 前端依赖（Node.js 20）
```bash
# Windows使用winget安装
winget install OpenJS.NodeJS.LTS

# 或者从官网下载安装
# https://nodejs.org/download/release/v20.11.0/
```

### 2. 启动后端服务

**方法一：使用启动脚本**
```bash
cd backend
start.bat
```

**方法二：手动启动**
```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

后端服务启动后访问：http://localhost:8080

### 3. 启动前端服务

**方法一：使用启动脚本**
```bash
cd frontend
start.bat
```

**方法二：手动启动**
```bash
cd frontend
npm install
npm run dev
```

前端服务启动后访问：http://localhost:5173

### 4. 访问系统

打开浏览器访问：http://localhost:5173

**默认管理员账号**：
- 账号：admin（身份证号字段）
- 密码：admin

## 数据库配置

系统使用 SQLite 嵌入式数据库，无需额外配置数据库服务。

数据库文件路径：`backend/data/example_db.sqlite`

### 数据库表结构

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| exam | 考试表 |
| registration | 报名表 |
| certificate_item | 证书项表 |
| certificate_file | 证书文件表 |
| review_log | 审核记录表 |

### 数据初始化

系统启动时自动执行 `schema.sql`，创建表结构并初始化管理员账号。

## API接口

### 认证接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |
| GET | /api/auth/me | 获取当前用户 |

### 考试接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/exams | 获取所有考试 |
| GET | /api/exams/open | 获取开放中的考试 |
| GET | /api/exams/{id} | 获取考试详情 |
| POST | /api/exams | 创建考试（管理员） |
| PUT | /api/exams/{id} | 更新考试（管理员） |
| DELETE | /api/exams/{id} | 删除考试（管理员） |

### 报名接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/registrations | 创建报名 |
| PUT | /api/registrations/{id} | 更新报名 |
| POST | /api/registrations/{id}/submit | 提交报名 |
| GET | /api/registrations/{id} | 获取报名详情 |
| GET | /api/registrations/user/{userId} | 获取用户报名列表 |
| GET | /api/registrations/page | 分页查询报名（管理员） |

### 证书接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/certificates/items | 创建证书项 |
| PUT | /api/certificates/items/{id} | 更新证书项 |
| DELETE | /api/certificates/items/{id} | 删除证书项 |
| POST | /api/certificates/files/{itemId} | 上传证书文件 |
| DELETE | /api/certificates/files/{id} | 删除证书文件 |
| GET | /api/certificates/files/{id}/download | 下载证书文件 |

### 审核接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/reviews/{registrationId} | 审核报名 |
| GET | /api/reviews/{registrationId} | 获取审核记录 |
| GET | /api/reviews/detail/{registrationId} | 获取报名审核详情 |

## 状态码说明

### 报名状态
| 状态码 | 说明 |
|--------|------|
| 0 | 草稿 |
| 1 | 已提交 |
| 2 | 审核中 |
| 3 | 通过 |
| 4 | 不通过 |

### 考试状态
| 状态码 | 说明 |
|--------|------|
| 0 | 未开始 |
| 1 | 开放中 |
| 2 | 已结束 |

### 用户角色
| 角色码 | 说明 |
|--------|------|
| 0 | 普通用户 |
| 1 | 管理员 |

## 配置说明

### 后端配置（application.yml）

```yaml
server:
  port: 8080                    # 服务端口

spring:
  datasource:
    url: jdbc:sqlite:./data/example_db.sqlite  # SQLite数据库路径
    driver-class-name: org.sqlite.JDBC         # SQLite驱动
  
  servlet:
    multipart:
      max-file-size: 5MB        # 单文件最大5MB
      max-request-size: 50MB    # 请求最大50MB

jwt:
  secret: teacher_recruitment_jwt_secret_key_2024  # JWT密钥
  expiration: 86400000          # JWT过期时间（毫秒）

file:
  upload-dir: ./uploads         # 文件上传目录
```

### 前端配置（vite.config.ts）

```typescript
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,                 # 前端服务端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  # 后端服务地址
        changeOrigin: true
      }
    }
  }
})
```

## 开发说明

### 后端开发

1. **添加新实体**
   - 在 `entity/` 目录创建实体类
   - 在 `mapper/` 目录创建Mapper接口
   - 在 `service/` 目录创建Service接口
   - 在 `service/impl/` 目录创建Service实现
   - 在 `controller/` 目录创建Controller

2. **添加新接口**
   - 在Controller中添加新的请求映射方法
   - 使用 `@RequestBody` 接收JSON参数
   - 使用 `Response.success()` 和 `Response.error()` 返回响应

### 前端开发

1. **添加新页面**
   - 在 `views/` 目录创建Vue组件
   - 在 `router/index.ts` 中添加路由配置

2. **添加新API**
   - 在 `api/` 目录创建API文件
   - 使用Axios封装HTTP请求

3. **状态管理**
   - 在 `stores/` 目录创建Pinia store
   - 使用响应式数据管理应用状态

## 部署说明

### 后端打包部署

```bash
cd backend
mvn clean package
java -jar target/teacher-recruitment-1.0.0.jar
```

### 前端打包部署

```bash
cd frontend
npm run build
# 将dist目录部署到静态资源服务器
```

### Docker部署（可选）

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/teacher-recruitment-1.0.0.jar app.jar
COPY data/ data/
COPY uploads/ uploads/
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

## 常见问题

### Q1: 启动后无法访问
- 检查端口是否被占用（默认8080）
- 检查防火墙是否允许访问
- 检查数据库文件目录是否有读写权限

### Q2: 文件上传失败
- 检查文件格式是否为JPG/PNG/PDF
- 检查文件大小是否超过5MB
- 检查uploads目录是否存在且有读写权限

### Q3: 登录失败
- 检查用户名和密码是否正确
- 默认管理员账号：admin / admin
- 用户注册后使用身份证号或手机号登录

### Q4: 报名提交后无法修改
- 这是系统设计的正常行为
- 提交后状态变为"已提交"，无法修改或补充材料
- 如果需要修改，请联系管理员重置状态

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request！

---

**项目维护**: 教师招聘系统开发团队
**最后更新**: 2026年7月
