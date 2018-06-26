const dd = {
    state: {
      dd: {},
      ddList:[],
      ddChildren:[]
    },
    mutations: {
      setDd(state, obj) {
        state.dd = Object.assign({}, obj)
      },
      setDdList(state, list) {
        state.ddList = list
      },
      setDdChildren(state,list){
        state.ddChildren=list
      }
    },
    actions: {
      query_dd_json: function ({
        commit,
        state
      }, param) {
        this._vm.$http.get('/dd/list').then((res) => {
          commit('setDdList', res)
        });
      },
      save_dd: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/dd/save', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
  
      },
      update_dd: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/dd/update', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      del_dd: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/dd/del', param).then((res) => {
              resolve(res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_dd: function ({
        commit,
        state
      }, param) {
          let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/dd/get', param).then((res) => {
              commit('setDd',res)
          }).catch((res)=>{
            reject(res)
          });
        })
      },
      get_dd_children: function ({
        commit,
        state
      }, param) {
        let vm=this._vm;
        return new Promise(function (resolve, reject) {
          vm.$http.post('/dd/get', param).then((res) => {
              commit('setDdChildren',res.children)
          }).catch((res)=>{
            reject(res)
          });
        })
      }
      
    },
    
  }
  
  export default dd
  