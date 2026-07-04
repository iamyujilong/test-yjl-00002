
<template>
  <div class="registration-detail-page">
    <el-card v-if="detail">
      <div class="card-header">
        <h3>报名详情 - {{ detail.registration?.name }}</h3>
        <el-button @click="$router.back()">返回列表</el-button>
      </div>

      <el-descriptions :column="2" border class="mb-30">
        <el-descriptions-item label="考试名称">{{ detail.exam?.name }}</el-descriptions-item>
        <el-descriptions-item label="教师类型">{{ detail.registration?.teacherType === 0 ? '中小学' : '大学' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.registration?.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.registration?.gender === 0 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ detail.registration?.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detail.registration?.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.registration?.phone }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ detail.registration?.education }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ detail.registration?.major }}</el-descriptions-item>
        <el-descriptions-item label="职称">{{ detail.registration?.title }}</el-descriptions-item>
        <el-descriptions-item label="教师资格证学段">{{ detail.registration?.teachCertificateLevel }}</el-descriptions-item>
        <el-descriptions-item label="教师资格证科目">{{ detail.registration?.teachCertificateSubject }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <el-tag :type="getStatusType(detail.registration?.status)">{{ getStatusText(detail.registration?.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间" :span="2">{{ detail.registration?.submitTime ? formatDate(detail.registration?.submitTime) : '-' }}</el-descriptions-item>
      </el-descriptions>

      <h4>证书材料</h4>
      <div v-for="cert in detail.certificates" :key="cert.id" class="certificate-item">
        <el-card>
          <div class="cert-header">
            <span>{{ getCertificateTypeName(cert.type) }} - {{ cert.name }}</span>
            <span v-if="cert.level" class="level-tag">{{ getLevelText(cert.level) }}</span>
          </div>
          <div class="file-list">
            <div v-for="file in getFiles(cert.id)" :key="file.id" class="file-item">
              <el-icon><Document /></el-icon>
              <span>{{ file.fileName }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <h4>审核记录</h4>
      <el-timeline v-if="detail.reviewLogs && detail.reviewLogs.length > 0">
        <el-timeline-item
          v-for="log in detail.reviewLogs"
          :key="log.id"
          :type="log.reviewStatus === 3 ? 'success' : 'danger'"
        >
          <template #dot>
            <el-icon v-if="log.reviewStatus === 3"><Check /></el-icon>
            <el-icon v-else><Close /></el-icon>
          </template>
          <div>
            <p>{{ log.reviewStatus === 3 ? '审核通过' : '审核不通过' }}</p>
            <p v-if="log.remark">备注：{{ log.remark }}</p>
            <p>{{ formatDate(log.reviewTime) }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
      <p v-else class="empty-text">暂无审核记录</p>

      <div class="actions" v-if="detail.registration?.status === 1 || detail.registration?.status === 2">
        <el-button type="success" @click="handlePass">审核通过</el-button>
        <el-button type="danger" @click="handleReject">审核不通过</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getRegistrationDetailApi } from '@/api/review'
import { reviewRegistrationApi } from '@/api/review'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Check, Close } from '@element-plus/icons-vue'

const route = useRoute()
const userStore = useUserStore()

const detail = ref<any>(null)
const certificateFiles = ref<Map<number, any[]>>(new Map())

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

const getCertificateTypeName = (type: string) => {
  switch (type) {
    case 'HONOR': return '荣誉称号'
    case 'OPEN_CLASS': return '公开课赛课证书'
    case 'GRADUATION': return '毕业年级证明'
    default: return type
  }
}

const getLevelText = (level: string) => {
  switch (level) {
    case 'PROVINCIAL': return '省级'
    case 'MUNICIPAL': return '市级'
    case 'NATIONAL': return '国家级'
    default: return level
  }
}

const getFiles = (itemId: number) => {
  return certificateFiles.value.get(itemId) || []
}

const handlePass = async () => {
  try {
    await reviewRegistrationApi(Number(route.params.id), userStore.userId!, 3)
    ElMessage.success('审核通过')
    loadDetail()
  } catch (error: any) {
    ElMessage.error(error)
  }
}

const handleReject = () => {
  ElMessageBox.prompt('请输入不通过原因:', '审核不通过', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async ({ value }) => {
    try {
      await reviewRegistrationApi(Number(route.params.id), userStore.userId!, 4, value)
      ElMessage.success('审核完成')
      loadDetail()
    } catch (error: any) {
      ElMessage.error(error)
    }
  }).catch(() => {
    ElMessage.info('已取消')
  })
}

const loadDetail = async () => {
  try {
    const response = await getRegistrationDetailApi(Number(route.params.id))
    detail.value = response.data

    if (detail.value.certificates) {
      for (const cert of detail.value.certificates) {
        if (cert.files) {
          certificateFiles.value.set(cert.id, cert.files)
        }
      }
    }
  } catch (error: any) {
    console.error(error)
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.registration-detail-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h3 {
  margin: 0;
}

.mb-30 {
  margin-bottom: 30px;
}

.certificate-item {
  margin-bottom: 20px;
}

.cert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  font-weight: bold;
}

.level-tag {
  padding: 4px 12px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 12px;
}

.file-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.file-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.empty-text {
  text-align: center;
  color: #909399;
  padding: 20px;
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 20px;
  justify-content: center;
}

.actions .el-button {
  width: 150px;
}
</style>
