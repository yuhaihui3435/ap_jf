const role = {
  state: {
    role: {
      id: '',
      name: '',
      code: '',
      opId: '',
      cAt: '',
      lAt: '',
      dAt: '',
    },
    roleList: [],
    totalPage: 0,
    pageNumber: 1,
    pageSize: 15,
    totalRow: 0,
  },
  mutations: {
    setRole(state, obj) {
      state.role = Object.assign({}, obj)
    },
    setRoleList(state, list) {
      state.roleList = list
    },
    setRolePage(state, page) {
      state.roleList = page.list
      state.totalPage = page.totalPage
      state.pageNumber = page.pageNumber
      state.pageSize = page.pageSize
      state.totalRow = page.totalRow
    },
  },
  actions: {
    list_role: function ({
      commit,
      state
    }, param) {
      this._vm.$http.post('/role/list', param).then((res) => {
        commit('setRoleList', res)
      });
    },
    page_role: function ({
      commit,
      state
    }, param) {
      this._vm.$http.post('/role/page', param).then((res) => {
        commit('setRolePage', res)
      });
    },
    save_role: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/save',param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })

    },
    update_role: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/update', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    del_role: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/logicDel', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    get_role: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/get', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    save_role_reses: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/saveRoleReses', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },save_role_sers: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/role/saveRoleSers', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
  },

}

export default role
