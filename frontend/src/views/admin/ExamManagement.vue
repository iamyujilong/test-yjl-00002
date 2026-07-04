
<template>
  <div class="exam-management-page">
    <el-card>
      <div class="card-header">
        <h3>考试管理</h3>
        <el-button type="primary" @click="showAddDialog = true">新增考试</el-button>
      </div>
      <el-table :data="exams" stripe>
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
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showAddDialog" title="新增考试" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="考试名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="教师类型" prop="teacherType">
          <el-select v-model="form.teacherType">
            <el-option label="中小学" :value="0" />
            <el-option label="大学" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="报名开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" />
        </el-form-item>
        <el-form-item label="报名结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElForm, ElMessageBox } from 'element-plus'
import { getExamListApi, createExamApi, updateExamApi, deleteExamApi } from '@/api/exam'

const exams = ref<any[]>([])
const showAddDialog = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const editingExam = ref<any>(null)

const form = reactive({
  name: '',
  description: '',
  teacherType: 0,
  startTime: '',
  endTime: ''
})

const rules = {
  name: [{ required: true, message: '请输入考试名称', trigger: 'blur' }],
  teacherType: [{ required: true, message: '请选择教师类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择报名开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择报名结束时间', trigger: 'change' }]
}

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

const handleEdit = (row: any) => {
  editingExam.value = row
  Object.assign(form, row)
  showAddDialog.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定删除该考试吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteExamApi(row.id)
      ElMessage.success('删除成功')
      loadExams()
    } catch (error: any) {
      ElMessage.error(error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (editingExam.value) {
        await updateExamApi(editingExam.value.id, form)
        ElMessage.success('更新成功')
      } else {
        await createExamApi(form)
        ElMessage.success('创建成功')
      }
      showAddDialog.value = false
      editingExam.value = null
      Object.assign(form, { name: '', description: '', teacherType: 0, startTime: '', endTime: '' })
      loadExams()
    } catch (error: any) {
      ElMessage.error(error)
    }
  })
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
.exam-management-page {
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
</style>
