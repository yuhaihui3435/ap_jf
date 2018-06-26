const res = {
    state: {
      res: {
            id:'',
            name:'',
            url:'',
            seq:'',
            icon:'',
            logged:'',
            enabled:'',
            pId:'',
            code:'',
            opId:'',
            cAt:'',
            dAt:'',
            lAt:'',
      },
      resList:[],
      totalPage:0,
      pageNumber:1,
      pageSize:15,
      totalRow:0,
    },
    mutations: {
      setRes(state, obj) {
        state.res = Object.assign({}, obj)
      },
      setResList(state, list) {
        state.resList = list
      },
      setResPage(state, page) {
        state.resList = page.list
        state.totalPage=page.totalPage
        state.pageNumber=page.pageNumber
        state.pageSize=page.pageSize
        state.totalRow=page.totalRow
      },
    },
    actions: {
      list_res: function ({
        commit,
        state
      }, param) {
        this._vm.$http.post('/res/list',param).then((res) => {
          commit('setResList', res)
        });
      },
      page_res: function ({
              commit,
              state
      }, param) {
         this._vm.$http.post('/res/page',param).then((res) => {
            commit('setResPage', res)
         });
      },
      save_res: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/res/save', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })

      },
      update_res: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/res/update', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      del_res: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/res/logicDel', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_res: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/res/get', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_res_tree_json: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/res/getTreeJson', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
    },

  }

export default res