<template>
<div>
  <v-dialog v-model="dialog" persistent max-width="500px">
      <v-card >
        <v-card-title>
          <span class="headline">{{title}}</span>
        </v-card-title>
        <v-card-text>
            <v-form v-model="fValid" ref="form" lazy-validation>
                <v-container grid-list-md>
                  <v-layout wrap>
                          <v-flex xs12 sm12 md12 v-show="opt=='add'||opt=='edit'" v-if="!!treeRoot">
                              <v-text-field v-model="treeRoot.name"  label="上级菜单" disabled>
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.code"  label="编号" required
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.name"  label="菜单名" required
                                  :rules="[
                                  rules.required,
                                  (v) => !!v||(v!=undefined&&v.length <= 100) || '最多 100 字符',
                                  ]"
                                  :counter="100">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.url"  label="菜单url" 
                                  :rules="[
                                  (v)=>!!!v||(v!=undefined&&v.length <= 255) || '最多 255 字符',
                                  ]"
                                  :counter="255">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.seq"  label="顺序" 
                                  :rules="[
                                  rules.digital,
                                  ]"
                                  >
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                              <v-text-field v-model="vo.icon"  label="图标" 
                                  :rules="[
                                  (v)=>!!!v||(v!=undefined&&v.length <= 50) || '最多 50 字符',
                                  ]"
                                  :counter="50">
                              </v-text-field>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                                <v-select :items="resSelectData" v-model="vo.logged" label="记录日志"  item-value="value" item-text="text"></v-select>
                           </v-flex>
                           <v-flex xs12 sm6 md4 v-show="opt=='add'||opt=='edit'">
                                <v-select :items="resSelectData" v-model="vo.enabled" :rules="[rules.required]" label="是否可用"  item-value="value" item-text="text"></v-select>
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
                                    <v-list-tile-content>编号:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.code}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>菜单名:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.name}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>菜单url:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.url}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>顺序:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.seq}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>图标:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.icon}}</v-list-tile-content>
                             </v-list-tile>
                              <v-list-tile>
                                    <v-list-tile-content>是否可用:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.enabledStr}}</v-list-tile-content>
                             </v-list-tile>
                             <v-list-tile>
                                    <v-list-tile-content>记录日志:</v-list-tile-content><v-list-tile-content class="align-end">{{resView.loggedStr}}</v-list-tile-content>
                             </v-list-tile>
                  </v-list>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error darken-1" flat @click.native="viewDialog = false">关闭</v-btn>
          </v-card-actions>
        </v-card>
        </v-dialog>
       <v-container fluid grid-list-md >
        <v-layout row wrap>
        <v-flex xs12 sm6 md4>
          <v-toolbar color="blue" >
          <v-toolbar-title  class="white--text">菜单树</v-toolbar-title>
          <v-divider class="mx-3" inset vertical dark  ></v-divider>
          <v-spacer></v-spacer>
        </v-toolbar>
          <v-card > 
            <treeselect ref="resTree" v-model="treeVal"   valueFormat="object" :noChildrenText="'没有子菜单'" :noOptionsText="'没有数据'"  :placeholder="'请选择...'"  :options="treeData" />
          </v-card>
        </v-flex>
        <v-flex xs12 sm6 md8>
          <v-toolbar color="blue" >
          <v-toolbar-title  class="white--text">菜单列表</v-toolbar-title>
          <v-divider class="mx-3" inset vertical dark  ></v-divider>
          <v-spacer></v-spacer>
          <v-btn  @click.native="add()" class="blue--text"   >新增子菜单<v-icon>add</v-icon></v-btn>
        </v-toolbar>
        
        <v-data-table :headers="resHeaders" :total-items="totalRow" :items="resList" :hide-actions="totalRow==0"   :rows-per-page-items="rowsPerPageItems" :pagination.sync="resQuery"  class="elevation-1" no-data-text="数据为空" no-results-text="没有筛选到正确的数据">
              <template slot="items" slot-scope="props">
                    <td>
                               {{props.item.code}}
                    </td>
                    <td>
                               {{props.item.name}}
                    </td>
                    <td>
                               {{props.item.url}}
                    </td>
                    <td>
                               {{props.item.seq}}
                    </td>
                    <td>
                               {{props.item.icon}}
                    </td>
                    <td>
                               {{props.item.loggedStr}}
                    </td>
                    <td>
                               {{props.item.enabledStr}}
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
                </td>
              </template>
          </v-data-table>
          
          </v-flex>
          </v-layout>
       </v-container>
