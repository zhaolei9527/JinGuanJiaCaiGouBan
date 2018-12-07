package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.db.DBOpenHelper;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.SpinerPopWindow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by 赵磊 on 2017/7/13.
 */

public class LinkActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.ll_Save_Link)
    LinearLayout llSaveLink;
    @BindView(R.id.tv_Link_name)
    EditText tvLinkName;
    @BindView(R.id.fl_More_Link)
    FrameLayout flMoreLink;
    @BindView(R.id.tv_Delete)
    TextView tvDelete;
    @BindView(R.id.et_Service_IP)
    EditText etServiceIP;
    @BindView(R.id.et_Service_Host)
    EditText etServiceHost;
    @BindView(R.id.et_Service_username)
    EditText etServiceUsername;
    @BindView(R.id.et_Service_pwd)
    EditText etServicePwd;
    @BindView(R.id.tv_query_DB)
    TextView tvQueryDB;
    @BindView(R.id.et_app_pwd)
    EditText etAppPwd;
    @BindView(R.id.et_app_pwd_again)
    EditText etAppPwdAgain;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ll_change_pwd)
    LinearLayout llChangePwd;
    @BindView(R.id.tv_check_DB)
    TextView tvCheckDB;
    @BindView(R.id.ll_do_change_pwd)
    LinearLayout llDoChangePwd;
    private String linkName;
    private String serviceIP;
    private String serviceHost;
    private String serviceUsername;
    private String servicePwd;
    private String checkDB;
    private Dialog dialogLoading;
    private String appPwd;
    private String appPwdAgain;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private ArrayList<String> list;
    private Map<String, String> allSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void ready() {
        super.ready();
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int setthislayout() {
        return R.layout.activcity_link;
    }

    @Override
    protected void initview() {
        dialogLoading = Utils.showLoadingDialog(context);
        String Link = getIntent().getStringExtra("Link");
        if (!TextUtils.isEmpty(Link)) {
            String URl = (String) SpUtil.get(context, "URl#" + Link, "");
            Log.e("LoginActivity", URl);
            String[] split = URl.split("#");
            tvLinkName.setText(Link);
            etServiceIP.setText(split[0]);
            etServiceHost.setText(split[1]);
            etServiceUsername.setText(split[2]);
            etServicePwd.setText(split[3]);
            tvCheckDB.setText(split[4]);
        }
    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llSaveLink.setOnClickListener(this);
        tvCheckDB.setOnClickListener(this);
        tvQueryDB.setOnClickListener(this);
        llChangePwd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        flMoreLink.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        allSP = (Map<String, String>) SpUtil.getAll(context);
        list = new ArrayList<>();
        for (Map.Entry<String, String> entry : allSP.entrySet()) {
            Log.e("LinkActivity", entry.getKey());
            if (entry.getKey().startsWith("URl#")) {
                String[] split = entry.getKey().split("#");
                list.add(split[1]);
            }
        }
        mSpinerPopWindow = new SpinerPopWindow<String>(this, list, itemClickListener);
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String URl = (String) SpUtil.get(context, "URl#" + list.get(position), "");
            Log.e("LoginActivity", URl);
            String[] split = URl.split("#");
            tvLinkName.setText(list.get(position));
            etServiceIP.setText(split[0]);
            etServiceHost.setText(split[1]);
            etServiceUsername.setText(split[2]);
            etServicePwd.setText(split[3]);
            tvCheckDB.setText(split[4]);
            mSpinerPopWindow.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_Delete:
                if (!TextUtils.isEmpty(tvLinkName.getText().toString().trim())) {
                    new CommomDialog(context, R.style.dialog, "确定删除当前链接", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, final boolean confirm) {
                            if (confirm) {
                                String lastURL = (String) SpUtil.get(context, "lastURL", "");
                                if (tvLinkName.getText().toString().trim().equals(lastURL)) {
                                    Log.e("LinkActivity", "与上次登录内容相同");
                                    SpUtil.remove(context, "lastURL");
                                    SpUtil.remove(context, "username");
                                    SpUtil.remove(context, "pwd");
                                }
                                SpUtil.remove(context, "URl#" + tvLinkName.getText().toString().trim());
                                EasyToast.showShort(context, "已清除");
                                initData();
                                tvLinkName.setText("");
                                etServiceIP.setText("");
                                etServiceHost.setText("");
                                etServiceUsername.setText("");
                                etServicePwd.setText("");
                                tvCheckDB.setText("");


                            }
                            dialog.dismiss();
                        }
                    }).setTitle("提示").show();
                }
                break;
            case R.id.fl_More_Link:
                if (list.isEmpty()) {
                    CommomDialog.showMessage(context, "暂未添加链接数据库");
                    return;
                }
                mSpinerPopWindow.showAsDropDown(tvLinkName);
                break;
            case R.id.fl_back:
                finish();
                break;
            case R.id.ll_Save_Link:
                saveLink();
                break;
            case R.id.tv_query_DB:
            case R.id.tv_check_DB:
                queryDB();
                break;
            case R.id.ll_change_pwd:
                llDoChangePwd.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:
                appPwd = etAppPwd.getText().toString().trim();
                appPwdAgain = etAppPwdAgain.getText().toString().trim();

                if (TextUtils.isEmpty(appPwd)) {
                    CommomDialog.showMessage(context, "请输入密码");
                    return;
                }

                if (TextUtils.isEmpty(appPwdAgain)) {
                    CommomDialog.showMessage(context, "请再次输入密码");
                    return;
                }

                if (!appPwd.equals(appPwdAgain)) {
                    CommomDialog.showMessage(context, "两次输入密码不一致");
                    return;
                }

                SpUtil.putAndApply(context, "Linkpwd", appPwd);

                EasyToast.showShort(context, "链接密码已修改");

                finish();
                break;
            default:
                break;
        }
    }

    /*
     *校验链接登录信息
     */
    private boolean checkMessage() {
        linkName = tvLinkName.getText().toString().trim();
        serviceIP = etServiceIP.getText().toString().trim();
        serviceHost = etServiceHost.getText().toString().trim();
        serviceUsername = etServiceUsername.getText().toString().trim();
        servicePwd = etServicePwd.getText().toString().trim();

        if (TextUtils.isEmpty(linkName)) {
            CommomDialog.showMessage(context, "请输入连接名称");
            return false;
        }

        if (TextUtils.isEmpty(serviceIP)) {
            CommomDialog.showMessage(context, "请输入连接地址");
            return false;
        }


        if (TextUtils.isEmpty(serviceHost)) {
            CommomDialog.showMessage(context, "请输入连接端口");
            return false;
        }

        if (TextUtils.isEmpty(serviceUsername)) {
            CommomDialog.showMessage(context, "请输入登录名称");
            return false;
        }

        if (TextUtils.isEmpty(servicePwd)) {
            CommomDialog.showMessage(context, "请输入登录登录密码");
            return false;
        }

        return true;
    }

    /*
     * 保存服务器连接配置
     * */
    private void saveLink() {
        if (checkMessage()) {
            checkDB = tvCheckDB.getText().toString().trim();
            if (TextUtils.isEmpty(checkDB)) {
                CommomDialog.showMessage(context, "请选择链接数据库");
                return;
            }
        }
        Map<String, String> allSP = (Map<String, String>) SpUtil.getAll(context);
        for (Map.Entry<String, String> entry : allSP.entrySet()) {
            Log.e("LinkActivity", entry.getKey());
            String s = "URl#" + linkName;
            Log.e("LinkActivity", s);
            if ((s).equals(entry.getKey())) {
                CommomDialog.showMessage(context, "连接名称已存在");
                return;
            }
        }
        SpUtil.putAndApply(context, "URl#" + linkName, serviceIP + "#" + serviceHost + "#" + serviceUsername + "#" + servicePwd + "#" + checkDB);
        EasyToast.showShort(context, "连接保存成功");
        finish();
    }

    /*
     * 获取服务器连接
     * */
    private void queryDB() {
        if (checkMessage()) {
            dialogLoading.show();
            App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
                @Override
                public void doSth() {
                    Log.e("LinkActivity", "开始链接");
                    UrlUtils.checkUrl(serviceIP, serviceHost, serviceUsername, servicePwd);
                    Connection conn = DBOpenHelper.getConn();
                    try {
                        Log.e("LinkActivity", "链接开始");
                        if (!conn.isClosed()) {
                            Statement stmt = conn.createStatement();// 创建SQL命令对象
                            ResultSet resultSet = stmt.executeQuery("SELECT NAME FROM SYSDATABASES\n" +
                                    " WHERE NAME <> 'MASTER'\n" +
                                    "   AND NAME <> 'MODEL'\n" +
                                    "   AND NAME <> 'MSDB'\n" +
                                    "   AND NAME <> 'NORTHWIND'\n" +
                                    "   AND NAME <> 'TEMPDB'\n" +
                                    "   AND NAME <> 'PUBS'\n" +
                                    " ORDER BY NAME");
                            // 循环输出每一条记录
                            final ArrayList<String> DBName = new ArrayList<>();
                            while (resultSet.next()) {
                                // 输出每个字段
                                Log.e("LinkActivity", resultSet.getString("NAME"));
                                DBName.add(resultSet.getString("NAME"));
                            }
                            Log.e("LinkActivity", "读取完毕");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SinglePicker<String> picker = new SinglePicker<>(LinkActivity.this, DBName);
                                    picker.setCanLoop(true);//不禁用循环
                                    picker.setTopBackgroundColor(0xFFEEEEEE);
                                    picker.setTopHeight(50);
                                    picker.setTopLineColor(0xFF33B5E5);
                                    picker.setTopLineHeight(1);
                                    picker.setTitleText("请选择");
                                    picker.setTitleTextColor(0xFF999999);
                                    picker.setTitleTextSize(12);
                                    picker.setCancelTextColor(0xFF33B5E5);
                                    picker.setCancelTextSize(13);
                                    picker.setSubmitTextColor(0xFF33B5E5);
                                    picker.setSubmitTextSize(13);
                                    picker.setSelectedTextColor(0xFFEE0000);
                                    picker.setUnSelectedTextColor(0xFF999999);
                                    picker.setWheelModeEnable(false);
                                    LineConfig config = new LineConfig();
                                    config.setColor(Color.BLUE);//线颜色
                                    config.setAlpha(120);//线透明度
                                    picker.setLineConfig(config);
                                    picker.setItemWidth(200);
                                    picker.setBackgroundColor(0xFFE1E1E1);
                                    picker.setOnItemPickListener(new OnItemPickListener<String>() {
                                        @Override
                                        public void onItemPicked(int index, String item) {
                                            EasyToast.showShort(context, item);
                                            tvCheckDB.setText(item);
                                        }
                                    });
                                    picker.show();
                                    EasyToast.showShort(context, "获取成功");
                                    dialogLoading.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommomDialog.showMessage(context, "链接失败，请检查链接信息");
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e("LinkActivity", "链接失败");
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接失败，请检查链接信息");
                            }
                        });
                    }
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
