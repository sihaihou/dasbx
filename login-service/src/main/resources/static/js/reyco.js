/**
 * 根据名称获取Cookie
 * @param {*} name 
 */
const getCookie = (name) => {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return (arr[2]);
    else
        return null;
}
/**
 * 添加Cookie
 * @param {*} name 
 * @param {*} value 
 * @param {*} expire 
 * @param {*} path 
 */
const setCookie = (name, value, expire, path) => {
    var currDate = new Date();
    currDate.setDate(currDate.getDate() + expire);
    document.cookie = name + "=" + escape(value) + (expire==null ? "" : ";expires="+currDate.toUTCString()) + ";path=" + (path==null ? "/" : path);
}
/**
 * 删除Cookie
 * @param {*} name 
 */
const delCookie = (name,path) => {
    var expDate = new Date();
    expDate.setTime(expDate.getTime() - 1);
    var value = getCookie(name);
    if (value != null) {
        document.cookie = name + "=" + value + ";expires=" + expDate.toUTCString()+";path=" + (path ? "/" : path);
    }
}