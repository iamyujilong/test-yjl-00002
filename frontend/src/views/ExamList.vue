
<template>
  <div class="exam-list-page">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>教师招聘考试列表</h1>
          <div class="header-actions">
            <el-button @click="$router.push('/')">首页</el-button>
            <el-button v-if="userStore.isLoggedIn()" @click="$router.push('/my-registrations')">我的报名</el-button>
            <el-button v-if="userStore.isAdmin()" type="primary" @click="$router.push('/admin/exams')">管理后台</el-button>
            <el-button v-if="!userStore.isLoggedIn()" @click="$router.push('/login')">登录</el-button>
            <el-button v-if="userStore.isLoggedIn()" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card>
          <div class="filter-bar">
            <el-select v-model="teacherTypeFilter" placeholder="选择教师类型" clearable>
              <el-option :label="'中小学'" :value="0" />
              <el-option :label="'大学'" :value="1" />
            </el-select>
            <el-select v-model="statusFilter" placeholder="选择状态" clearable>
              <el-option :label="'未开始'" :value="0" />
              <el-option :label="'开放中'" :value="1" />
              <el-option :label="'已结束'" :value="2" />
            </el-select>
          </div>
          <el-table :data="filteredExams" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="考试名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="teacherType" label="教师类型" width="100">
              <template #default="{ row }">
                {{ row.teacherType === 0 ? '中小学' : '大学' }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" label="报名开始" width="180">
              <template #default="{ row }">
                {{ formatDate(row.startTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="endTime" label="报名结束" width="180">
              <template #default="{ row }">
                {{ formatDate(row.endTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button 
                  v-if="row.status === 1 && userStore.isLoggedIn()" 
                  type="primary" 
                  size="small"
                  @click="handleRegister(row)"
                >
                  立即报名
                </el-button>
                <span v-else-if="!userStore.isLoggedIn()">请登录</span>
                <span v-else>{{ getStatusText(row.status) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getExamListApi } from '@/api/exam'

const router = useRouter()
const userStore = useUserStore()

const exams = ref<any[]>([])
const teacherTypeFilter = ref<number | undefined>()
const statusFilter = ref<number | undefined>()

const filteredExams = computed(() => {
  return exams.value.filter(exam => {
    if (teacherTypeFilter.value !== undefined && exam.teacherType !== teacherTypeFilter.value) {
      return false
    }
    if (statusFilter.value !== undefined && exam.status !== statusFilter.value) {
      return false
    }
    return true
  })
})

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'success'
    case 2: return 'warning'
    default: return 'info'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '未开始'
    case 1: return '开放中'
    case 2: return '已结束'
    default: return '未知'
  }
}

const handleRegister = (exam: any) => {
  router.push(`/registration/${exam.id}`)
}

const handleLogout = () => {
  userStore.logout()
  window.location.href = '/'
}

const loadExams = async () => {
  try {
    const response = await getExamListApi()
    exams.value = response.data
  } catch (error: any) {
    console.error(error)
  }
}

onMounted(() => {
  loadExams()
})
</script>

<style scoped>
.exam-list-page {
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
  padding: 20px;
}

.filter-bar {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.filter-bar .el-select {
  width: 200px;
}
</style>
