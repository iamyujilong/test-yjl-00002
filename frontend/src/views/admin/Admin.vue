
<template>
  <div class="admin-page">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">管理后台</div>
        <el-menu :default-active="activeMenu" class="sidebar-menu">
          <el-menu-item index="/admin/exams">
            <el-icon><Document /></el-icon>
            <span>考试管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/registrations">
            <el-icon><List /></el-icon>
            <span>报名审核</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-content">
            <span>欢迎，{{ userStore.name }}</span>
            <el-button @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Document, List } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
}

.sidebar {
  background: #2a3f5f;
  color: white;
}

.logo {
  padding: 20px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  border-bottom: 1px solid #1a2e4a;
}

.sidebar-menu {
  border-right: none;
}

.sidebar-menu .el-menu-item {
  color: #b8c7ce;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-menu-item.is-active {
  background: #1a2e4a;
  color: white;
}

.header {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.main-content {
  padding: 20px;
  background: #f5f7fa;
}
</style>
