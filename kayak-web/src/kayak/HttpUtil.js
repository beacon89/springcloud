import axios from 'axios';

import iview from 'iview';

import { Base64 } from 'js-base64';

let httpUtil = {
};

const ajaxUrl = "http://localhost:8080/";
// const ajaxUrl = "http://base.test.os.kkws.cn:8080/";
// const ajaxUrl = "/";
httpUtil.comnUploadUrl = ajaxUrl + "base/comn-upload.json";

httpUtil.comnUpdate = function (data) {
    data.url = "base/comn-update.json";
    if(data.successAlert !== false){
        data.successAlert = true;
    }
    return httpUtil.ajax(data);
};

httpUtil.update = function (data) {
    if(data.successAlert !== false){
        data.successAlert = true;
    }
    return httpUtil.ajax(data);
};

httpUtil.query = function (data) {
    data.mask = false;
    return httpUtil.ajax(data);
};

httpUtil.comnQuery = function (data) {
    data.url = "base/comn-query.json";
    data.mask = false;
    return httpUtil.ajax(data);
};

httpUtil.comnQueryTree = function (data) {
    data.mask = false;
    data.url = "base/comn-query-tree.json";
    return httpUtil.ajax(data);
};

httpUtil.ajax = function (data) {
   return new Promise((resolve, reject) => {
       if(data.exeid){
           if(!data.params){
               data.params = {};
           }
           data.params.exeid = data.exeid;
       }

       let _ajaxUrl;
       if(data.url.indexOf("http") == -1){
           _ajaxUrl = ajaxUrl + data.url;
       }else {
           _ajaxUrl = data.url;
       }

       var authorization = localStorage.getItem("authorization");

       if(authorization){
           var playLoad = authorization.split(".")[0];

           var timestamp=new Date().getTime();

           var datatime = timestamp - playLoad;

           if(datatime > (15 * 60 * 60 * 1000)){//超越15分钟则尝试刷新token
               axios({
                   method:"POST",
                   url: ajaxUrl + "freshToken.json",
                   headers:{"authorization":authorization}
               }).then(res => {
                   let reData = res.data;
                   if(reData.success){
                       localStorage.setItem("authorization",reData.token);
                   }else{
                       iview.Message.error(reData.returnmsg);
                   }
               }).catch(function(err){
                   iview.Message.error("服务器异常，请稍后尝试");
                   console.log(err);
               });
           }

       }

       var mask = data.mask;

       if(mask !== false){
           iview.Spin.show({
               render: (h) => {
                   return h('div', [
                       h('Icon', {
                           'class': 'demo-spin-icon-load',
                           props: {
                               type: 'load-c',
                               size: 18
                           }
                       }),
                       h('div', '正在处理...')
                   ])
               }
           });
       }

       axios({
           method:data.method,
           url: _ajaxUrl,
           data:data.params,
           headers:{"authorization":authorization}
       }).then(res => {
           let reData = res.data;
           if(reData.success){
               if(data.successAlert){
                   iview.Message.success(reData.returnmsg);
               }
               resolve(reData);
           }else{
               iview.Message.error(reData.returnmsg);
           }

           if(mask !== false){
               iview.Spin.hide();
           }
       }).catch(function(err){
           iview.Message.error("服务器异常，请稍后尝试");
           console.log(err);

           if(mask !== false){
               iview.Spin.hide();
           }
       });
    });
};

export default httpUtil;