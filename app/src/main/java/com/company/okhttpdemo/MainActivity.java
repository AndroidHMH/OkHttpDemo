package com.company.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp实现网络请求
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.doGet_btn)
    Button doGetBtn;
    @BindView(R.id.doGet_can_btn)
    Button doGetCanBtn;
    @BindView(R.id.doPost_btn)
    Button doPostBtn;
    @BindView(R.id.doPost_can_btn)
    Button doPostCanBtn;
    @BindView(R.id.doGet_head_btn)
    Button doGetHeadBtn;
    @BindView(R.id.doPost_head_btn)
    Button doPostHeadBtn;
    @BindView(R.id.doGet_tong_btn)
    Button doGetTongBtn;
    @BindView(R.id.doPost_tong_btn)
    Button doPostTongBtn;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    //初始化数据
    private void init() {
        //获取OkHttpClient对象
        okHttpClient = new OkHttpClient.Builder().build();
    }

    @OnClick({R.id.doGet_btn, R.id.doGet_can_btn, R.id.doPost_btn, R.id.doPost_can_btn, R.id.doGet_head_btn, R.id.doPost_head_btn, R.id.doGet_tong_btn, R.id.doPost_tong_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.doGet_btn:
                doGet("https://www.baidu.com");
                break;
            case R.id.doGet_tong_btn:
                //同步请求需要开启一个子线程,在子线程中请求数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取请求结果
                        final String response = doGetTong("https://www.baidu.com");
                          /*
                            拿到数据之后，Gson解析数据
                           */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response != null) {
                                    Toast.makeText(MainActivity.this, "请求结果 ： " + response, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.doGet_can_btn:
                Map<String, String> getParams = new HashMap<>();
                getParams.put("key1", "value1");
                getParams.put("key2", "value2");
                getParams.put("key3", "value3");
                doGet("请求地址", getParams);
                break;
            case R.id.doGet_head_btn:
                Map<String, String> getHeaders = new HashMap<>();
                getHeaders.put("key1", "value1");
                getHeaders.put("key2", "value2");
                getHeaders.put("key3", "value3");
                doGetHeader("请求地址", getHeaders);
                break;
            case R.id.doPost_btn:
                doPost("请求地址");
                break;
            case R.id.doPost_tong_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doPostTong("请求地址");
                    }
                }).start();
                break;
            case R.id.doPost_can_btn:
                Map<String, String> postParams = new HashMap<>();
                postParams.put("key1", "value1");
                postParams.put("key2", "value2");
                postParams.put("key3", "value3");
                doPost("请求地址", postParams);
                break;
            case R.id.doPost_head_btn:
                Map<String, String> postHeaders = new HashMap<>();
                postHeaders.put("key1", "value1");
                postHeaders.put("key2", "value2");
                postHeaders.put("key3", "value3");
                doPostHeader("请求地址", postHeaders);
                break;
        }
    }

    /**
     * 发送无参的get请求
     *
     * @param url 请求地址
     */
    private void doGet(String url) {
        Request request = new Request.Builder()
                //设置url地址
                .url(url).build();
        //获取Call对象
        Call call = okHttpClient.newCall(request);
        //发送异步请求
        call.enqueue(new Callback() {
            /*
            在这个内部类的方法都是在子线程中，如果你需要更新UI的就要做线程之间的切换
             */

            /**
             * 请求失败的监听
             * @param call
             * @param e 失败的错误信息
             */
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "错误信息 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 请求成功的监听
             * @param call
             * @param response  请求结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取请求体对象
                ResponseBody responseBody = response.body();
                //将请求结果转化为字符串
                final String responseStr = responseBody.string();
                /*
                拿到数据之后，Gson解析数据
                 */
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求结果 : " + responseStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 发送同步的get请求
     *
     * @return 请求结果转为字符串
     */
    private String doGetTong(String url) {
        Request request = new Request.Builder()
                //设置url地址
                .url(url).build();
        //获取Call对象
        Call call = okHttpClient.newCall(request);
        try {
            //发送同步请求
            Response response = call.execute();
            String responseStr = response.body().string();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送有参的get请求
     * get请求需要自己手动拼接参数
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    private void doGet(String url, Map<String, String> params) {
        //拼接url地址
        if (!params.isEmpty() && params != null) {
            StringBuilder sbUrl = new StringBuilder(url);
            sbUrl.append("?");
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                sbUrl.append(key).append("=").append(params.get(key)).append("&");
            }
            url = sbUrl.substring(0, sbUrl.length() - 1);
        }
        Request request = new Request.Builder()
                //设置url地址
                .url(url).build();
        //获取Call对象
        Call call = okHttpClient.newCall(request);
        //发送异步请求
        call.enqueue(new Callback() {
            /*
            在这个内部类的方法都是在子线程中，如果你需要更新UI的就要做线程之间的切换
             */

            /**
             * 请求失败的监听
             * @param call
             * @param e 失败的错误信息
             */
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "错误信息 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 请求成功的监听
             * @param call
             * @param response  请求结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取请求体对象
                ResponseBody responseBody = response.body();
                //将请求结果转化为字符串
                final String responseStr = responseBody.string();
                  /*
                拿到数据之后，Gson解析数据
                 */
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求结果 : " + responseStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 发送带请求头的get请求
     *
     * @param url     请求地址
     * @param headers 请求头信息
     */
    private void doGetHeader(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        //设置url
        builder.url(url);
        //添加请求头
        if (headers.isEmpty() && headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                builder.addHeader(key, headers.get(key));
            }
        }
        //获取Request对象
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            /*
            在这个内部类的方法都是在子线程中，如果你需要更新UI的就要做线程之间的切换
             */

            /**
             * 请求失败的监听
             * @param call
             * @param e 失败的错误信息
             */
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "错误信息 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 请求成功的监听
             * @param call
             * @param response  请求结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取请求体对象
                ResponseBody responseBody = response.body();
                //将请求结果转化为字符串
                final String responseStr = responseBody.string();
                  /*
                拿到数据之后，Gson解析数据
                 */
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求结果 : " + responseStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 发送无参的post请求
     * 无参的post请求与get请求没有区别，完全可以当做无参的get请求处理
     * 就不写了，直接看无参的get请求即可
     *
     * @param url 请求地址
     */
    private void doPost(String url) {
        //调用get请求的方法
        doGet(url);
    }

    /**
     * 发送同的post请求
     * 无参同步的post请求与同步get请求没有区别，完全可以当做无参同步的get请求处理
     * 就不写了，直接看无参同步的get请求即可
     *
     * @return 请求结果转为String
     */
    private String doPostTong(String url) {
        //调用get请求的同步方法
        String request = doGetTong(url);
        return request;
    }

    /**
     * 发送有参的post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    private void doPost(String url, Map<String, String> params) {
        //创建FormBody.Builder对象
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数信息
        if (params.isEmpty() && params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                builder.add(key, params.get(key));
            }
        }
        //获取FormBody对象
        FormBody formBody = builder.build();
        //获取Request对象
        Request request = new Request.Builder().post(formBody).url(url).build();
        //发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            /*
            在这个内部类的方法都是在子线程中，如果你需要更新UI的就要做线程之间的切换
             */

            /**
             * 请求失败的监听
             * @param call
             * @param e 失败的错误信息
             */
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "错误信息 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 请求成功的监听
             * @param call
             * @param response  请求结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取请求体对象
                ResponseBody responseBody = response.body();
                //将请求结果转化为字符串
                final String responseStr = responseBody.string();
                  /*
                拿到数据之后，Gson解析数据
                 */
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求结果 : " + responseStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 发送带请求头的post请求
     *
     * @param url     请求地址
     * @param headers 请求头信息
     */
    private void doPostHeader(String url, Map<String, String> headers) {
        //获取FormBody对象
        FormBody.Builder formBuilder = new FormBody.Builder();
        /*
        添加参数的操作
         */
        FormBody formBody = formBuilder.build();
        //创建Request.Builder对象，并设置请求方式
        Request.Builder builder = new Request.Builder().post(formBody);
        //添加请求头
        if (headers.isEmpty() && headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                builder.addHeader(key, headers.get(key));
            }
        }
        //获取Request对象
        Request request = builder.build();
        //发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            /*
            在这个内部类的方法都是在子线程中，如果你需要更新UI的就要做线程之间的切换
             */

            /**
             * 请求失败的监听
             * @param call
             * @param e 失败的错误信息
             */
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "错误信息 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 请求成功的监听
             * @param call
             * @param response  请求结果
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取请求体对象
                ResponseBody responseBody = response.body();
                //将请求结果转化为字符串
                final String responseStr = responseBody.string();
                  /*
                拿到数据之后，Gson解析数据
                 */
                //切换回主线程循环执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求结果 : " + responseStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
