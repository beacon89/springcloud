import httpUtil from '../kayak/HttpUtil.js';
import util from '../kayak/Util.js';
import dict from '../kayak/Dict.js';
import Md5Util from '../kayak/md5.js';
import base64 from '../kayak/Base64.js';

let kayakInstall = {};

let kayak = {};

//注册控件
kayak.httpUtil = httpUtil;
kayak.util = util;
kayak.dict = dict;
kayak.md5 = Md5Util;
kayak.base64 = base64;

kayakInstall.install = function(vue){
    vue.prototype.kayak = kayak;
};

export default kayakInstall;