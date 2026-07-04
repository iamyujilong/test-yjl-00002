
import axios from '@/utils/axios'

export const createCertificateItemApi = async (data: any) => {
  return axios.post('/certificates/items', data)
}

export const updateCertificateItemApi = async (id: number, data: any) => {
  return axios.put(`/certificates/items/${id}`, data)
}

export const deleteCertificateItemApi = async (id: number) => {
  return axios.delete(`/certificates/items/${id}`)
}

export const getCertificateItemsByRegistrationIdApi = async (registrationId: number) => {
  return axios.get(`/certificates/items/registration/${registrationId}`)
}

export const uploadCertificateFileApi = async (itemId: number, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return axios.post(`/certificates/files/${itemId}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export const getCertificateFilesByItemIdApi = async (itemId: number) => {
  return axios.get(`/certificates/files/item/${itemId}`)
}

export const deleteCertificateFileApi = async (id: number) => {
  return axios.delete(`/certificates/files/${id}`)
}
