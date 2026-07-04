
import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/exams',
    name: 'ExamList',
    component: () => import('@/views/ExamList.vue')
  },
  {
    path: '/registration/:examId',
    name: 'Registration',
    component: () => import('@/views/Registration.vue')
  },
  {
    path: '/my-registrations',
    name: 'MyRegistrations',
    component: () => import('@/views/MyRegistrations.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/Admin.vue'),
    children: [
      {
        path: 'exams',
        name: 'AdminExams',
        component: () => import('@/views/admin/ExamManagement.vue')
      },
      {
        path: 'registrations',
        name: 'AdminRegistrations',
        component: () => import('@/views/admin/RegistrationReview.vue')
      },
      {
        path: 'registration-detail/:id',
        name: 'RegistrationDetail',
        component: () => import('@/views/admin/RegistrationDetail.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.path.startsWith('/admin') && userStore.role !== 1) {
    next('/login')
    return
  }
  
  next()
})

export default router
