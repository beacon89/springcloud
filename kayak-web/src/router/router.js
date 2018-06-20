import Main from '@/views/Main.vue';

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
    path: '/login',
    name: 'login',
    meta: {
        title: '开科唯识.金融开放平台 - 登录'
    },
    component: () => import('@/views/login.vue')
};

export const page404 = {
    path: '/*',
    name: 'error-404',
    meta: {
        title: '404-页面不存在'
    },
    component: () => import('@/views/error-page/404.vue')
};

export const page403 = {
    path: '/403',
    meta: {
        title: '403-权限不足'
    },
    name: 'error-403',
    component: () => import('@/views/error-page/403.vue')
};

export const page500 = {
    path: '/500',
    meta: {
        title: '500-服务端错误'
    },
    name: 'error-500',
    component: () => import('@/views/error-page/500.vue')
};

export const locking = {
    path: '/locking',
    name: 'locking',
    component: () => import('@/views/main-components/lockscreen/components/locking-page.vue')
};

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    redirect: '/home',
    component: Main,
    children: [
        { path: 'home', title: {i18n: 'home'}, name: 'home_index', component: () => import('@/views/home/home.vue') },
        { path: 'ownspace', title: '个人中心', name: 'ownspace_index', component: () => import('@/views/own-space/own-space.vue') },
        { path: 'message', title: '消息中心', name: 'message_index', component: () => import('@/views/message/message.vue') }
    ]
};

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
    {
        path: '/sys-setting',
        icon: 'ios-cog-outline',
        title: '系统设置',
        name: 'sys-setting',
        component: Main,
        children: [
            { path: 'sys-params', title: '系统参数', name: 'sys-params', component: () => import('@/views/sys-setting/sys-params/sys-params.vue') },
            { path: 'sys-dict', title: '数字字典', name: 'sys-dict', component: () => import('@/views/sys-setting/sys-dict/sys-dict.vue') },
            { path: 'sys-role', title: '角色管理', name: 'sys-role', component: () => import('@/views/sys-setting/sys-role/sys-role.vue') },
            { path: 'sys-user', title: '用户管理', name: 'sys-user', component: () => import('@/views/sys-setting/sys-user/sys-user.vue') },
        ]
    },
    {
        path: '/domain-setting',
        icon: 'levels',
        title: '域名管理',
        name: 'domain-setting',
        component: Main,
        children: [
            { path: 'domain-record',icon:'levels',title: '域名管理', name: 'domain-record', component: () => import('@/views/domainrecord/domainrecord.vue') },
            { path: 'k8s-record',icon:'social-dribbble-outline',title: '控制管理', name: 'k8s-record', component: () => import('@/views/k8s/k8sreplicationcontroller.vue') },
        ]
    }
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    loginRouter,
    otherRouter,
    locking,
    ...appRouter,
    page500,
    page403,
    page404
];


