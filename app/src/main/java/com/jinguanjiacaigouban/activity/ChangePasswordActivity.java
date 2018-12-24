package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.bean.proPassBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;

import java.util.List;


/**
 * Created by 赵磊 on 2017/5/24.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout rl_back;
    private EditText et_oldpassword;
    private EditText et_newpassword;
    private EditText et_newpasswordagain;
    private Button btn_save;
    private String oldpassword;
    private String newpassword;
    private Dialog dialog;


    @Override
    protected int setthislayout() {
        return R.layout.change_password_layout;
    }

    @Override
    protected void initview() {
        rl_back = (FrameLayout) findViewById(R.id.rl_back);
        et_oldpassword = (EditText) findViewById(R.id.et_oldpassword);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_newpasswordagain = (EditText) findViewById(R.id.et_newpasswordagain);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void initListener() {
        btn_save.setOnClickListener(this);
        rl_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_save:
                submit();
            default:
                break;
        }
    }

    private void submit() {
        oldpassword = et_oldpassword.getText().toString().trim();
        newpassword = et_newpassword.getText().toString().trim();
        String newpasswordagain = et_newpasswordagain.getText().toString().trim();



        if (!newpassword.equals(newpasswordagain)) {
            EasyToast.showShort(context, "两次输入密码不一致");
            return;
        }

        if (Utils.isConnected(context)) {
            dialog = Utils.showLoadingDialog(context);
            if (!dialog.isShowing()) {
                dialog.show();
            }

            getData();

        } else {
            EasyToast.showShort(context, "网络未连接");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    public void getData() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_pass = DBService.doConnection("pro_pass"
                            , String.valueOf(SpUtil.get(context, "MC", ""))
                            , (String) SpUtil.get(context, "androidIMEI", "")
                            , et_oldpassword.getText().toString()
                            , et_newpassword.getText().toString()
                            , et_newpasswordagain.getText().toString()
                    );

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_pass)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proPassBean> proPassBeans = proPassBean.arrayproPassBeanFromData(pro_pass);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(proPassBeans.get(0).getErr())) {
                                EasyToast.showShort(context, "修改成功");
                                startActivity(new Intent(context, LoginActivity.class));
                                finish();
                            } else {
                                CommomDialog.showMessage(context, proPassBeans.get(0).getErr());
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }


}
