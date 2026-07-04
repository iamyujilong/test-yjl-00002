
<template>
  <div class="my-registrations-page">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>我的报名</h1>
          <div class="header-actions">
            <el-button @click="$router.push('/exams')">考试列表</el-button>
            <el-button v-if="userStore.isAdmin()" type="primary" @click="$router.push('/admin/exams')">管理后台</el-button>
            <el-button @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card>
          <div class="filter-bar">
            <el-select v-model="statusFilter" placeholder="选择状态" clearable>
              <el-option label="草稿" :value="0" />
              <el-option label="已提交" :value="1" />
              <el-option label="审核中" :value="2" />
              <el-option label="通过" :value="3" />
              <el-option label="不通过" :value="4" />
            </el-select>
          </div>
          <el-table :data="registrations" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column label="考试名称" width="200">
              <template #default="{ row }">
                {{ getExamName(row.examId) }}
              </template>
            </el-table-column>
            <el-table-column prop="title" label="职称" width="150" />
            <el-table-column prop="teacherType" label="教师类型" width="100">
              <template #default="{ row }">
                {{ row.teacherType === 0 ? '中小学' : '大学' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="submitTime" label="提交时间" width="180">
              <template #default="{ row }">
                {{ row.submitTime ? formatDate(row.submitTime) : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button v-if="row.status === 0" type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button v-if="row.status === 0" type="success" size="small" @click="handleSubmit(row)">提交</el-button>
                <el-button v-if="userStore.isAdmin()" size="small" @click="handleDetail(row)">查看详情</el-button>
                <span v-else-if="row.status >= 1">{{ getStatusText(row.status) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getRegistrationByUserIdApi } from '@/api/registration'
import { getExamListApi } from '@/api/exam'
import { submitRegistrationApi } from '@/api/registration'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const registrations = ref<any[]>([])
const exams = ref<any[]>([])
const statusFilter = ref<number | undefined>()

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'warning'
    case 2: return 'primary'
    case 3: return 'success'
    case 4: return 'danger'
    default: return 'info'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '草稿'
    case 1: return '已提交'
    case 2: return '审核中'
    case 3: return '通过'
    case 4: return '不通过'
    default: return '未知'
  }
}

const getExamName = (examId: number) => {
  const exam = exams.value.find(e => e.id === examId)
  return exam ? exam.name : '未知考试'
}

const handleEdit = (row: any) => {
  router.push(`/registration/${row.examId}`)
}

const handleSubmit = async (row: any) => {
  try {
    await submitRegistrationApi(row.id)
    ElMessage.success('提交成功')
    loadRegistrations()
  } catch (error: any) {
    ElMessage.error(error)
  }
}

const handleDetail = (row: any) => {
  router.push(`/admin/registration-detail/${row.id}`)
}

const handleLogout = () => {
  userStore.logout()
  window.location.href = '/'
}

const loadRegistrations = async () => {
  if (!userStore.userId) return
  try {
    const response = await getRegistrationByUserIdApi(userStore.userId!)
    registrations.value = response.data
  } catch (error: any) {
    console.error(error)
  }
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
  loadRegistrations()
  loadExams()
})
</script>

<style scoped>
.my-registrations-page {
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
  margin-bottom: 20px;
}

.filter-bar .el-select {
  width: 200px;
}
</style>