</div>
</template>
<script>
import { mapState } from "vuex";
import Kit from "../../../libs/kit.js";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: { Treeselect },
  data() {
    return {
      fValid: true,
      valid: true,
      rowsPerPageItems: [15],
      resQuery: { pn: 1, sortBy: "", descending: "" }, //列表查询参数数据对象
      resView: {}, //查询详细数据对象
      loading: false,
      title: "新增菜单",
      rules: Kit.inputRules,
      vo: {},
      treeData: [],
      treeVal: null,
      treeRoot:{id:'',name:''},
      resHeaders: [
        {
          text: "编号",
          sortable: true,
          value: "code"
        },
        {
          text: "菜单名",
          sortable: true,
          value: "name"
        },
        {
          text: "菜单url",
          sortable: false,
          value: "url"
        },
        {
          text: "顺序",
          sortable: false,
          value: "seq"
        },
        {
          text: "图标",
          sortable: false,
          value: "icon"
        },
        {
          text: "记录日志",
          sortable: false,
          value: "logged"
        },
        {
          text: "是否可用",
          sortable: false,
          value: "enabled"
        },
        { text: "操作", sortable: false }
      ],
      dialog: false,
      viewDialog: false,
      opt: "",
      resSelectData: [{ text: "是", value: "0" }, { text: "否", value: "1" }],
      
    };
  },
  computed: {
    ...mapState({
      res: state => state.res.res,
      resList: state => state.res.resList,
      totalRow: state => state.res.totalRow,
      pageNumber: state => state.res.pageNumber,
      pageSize: state => state.res.pageSize,
      totalPage: state => state.res.totalPage
    })
  },
  mounted() {
    
    this.getResTree()
    // this.resQuery['pn']=this.resQuery.page;
    // this.search();
  },
  
  methods: {
    
    // treeSelectHandle(node,instanceId){
    //   this.resQuery.pId=node.id;
    //   this.search();
    // },
    getResTree(){
      this.$store.dispatch("get_res_tree_json").then(res => {
        this.treeData = res;
        let d=this.treeData;
        let vm=this;
        this.$nextTick(function(){
          vm.treeData=d;
        })
      });
    },
    search() {
      
      this.$store.dispatch("page_res", this.resQuery);
    },
    add() {
      let vm = this;
      this.loading = false;
      let treeRoot = Object.assign({id:0,name:'根菜单'},this.treeVal);
      this.$refs.form.reset();
      this.vo={};
      this.$nextTick(function(){
        vm.treeRoot=treeRoot;
      })
      this.opt = "add";
      this.title = "新增菜单";
      this.dialog = true;
    },
    edit(res) {
      this.loading = false;
      this.vo=Object.assign({},res)
      this.opt = "edit";
      this.dialog = true;
      this.title = "修改菜单";
    },
    save() {
      let vm = this;
      this.vo.pId=this.treeRoot.id;
      if (this.$refs.form.validate()) {
        vm.loading = true;
        this.$store
          .dispatch("save_res", vm.vo)
          .then(res => {
            vm.loading = false;
            if (res.resCode == "success") {
              vm.dialog = false;
              vm.search();
              vm.getResTree();
            }
          })
          .catch(() => {
            vm.loading = false;
            vm.dialog = false;
          });
      }
    },
    update(res) {
      let vm = this;

      if (this.$refs.form.validate()) {
        vm.loading = true;
        this.$store
          .dispatch("update_res", vm.vo)
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
    del(res) {
      let vm = this;
      this.$APDialog.confirm(function(ret) {
        if (ret) {
          vm.$store.dispatch("del_res", { ids: res.id }).then(res => {
            if (res.resCode == "success") {
              vm.search();
              vm.getResTree();  
            }
          });
        } else {
        }
      });
    },
    view(res) {
      let vm = this;
      vm.viewDialog = true;
      vm.$store.dispatch("get_res", { id: res.id }).then(res => {
        vm.resView = res;
      });
    },
    clearQueryParam() {
      this.resQuery["name"] = "";
      this.resQuery["enabled"] = "";
      this.resQuery["code"] = "";
    }
  },
  watch: {
    resQuery: {
      handler(val, oldVal) {
        if (
          this.resQuery.sortBy != "" ||
          (oldVal.page != undefined && val.page != oldVal.page)
        ) {
          this.search();
        }
      },
      deep: true
    },
    treeVal:{
      handler(val,oldVal){
        if(val==undefined){
          this.resQuery.pId=0;
          this.search();
        }else if(oldVal==undefined||val.id!=oldVal.id){
          this.resQuery.pId=val.id==undefined?0:val.id;
          this.search();
        }
      },
      deep:true
    }

  }
};
</script>
<style>
</style>