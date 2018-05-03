package com.company.okhttpdemo.demo;

/**
 * Created by asus on 2018/5/2.
 */

/**
 * 自定义接口存储请求结果
 */
public interface MyNetWorkCallBack {
    /**
     * 请求失败
     *
     * @param errorMsg 请求失败信息
     */
    void onError(String errorMsg);

    /**
     * 请求成功
     * 一般我们请求的结果都为json数据，所以这里就直接存储String。
     * 根据需求更改类型即可。
     *
     * @param successMsg 请求成功的结果并转为字符串
     */
    void onSuccess(String successMsg);
}
