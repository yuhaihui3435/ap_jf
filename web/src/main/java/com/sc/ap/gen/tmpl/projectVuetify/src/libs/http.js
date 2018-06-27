import axios from 'axios';
import store from '../store/index'
import Kit from '../libs/kit'
import qs from 'qs'
import {router} from '../router'


require('es6-promise').polyfill()

let env=process.env.NODE_ENV;

let axiosIns = axios.create({});
if (env == 'development') {
    axiosIns.defaults.baseURL = '/api';
} else if (env == 'debug') {
    axiosIns.defaults.baseURL = '/api';
} else if (env == 'production') {
    axiosIns.defaults.baseURL = '';
}


axiosIns.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';
axiosIns.defaults.headers.get['X-Requested-With'] = 'XMLHttpRequest';
axiosIns.defaults.responseType = 'json';
axiosIns.defaults.transformRequest = [function (data) {

    if(  data instanceof FormData){

        return data;
    }

    return qs.stringify(data);
}
];
axiosIns.defaults.validateStatus = function (status) {
    return true;
};
axiosIns.interceptors.request.use(function (config) {
    store.commit('setLoadingStatus',true)
    //配置config
    config.headers.Accept = 'application/json';

    return config;
});

axiosIns.interceptors.response.use(function (response) {
    store.commit('setLoadingStatus',false)
    let data = response.data;
    let status = response.status;
    if (status === 200) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(response);
    }
});

const ajaxMethod = ['get', 'post'];
let api = {};
ajaxMethod.forEach((method)=> {
    //数组取值的两种方式
    api[method] = function (uri, data, config) {
        return new Promise(function (resolve, reject) {
            axiosIns[method](uri, data, config).then((response)=> {
                let data=response.data;
                    if(data.resCode&&data.resCode=='success') {
                        Kit.msg.ok(data.resMsg);
                    }
                    else if(data.resCode&&data.resCode=='fail') {
                        Kit.msg.err(data.resMsg);
                    }
                    resolve(data);

            }).catch((response)=> {
            if (response.status === 401||response.status === 900||response.status === 901) {
            Kit.msg.err(response.data.resMsg)
            store.commit('logout')
            router.push({
              path: "/login"
            });
          }else if (response.status === 403) {
            Kit.msg.err(response.data.resMsg)
            reject(response)
          }
          else if (response.status === 500) {
            Kit.msg.err('服务器错误')
            reject(response)
          }
          else if (response.status === 404) {
            Kit.msg.err('您访问的资源不存在404')
            reject(response)
          }
            })
        })
    }
});

export default api

