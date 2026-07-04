
import axios from '@/utils/axios'

export const reviewRegistrationApi = async (registrationId: number, reviewerId: number, status: number, remark?: string) => {
  return axios.post(`/reviews/${registrationId}`, null, {
    params: { reviewerId, status, remark }
  })
}

export const getReviewLogsApi = async (registrationId: number) => {
  return axios.get(`/reviews/${registrationId}`)
}

export const getRegistrationDetailApi = async (registrationId: number) => {
  return axios.get(`/reviews/detail/${registrationId}`)
}
