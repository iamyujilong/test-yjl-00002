
import axios from '@/utils/axios'

export const getExamListApi = async () => {
  return axios.get('/exams')
}

export const getOpenExamListApi = async () => {
  return axios.get('/exams/open')
}

export const getExamByIdApi = async (id: number) => {
  return axios.get(`/exams/${id}`)
}

export const createExamApi = async (data: any) => {
  return axios.post('/exams', data)
}

export const updateExamApi = async (id: number, data: any) => {
  return axios.put(`/exams/${id}`, data)
}

export const deleteExamApi = async (id: number) => {
  return axios.delete(`/exams/${id}`)
}
