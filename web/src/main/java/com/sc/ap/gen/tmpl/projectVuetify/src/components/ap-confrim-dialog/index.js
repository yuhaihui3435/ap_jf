import index from './index.vue';


let instance;

const APDialog = function (config = {}) {
  let Tpl = this.extend(index);
  instance = new Tpl();
  for (let key in config) {
    if (config[key] && config[key] != '') {
      instance.$data[key] = config[key];
    }
  }
  instance.$data.dialog = true;
  document.body.appendChild(instance.$mount().$el);
}

const Confirm = function (callback, message) {
  let config = {
    msg: message,
    btns: [{action:function () {
      Dismiss();
      callback && callback(false);
    },label:"取消",color:'error darken-1'},{action:function () {
      Dismiss();
      callback && callback(true);
    },label:'确认',color:'success darken-1'}]
  };

  APDialog.call(this, {
    ...config
  });

}

const Alert = function (callback, message) {
  let config = {
    msg: message,
    btns: [{label:'确认',action:function () {
      Dismiss();
      callback && callback();
    },color:'success darken-1'}]
  };

  APDialog.call(this, {
    ...config
  });

}


const Dismiss = () => {
  instance.$data.dialog = false;
}

export default {
  install(Vue) {
    Vue.prototype.$APDialog = APDialog.bind(Vue);
    Vue.prototype.$APDialog.confirm = Confirm.bind(Vue);
    Vue.prototype.$APDialog.alert = Alert.bind(Vue);
    Vue.prototype.$APDialog['dismiss'] = Dismiss;
  }
}
