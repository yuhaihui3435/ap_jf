<template>
  <v-container fluid>
      <v-layout row>
          <v-flex xs12 sm4 md4>
              <v-toolbar color="blue" >
                      <v-toolbar-title class="white--text">用户信息表列表</v-toolbar-title>
              </v-toolbar>
              <v-card >
                  <v-card-text>
                    <v-divider></v-divider>
                      <v-list dense>
                        <v-list-tile>
                          <v-list-tile-content>上一次登录时间:</v-list-tile-content>
                          <v-list-tile-content class="align-end">{{ lastLoginTime }}</v-list-tile-content>
                        </v-list-tile>
                        <v-list-tile>
                          <v-list-tile-content>上一次登录位置:</v-list-tile-content>
                          <v-list-tile-content class="align-end">{{ lastLoginIp }}</v-list-tile-content>
                        </v-list-tile>
                      </v-list>
                  </v-card-text>
              </v-card>
          </v-flex>
      </v-layout>
  </v-container>
</template>
<script>
import { mapState } from "vuex";
import Kit from "../libs/kit.js";
export default {
  data() {
    return {
      
    };
  },

  mounted() {
    
  },
  computed: {
    ...mapState({
      lastLoginTime: state => state.lastLoginTime,
      lastLoginIp: state => state.lastLoginIp,
    })
  },

  created() {
    
  }
  ,components:{
    
  }
};
</script>
<style>
</style>
