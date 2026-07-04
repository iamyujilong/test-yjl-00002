
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { loginApi, registerApi, getCurrentUserApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref<number | null>(null)
  const name = ref('')
  const role = ref(0)
  const idCard = ref('')
  const phone = ref('')

  const login = async (idCardOrPhone: string, password: string) => {
    const response = await loginApi(idCardOrPhone, password)
    token.value = response.data.token
    localStorage.setItem('token', token.value)
    userId.value = response.data.user.id
    name.value = response.data.user.name
    role.value = response.data.user.role
    idCard.value = response.data.user.idCard
    phone.value = response.data.user.phone
    return response
  }

  const register = async (idCard: string, name: string, phone: string, password: string) => {
    const response = await registerApi(idCard, name, phone, password)
    token.value = response.data.token
    localStorage.setItem('token', token.value)
    userId.value = response.data.user.id
    name.value = response.data.user.name
    role.value = response.data.user.role
    idCard.value = response.data.user.idCard
    phone.value = response.data.user.phone
    return response
  }

  const fetchCurrentUser = async () => {
    if (!token.value) return
    try {
      const response = await getCurrentUserApi(token.value)
      userId.value = response.data.id
      name.value = response.data.name
      role.value = response.data.role
      idCard.value = response.data.idCard
      phone.value = response.data.phone
    } catch {
      logout()
    }
  }

  const logout = () => {
    token.value = ''
    userId.value = null
    name.value = ''
    role.value = 0
    idCard.value = ''
    phone.value = ''
    localStorage.removeItem('token')
  }

  const isLoggedIn = () => !!token.value

  const isAdmin = () => role.value === 1

  return {
    token,
    userId,
    name,
    role,
    idCard,
    phone,
    login,
    register,
    fetchCurrentUser,
    logout,
    isLoggedIn,
    isAdmin
  }
})
