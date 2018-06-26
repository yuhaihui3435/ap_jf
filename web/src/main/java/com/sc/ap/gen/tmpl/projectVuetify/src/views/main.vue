<template>
<div>
    <v-navigation-drawer persistent :mini-variant="miniVariant" :clipped="clipped" v-model="drawer" enable-resize-watcher fixed app>
      <v-list>
        <v-list-group v-model="item.path == currOpenedMenu" v-for="(item, i) in menuList" :key="i" :prepend-icon="item.icon" no-action>
          <v-list-tile slot="activator">
            <v-list-tile-content>
              <v-list-tile-title v-text="item.meta.title"></v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
          <v-list-tile v-for="subItem in item.children" :key="subItem.title" @click="menuClick(item.path,subItem.path)" v-model="subItem.path==currMenuItem">
            <v-list-tile-content>
              <v-list-tile-title>{{ subItem.meta.title }}</v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-icon>{{ subItem.icon }}</v-icon>
            </v-list-tile-action>
          </v-list-tile>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar app :clipped-left="clipped">
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title v-text="title"></v-toolbar-title>
      <v-spacer></v-spacer>
      <strong>{{nickname}}</strong>
      <v-menu offset-y>
        <v-btn slot="activator" large icon dark color="indigo">
          <v-avatar size="48px">
            <img :src="avatar">
          </v-avatar>
        </v-btn>
        <v-list>
          <v-list-tile v-for="(item, index) in mList" :key="index" @click="mListClick(index)">
            <v-list-tile-avatar>
              <v-icon :color="item.color">{{ item.icon }}</v-icon>
            </v-list-tile-avatar>
            <v-list-tile-title>{{ item.title }}</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-menu>
    </v-toolbar>
    <v-content>
        <!-- <v-progress-linear :indeterminate="loadingStatus" :active="loadingStatus"  style="margin: 5px 0;"></v-progress-linear> -->
    <v-dialog
      v-model="loadingStatus"
      hide-overlay
      persistent
      width="300"
    >
      <v-card
        color="primary"
        dark
      >
        <v-card-text>
          数据通信中......
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          ></v-progress-linear>
        </v-card-text>
      </v-card>
      </v-dialog>
        <v-breadcrumbs>
            <v-icon slot="divider">chevron_right</v-icon>
							<v-breadcrumbs-item v-for="(item , i) in breadcrumbsList" :key="i" :to="i==0?'/':''" :disabled="item.disabled" >
									{{ item.text }}
							</v-breadcrumbs-item>
        </v-breadcrumbs>

            <router-view ></router-view>

    </v-content>
    <v-footer :fixed="fixed" app>
      <span>&copy; 2017</span>
    </v-footer>
    </div>
</template>
<script>
import { mapState } from "vuex";
const packageConfig = require("../../package");

export default {
  data() {
    return {
      clipped: true,
      drawer: true,
      fixed: false,
      mList: [
        {
          title: "个人资料",
          icon: "person",
          color: "blue white--text"
        },
        {
          title: "退出系统",
          icon: "power_settings_new",
          color: "blue white--text"
        }
      ],
      miniVariant: false,
      title: packageConfig.description
    };
  },

  computed: {
    ...mapState({
      menuList: state => state.app.menuList,
      breadcrumbsList: state => state.breadcrumbsList,
      currOpenedMenu: state => state.currOpenedMenu,
      currMenuItem: state => state.currMenuItem,
      snackbar: state => state.snackbar,
      loadingStatus: state => state.loadingStatus,
      tabsData: state => state.tabsData,
      nickname:state=>state.nickname,
      avatar:state=>state.avatar,
    })
  },
  methods: {

    //右侧toolbar上的下来菜单的处理，
    mListClick(i) {
      let vm=this;
      if (i == 0) {
        this.$router.push("/pc");
      } else if (i == 1) {
        this.$store.dispatch("logout").then(res=>{
          if(res.resCode=='success'){
            vm.$router.push({path:'/login'})
          }
        });
      }
      this.$store.commit("setCurrMenuState", {});
      localStorage.removeItem('currMenuState');
    },
    //面包屑点击处理
    breadcrumbsClick(i) {
      if (i == 0) {
        this.$router.push("/");
      }
    },
    //左侧菜单点击处理，
    menuClick(pPath, path) {
      let data = { currOpenedMenu: pPath, currMenuItem: path };
      this.$store.commit("setCurrMenuState", data);
      localStorage.setItem("currMenuState", JSON.stringify(data));
      this.$router.push(pPath + "/" + path);
    }
  },
  mounted() {
    // 权限菜单过滤相关
    this.$store.commit('updateMenulist');
    this.$store.commit('setNickname',localStorage.getItem('nickname'))
    let avatar=localStorage.getItem('avatar');
    if(!avatar)
      avatar="../../static/none.png";
    this.$store.commit('setAvatar',avatar)
    //恢复菜单前一次的展开状态
    let currMenuState = localStorage.getItem("currMenuState");
    if (currMenuState != null) {
      this.$store.commit("setCurrMenuState", JSON.parse(currMenuState));
    }
    let lastLogin=localStorage.getItem("lastLogin");
    if(lastLogin!=null){
      this.$store.commit('setLastLogin',JSON.parse(lastLogin));
    }
  },
  name: "Main"
};
</script>
<style>
</style>
