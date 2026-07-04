
<template>
  <div class="home-page">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>教师招聘报名和资格审核系统</h1>
          <div class="header-actions">
            <el-button v-if="!userStore.isLoggedIn()" @click="$router.push('/login')">登录</el-button>
            <el-button v-else @click="$router.push('/exams')">报名考试</el-button>
            <el-button v-else @click="$router.push('/my-registrations')">我的报名</el-button>
            <el-button v-if="userStore.isAdmin()" type="primary" @click="$router.push('/admin/exams')">管理后台</el-button>
            <el-button v-if="userStore.isLoggedIn()" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card class="welcome-card">
          <div class="welcome-content">
            <h2>欢迎使用教师招聘报名系统</h2>
            <p>面向教育局和学校的教师招聘报名与资格审核平台</p>
            <div class="feature-list">
              <div class="feature-item">
                <el-icon><User /></el-icon>
                <span>用户注册登录</span>
              </div>
              <div class="feature-item">
                <el-icon><EditPen /></el-icon>
                <span>考试报名</span>
              </div>
              <div class="feature-item">
                <el-icon><Upload /></el-icon>
                <span>证书上传</span>
              </div>
              <div class="feature-item">
                <el-icon><CheckCircle /></el-icon>
                <span>资格审核</span>
              </div>
            </div>
            <el-button type="primary" size="large" @click="$router.push('/exams')">查看考试列表</el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { User, EditPen, Upload, CheckCircle } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  window.location.href = '/'
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
}

.header {
  background: linear-gradient(135deg, #409EFF 0%, #667EEA 100%);
  color: white;
  padding: 0 40px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.header-content h1 {
  font-size: 20px;
  font-weight: bold;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.main-content {
  padding: 40px;
}

.welcome-card {
  text-align: center;
  padding: 60px;
}

.welcome-content h2 {
  font-size: 32px;
  margin-bottom: 16px;
  color: #303133;
}

.welcome-content p {
  font-size: 16px;
  color: #606266;
  margin-bottom: 40px;
}

.feature-list {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-bottom: 40px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  color: #303133;
}

.feature-item .el-icon {
  font-size: 24px;
  color: #409EFF;
}
</style>
