<template>
    <v-container>
  <v-card>
    
         <v-tabs color="blue"  icons-and-text dark >
             <v-tab   href="#tab-2">
                个人资料
                <v-icon>person</v-icon>
             </v-tab>
             <v-tab   href="#tab-4">
                密码修改
                <v-icon>lock</v-icon>
             </v-tab>
            
             <v-tab-item id="tab-2">
               <v-container fluid>
                  <v-flex md4 sm6 xs8 >
                    <v-form v-model="fValid" ref="form" lazy-validation>
                      <v-text-field v-model="user.loginname"  label="登录用户名" disabled  ></v-text-field>
                      <v-text-field v-model="user.nickname"  label="昵称"  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50"></v-text-field>
                      <v-text-field v-model="user.phone" label="电话"  :rules="[
                                  rules.required,
                                  rules.phone,
                                  (v) => !!v||(v!=undefined&&v.length <= 20) || '最多 20 字符',
                                  ]"
                                  :counter="20"></v-text-field>
                      <v-text-field v-model="user.email" label="EMAIL"  :rules="[
                                  rules.email,
                                  (v)=>!!!v||(v!=undefined&&v.length <= 100) || '最多 100 字符',
                                  (v)=>!!!v||(v!=undefined&&v.length >= 6)|| '最少 6 字符',
                                  ]"
                                  :counter="100"></v-text-field>
                      <v-text-field v-model="user.statusStr" label="状态"  disabled></v-text-field>
                      <v-avatar size="68px">
                        <img :src="user.avatar=='undefiend'?imgUrl:user.avatar">
                      </v-avatar>
                      <vue-core-image-upload
                        class=""
                        :inputOfFile="'file'"
                        :crop="false"
                        :text="'上传头像'"
                        @imagechanged="imagechanged"
                        @imageuploaded="imageuploaded"
                        @errorhandle="errorhandle"
                        @imageuploading="imageuploading"
                        :max-file-size="imgMaxSize"
                        :url="uploadUrl" 
                        :isXhr="false"
                        >
                      </vue-core-image-upload>
                      
                      <v-btn color="primary"  @click="saveUserInfo" :loading="loading||uploadLoading" :disabled="loading" >保存</v-btn>
                    </v-form>
                  </v-flex>
               </v-container>
             </v-tab-item>
             <v-tab-item id="tab-4">
              <v-container fluid>
                  <v-flex md4 sm6 xs8 >
                    <v-form v-model="fValid" ref="form1" lazy-validation>
                    <v-text-field v-model="pwd.oldPassword"  label="旧密码"  :append-icon="oldPwd ? 'visibility_off' : 'visibility'" :type="oldPwd ? 'text' : 'password'" @click:append="oldPwd = !oldPwd" :rules="[
                                  rules.required,
                                  ]" >
                    </v-text-field>
                    <v-text-field :id="'newPwd'"  v-model="pwd.newPassword" label="新密码" :append-icon="newPwd ? 'visibility_off' : 'visibility'" :type="newPwd ? 'text' : 'password'" @click:append="newPwd = !newPwd"  :rules="[
                                  rules.required,
                                  (v)=>!!!v||(v!=undefined&&v.length <= 100) || '最多 100 字符',
                                  (v)=>!!!v||(v!=undefined&&v.length >= 6)|| '最少 6 字符',
                                  ]"
                                  :counter="100"></v-text-field>
                    <v-text-field :id="'reNewPwd'"  v-model="pwd.rePassword" label="新密码确认" :append-icon="reNewPwd ? 'visibility_off' : 'visibility'" :type="reNewPwd ? 'text' : 'password'" @click:append="reNewPwd = !reNewPwd"  :rules="[
                                  rules.required,
                                  (v)=>!!!v||(v!=undefined&&v.length <= 100) || '最多 100 字符',
                                  (v)=>!!!v||(v!=undefined&&v.length >= 6)|| '最少 6 字符',
                                  checkPwd
                                  ]"
                                  :counter="100"></v-text-field>
                    <v-btn color="primary"  @click="savePwd" :loading="loading" :disabled="loading" >保存</v-btn>
                    </v-form>
                  </v-flex>
               </v-container>
             </v-tab-item>
         </v-tabs>
    
  </v-card>
  
</v-container>

</template>
<script>
import VueCoreImageUpload from 'vue-core-image-upload'
import Kit from "../libs/kit.js";
export default {
  components: {
    'vue-core-image-upload': VueCoreImageUpload,
  },
  data() {
    return {
      oldPwd:false,
      newPwd:false,
      reNewPwd:false,
      fValid:true,
      uploadLoading:false,
      user: {},
      pwd:{},
      loading: false,
      rules: Kit.inputRules,
      checkPwd:function(val){
        
        let newPwd=document.getElementById('newPwd');
        let reNewPwd=document.getElementById('reNewPwd');
        
        if(newPwd==undefined&&reNewPwd==undefined)return '';
        newPwd=newPwd.value;
        reNewPwd=reNewPwd.value;
        return (newPwd===reNewPwd)||'两次输入的密码不一致'
      },
      env:Kit.env,
      imgUrl: '../../static/none.png',
      uploadUrl:Kit.env==''?'/cmn/act01':'/api/cmn/act01',
      imgMaxSize:52428
    };
  },
  computed: {},
  mounted() {
    this.$store
      .dispatch("get_curr_user")
      .then(res => {
        this.user = res;
        localStorage.setItem('avatar',res.avatar);
      })
      .catch(res => {
        this.loading = false;
      });
  },

  methods: {
    saveUserInfo() {
      let vm = this;
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("update_user",vm.user)
          .then(res => {
            vm.loading = false;
            if (res.resCode == "success") {
              vm.$store.commit('setAvatar',vm.user.avatar)
              localStorage.setItem('avatar',vm.user.avatar)
            }
          })
          .catch((response) => {
            vm.loading = false;
          });
      }
    },
    savePwd() {
      let vm = this;
      if (this.$refs.form1.validate()) {
        this.loading = true;
        this.$store
          .dispatch("update_pwd",vm.pwd)
          .then(res => {
            vm.loading = false;
            if (res.resCode == "success") {
              
            }
          })
          .catch((response) => {
            vm.loading = false;
          });
      }
    },
    imageuploaded(res) {
      this.uploadLoading=false;
     
    },
    errorhandle(res){
       this.uploadLoading=false;
        if(res.indexOf('FILE IS TOO LARGER MAX FILE IS')>-1){
          Kit.msg.err('上传图片大小不能超过'+parseInt(this.imgMaxSize/1024)+'k')
        }
    },
    imageuploading(){
      this.uploadLoading=true;
    },
    imagechanged(file){
      let vm=this;
      var reader = new FileReader(); 
      reader.readAsDataURL(file); 
      reader.onload = function(e){ 
        vm.imgUrl=this.result
        vm.user.avatar=this.result
      } 
    }
  }
};
</script>
<style>
</style>
