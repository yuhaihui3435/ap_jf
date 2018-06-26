<template>
<v-container>
  <v-card>
    <v-form v-model="valid">
         <v-tabs color="blue"  icons-and-text dark >
             <v-tab   href="#tab-1">
                基础设置
                <v-icon>build</v-icon>
             </v-tab>
             <v-tab   href="#tab-2">
                图片储存服务设置
                <v-icon>image</v-icon>
             </v-tab>
             <v-tab   href="#tab-3">
                短信服务设置
                <v-icon>sms</v-icon>
             </v-tab>
            <v-tab-item id="tab-1">
              <v-container fluid>
                <v-flex md4 sm6 xs8>
                  登录策略
                  <v-radio-group v-model="param.userOnlineTactics" :mandatory="'loginFirst'" >
                    <v-radio label="先登录为主" value="loginFirst"></v-radio>
                    <v-radio label="后登录为主" value="afterLoggingIn"></v-radio>
                  </v-radio-group>
                </v-flex>
              </v-container>
            </v-tab-item>
             <v-tab-item id="tab-2">
               <v-container fluid>
                  <v-flex md4 sm6 xs8 >
                    
                    <v-text-field v-model="param.AK"  label="AK"  ></v-text-field>
                    <v-text-field v-model="param.SK" label="SK"  ></v-text-field>
                    <v-text-field v-model="param.qn_bucket" label="存储空间名"  ></v-text-field>
                    <v-text-field v-model="param.qn_url" label="图片服务器地址"  ></v-text-field>
                    
                  </v-flex>
               </v-container>
             </v-tab-item>
             <v-tab-item id="tab-3">
              <v-container fluid>
                  <v-flex md4 sm6 xs8 >
                    <v-text-field v-model="param.smsAppKey"  label="短信 AK"  ></v-text-field>
                    <v-text-field v-model="param.smsAppSecret" label="短信 SK"  ></v-text-field>
                  </v-flex>
               </v-container>
             </v-tab-item>
            
         </v-tabs>
         <v-btn color="primary"  @click="save" :loading="loading" :disabled="loading" >保存</v-btn>
         <!-- <v-btn color="primary">刷新缓存</v-btn> -->
    </v-form>
  </v-card>
</v-container> 
</template>
<script>
import { mapState } from "vuex";
import Kit from '../../../libs/kit.js'
export default {
  data() {
    return {
      valid:true,
      loading:false,

    };
  },
  computed:{
    ...mapState({
          param:state=>state.param.param,
        }),
  },
  mounted() {
    this.$store.dispatch('query_param_json');
  },
  methods:{
    save(){
      this.loading=true;
      this.$store.dispatch('save_param').then(()=>{
        this.loading=false;
      }).catch(res=>{
        this.loading=false;
      });
    }
  }
};
</script>
<style>
</style>
