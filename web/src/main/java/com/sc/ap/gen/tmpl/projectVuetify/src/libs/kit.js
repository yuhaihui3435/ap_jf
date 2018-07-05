import store from '../store/index'
import IDV from './IDValidator'
let env=process.env.NODE_ENV;
let kit = {

  msg: {

  },
  inputRules: {
    required: (val) => !!val || '必填',
    email: (val) => {
      if(!!!val)return true;
      const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      return pattern.test(val) || 'e-mail格式错误'
    },
    digital:(val)=>{
      const pattern=/^[0-9]*$/
      return (!!!val||pattern.test(val)) || '必须是数字'
    },
    chart:(val)=>{
      if(!!!val)return true
      const pattern=/[a-zA-Z]/
      return pattern.test(val)||'必须是大写或者小写字母'
    },
    chinese:(val)=>{
      if(!!!val)return true
      const pattern=/[\u4e00-\u9fa5]/
      return pattern.test(val)||'必须是中文'
    },
    phone:(val)=>{
      if(!!!val)return true
      const pattern=/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/
      return pattern.test(val)||'手机号码格式不正确'
    },
    url:(val)=>{
      if(!!!val)return true
      const pattern = "^((https|http|ftp|rtsp|mms)?://)"
      + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
       + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
       + "|" // 允许IP和DOMAIN（域名）
       + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
       + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
       + "[a-z]{2,6})" // first level domain- .com or .museum
       + "(:[0-9]{1,4})?" // 端口- :80
       + "((/?)|" // a slash isn't required if there is no file name
       + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
      return pattern.test(val)||'URL格式不正确'

    },
    userName:(val)=>{
      if(!!!val)return true
      const pattern =/^[a-zA-Z0-9_-]{4,16}$/
      return pattern.test(val)||'用户名由字母，数字，下划线，减号组成'
    },
    pwd:(val)=>{
      if(!!!val)return true
      const pattern =/^[a-zA-Z]\w{5,17}$/
      return pattern.test(val)||'密码以字母开头，长度在6~18之间，只能包含字母、数字和下划线'
    },
    strongPwd:(val)=>{
      if(!!!val)return true
      const pattern =/^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/
      return pattern.test(val)||'密码最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符'
    },
    ip:(val)=>{
      if(!!!val)return true
      const pattern =/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
      return pattern.test(val)||'IP格式不正确'
    },
    ID:(val)=>{
      if(!!!val)return true
      let idv=new IDValidator();
      let ret=idv.isValid(value);
      return ret||'身份证号无效'
    },
    zipCode:(val)=>{
      if(!!!val)return true
      const pattern =/[1-9]\d{5}(?!\d)/
      return pattern.test(val)||'邮编无效'
    },
    amount:(val)=>{
      if(!!!val)return true
      const pattern =/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/
      return pattern.test(val)||'金额无效'
    }
  }
};

kit.title = function (title) {
  title = title || '应用程序';
  window.document.title = title;
};

kit.inOf = function (arr, targetArr) {
  let res = true;
  arr.forEach(item => {
    if (targetArr.indexOf(item) < 0) {
      res = false;
    }
  });
  return res;
};

kit.oneOf = function (ele, targetArr) {
  if (targetArr.indexOf(ele) >= 0) {
    return true;
  } else {
    return false;
  }
};

kit.msg.ok = function (msg) {
  let data = {
    y: 'top',
    mode: 'multi-line',
    color: 'success',
    timeout: 3000,
    text: msg,
    show: true,
  }

  store.commit('setSnackbar', data);

}

kit.msg.info = function (msg) {
  let data = {
    y: 'top',
    mode: 'multi-line',
    color: 'info',
    timeout: 3000,
    text: msg,
    show: true,
  }

  store.commit('setSnackbar', data);

}

kit.msg.err = function (msg) {
  let data = {
    y: 'top',
    mode: 'multi-line',
    color: 'error',
    timeout: 3000,
    text: msg,
    show: true,
  }

  store.commit('setSnackbar', data);

}
kit.env=(env == 'development' ? '/api' : '');
kit.envImg=(env == 'development' ? '' : '/');//修改生成环境的上下文路径
export default kit
