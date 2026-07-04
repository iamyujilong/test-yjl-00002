
import axios from '@/utils/axios'

export const loginApi = async (idCardOrPhone: string, password: string) => {
  return axios.post('/auth/login', { idCardOrPhone, password })
}

export const registerApi = async (idCard: string, name: string, phone: string, password: string) => {
  return axios.post('/auth/register', { idCard, name, phone, password })
}

export const getCurrentUserApi = async (token: string) => {
  return axios.get('/auth/me', {
    headers: { Authorization: `Bearer ${token}` }
  })
}
