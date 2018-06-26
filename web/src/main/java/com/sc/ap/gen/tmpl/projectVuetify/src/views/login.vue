<template>
      <v-container fluid fill-height >
        <v-layout  align-center justify-center >
          <v-flex xs12 sm8 md4 >
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-btn icon><v-icon>face</v-icon></v-btn>
                <v-toolbar-title>系统登录</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form v-model="fValid" ref="form" lazy-validation>
                  <v-text-field prepend-icon="person" v-model="form.loginname" :rules="[ rules.required,]" label="用户名" type="text"></v-text-field>
                  
                  <v-text-field id="password" prepend-icon="lock" :rules="[ rules.required,]" v-model="form.password" label="密码" type="password" ></v-text-field>
                  
                  <v-layout row >
                  <v-flex xs12 md6 lg8>
                  <v-text-field id="checkCode" prepend-icon="text_format" :rules="[ rules.required,]" v-model="form.checkCode" label="验证码" type="text"></v-text-field>
                  </v-flex>
                  <v-flex xs12 md6 lg4>
                  <img :src="getCaptchUrl" @click="createCaptchUrl"  class="mt-2 ml-2">
                  </v-flex>
                  </v-layout>
                  <v-switch v-model="form.rememberMe" label="记住我" color="blue darken-3" value="0" hide-details ></v-switch>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" :loading="loading||!fValid" @click="login">登录<v-icon right dark>fas fa-sign-in-alt</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    
</template>
<script>
import Kit from "../libs/kit.js";
export default {
  data() {
    return {
      loading:false,
      fValid: true,
      form: {
        rememberMe: "",
        loginname: "",
        password: "",
        checkCode: ""
      },
      getCaptchUrl: "",
      rules: Kit.inputRules
    };
  },
  mounted() {
    this.createCaptchUrl();
  },
  methods: {
    createCaptchUrl() {
      let now = new Date().getTime();
      let url = Kit.env + "/login/createCaptch";
      this.getCaptchUrl = url + "?tt=" + now;
    },
    login() {
      let vm = this;
      if (this.$refs.form.validate()) {
        vm.loading=true;
        vm.$http.post("/login/login", vm.form).then(res => {
            vm.loading=false;
            localStorage.removeItem('currMenuState');
          if (res.resCode&&res.resCode == "success") {
           
            let resData=JSON.parse(res.resData);
            vm.$store.commit('setNickname',resData.nickname);
            localStorage.setItem("nickname", resData.nickname);
            vm.$store.commit('setLastLogin',{lastLoginTime:resData.lastLoginTime,lastLoginIp:resData.lastLoginIp});
            localStorage.setItem('lastLogin',JSON.stringify({lastLoginTime:resData.lastLoginTime,lastLoginIp:resData.lastLoginIp}));
            localStorage.setItem('avatar',resData.avatar)
            vm.$store.commit('setAvatar',resData.avatar)

            if (resData.resList != undefined)
              localStorage.setItem("resList", JSON.stringify(resData.resList));
            if (resData.serList != undefined)
              localStorage.setItem("serList", JSON.stringify(resData.serList));

            // 权限菜单过滤相关
            this.$store.commit('updateMenulist');
            vm.$router.push({
              path: "/"
            });
          }else if(res.resCode&&res.resCode=='fail'){
              vm.createCaptchUrl();
          }
        });
      }
    }
  }
};
</script>
<style>
</style>
