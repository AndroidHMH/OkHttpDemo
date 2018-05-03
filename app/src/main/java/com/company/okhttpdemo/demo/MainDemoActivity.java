package com.company.okhttpdemo.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.company.okhttpdemo.App;
import com.company.okhttpdemo.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下面用工具来实现OkHttp
 * 注释我就不写那么详细了，只写方法注释。如有不懂请起MainActivity里面查看
 */
public class MainDemoActivity extends AppCompatActivity {

    @BindView(R.id.doGet_btn)
    Button doGetBtn;
    @BindView(R.id.doGet_tong_btn)
    Button doGetTongBtn;
    @BindView(R.id.doGet_can_btn)
    Button doGetCanBtn;
    @BindView(R.id.doGet_head_btn)
    Button doGetHeadBtn;
    @BindView(R.id.doPost_btn)
    Button doPostBtn;
    @BindView(R.id.doPost_tong_btn)
    Button doPostTongBtn;
    @BindView(R.id.doPost_can_btn)
    Button doPostCanBtn;
    @BindView(R.id.doPost_head_btn)
    Button doPostHeadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    //初始化数据
    private void init() {
        App.context = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.context = null;
    }

    @OnClick({R.id.doGet_btn, R.id.doGet_tong_btn, R.id.doGet_can_btn, R.id.doGet_head_btn, R.id.doPost_btn, R.id.doPost_tong_btn, R.id.doPost_can_btn, R.id.doPost_head_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.doGet_btn:
                OkHttpUtils.getInstance().doGet("https://www.baidu.com", new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });
                break;
            case R.id.doGet_tong_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String message = OkHttpUtils.getInstance().doGetTong("https://www.baidu.com");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (message != null) {
                                    //成功的逻辑处理，可更新UI
                                    Log.d("MainDemoActivity", message);
                                } else {
                                    //请求失败的逻辑处理
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
                OkHttpUtils.getInstance().doGet("请求地址", getParams, new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });

                break;
            case R.id.doGet_head_btn:
                Map<String, String> getHeaders = new HashMap<>();
                getHeaders.put("key1", "value1");
                getHeaders.put("key2", "value2");
                getHeaders.put("key3", "value3");
                OkHttpUtils.getInstance().doGetHeader("请求地址", getHeaders, new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });
                break;
            case R.id.doPost_btn:
                OkHttpUtils.getInstance().doPost("请求地址", new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });
                break;
            case R.id.doPost_tong_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String message = OkHttpUtils.getInstance().doPostTong("请求地址");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (message != null) {
                                    //成功的逻辑处理，更新UI
                                } else {
                                    //失败的逻辑处理
                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.doPost_can_btn:
                Map<String, String> postParams = new HashMap<>();
                postParams.put("key1", "value1");
                postParams.put("key2", "value2");
                postParams.put("key3", "value3");
                OkHttpUtils.getInstance().doPost("请求地址", postParams, new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });
                break;
            case R.id.doPost_head_btn:
                Map<String, String> postHeaders = new HashMap<>();
                postHeaders.put("key1", "value1");
                postHeaders.put("key2", "value2");
                postHeaders.put("key3", "value3");
                OkHttpUtils.getInstance().doPostHeader("请求地址", postHeaders, new MyNetWorkCallBack() {
                    //在工具类里面已经做了线程切换，可直接更新UI
                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainDemoActivity", errorMsg);
                    }

                    @Override
                    public void onSuccess(String successMsg) {
                        Log.d("MainDemoActivity", successMsg);
                    }
                });
                break;
        }
    }
}
