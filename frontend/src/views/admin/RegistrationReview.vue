
<template>
  <div class="registration-review-page">
    <el-card>
      <div class="filter-bar">
        <el-select v-model="statusFilter" placeholder="选择状态" clearable>
          <el-option label="草稿" :value="0" />
          <el-option label="已提交" :value="1" />
          <el-option label="审核中" :value="2" />
          <el-option label="通过" :value="3" />
          <el-option label="不通过" :value="4" />
        </el-select>
        <el-select v-model="teacherTypeFilter" placeholder="选择教师类型" clearable>
          <el-option label="中小学" :value="0" />
          <el-option label="大学" :value="1" />
        </el-select>
        <el-input v-model="minAgeFilter" placeholder="最小年龄" type="number" style="width: 120px;" />
        <el-input v-model="maxAgeFilter" placeholder="最大年龄" type="number" style="width: 120px;" />
        <el-button type="primary" @click="handleFilter">筛选</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      <el-table :data="registrations" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 0 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="birthDate" label="出生日期" width="120">
          <template #default="{ row }">
            {{ row.birthDate }} ({{ calculateAge(row.birthDate) }}岁)
          </template>
        </el-table-column>
        <el-table-column prop="title" label="职称" width="150" />
        <el-table-column prop="teacherType" label="教师类型" width="100">
          <template #default="{ row }">
            {{ row.teacherType === 0 ? '中小学' : '大学' }}
          </template>
        </el-table-column>
        <el-table-column prop="teachCertificateLevel" label="资格证学段" width="120" />
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
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleDetail(row)">查看详情</el-button>
            <el-button v-if="row.status === 1 || row.status === 2" size="small" type="success" @click="handlePass(row)">通过</el-button>
            <el-button v-if="row.status === 1 || row.status === 2" size="small" type="danger" @click="handleReject(row)">不通过</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getRegistrationPageApi } from '@/api/registration'
import { reviewRegistrationApi } from '@/api/review'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const registrations = ref<any[]>([])
const statusFilter = ref<number | undefined>()
const teacherTypeFilter = ref<number | undefined>()
const minAgeFilter = ref<number | undefined>()
const maxAgeFilter = ref<number | undefined>()

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const calculateAge = (birthDate: string) => {
  if (!birthDate) return 0
  const birth = new Date(birthDate)
  const now = new Date()
  let age = now.getFullYear() - birth.getFullYear()
  if (now.getMonth() < birth.getMonth() || (now.getMonth() === birth.getMonth() && now.getDate() < birth.getDate())) {
    age--
  }
  return age
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

const handleDetail = (row: any) => {
  router.push(`/admin/registration-detail/${row.id}`)
}

const handlePass = async (row: any) => {
  try {
    await reviewRegistrationApi(row.id, userStore.userId!, 3)
    ElMessage.success('审核通过')
    loadRegistrations()
  } catch (error: any) {
    ElMessage.error(error)
  }
}

const handleReject = (row: any) => {
  ElMessageBox.prompt('请输入不通过原因:', '审核不通过', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async ({ value }) => {
    try {
      await reviewRegistrationApi(row.id, userStore.userId!, 4, value)
      ElMessage.success('审核完成')
      loadRegistrations()
    } catch (error: any) {
      ElMessage.error(error)
    }
  }).catch(() => {
    ElMessage.info('已取消')
  })
}

const handleFilter = () => {
  loadRegistrations()
}

const handleReset = () => {
  statusFilter.value = undefined
  teacherTypeFilter.value = undefined
  minAgeFilter.value = undefined
  maxAgeFilter.value = undefined
  loadRegistrations()
}

const loadRegistrations = async () => {
  try {
    const params: any = {}
    if (statusFilter.value !== undefined) params.status = statusFilter.value
    if (teacherTypeFilter.value !== undefined) params.teacherType = teacherTypeFilter.value
    if (minAgeFilter.value !== undefined) params.minAge = minAgeFilter.value
    if (maxAgeFilter.value !== undefined) params.maxAge = maxAgeFilter.value

    const response = await getRegistrationPageApi(params)
    registrations.value = response.data.records || response.data
  } catch (error: any) {
    console.error(error)
  }
}

onMounted(() => {
  loadRegistrations()
})
</script>

<style scoped>
.registration-review-page {
  padding: 20px;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-bar .el-select,
.filter-bar .el-input {
  width: 150px;
}
</style>
