
<template>
  <div class="registration-page">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>考试报名</h1>
          <div class="header-actions">
            <el-button @click="$router.push('/exams')">返回考试列表</el-button>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card v-if="exam">
          <h3>{{ exam.name }}</h3>
          <p>{{ exam.description }}</p>
          <p>教师类型：{{ exam.teacherType === 0 ? '中小学' : '大学' }}</p>
          <p>报名时间：{{ formatDate(exam.startTime) }} - {{ formatDate(exam.endTime) }}</p>
        </el-card>

        <el-card v-if="registration">
          <el-steps :active="currentStep" finish-status="success">
            <el-step title="基本信息" />
            <el-step title="资格证与职称" />
            <el-step title="证书材料" />
            <el-step title="确认提交" />
          </el-steps>

          <div v-show="currentStep === 0">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" />
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                  <el-radio :label="0">男</el-radio>
                  <el-radio :label="1">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="出生日期" prop="birthDate">
                <el-date-picker v-model="form.birthDate" type="date" placeholder="选择出生日期" />
              </el-form-item>
              <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="form.idCard" />
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" />
              </el-form-item>
              <el-form-item label="学历" prop="education">
                <el-input v-model="form.education" />
              </el-form-item>
              <el-form-item label="专业" prop="major">
                <el-input v-model="form.major" />
              </el-form-item>
            </el-form>
          </div>

          <div v-show="currentStep === 1">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
              <el-form-item label="职称" prop="title">
                <el-select v-model="form.title" placeholder="请选择职称">
                  <el-option label="无职称" value="无职称" />
                  <el-option label="中小学三级教师" value="中小学三级教师" />
                  <el-option label="中小学二级教师" value="中小学二级教师" />
                  <el-option label="中小学一级教师" value="中小学一级教师" />
                  <el-option label="中小学高级教师" value="中小学高级教师" />
                  <el-option label="中小学正高级教师" value="中小学正高级教师" />
                  <el-option label="助教" value="助教" />
                  <el-option label="讲师" value="讲师" />
                  <el-option label="副教授" value="副教授" />
                  <el-option label="教授" value="教授" />
                </el-select>
              </el-form-item>
              <el-form-item label="教师资格证学段" prop="teachCertificateLevel">
                <el-select v-model="form.teachCertificateLevel" placeholder="请选择学段">
                  <el-option label="初级" value="初级" />
                  <el-option label="中级" value="中级" />
                  <el-option label="高级" value="高级" />
                </el-select>
              </el-form-item>
              <el-form-item label="教师资格证科目" prop="teachCertificateSubject">
                <el-input v-model="form.teachCertificateSubject" placeholder="请输入科目" />
              </el-form-item>
            </el-form>
          </div>

          <div v-show="currentStep === 2">
            <div v-if="exam?.teacherType === 0">
              <h4>中小学教师需提交材料：</h4>
              <div v-for="category in primarySchoolCategories" :key="category.type" class="certificate-category">
                <h5>{{ category.name }}</h5>
                <div v-for="(item, idx) in getCategoryItems(category.type)" :key="item.id">
                  <el-form-item :label="`证书${idx + 1}`">
                    <el-input v-model="item.name" placeholder="证书名称" />
                    <el-select v-model="item.level" placeholder="级别" style="margin-left: 10px;">
                      <el-option label="省级" value="PROVINCIAL" />
                      <el-option label="市级" value="MUNICIPAL" />
                    </el-select>
                  </el-form-item>
                  <div class="file-upload-area">
                    <el-upload
                      :action="`/api/certificates/files/${item.id}`"
                      :headers="{ Authorization: `Bearer ${userStore.token}` }"
                      :file-list="getItemFiles(item.id)"
                      :on-success="(response: any) => handleFileUpload(item.id, response)"
                      :on-remove="(file: any) => handleFileRemove(file)"
                      :before-upload="beforeUpload"
                      multiple
                    >
                      <el-button size="small" type="primary">上传证书图片</el-button>
                    </el-upload>
                  </div>
                </div>
                <el-button size="small" @click="addCertificateItem(category.type)">+ 添加证书</el-button>
              </div>
            </div>

            <div v-if="exam?.teacherType === 1">
              <h4>大学教师需提交材料：三项市级荣誉或五项国家级荣誉</h4>
              <div v-for="category in universityCategories" :key="category.type" class="certificate-category">
                <h5>{{ category.name }}</h5>
                <div v-for="(item, idx) in getCategoryItems(category.type)" :key="item.id">
                  <el-form-item :label="`证书${idx + 1}`">
                    <el-input v-model="item.name" placeholder="证书名称" />
                    <el-select v-model="item.level" placeholder="级别" style="margin-left: 10px;">
                      <el-option label="市级" value="MUNICIPAL" />
                      <el-option label="国家级" value="NATIONAL" />
                    </el-select>
                  </el-form-item>
                  <div class="file-upload-area">
                    <el-upload
                      :action="`/api/certificates/files/${item.id}`"
                      :headers="{ Authorization: `Bearer ${userStore.token}` }"
                      :file-list="getItemFiles(item.id)"
                      :on-success="(response: any) => handleFileUpload(item.id, response)"
                      :on-remove="(file: any) => handleFileRemove(file)"
                      :before-upload="beforeUpload"
                      multiple
                    >
                      <el-button size="small" type="primary">上传证书图片</el-button>
                    </el-upload>
                  </div>
                </div>
                <el-button size="small" @click="addCertificateItem(category.type)">+ 添加证书</el-button>
              </div>
            </div>
          </div>

          <div v-show="currentStep === 3">
            <h4>报名信息确认</h4>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="姓名">{{ form.name }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ form.gender === 0 ? '男' : '女' }}</el-descriptions-item>
              <el-descriptions-item label="出生日期">{{ form.birthDate }}</el-descriptions-item>
              <el-descriptions-item label="身份证号">{{ form.idCard }}</el-descriptions-item>
              <el-descriptions-item label="职称">{{ form.title }}</el-descriptions-item>
              <el-descriptions-item label="教师资格证">{{ form.teachCertificateLevel }} - {{ form.teachCertificateSubject }}</el-descriptions-item>
            </el-descriptions>
            <el-alert type="warning" title="提交后将无法修改报名信息和补充材料，请确认信息无误" />
          </div>

          <div class="step-actions">
            <el-button v-if="currentStep > 0" @click="currentStep--">上一步</el-button>
            <el-button v-if="currentStep < 2" type="primary" @click="currentStep++">下一步</el-button>
            <el-button v-if="currentStep === 2" type="primary" @click="currentStep++">下一步（确认）</el-button>
            <el-button v-if="currentStep === 3" type="primary" @click="handleSubmit">提交报名</el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getExamByIdApi } from '@/api/exam'
