<template>
<v-container>
  <v-dialog v-model="dialog" persistent max-width="500px">
      <v-card >
        <v-card-title>
          <span class="headline">{{title}}</span>
        </v-card-title>
        <v-card-text>
            <v-form v-model="fValid" ref="form" lazy-validation>
                <v-container grid-list-md>
                  <v-layout wrap>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.loginname"  label="登录账号" 
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  (v) => !!v||(v!=undefined&&v.length >= 6)|| '最少 6 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.nickname"  label="昵称" 
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.phone"  label="电话号" 
                                  :rules="[
                                  rules.required,
                                  rules.phone,
                                  (v) => !!v||(v!=undefined&&v.length <= 20) || '最多 20 字符',
                                  ]"
                                  :counter="20">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.email"  label="邮件" 
                                  :rules="[
                                  rules.email,
                                  (v)=>!!!v||(v!=undefined&&v.length <= 100) || '最多 100 字符',
                                  (v)=>!!!v||(v!=undefined&&v.length >= 6)|| '最少 6 字符',
                                  ]"
                                  :counter="100">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='edit'||opt=='add'">
                                <v-select :items="userSelectData" v-model="vo.status" required label="状态" :rules="[rules.required]" item-value="value"  item-text="text"></v-select>
                           </v-flex>
                  </v-layout>
                </v-container>
            </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error darken-1" flat @click.native="dialog = false">关闭</v-btn>
          <v-btn color="success darken-1" flat @click.native="save" :loading="loading" v-show="opt=='add'" :disabled="loading||!fValid">保存</v-btn>
          <v-btn color="success darken-1" flat @click.native="update" v-show="opt=='edit'" :loading="loading" :disabled="loading||!fValid">保存</v-btn>
        </v-card-actions>
      </v-card>
  </v-dialog>
  <v-dialog v-model="viewDialog" persistent max-width="300px">
        <v-card >
          <v-card-title>
            <span class="headline">查看详细</span>
          </v-card-title>
            <v-divider></v-divider>
                  <v-list dense>
                             <v-list-tile>
                                    <v-list-tile-content>头像:</v-list-tile-content><v-list-tile-content class="align-end"><v-avatar size="38px"><img :src="userView.avatar?userView.avatar:'../../../../static/none.png'"></v-avatar></v-list-tile-content>
                              </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>登录账号:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.loginname}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>昵称:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.nickname}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>电话号:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.phone}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>邮件:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.email}}</v-list-tile-content>
                             </v-list-tile>
                              
                              <v-list-tile>
                                    <v-list-tile-content>状态:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.statusStr}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>创建时间:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.cAt | formatDate}}</v-list-tile-content>
                             </v-list-tile>
                             <v-list-tile v-if="userView.userRoleNames!=''">
                                    <v-list-tile-content>角色:</v-list-tile-content><v-list-tile-content class="align-end">{{userView.userRoleNames!=undefined?userView.userRoleNames.join(','):'未设置'}}</v-list-tile-content>
                             </v-list-tile>
                  </v-list>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="viewDialog = false">关闭</v-btn>
          </v-card-actions>
        </v-card>
  </v-dialog>
  <v-dialog v-model="setRoleDialog" persistent max-width="500px">
        <v-card >
          <v-card-title>
            <span class="headline">设置角色</span>
          </v-card-title>
            <v-divider></v-divider>
                  <v-card-text>
                    <v-container fluid>
                      <v-layout row wrap>
                        <v-flex xs12 sm3 md3 v-for="x in roleList" :key="x.id">
                            <v-checkbox v-model="selectUser.userRoleCodes" :label="x.name" color="info" :value="x.code" hide-details ></v-checkbox>
                        </v-flex>
                      </v-layout>
                    </v-container>
                  </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="setRoleDialog = false">关闭</v-btn>
            <v-btn color="success darken-1" flat @click.native="saveUserRoles" :loading="loading" :disabled="loading">保存</v-btn>
          </v-card-actions>
        </v-card>
  </v-dialog>
          <v-toolbar color="blue" >
          <v-toolbar-title class="white--text">用户信息表列表</v-toolbar-title>
          <v-divider class="mx-3" inset vertical dark  ></v-divider>
          <v-spacer></v-spacer>
          <v-btn  class="blue--text" @click.native="add()">新增<v-icon>add</v-icon></v-btn>
          </v-toolbar>
          <v-card >
              
              <v-container grid-list-md>
                      <v-layout row wrap>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="userQuery.loginname"  label="登录账号" single-line hide-details ></v-text-field>
                         </v-flex>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="userQuery.nickname"  label="昵称" single-line hide-details ></v-text-field>
                         </v-flex>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="userQuery.phone"  label="电话号" single-line hide-details ></v-text-field>
                         </v-flex>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="userQuery.email"  label="邮件" single-line hide-details ></v-text-field>
                         </v-flex>
                        <v-flex xs12 sm3 md3>
                            <v-select :items="userSelectData" v-model="userQuery.status" label="状态"  item-value="value" item-text="text"></v-select>
                        </v-flex>
                         <v-flex xs12 sm3 md3 >
                              <v-menu ref="cAtQueryBeginDateMenu" :close-on-content-click="false" v-model="cAtQueryBeginDateMenu" 
                                   :nudge-right="40" lazy transition="scale-transition" offset-y full-width max-width="290px" min-width="290px" >
                                   <v-text-field slot="activator" v-model="userQuery.beginCAt" label="创建时间查询开始日期" prepend-icon="event" readonly ></v-text-field>
                                   <v-date-picker v-model="userQuery.beginCAt" locale="zh-cn"  @input="$refs.cAtQueryBeginDateMenu.save(userQuery.beginCAt)"></v-date-picker>
                             </v-menu>
                         </v-flex>
                         <v-flex xs12 sm3 md3 >
                              <v-menu ref="cAtQueryEndDateMenu" :close-on-content-click="false" v-model="cAtQueryEndDateMenu" 
                                    :nudge-right="40" lazy transition="scale-transition" offset-y full-width max-width="290px" min-width="290px" >
                                    <v-text-field slot="activator" v-model="userQuery.endCAt" label="创建时间查询截至日期" prepend-icon="event" readonly ></v-text-field>
                                    <v-date-picker v-model="userQuery.endCAt" locale="zh-cn"  @input="$refs.cAtQueryEndDateMenu.save(userQuery.endCAt)"></v-date-picker>
                              </v-menu>
                         </v-flex>
                        <v-flex xs12 sm3 md3>
                             <v-btn color="primary" class="white--text" @click="search()">
                                 搜索<v-icon>search</v-icon>
                             </v-btn>
                             <v-btn color="primary" class="white--text" @click="clearQueryParam()">
                                 清空<v-icon>clear</v-icon>
                             </v-btn>
                        </v-flex>
                      </v-layout>
              </v-container>
              
            <v-data-table :headers="userHeaders" :total-items="totalRow" :items="userList" :rows-per-page-items="rowsPerPageItems" :pagination.sync="userQuery"  class="elevation-1" no-data-text="数据为空" no-results-text="没有筛选到正确的数据">
              <template slot="items" slot-scope="props">
                    <td>
                               {{props.item.loginname}}
                    </td>
                    <td>
                               {{props.item.nickname}}
                    </td>
                    <td>
                               {{props.item.phone}}
                    </td>
                    <td>
                               {{props.item.email}}
                    </td>
                    <td>
                               {{props.item.statusStr}}
                    </td>
                    <td>
                               {{props.item.cAt | formatDate}}
                    </td>
                    <td>
                               {{props.item.userRoleNames!=undefined?props.item.userRoleNames.join(','):'未设置'}}
                    </td>
                <td class=" layout px-0">
                  <v-btn icon class="mx-0" @click="edit(props.item)">
                    <v-icon color="teal">edit</v-icon>
                  </v-btn>
                  <v-btn icon class="mx-0" @click="del(props.item)">
                    <v-icon color="pink">delete</v-icon>
                  </v-btn>
                  <v-btn icon class="mx-0" @click="view(props.item)">
                      <v-icon color="teal">fas fa-eye</v-icon>
                  </v-btn>
                  <v-tooltip bottom>
                  <v-btn icon class="mx-0" @click="resetPwd(props.item)">
                      <v-icon color="teal">redo</v-icon>
                  </v-btn>
                  <span>重置密码</span>
                  </v-tooltip>
                  <v-tooltip bottom>
                  <v-btn   slot="activator" icon class="mx-0" @click="setRole(props.item)">
                      <v-icon color="teal">supervisor_account</v-icon>
                  </v-btn>
                  <span>设置角色</span>
                  </v-tooltip>
                </td>
              </template>
            </v-data-table>
          </v-card>
