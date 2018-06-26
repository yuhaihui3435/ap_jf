const ser = {
    state: {
      ser: {
            id:'',
            code:'',
            type:'',
            name:'',
            url:'',
            opId:'',
            cAt:'',
            lAt:'',
            dAt:'',
      },
      serList:[],
      totalPage:0,
      pageNumber:1,
      pageSize:15,
      totalRow:0,
    },
    mutations: {
      setSer(state, obj) {
        state.ser = Object.assign({}, obj)
      },
      setSerList(state, list) {
        state.serList = list
      },
      setSerPage(state, page) {
        state.serList = page.list
        state.totalPage=page.totalPage
        state.pageNumber=page.pageNumber
        state.pageSize=page.pageSize
        state.totalRow=page.totalRow
      },
    },
    actions: {
      list_ser: function ({
        commit,
        state
      }, param) {
        this._vm.$http.post('/ser/list',param).then((res) => {
          commit('setSerList', res)
        });
      },
      page_ser: function ({
              commit,
              state
      }, param) {
         this._vm.$http.post('/ser/page',param).then((res) => {
            commit('setSerPage', res)
         });
      },
      save_ser: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/ser/save', param).then((res) => {
              resolve(res)
          });
        })

      },
      update_ser: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/ser/update', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      del_ser: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/ser/logicDel', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_ser: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/ser/get', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_ser_tree_json: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/ser/getTreeJson', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
    },

  }

export default ser