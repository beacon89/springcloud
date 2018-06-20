import httpUtil from './HttpUtil.js';
let dict = {
};

dict.dicts = {};

dict.dict = function (dict) {
    return new Promise((resolve, reject) => {
        let dt = this.dicts[dict];
        if(dt==null){
            httpUtil.ajax({url:"base/dict/"+dict+".json",method:"POST",mask:false}).then(data=>{
                this.dicts[dict] = data.rows;
                return resolve(data.rows);
            });
        }else{
            return resolve(dt);
        }
    });
};

dict.dictTransfer = function (dict,key) {
    return new Promise((resolve, reject) => {
        if(dict===null || dict==='' || key===null || key===''){
            return resolve('');
        }
        dict.dict(dict).then(data=>{//取得字典数据
            if(data==null){//取不到字典数据
                return key;
            }

            let vals = [];
            let keys = String(key).split(',');
            let len = keys.length;
            for(let i=0;i<len;i++){
                for(let j=0;j<data.length;j++){
                    if(data[j].itemkey == key){
                        vals.push(data[j].itemval==null?'':data[j].itemval);
                    }
                }
            }
            return resolve(vals.join('，'));
        });
    });
};


export default dict;