import { createRegistrationApi, updateRegistrationApi, submitRegistrationApi, getRegistrationByUserIdAndExamIdApi } from '@/api/registration'
import { createCertificateItemApi, getCertificateItemsByRegistrationIdApi, getCertificateFilesByItemIdApi, deleteCertificateFileApi } from '@/api/certificate'
import { ElMessage, ElForm, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const exam = ref<any>(null)
const registration = ref<any>(null)
const currentStep = ref(0)
const formRef = ref<InstanceType<typeof ElForm>>()

const form = reactive({
  name: '',
  gender: 0,
  birthDate: '',
  idCard: '',
  phone: '',
  education: '',
  major: '',
  title: '',
  teachCertificateLevel: '',
  teachCertificateSubject: ''
})

const certificateItems = ref<any[]>([])
const certificateFiles = ref<Map<number, any[]>>(new Map())

const primarySchoolCategories = [
  { type: 'HONOR', name: '荣誉称号（省级或市级）' },
  { type: 'OPEN_CLASS', name: '公开课赛课证书' },
  { type: 'GRADUATION', name: '毕业年级证明' }
]

const universityCategories = [
  { type: 'HONOR', name: '荣誉称号（三项市级或五项国家级）' }
]

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  birthDate: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  title: [{ required: true, message: '请选择职称', trigger: 'change' }]
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getCategoryItems = (type: string) => {
  return certificateItems.value.filter(item => item.type === type)
}

const getItemFiles = (itemId: number) => {
  return certificateFiles.value.get(itemId) || []
}

const addCertificateItem = async (type: string) => {
  if (!registration.value) {
    await saveRegistration()
  }
  const item = {
    registrationId: registration.value.id,
    type,
    name: '',
    level: type === 'HONOR' ? 'MUNICIPAL' : ''
  }
  try {
    const response = await createCertificateItemApi(item)
    certificateItems.value.push(response.data)
    certificateFiles.value.set(response.data.id, [])
  } catch (error: any) {
    ElMessage.error(error)
  }
}

const handleFileUpload = (itemId: number, response: any) => {
  const files = certificateFiles.value.get(itemId) || []
  files.push({
    name: response.data.fileName,
    url: `/api/certificates/files/${response.data.id}/download`
  })
  certificateFiles.value.set(itemId, files)
}

const handleFileRemove = async (file: any) => {
  if (file.response?.data?.id) {
    try {
      await deleteCertificateFileApi(file.response.data.id)
    } catch (error: any) {
      ElMessage.error(error)
    }
  }
}

const beforeUpload = (file: File) => {
  const allowedTypes = ['image/jpeg', 'image/png', 'application/pdf']
  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('文件格式不支持，仅支持JPG/PNG/PDF格式')
    return false
  }
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('文件大小超过限制，单文件最大5MB')
    return false
  }
  return true
}

