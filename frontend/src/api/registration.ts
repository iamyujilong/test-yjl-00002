
import axios from '@/utils/axios'

export const createRegistrationApi = async (data: any) => {
  return axios.post('/registrations', data)
}

export const updateRegistrationApi = async (id: number, data: any) => {
  return axios.put(`/registrations/${id}`, data)
}

export const submitRegistrationApi = async (id: number) => {
  return axios.post(`/registrations/${id}/submit`)
}

export const getRegistrationByIdApi = async (id: number) => {
  return axios.get(`/registrations/${id}`)
}

export const getRegistrationByUserIdApi = async (userId: number) => {
  return axios.get(`/registrations/user/${userId}`)
}

export const getRegistrationByExamIdApi = async (examId: number) => {
  return axios.get(`/registrations/exam/${examId}`)
}

export const getRegistrationPageApi = async (params: any) => {
  return axios.get('/registrations/page', { params })
}

export const getRegistrationByUserIdAndExamIdApi = async (userId: number, examId: number) => {
  return axios.get(`/registrations/user/${userId}/exam/${examId}`)
}
