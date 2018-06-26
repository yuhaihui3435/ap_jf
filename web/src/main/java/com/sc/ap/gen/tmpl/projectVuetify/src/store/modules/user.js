const user = {
  state: {
    user: {
      id: '',
      loginname: '',
      password: '',
      nickname: '',
      phone: '',
      email: '',
      avatar: '',
      status: '',
      salt: '',
      lAt: '',
      opId: '',
      cAt: '',
      dAt: '',
    },
    userList: [],
    totalPage: 0,
    pageNumber: 1,
    pageSize: 15,
    totalRow: 0,
  },
  mutations: {
    setUser(state, obj) {
      state.user = Object.assign({}, obj)
    },
    setUserList(state, list) {
      state.userList = list
    },
    setUserPage(state, page) {
      state.userList = page.list
      state.totalPage = page.totalPage
      state.pageNumber = page.pageNumber
      state.pageSize = page.pageSize
      state.totalRow = page.totalRow
    },
  },
  actions: {
    list_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
      vm.$http.post('/user/list', param).then((res) => {
        commit('setUserList', res)
      }).catch((res)=>{
        reject(res)
      });
    })
    },
    page_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
      vm.$http.post('/user/page', param).then((res) => {
        commit('setUserPage', res)
      }).catch((res)=>{
        reject(res)
      });
    })
    },
    save_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/save', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })

    },
    update_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/update', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    update_pwd: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/updatePwd', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    del_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/logicDel', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    get_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/get', param).then((res) => {
          resolve( res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    get_curr_user: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/getCurrUser', param).then((res) => {
          resolve( res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
    save_user_roles: function ({
      commit,
      state
    }, param) {
      let vm = this._vm;
      return new Promise(function (resolve, reject) {
        vm.$http.post('/user/saveUserRoles', param).then((res) => {
          resolve(res)
        }).catch((res)=>{
          reject(res)
        });
      })
    },
  },

}

export default user