const saveRegistration = async () => {
  if (registration.value) {
    await updateRegistrationApi(registration.value.id, form)
  } else {
    const data = {
      ...form,
      userId: userStore.userId,
      examId: route.params.examId,
      teacherType: exam.value.teacherType
    }
    const response = await createRegistrationApi(data)
    registration.value = response.data
  }
}

const handleSubmit = () => {
  ElMessageBox.confirm(
    '提交后将无法修改报名信息和补充材料，请确认信息无误后再提交！',
    '确认提交',
    {
      confirmButtonText: '确定提交',
      cancelButtonText: '返回修改',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await saveRegistration()
      await submitRegistrationApi(registration.value.id)
      ElMessage.success('报名提交成功')
      router.push('/my-registrations')
    } catch (error: any) {
      ElMessage.error(error)
    }
  }).catch(() => {
    ElMessage.info('已取消提交')
  })
}

const loadData = async () => {
  try {
    const examResponse = await getExamByIdApi(Number(route.params.examId))
    exam.value = examResponse.data

    if (userStore.userId) {
      const regResponse = await getRegistrationByUserIdAndExamIdApi(userStore.userId!, Number(route.params.examId))
      if (regResponse.data && regResponse.data.length > 0) {
        registration.value = regResponse.data[0]
        Object.assign(form, registration.value)

        const certResponse = await getCertificateItemsByRegistrationIdApi(registration.value.id)
        certificateItems.value = certResponse.data
        for (const item of certificateItems.value) {
          const filesResponse = await getCertificateFilesByItemIdApi(item.id)
          certificateFiles.value.set(item.id, filesResponse.data.map((f: any) => ({
            name: f.fileName,
            url: `/api/certificates/files/${f.id}`
          })))
        }
      }
    }

    if (!form.name && userStore.name) form.name = userStore.name
    if (!form.idCard && userStore.idCard) form.idCard = userStore.idCard
    if (!form.phone && userStore.phone) form.phone = userStore.phone
  } catch (error: any) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.registration-page {
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

.main-content {
  padding: 20px;
}

.certificate-category {
  margin-bottom: 20px;
  padding: 15px;
  background: #f9fafc;
  border-radius: 8px;
}

.certificate-category h5 {
  margin-bottom: 15px;
  font-size: 14px;
  color: #303133;
}

.file-upload-area {
  margin-bottom: 15px;
}

.step-actions {
  margin-top: 30px;
  text-align: center;
}

.step-actions .el-button {
  width: 120px;
  margin: 0 10px;
}
</style>
