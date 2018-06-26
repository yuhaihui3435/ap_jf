<template>

<div>
  <v-dialog v-model="dialog" persistent max-width="500px">
      <v-card >
        <v-card-title>
          <span class="headline">{{title}}</span>
        </v-card-title>
        <v-card-text>
            <v-form v-model="fValid" ref="form" lazy-validation @submit.prevent>
                <v-container grid-list-md>
                  <v-layout wrap>
                      <v-flex xs12 sm6 md4>   
                        <v-text-field v-model="vo.dict"  label="字典名"  :disabled="pName!=''||opt=='update'" :rules="[rules.required,(v) => !!v&&v.length <= 50 || '最多 50 字符']" :counter="50"></v-text-field>
                      </v-flex>
                      <v-flex xs12 sm6 md4>
                        <v-text-field v-model="pName"  label="上级名字"   disabled></v-text-field>
                      </v-flex>
                      <v-flex xs12 sm6 md4>
                        <v-text-field v-model="vo.name"  label="名字"  :rules="[rules.required,(v) => !!v&&v.length <= 50 || '最多 50 字符']" :counter="50"></v-text-field>
                      </v-flex>
                      <v-flex xs12 sm6 md4>
                        <v-text-field v-model="vo.val"  label="值"  :rules="[(v) => !!!v||(v!=undefined&&v.length <= 50) || '最多 50 字符']" :counter="50"></v-text-field>
                      </v-flex>
                      <v-flex xs12 sm6 md4>
                        <v-text-field v-model="vo.idx"  label="序号"  :rules="[rules.digital]"></v-text-field>
                      </v-flex>
                  </v-layout>
                </v-container>
            </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error darken-1" flat @click.native="dialog = false">关闭</v-btn>
          <v-btn color="success darken-1" flat @click.native="save" :loading="loading" v-show="opt=='add'" :disabled="loading||!fValid">保存</v-btn>
          <v-btn color="success darken-1" flat @click.native="update" v-show="opt=='update'" :loading="loading" :disabled="loading||!fValid">保存</v-btn>
        </v-card-actions>
      </v-card>
  </v-dialog>
  <v-container fluid grid-list-md>
    <v-layout row wrap>
    <v-flex xs12 sm4 md4 >
      <v-card>
        <v-toolbar color="blue" dark>
          <v-toolbar-title>数据字典</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="toAdd()">
            <v-icon>add</v-icon>
          </v-btn>
          <v-btn icon @click="refresh()">
            <v-icon>refresh</v-icon>
          </v-btn>
        </v-toolbar>
        <v-list >
            <v-list-tile v-for="item in ddList" :key="item.id" >
              <v-list-tile-content>
                <v-list-tile-title>{{ item.name }}({{item.dict}})</v-list-tile-title>
              </v-list-tile-content>
              <v-list-tile-action>
                      <v-btn icon @click="getChildren(item)">
                        <v-icon color="teal">fas fa-eye</v-icon>
                      </v-btn>
              </v-list-tile-action>
               <v-list-tile-action @click="toAdd(item)">
                      <v-btn icon >
                        <v-icon color="blue">add_circle_outline</v-icon>
                      </v-btn>
              </v-list-tile-action>
              <v-list-tile-action>
                      <v-btn icon @click="toEdit(item)">
                        <v-icon color="teal">edit</v-icon>
                      </v-btn>
              </v-list-tile-action>
              <v-list-tile-action>
                      <v-btn icon @click="del(item)">
                        <v-icon color="pink" >delete</v-icon>
                      </v-btn>
              </v-list-tile-action>
            </v-list-tile>
        </v-list>
      </v-card>
    </v-flex>
    <v-flex xs12 sm8 md8 >
      <v-card >
        <v-toolbar color="blue" dark>
          <v-toolbar-title>{{ddListTitle}}</v-toolbar-title>
          <v-spacer></v-spacer>
        </v-toolbar>
        <v-data-table :headers="headers" :items="ddChildren" hide-actions class="elevation-1" no-data-text="数据为空" no-results-text="没有筛选到正确的数据">
          <template slot="items" slot-scope="props">
            <td>{{ props.item.dict }}</td>
            <!-- <td >{{ props.item.pName }}</td> -->
            <td >{{ props.item.name }}</td>
            <td >{{ props.item.val }}</td>
            <td >{{ props.item.idx }}</td>
            <td class=" layout px-0">
              <v-btn icon class="mx-0" @click="toEdit(props.item)">
                <v-icon color="teal">edit</v-icon>
              </v-btn>
              <v-btn icon class="mx-0" @click="del(props.item)">
                <v-icon color="pink">delete</v-icon>
              </v-btn>
            </td>
          </template>
      </v-data-table>
      </v-card>
    </v-flex>
  </v-layout>
  </v-container>
