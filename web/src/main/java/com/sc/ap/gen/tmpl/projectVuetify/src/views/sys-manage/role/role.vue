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
                              <v-text-field v-model="vo.code"  label="编号" required :disabled="opt=='edit'"
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'" >
                              <v-text-field v-model="vo.name"  label="角色名" required
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
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
                                    <v-list-tile-content>角色名:</v-list-tile-content><v-list-tile-content class="align-end">{{roleView.name}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>编号:</v-list-tile-content><v-list-tile-content class="align-end">{{roleView.code}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>创建时间:</v-list-tile-content><v-list-tile-content class="align-end">{{roleView.cAt | formatDate}}</v-list-tile-content>
                             </v-list-tile>
                  </v-list>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="viewDialog = false">关闭</v-btn>
          </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="setResDialog" persistent max-width="500px"  scrollable>
        <v-card >
          <v-card-title>
            <span class="headline">设置菜单</span>
          </v-card-title>
            <v-divider></v-divider>
           <v-card-text  style="height:500px;overflow-y:true;">
            <treeselect ref="resTree" v-model="vo.ownReses"  :limit="3" :limitText="(count)=>'还有'+count+'项'"  valueFormat="object" :disabled="treeData.length==0" :noChildrenText="'没有子菜单'"   :noOptionsText="'没有数据'"  :placeholder="'请选择...'" :flat="false" :multiple="true"  :options="treeData" />    
          </v-card-text>      
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="setResDialog = false">关闭</v-btn>
            <v-btn color="success darken-1" flat @click.native="saveRoleReses" :loading="loading" :disabled="loading">保存</v-btn>
          </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="setSerDialog" persistent max-width="500px" scrollable>
        <v-card >
          <v-card-title>
            <span class="headline">设置服务</span>
          </v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height:500px;overflow-y:true;">
            <treeselect ref="serTree" v-model="vo.ownSers" :flat="false" :limit="3" :limitText="(count)=>'还有'+count+'项'" valueFormat="object"  :disabled="treeData.length==0" :noChildrenText="'没有子服务'"   :noOptionsText="'没有数据'"  :placeholder="'请选择...'"  :multiple="true"  :options="treeData"/> 
            </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="setSerDialog = false">关闭</v-btn>
            <v-btn color="success darken-1" flat @click.native="saveRoleSers" :loading="loading" :disabled="loading">保存</v-btn>
          </v-card-actions>
        </v-card>
    </v-dialog>
          <v-toolbar color="blue" >
          <v-toolbar-title  class="white--text">角色列表</v-toolbar-title>
          <v-divider class="mx-3" inset vertical dark  ></v-divider>
          <v-spacer></v-spacer>
          <v-btn  @click.native="add()" class="blue--text"   >新增<v-icon>add</v-icon></v-btn>
        </v-toolbar>
          
          <v-card >
              <v-container grid-list-sm>
                      <v-layout row wrap>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="roleQuery.code"  label="编号" single-line hide-details ></v-text-field>
                         </v-flex>
                         <v-flex xs12 sm3 md3>
                            <v-text-field v-model="roleQuery.name"  label="角色名" single-line hide-details ></v-text-field>
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
              
            <v-data-table :headers="roleHeaders" :total-items="totalRow" :items="roleList" :rows-per-page-items="rowsPerPageItems" :pagination.sync="roleQuery"  class="elevation-1" no-data-text="数据为空" no-results-text="没有筛选到正确的数据">
              <template slot="items" slot-scope="props">
                    <td>
                               {{props.item.name}}
                    </td>
                    <td>
                               {{props.item.code}}
                    </td>
                    <td>
                               {{props.item.cAt | formatDate}}
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
                  <v-btn slot="activator" icon class="mx-0" @click="setRes(props.item)">
                      <v-icon color="teal">poll</v-icon>
                  </v-btn>
                  <span>设置菜单</span>
                  </v-tooltip>
                  <v-tooltip bottom>
                  <v-btn slot="activator" icon class="mx-0" @click="setSer(props.item)">
                      <v-icon color="teal">http</v-icon>
                  </v-btn>
                  <span>设置服务</span>
                  </v-tooltip>
                </td>
              </template>
          </v-data-table>
          <!-- <div class="text-xs-center pt-2" v-if="totalPage>0">
                <v-pagination v-model="roleQuery.pn" :length="totalPage"></v-pagination>
          </div> -->
          </v-card>
</v-container>
</template>
<script>
import { mapState } from "vuex";
import Kit from "../../../libs/kit.js";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
var moment = require("moment");
export default {
  components: { Treeselect },
  data() {
    return {
      fValid: true,
      valid: true,
      vo:{},
      rowsPerPageItems:[15],
      roleQuery: { pn: 1, sortBy: "", descending: "" }, //列表查询参数数据对象
      roleView: {}, //查询详细数据对象
      loading: false,
      setResDialog:false,
      setSerDialog:false,
      title: "新增角色表",
      rules: Kit.inputRules,
      treeData: [],
      roleHeaders: [
        {
          text: "角色名",
          sortable: true,
          value: "name"
        },
        {
          text: "编号",
          sortable: true,
          value: "code"
        },
        {
          text: "创建时间",
          sortable: false,
          value: "cAt"
        },
        { text: "操作", sortable: false }
      ],
      dialog: false,
      viewDialog: false,
      opt: ""
    };
  },
  computed: {
    ...mapState({
      role: state => state.role.role,
      roleList: state => state.role.roleList,
      totalRow: state => state.role.totalRow,
      pageNumber: state => state.role.pageNumber,
      pageSize: state => state.role.pageSize,
      totalPage: state => state.role.totalPage,
    })
  },
  mounted() {
    this.search();
  },
  methods: {
    setRes(role){
      let vm=this;
      vm.loading=false;
      vm.treeData=[]
      vm.vo={};
      this.$store.dispatch('get_role',{id:role.id}).then(res=>{
        this.$store.dispatch('get_res_tree_json',{pId:0}).then(listRes=>{
          listRes.forEach(function (value, key, map) {
            vm.treeData.push(value)
          })

          vm.vo=Object.assign({},res)
        })
      })
      vm.setResDialog=true;
    },
    saveRoleReses(){
      let vm=this;
      vm.loading=true;
      let reses=this.vo.ownReses;
      let resCodes=[];
      reses.forEach(function(curr,index,array){
        resCodes.push(curr.code);
      })
      let param={roleCode:this.vo.code,reses:resCodes.join(',')}
      this.$store.dispatch('save_role_reses',param).then(res=>{
        vm.loading=false;
        if(res.resCode=='success'){
          vm.setResDialog=false;
        }
      })
    },
    setSer(role){
      let vm=this;
      vm.loading=false;
      vm.treeData=[]
      vm.vo={};
      this.$store.dispatch('get_role',{id:role.id}).then(res=>{
        this.$store.dispatch('get_ser_tree_json',{pId:0}).then(listSer=>{
          listSer.forEach(function (value, key, map) {
            vm.treeData.push(value)
          })
          vm.vo=Object.assign({},res)
        })
      })
      vm.setSerDialog=true;
    },
    saveRoleSers(){
      let vm=this;
      vm.loading=true;
      let sers=this.vo.ownSers;
      let serCodes=[];
      sers.forEach(function(curr,index,array){
        serCodes.push(curr.code);
      })
      let param={roleCode:this.vo.code,sers:serCodes.join(',')}
      this.$store.dispatch('save_role_sers',param).then(res=>{
        vm.loading=false;
        if(res.resCode=='success'){
          vm.setSerDialog=false;
        }
      })
      
    },
    search() {
      this.roleQuery['pn']=this.roleQuery.page;
      this.$store.dispatch("page_role", this.roleQuery);
    },
    add() {
      this.loading = false;
      this.$refs.form.reset();
      this.opt = "add";
      this.title = "新增角色表";
      this.dialog = true;
    },
    edit(role) {
      this.loading = false;
      this.opt = "edit";
      this.vo=Object.assign({},role)
      this.dialog = true;
      this.title = "修改角色表";
    },
    save() {
      let vm = this;
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("save_role",this.vo)
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
    update(role) {
      let vm = this;
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("update_role",this.vo)
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
    del(role) {
      let vm = this;
      this.$APDialog.confirm(function(ret) {
        if (ret) {
          vm.$store.dispatch("del_role", { ids: role.id }).then(res => {
            if (res.resCode == "success") {
              vm.search();
            }
          });
        } else {
        }
      });
    },
    view(role) {
      let vm = this;
      vm.viewDialog = true;
      vm.$store.dispatch("get_role", { id: role.id }).then(res => {
        vm.roleView = res;
      });
    },
    clearQueryParam() {
      this.roleQuery["name"] = "";
      this.roleQuery["code"] = "";
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
    roleQuery: {
      handler(val,oldVal) {
        
        if ((this.roleQuery.sortBy != "")||(val.page!=oldVal.page)) {
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