let util = {
};

util.clone = function(obj, copyObj){
    for (var attr in obj) {
        obj[attr] = "";
        if (copyObj.hasOwnProperty(attr)) obj[attr] = copyObj[attr];
    }
};

util.date2str = function(date){
    var month = date.getMonth()+1, day = date.getDate();
    if(month<10){
        month = '0'+String(month);
    }
    if(day<10){
        day = '0'+String(day);
    }
    return String(date.getFullYear()) + String(month) + String(day);
};


export default util;