</v-container> 
</template>
<script>
import { mapState } from "vuex";
import Kit from "../../../libs/kit.js";
var moment = require("moment");
export default {
  data() {
    return {
      fValid: true,
      valid: true,
      vo:{},
      rowsPerPageItems: [15],
      userQuery: { pn: 1, sortBy: "", descending: "" }, //列表查询参数数据对象
      userView: {}, //查询详细数据对象
      selectUser:{},
      loading: false,
      title: "新增用户信息表",
      rules: Kit.inputRules,
      userHeaders: [
        {
          text: "登录账号",
          sortable: true,
          value: "loginname"
        },
        {
          text: "昵称",
          sortable: true,
          value: "nickname"
        },
        {
          text: "电话号",
          sortable: true,
          value: "phone"
        },
        {
          text: "邮件",
          sortable: false,
          value: "email"
        },
        {
          text: "状态",
          sortable: false,
          value: "status"
        },
        {
          text: "创建时间",
          sortable: true,
          value: "cAt"
        },
        {
          text: "角色",
          sortable: false,
          value: "userRoleNames"
        },
        { text: "操作", sortable: false }
      ],
      dialog: false,
      viewDialog: false,
      setRoleDialog:false,
      opt: "",
      userSelectData: [
        { text: "正常", value: "0" },
        { text: "禁用", value: "1" }
      ],
      cAtQueryBeginDateMenu: false,
      cAtQueryEndDateMenu: false
    };
  },
  computed: {
    ...mapState({
      user: state => state.user.user,
      userList: state => state.user.userList,
      totalRow: state => state.user.totalRow,
      pageNumber: state => state.user.pageNumber,
      pageSize: state => state.user.pageSize,
      totalPage: state => state.user.totalPage,
      roleList:state=>state.role.roleList
    })
  },
  mounted() {
    this.search();
  },
  methods: {
    setRole(user){
      let vm=this;
      this.$store.dispatch('get_user',{id:user.id}).then(res=>{
        vm.selectUser=Object.assign({},res);
        vm.$store.dispatch('list_role');
      })
      this.setRoleDialog=true;
    },
    saveUserRoles(){
      let data=this.selectUser.userRoleCodes;
      if(!!data&&data instanceof Array)data=data.join(',');
      let param={loginname:this.selectUser.loginname,userRoleCodes:data}
      let vm=this;
      vm.loading=true
      this.$store.dispatch('save_user_roles',param).then(res=>{
        vm.loading=false;
        if(res.resCode=='success'){
          vm.setRoleDialog=false;
          vm.search()
        }
      })
    },
    search() {
      this.userQuery['pn']=this.userQuery.page;
      this.$store.dispatch("page_user", this.userQuery).catch(res=>{});
    },
    add() {
      this.loading = false;
      this.$refs.form.reset();
      this.opt = "add";
      this.title = "新增用户信息表";
      this.dialog = true;
    },
    edit(user) {
      this.loading = false;
      // this.$refs.form.reset();
      this.opt = "edit";
      this.vo=Object.assign({},user)
      this.dialog = true;
      this.title = "修改用户信息表";
    },
    save() {
      let vm = this;
      
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("save_user",vm.vo)
          .then(res => {
            vm.loading = false;
            if (res.resCode == "success") {
              vm.dialog = false;
              vm.search();
            }
          })
          .catch((response) => {
            vm.loading = false;
            vm.dialog = false;
          });
      }
    },
    update(user) {
      let vm = this;
      
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("update_user",vm.vo)
          .then(res => {
            vm.loading = false;
            if (res.resCode == "success") {
              vm.dialog = false;
              vm.search();
            }
          })
          .catch(() => {
            vm.loading = false;
            vm.dialog = false;
          });
      }
    },
    del(user) {
      let vm = this;
      this.$APDialog.confirm(function(ret) {
        if (ret) {
          vm.$store.dispatch("del_user", { ids: user.id }).then(res => {
            if (res.resCode == "success") {
              vm.search();
            }
          });
        } else {
        }
      });
    },
    view(user) {
      let vm = this;
      vm.viewDialog = true;
      vm.$store.dispatch("get_user", { id: user.id }).then(res => {
        vm.userView = res;
      });
    },
    clearQueryParam() {
      this.userQuery["loginname"] = "";
      this.userQuery["nickname"] = "";
      this.userQuery["phone"] = "";
      this.userQuery["email"] = "";
      this.userQuery["status"] = "";
      this.userQuery["beginCAt"] = "";
      this.userQuery["endCAt"] = "";
    },
    resetPwd(item){

    }
  },
  filters: {
    formatDate(time) {
      if (!!!time) return "";
      var date = new Date(time);
      return moment(date).format("YYYY-MM-DD hh:mm:ss");
    }
  },
  watch: {
    userQuery: {
      handler(val, oldVal) {
        if (this.userQuery.sortBy != "" || val.page != oldVal.page) {
          this.search();
        }
      },
      deep: true
    }
  }
};
</script>
<style>
</style>