// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import {
  router
} from './router'
import store from './store';
import http from './libs/http';
import {
  Vuetify,
  VApp,
  VNavigationDrawer,
  VFooter,
  VList,
  VBtn,
  VIcon,
  VGrid,
  VToolbar,
  transitions,
  VMenu,
  VAvatar,
  VBreadcrumbs,
  VTabs,
  VForm,
  VTextField,
  VCard,
  VSnackbar,
  VProgressLinear,
  VDataTable,
  VAlert,
  VDialog,
  VSwitch,
  VSelect,
  VTooltip,
  VDatePicker,
  Vpagination,
  VDivider,
  VCheckbox,VRadioGroup
} from 'vuetify'
import APDialog from './components/ap-confrim-dialog/index'
import '../node_modules/vuetify/src/stylus/app.styl'

Vue.use(Vuetify, {
  components: {
    VApp,
    VNavigationDrawer,
    VFooter,
    VList,
    VBtn,
    VIcon,
    VGrid,
    VToolbar,
    transitions,
    VMenu,
    VAvatar,
    VBreadcrumbs,
    VTabs,
    VForm,
    VTextField,
    VCard,
    VSnackbar,
    VProgressLinear,
    VDataTable,
    VAlert,
    VDialog,
    VSwitch,
    VSelect,
    VTooltip,
    VDatePicker,
    Vpagination,
    VDivider,
    VCheckbox,VRadioGroup
  },
  theme: {
    primary: "#2196F3",
    secondary: "#64B5F6",
    accent: "#FF3D00",
    error: "#D50000",
    warning: "#ffeb3b",
    info: "#303F9F",
    success: "#4CAF50"
  }
})

Vue.use(APDialog)
Vue.config.productionTip = false
Vue.prototype.$http = http;

router.beforeEach((to, from, next) => {

  if(to.path=='/home'){
    localStorage.removeItem('currMenuState');
    store.commit('setCurrMenuState',{});
  }

  let nickname = localStorage.getItem('nickname');
  if(nickname&&to.path=='/login'){
    next({path:'/'});
  }
  else if (!nickname&&to.path!='/login') {
    next({path:'/login'});
  } else {
    // store.commit('addTab', to)
    //处理每个页面上面的面包屑 的数据
    store.commit('setBreadcrumbsList', to)
    //面包屑数据处理结束
    next()
  }

});

router.afterEach((to) => {

});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store: store,
  render: h => h(App),
  data: {
    currentPageName: ''
  },
  mounted() {
    this.currentPageName = this.$route.name;
    
  },
  created() {

  }
})