</div>
</template>
<script>
import { mapState } from "vuex";
import Kit from "../../../libs/kit.js";
export default {
  data() {
    return {
      fValid: true,
      loading: false,
      title: "新增数据字典",
      ddListTitle: "字典数据列表",
      pName: "",
      pId: "",
      vo:{},
      rules: Kit.inputRules,
      icon: "menu",
      headers: [
        {
          text: "字典名",
          align: "left",
          sortable: false,
          value: "dict"
        },
        // { text: "上级名字", value: "pName" },
        { text: "名字", value: "name" },
        { text: "值", value: "val" },
        { text: "序号", value: "idx" },
        { text: "操作", value: "name", sortable: false }
      ],
      dialog: false,
      opt: "",
      selectId: ""
    };
  },
  computed: {
    ...mapState({
      dd: state => state.dd.dd,
      ddList: state => state.dd.ddList,
      ddChildren: state => state.dd.ddChildren
    })
  },
  mounted() {
    this.refresh();
    this.$store.commit("setDdChildren", []);
  },

  methods: {
    refresh() {
      this.$store.dispatch("query_dd_json");
    },
    save() {
      let vm = this;
      
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("save_dd",this.vo)
          .then(res => {
            this.loading = false;
            if (res.resCode == "success") {
              this.dialog = false;
              if (vm.pName == "") {
                vm.$store.dispatch("query_dd_json");
              } else {
                vm.$store.dispatch("get_dd_children", { id: vm.selectId });
              }
            }
          })
          .catch(() => {
            this.loading = false;
            this.dialog = false;
          });
      }
    },
    toAdd(item) {
      let vm=this;
      this.loading = false;
      this.$refs.form.reset();
      this.dialog = true;
      this.opt = "add";
      let data={}
      let _pName;
      if(item==undefined){
        data.pId=0;
        this.selectId=0;
        this.title="新增数据字典";
        _pName='';
      }else{
        _pName=item.name;
        this.selectId=item.id;
        this.title="新增数据字典值";
        data.pId=item.id;
        data.dict=item.dict;
      }
      this.$nextTick(function(){
        vm.vo=Object.assign({},data);
        vm.pName=_pName;
      });
    },
    getChildren(dd) {
      if (dd.id == undefined) this.$store.commit("setDdChildren", []);
      if (dd.name != undefined)
        this.ddListTitle = "字典【" + dd.name + "】数据列表";
      this.$store.dispatch("get_dd_children", { id: dd.id });
      this.selectId = dd.id;
      this.pName=dd.name;
    },
    toEdit(dd) {
      let vm=this;
      this.loading = false;
      let _pName;
      if(dd.pId!=0)
       _pName=this.pName;
      this.$refs.form.reset();
      this.opt = "update";
      this.dialog = true;
      this.selectId = dd.pId;
      this.title = dd.pId == 0 ? "修改数据字典" : "修改数据字典值";
      this.$nextTick(function(){
        vm.vo=Object.assign({},dd)
        vm.pName=_pName;
      })
    },
    update(dd) {
      let vm = this;
      
      if (this.$refs.form.validate()) {
        this.loading = true;
        this.$store
          .dispatch("update_dd",this.vo)
          .then(res => {
            this.loading = false;
            if (res.resCode == "success") {
              this.dialog = false;
              //刷新左侧数据，置空右侧数据
              if (vm.selectId == 0) {
                vm.$store.dispatch("query_dd_json");
                vm.$store.commit("setDdChildren", []);
              } else {
                //刷新右侧数据
                vm.$store.dispatch("get_dd_children", { id: vm.selectId });
              }
            }
          })
          .catch(() => {
            this.loading = false;
            this.dialog = false;
          });
      }
    },
    del(dd) {
      let vm = this;
      this.$APDialog.confirm(function(ret) {
        if (ret) {
          vm.$store.dispatch("del_dd", { id: dd.id }).then(res => {
            if (res.resCode == "success") {
              if (dd.pId != 0) vm.getChildren({ id: vm.selectId });
              else {
                vm.$store.commit("setDdChildren", []);
                vm.$store.dispatch("query_dd_json");
              }
            }
          });
        } else {
        }
      });
    }
  },
  // watch: {
  //   dd(val, oldVal) {}
  // }
};
</script>
<style>
</style>
