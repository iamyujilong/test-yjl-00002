
<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>用户登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="账号" prop="idCardOrPhone">
          <el-input v-model="form.idCardOrPhone" placeholder="请输入身份证号或手机号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        <p>管理员账号：admin / 密码：123456</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElForm } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()

const form = reactive({
  idCardOrPhone: '',
  password: ''
})

const rules = {
  idCardOrPhone: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      await userStore.login(form.idCardOrPhone, form.password)
      ElMessage.success('登录成功')
      if (userStore.isAdmin()) {
        router.push('/admin/exams')
      } else {
        router.push('/exams')
      }
    } catch (error: any) {
      ElMessage.error(error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #409EFF 0%, #667EEA 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
}

.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
}

.login-card .el-form-item {
  margin-bottom: 20px;
}

.login-card .el-form-item:last-child {
  text-align: center;
}

.login-card .el-button {
  width: 120px;
}

.tips {
  margin-top: 20px;
  text-align: center;
  color: #909399;
  font-size: 12px;
}
</style>
