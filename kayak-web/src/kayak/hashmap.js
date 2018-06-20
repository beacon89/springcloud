exports.install = function (Vue, options) {
    Vue.prototype.$Hashmap = (x) => {
        var entry = new Object();
            var size = 0;
            if (x instanceof Array) {
                for (var i in x) {
                    if (x[i][0] !== '' && x[i][1] != '' && x[i][0] !== null && x[i][1] != null) {
                        var key = x[i][0];
                        var value = x[i][1];
                        if(!(key in entry)){
                            size++;
                            entry[key] = value;
                        }
                    }
                }
            }
            if(size > 0){
                return entry;
            }
            return null;
    };

    Vue.prototype.$checkNull = (x) => {
        if(x !== '' && x !== null && typeof(x) !== undefined){
            return true;
        }else{
            return false;
        }
    };

    Vue.prototype.$checkUndefined = (x) => {
        if(typeof(x) === 'undefined' || x === NaN){
            return true;
        }else{
            return false;
        }
    };

    Vue.prototype.$replaceAll = (x,y,z) =>{
        return x.replace(new RegExp(y,"gm"),z);
    };
};

