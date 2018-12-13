package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.bean.proCsAddBean;
import com.jinguanjiacaigouban.bean.proCsEditBean;
import com.jinguanjiacaigouban.bean.proCsInsertBean;
import com.jinguanjiacaigouban.bean.proCslxBean;
import com.jinguanjiacaigouban.bean.proPymBean;
import com.jinguanjiacaigouban.bean.profl1Bean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/11/21
 * 功能描述：
 */
public class GongHuoShangEditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.fl_back)
    FrameLayout fl_back;
    @BindView(R.id.ll_Save_GongHuoShang)
    LinearLayout llSaveGongHuoShang;
    @BindView(R.id.tv_BH)
    TextView tv_BH;
    @BindView(R.id.et_TYM)
    EditText et_TYM;
    @BindView(R.id.et_MC)
    EditText et_MC;
    @BindView(R.id.et_JM)
    EditText et_JM;
    @BindView(R.id.et_Phone)
    EditText et_Phone;
    @BindView(R.id.ll_pro_fl1)
    FrameLayout ll_pro_fl1;
    @BindView(R.id.ll_pro_fl2)
    FrameLayout ll_pro_fl2;
    @BindView(R.id.ll_pro_fl3)
    FrameLayout ll_pro_fl3;
    @BindView(R.id.et_beizhu)
    EditText et_beizhu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.yiji)
    EditText yiji;
    @BindView(R.id.erji)
    EditText erji;
    @BindView(R.id.sanji)
    EditText sanji;
    @BindView(R.id.fl_address)
    FrameLayout flAddress;
    private String type;
    private String strBH;
    private Dialog dialog;
    SpinerPopWindow<String> stringSpinerPopWindow;
    ArrayList<String> profl1list = new ArrayList<>();
    ArrayList<String> profl2list = new ArrayList<>();
    ArrayList<String> profl3list = new ArrayList<>();
    ArrayList<String> addresslist = new ArrayList<>();

    @Override
    protected int setthislayout() {
        return R.layout.activcity_gonghuoshang_edit;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
        type = getIntent().getStringExtra("type");
        strBH = getIntent().getStringExtra("strBH");

        if (!TextUtils.isEmpty(type)) {
            tvTitle.setText("供货商_修改");
        } else {
            tvTitle.setText("供货商_新增");
        }
    }

    boolean input = false;

    @Override
    protected void initListener() {
        ll_pro_fl1.setOnClickListener(this);
        ll_pro_fl2.setOnClickListener(this);
        ll_pro_fl3.setOnClickListener(this);
        fl_back.setOnClickListener(this);
        llSaveGongHuoShang.setOnClickListener(this);
        flAddress.setOnClickListener(this);

        et_MC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input) {
                    if (!TextUtils.isEmpty(editable.toString().trim())) {
                        dialog.show();
                        getproPymData(editable.toString().trim(), et_JM);
                    }
                }
            }
        });

    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(strBH)) {
            dialog.show();
            getEditData(strBH);
        } else {
            dialog.show();
            getproCsAdd();
            input = true;
        }
    }

    private void getproCsAdd() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_add = DBService.doConnection("pro_cs_add");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_add)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proCsAddBean> proCsAddBeans = proCsAddBean.arrayproCsAddBeanFromData(pro_cs_add);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proCsAddBeans.get(0).getErr())) {
                                tv_BH.setText(proCsAddBeans.get(0).getBH());
                                Utils.showSoundWAV(context,R.raw.susses);

                            } else {
                                CommomDialog.showMessage(context, proCsAddBeans.get(0).getErr());
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


    public void getEditData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection("pro_cs_edit", key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<proCsEditBean> proCsEditBeans = proCsEditBean.arrayproCsEditBeanFromData(pro_cs_edit);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(proCsEditBeans.get(0).getErr())) {
                                CommomDialog.showMessage(context, proCsEditBeans.get(0).getErr());
                                return;
                            }

                            if (proCsEditBeans.get(0).getCol0().equals("1")) {
                                et_MC.setFocusable(false);
                                et_MC.setEnabled(false);
                                et_JM.setEnabled(false);
                                et_JM.setFocusable(false);
                            }

                            etAddress.setText(proCsEditBeans.get(0).getCol1());
                            tv_BH.setText(proCsEditBeans.get(0).getCol2());
                            et_TYM.setText(proCsEditBeans.get(0).getCol3());
                            et_MC.setText(proCsEditBeans.get(0).getCol4());
                            et_JM.setText(proCsEditBeans.get(0).getCol5());
                            et_Phone.setText(proCsEditBeans.get(0).getCol6());
                            yiji.setText(proCsEditBeans.get(0).getCol7());
                            erji.setText(proCsEditBeans.get(0).getCol8());
                            sanji.setText(proCsEditBeans.get(0).getCol9());
                            et_beizhu.setText(proCsEditBeans.get(0).getCol10());
                            input = true;
                            Utils.showSoundWAV(context,R.raw.susses);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_address:
                if (addresslist.isEmpty()) {
                    dialog.show();
                    getProCslxData(etAddress);
                } else {
                    showSponer(addresslist, etAddress);
                }
                break;
            case R.id.ll_Save_GongHuoShang:
                if (checkMessage()) {
                    if (!TextUtils.isEmpty(type)) {
                        getProCsData("pro_cs_update"
                                , String.valueOf(SpUtil.get(context, "MC", ""))
                                , (String) SpUtil.get(context, "androidIMEI", "")
                                , UrlUtils.BBH
                                , etAddress.getText().toString().trim()
                                , tv_BH.getText().toString().trim()
                                , et_TYM.getText().toString().trim()
                                , et_MC.getText().toString().trim()
                                , et_JM.getText().toString().trim()
                                , et_Phone.getText().toString().trim()
                                , yiji.getText().toString().trim()
                                , erji.getText().toString().trim()
                                , sanji.getText().toString().trim()
                                , et_beizhu.getText().toString().trim()
                        );
                    } else {
                        getProCsData("pro_cs_insert"
                                , String.valueOf(SpUtil.get(context, "MC", ""))
                                , (String) SpUtil.get(context, "androidIMEI", "")
                                , UrlUtils.BBH
                                , etAddress.getText().toString().trim()
                                , tv_BH.getText().toString().trim()
                                , et_TYM.getText().toString().trim()
                                , et_MC.getText().toString().trim()
                                , et_JM.getText().toString().trim()
                                , et_Phone.getText().toString().trim()
                                , yiji.getText().toString().trim()
                                , erji.getText().toString().trim()
                                , sanji.getText().toString().trim()
                                , et_beizhu.getText().toString().trim());
                    }
                }

                break;
            case R.id.fl_back:
                finish();
                break;
            case R.id.ll_pro_fl1:
                if (profl1list.isEmpty()) {
                    dialog.show();
                    getproflData("pro_fl1", "", yiji);
                } else {
                    showSponer(profl1list, yiji);
                }
                break;
            case R.id.ll_pro_fl2:
                if (profl2list.isEmpty()) {
                    dialog.show();
                    getproflData("pro_fl2", "", erji);
                } else {
                    showSponer(profl2list, erji);
                }
                break;
            case R.id.ll_pro_fl3:
                if (profl3list.isEmpty()) {
                    dialog.show();
                    getproflData("pro_fl3", "", sanji);
                } else {
                    showSponer(profl3list, sanji);
                }
                break;
            default:
                break;
        }
    }

    /*
     *校验保存必须录入信息
     */
    private boolean checkMessage() {

        String address = etAddress.getText().toString().trim();
        String Mc = et_MC.getText().toString().trim();

        if (TextUtils.isEmpty(address)) {
            CommomDialog.showMessage(context, "请输入区域信息");
            return false;
        }

        if (TextUtils.isEmpty(Mc)) {
            CommomDialog.showMessage(context, "请输入名称信息");
            return false;
        }

        return true;
    }

    public void getproflData(final String host, final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection(host, key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<profl1Bean> profl1Beans = profl1Bean.arrayprofl1BeanFromData(pro_cs_edit);

                    if ("pro_fl1".equals(host)) {
                        profl1list = new ArrayList<>();
                        for (int i = 0; i < profl1Beans.size(); i++) {
                            profl1list.add(profl1Beans.get(i).getMC());
                        }
                        showSponer(profl1list, et);
                    } else if ("pro_fl2".equals(host)) {
                        profl2list = new ArrayList<>();
                        for (int i = 0; i < profl1Beans.size(); i++) {
                            profl2list.add(profl1Beans.get(i).getMC());
                        }
                        showSponer(profl2list, et);
                    } else {
                        profl3list = new ArrayList<>();
                        for (int i = 0; i < profl1Beans.size(); i++) {
                            profl3list.add(profl1Beans.get(i).getMC());
                        }
                        showSponer(profl3list, et);
                    }
                    Utils.showSoundWAV(context,R.raw.susses);

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

    private void showSponer(final ArrayList<String> profl1list, final EditText et) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringSpinerPopWindow = new SpinerPopWindow<>(context, profl1list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!et.getText().toString().trim().equals(profl1list.get(i))) {
                            et.setText(profl1list.get(i));
                        }
                        stringSpinerPopWindow.dismiss();
                    }
                });
                stringSpinerPopWindow.showAsDropDown(et);
            }
        });
    }

    public void getproflSearchData(final String host, final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection(host, key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<profl1Bean> profl1Beans = profl1Bean.arrayprofl1BeanFromData(pro_cs_edit);

                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < profl1Beans.size(); i++) {
                        list.add(profl1Beans.get(i).getMC());
                    }
                    showSponer(list, et);

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

    public void getproPymData(final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    final String pro_pym = DBService.doConnection("pro_pym", key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_pym)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<proPymBean> proPymBeans = proPymBean.arrayproPymBeanFromData(pro_pym);
                            if (TextUtils.isEmpty(proPymBeans.get(0).getErr())) {
                                et_JM.setText(proPymBeans.get(0).getPYM());
                                Utils.showSoundWAV(context,R.raw.susses);

                            } else {
                                CommomDialog.showMessage(context, proPymBeans.get(0).getErr());
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

    /**
     * 操作员、设备ID、版本号、区域、编号、通用码、名称、简码、电话、主营分类、二分类、三分类、备注。
     */
    public void getProCsData(final String host, final String strCZY, final String strSBID, final String strBBH, final String strLX, final String strBH, final String strMC, final String strPYM, final String strTYM, final String strLXDH, final String strFL1, final String strFL2, final String strFL3, final String strBZ) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection(host, strCZY, strSBID, strBBH, strLX, strBH, strMC, strPYM, strTYM, strLXDH, strFL1, strFL2, strFL3, strBZ);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<proCsInsertBean> proCsInsertBeans = proCsInsertBean.arrayproCsInsertBeanFromData(pro_cs_edit);


                    if (TextUtils.isEmpty(proCsInsertBeans.get(0).getErr().toString().trim())) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                EasyToast.showShort(context, "操作成功");
                                Utils.showSoundWAV(context,R.raw.susses);

                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                CommomDialog.showMessage(context, proCsInsertBeans.get(0).getErr().toString().trim());
                            }
                        });
                    }
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

    public void getProCslxData(final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection("pro_cslx", "");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    List<proCslxBean> proCslxBeans = proCslxBean.arrayproCslxBeanFromData(pro_cs_edit);

                    addresslist = new ArrayList<>();
                    for (int i = 0; i < proCslxBeans.size(); i++) {
                        addresslist.add(proCslxBeans.get(i).getMC());
                    }
                    Utils.showSoundWAV(context,R.raw.susses);

                    showSponer(addresslist, et);

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}
