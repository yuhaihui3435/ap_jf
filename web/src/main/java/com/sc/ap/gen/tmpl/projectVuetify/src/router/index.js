import Vue from 'vue'
import Router from 'vue-router'
import App from '@/App.vue';
import Main from '@/views/main.vue';

Vue.use(Router);

export const loginRouter = {
  path: '/login',
  name: 'login',
  meta: {
    title: '登录'
  },
  component: resolve => {
    require(['@/views/login.vue'], resolve);
  }
};
export const otherRouter = {
  path: '/',
  name: 'otherRouter',
  redirect: '/home',
  component:Main,
  children: [
    {
      path: 'home',
      title: '控制中心',
      meta:{title:'控制中心'},
      name: 'home',
      component: resolve => {
        require(['@/views/home.vue'], resolve);
      }
    },
    {
      path: 'pc',
      title: '个人中心',
      meta:{title:'个人中心'},
      name: 'pc_index',
      component: resolve => {
        require(['@/views/personCenter.vue'], resolve);
      }
    },
    {
      path: 'genCfg',
      title: '代码生成配置',
      meta:{title:'代码生成配置'},
      icon: "fab fa-java",
      name: 'gen_cfg',
      component: resolve => {
        require(['@/views/sys-manage/gen/genCfg.vue'], resolve);
      }
    },

  ]
};
// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [{
  path: '/sm',
  icon: 'settings',
  name: 'admin',
  title: '系统管理',
  component:Main,
  meta:{title:'系统管理'},
  children: [
    {
      path: 'user',
      title: '用户管理',
      meta:{title:'用户管理',pTitle:'系统管理',icon: "person",},
      icon: "person",
      name: 'sm_user',
      component: resolve => {
        require(['@/views/sys-manage/user/user.vue'], resolve);
      }
    },
    {
      path: 'role',
      title: '角色管理',
      meta:{title:'角色管理',pTitle:'系统管理',icon: "group",},
      icon: "group",
      name: 'sm_role',
      component: resolve => {
        require(['@/views/sys-manage/role/role.vue'], resolve);
      }
    },
    {
      path: 'res',
      title: '菜单管理',
      meta:{title:'菜单管理',pTitle:'系统管理',icon: "poll",},
      icon: "poll",
      name: 'sm_res',
      component: resolve => {
        require(['@/views/sys-manage/res/res.vue'], resolve);
      }
    },
    {
      path: 'ser',
      title: '服务管理',
      meta:{title:'服务管理',pTitle:'系统管理',icon: "http",},
      icon: "http",
      name: 'sm_ser',
      component: resolve => {
        require(['@/views/sys-manage/ser/ser.vue'], resolve);
      }
    },
    {
      path: 'dd',
      title: '数据字典',
      meta:{title:'数据字典',pTitle:'系统管理',icon: "local_library",},
      icon: "local_library",
      name: 'sm_dd',
      component: resolve => {
        require(['@/views/sys-manage/dd/dd.vue'], resolve);
      }
    },
    {
      path: 'param',
      title: '参数管理',
      meta:{title:'参数管理',pTitle:'系统管理',icon: "mouse",},
      icon: "mouse",
      name: 'sm_param',
      component: resolve => {
        require(['@/views/sys-manage/param/param.vue'], resolve);
      }
    },
  ]
}
]

export const routers = [
  loginRouter,
  otherRouter,
  ...appRouter,
];

// 路由配置
const RouterConfig = {
  // mode: 'history',
  routes: routers
};

export const router = new Router(RouterConfig);

