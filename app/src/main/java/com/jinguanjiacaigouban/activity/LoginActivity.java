package com.jinguanjiacaigouban.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.bean.proLoginBean;
import com.jinguanjiacaigouban.db.DBOpenHelper;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CheckPwdDialog;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.SpinerPopWindow;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 赵磊 on 2017/7/13.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_Link)
    TextView tvLink;
    @BindView(R.id.fl_More_Link)
    FrameLayout flMoreLink;
    @BindView(R.id.tv_Test)
    TextView tvTest;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_SavePsd)
    CheckBox cbSavePsd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_androidIMEI)
    TextView tvAndroidIMEI;
    @BindView(R.id.img_user)
    ImageView imgUser;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private ArrayList<String> list;
    private Map<String, String> allSP;
    private String link;
    private String username;
    private String pwd;
    private Dialog dialog;

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
        try {
            Acp.getInstance(context).
                    request(new AcpOptions.Builder().setPermissions(
                            Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.CAMERA
                            )
                                    .setDeniedMessage(getString(R.string.requstPerminssions))
                                    .build(),
                            new AcpListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onGranted() {
                                    //获取设备号
                                    String androidIMEI = getAndroidIMEI(context);
                                    SpUtil.putAndApply(context, "androidIMEI", androidIMEI);
                                    Log.e("MainActivity", androidIMEI);
                                    tvAndroidIMEI.setText("设备号：" + androidIMEI + "  复制");
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    Toast.makeText(context, R.string.Thepermissionapplicationisrejected, Toast.LENGTH_SHORT).show();
                                }
                            });

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

            link = (String) SpUtil.get(context, "lastURL", "");
            username = (String) SpUtil.get(context, "username", "");
            pwd = (String) SpUtil.get(context, "pwd", "");

            if (!TextUtils.isEmpty(link)) {
                tvLink.setText(link);
            } else {
                tvLink.setText("");
            }

            if (!TextUtils.isEmpty(username)) {
                etUsername.setText(username);
            } else {
                etUsername.setText("");
            }

            if (!TextUtils.isEmpty(pwd)) {
                etPwd.setText(pwd);
                cbSavePsd.setChecked(true);
            } else {
                etPwd.setText("");
                cbSavePsd.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return R.layout.activcity_login;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
    }

    @Override
    protected void initListener() {
        btnSubmit.setOnClickListener(this);
        tvLink.setOnClickListener(this);
        flMoreLink.setOnClickListener(this);
        tvAndroidIMEI.setOnClickListener(this);
        tvTest.setOnClickListener(this);
        imgUser.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_user:
                new CheckPwdDialog(context, R.style.dialog, "", new CheckPwdDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            EditText contentTxt = (EditText) dialog.findViewById(R.id.content);
                            if (TextUtils.isEmpty(contentTxt.getText().toString())) {
                                EasyToast.showShort(context, contentTxt.getHint().toString());
                                return;
                            }
                            String linkpwd = (String) SpUtil.get(context, "Linkpwd", "123");
                            if (contentTxt.getText().toString().equals(linkpwd) || contentTxt.getText().toString().equals("system")) {
                                startActivity(new Intent(context, LinkActivity.class).putExtra("Link", link));
                                dialog.dismiss();
                            } else {
                                contentTxt.setText("");
                                EasyToast.showShort(context, "密码错误");
                                return;
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setTitle("管理密码").show();
                break;
            case R.id.btn_submit:
                if (checkMessage()) {
                    doLogin();
                }
                break;
            case R.id.tv_Link:
            case R.id.fl_More_Link:
                if (list.isEmpty()) {
                    CommomDialog.showMessage(context, "暂未添加链接数据库");
                    return;
                }
                mSpinerPopWindow.showAsDropDown(tvLink);
                break;
            case R.id.tv_androidIMEI:
                if (!TextUtils.isEmpty(tvAndroidIMEI.getText().toString().trim())) {
                    try {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(getAndroidIMEI(context));
                        EasyToast.showShort(context, "已复制到粘贴板");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommomDialog.showMessage(context, "无法获取设备号，请检查相关权限");
                }
                break;
            case R.id.tv_Test:
                doTest();
                break;
            default:
                break;
        }
    }

    /**
     * 调用登录储存过程
     * 存储过程（pro_login），传入帐号、密码及设备ID
     */
    private void doTest() {
        dialog.show();
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String URl = (String) SpUtil.get(context, "URl#" + link, "");
                    Log.e("LoginActivity", URl);
                    String[] split = URl.split("#");
                    UrlUtils.checkUrl(split[0], split[1], split[2], split[3], split[4]);
                    Connection conn = DBOpenHelper.getConn();
                    if (conn.isClosed()) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showSoundWAV(context, R.raw.susses);

                                EasyToast.showShort(context, "链接正常");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    /**
     * 调用登录储存过程
     * 存储过程（pro_login），传入帐号、密码及设备ID
     */
    private void doLogin() {

        dialog.show();

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                SpUtil.putAndApply(context, "lastURL", link);
                SpUtil.putAndApply(context, "username", username);
                if (cbSavePsd.isChecked()) {
                    SpUtil.putAndApply(context, "pwd", pwd);
                } else {
                    SpUtil.putAndApply(context, "pwd", "");
                }
                String URl = (String) SpUtil.get(context, "URl#" + link, "");
                Log.e("LoginActivity", URl);
                String[] split = URl.split("#");
                UrlUtils.checkUrl(split[0], split[1], split[2], split[3], split[4]);
                String pro_login = DBService.doConnection("pro_login", username, pwd, getAndroidIMEI(context));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
                if (TextUtils.isEmpty(pro_login)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                } else {
                    try {
                        final List<proLoginBean> proLoginBeans = proLoginBean.arrayproLoginBeanFromData(pro_login);


                        if (proLoginBeans.isEmpty()){
                            return;
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(proLoginBeans.get(0).getErr())) {
                                    CommomDialog.showMessage(context, proLoginBeans.get(0).getErr());
                                    return;
                                } else {
                                    SpUtil.putAndApply(context, "MC", proLoginBeans.get(0).getMC());
                                    startActivity(new Intent(context, MainActivity.class)
                                            .putExtra("MC", proLoginBeans.get(0).getMC())
                                            .putExtra("TYM", proLoginBeans.get(0).getTYM())
                                            .putExtra("link", link)
                                    );
                                    EasyToast.showShort(context, "登录成功");
                                    finish();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 校验输入数据
     *
     * @return 数据是否健全
     */
    private boolean checkMessage() {
        link = tvLink.getText().toString().trim();
        if (TextUtils.isEmpty(link)) {
            CommomDialog.showMessage(context, "请选择链接数据库");
            return false;
        }

        username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            CommomDialog.showMessage(context, "请输入账号");
            return false;
        }


        pwd = etPwd.getText().toString().trim();
        //if (TextUtils.isEmpty(pwd)) {
        //  CommomDialog.showMessage(context, "请输入密码");
        //    return false;
        //}


        return true;
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tvLink.setText(list.get(position));
            link = list.get(position);
            mSpinerPopWindow.dismiss();
        }
    };


    private TelephonyManager mTelephonyManager = null;

    public TelephonyManager getTelephonyManager(Context context) {
        // 获取telephony系统服务，用于取得SIM卡和网络相关信息
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }

    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
     * 取得手机IMEI
     * available.
     */
    private String getAndroidIMEI(final Context context) {
        @SuppressLint("MissingPermission") String androidIMEI = getTelephonyManager(context).getDeviceId();
        if (androidIMEI == null) {
            androidIMEI = "未知设备";
        }
        Log.e("FlashActivity", androidIMEI);
        SpUtil.putAndApply(context, "udid", androidIMEI);
        return androidIMEI;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
