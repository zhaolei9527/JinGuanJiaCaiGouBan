package com.jinguanjiacaigouban.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hss01248.frescopicker.FrescoIniter;
import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.bean.proCsAddBean;
import com.jinguanjiacaigouban.bean.proPmInsertBean;
import com.jinguanjiacaigouban.bean.proPmSmBean;
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
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import me.iwf.photopicker.PhotoPickUtils;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/4
 * 功能描述：
 */
public class GoodShopEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_Save_GOODSHOP)
    LinearLayout llSaveGOODSHOP;
    @BindView(R.id.et_GHS)
    EditText etGHS;
    @BindView(R.id.img_selelte_gonghuoshang)
    ImageView imgSelelteGonghuoshang;
    @BindView(R.id.yiji)
    EditText yiji;
    @BindView(R.id.ll_pro_fl1)
    FrameLayout llProFl1;
    @BindView(R.id.erji)
    EditText erji;
    @BindView(R.id.ll_pro_fl2)
    FrameLayout llProFl2;
    @BindView(R.id.sanji)
    EditText sanji;
    @BindView(R.id.ll_pro_fl3)
    FrameLayout llProFl3;
    @BindView(R.id.tv_BH)
    TextView tvBH;
    @BindView(R.id.et_MC)
    EditText etMC;
    @BindView(R.id.tv_zxing)
    TextView tvZxing;
    @BindView(R.id.et_JM)
    EditText etJM;
    @BindView(R.id.et_TYM)
    EditText etTYM;
    @BindView(R.id.et_TM)
    EditText etTM;
    @BindView(R.id.cb_Check)
    CheckBox cbCheck;
    @BindView(R.id.tv_CGDJ)
    EditText tvCGDJ;
    @BindView(R.id.tv_YSDJ)
    EditText tvYSDJ;
    @BindView(R.id.tv_DDSL)
    EditText tvDDSL;
    @BindView(R.id.et_beizhu)
    EditText etBeizhu;
    @BindView(R.id.img_shop)
    ImageView imgShop;
    @BindView(R.id.ll_check_photo)
    LinearLayout llCheckPhoto;
    @BindView(R.id.ll_clear_photo)
    LinearLayout llClearPhoto;
    private Dialog dialog;
    private String type;
    private String strBH;
    public static BitmapDrawable bd;
    SpinerPopWindow<String> stringSpinerPopWindow;
    ArrayList<String> profl1list = new ArrayList<>();
    ArrayList<String> profl2list = new ArrayList<>();
    ArrayList<String> profl3list = new ArrayList<>();
    private String autoTM;
    private String pic = "";
    private String isclear = "";

    @Override
    protected int setthislayout() {
        return R.layout.activcity_goodshop_edit;
    }

    @Override
    protected void initview() {

        autoTM = (String) SpUtil.get(context, "autoTM", "0");
        if ("1".equals(autoTM)) {
            cbCheck.setChecked(true);
            etTM.setFocusable(false);
            etTM.setEnabled(false);
            etTM.setText("-1");
        } else {
            cbCheck.setChecked(false);
            etTM.setFocusable(true);
            etTM.setEnabled(true);
        }

        dialog = Utils.showLoadingDialog(context);
        type = getIntent().getStringExtra("type");
        strBH = getIntent().getStringExtra("strBH");

        if (!TextUtils.isEmpty(type)) {
            tvTitle.setText("商品_修改");
        } else {
            tvTitle.setText("商品_新增");
        }

        PhotoPickUtils.init(getApplicationContext(), new FrescoIniter());//第二个参数根据具体依赖库而定

    }

    boolean input = false;

    @Override
    protected void initListener() {
        llProFl1.setOnClickListener(this);
        llProFl2.setOnClickListener(this);
        llProFl3.setOnClickListener(this);
        flBack.setOnClickListener(this);
        tvZxing.setOnClickListener(this);
        llCheckPhoto.setOnClickListener(this);
        llClearPhoto.setOnClickListener(this);
        imgSelelteGonghuoshang.setOnClickListener(this);
        llSaveGOODSHOP.setOnClickListener(this);
        etMC.addTextChangedListener(new TextWatcher() {
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
                        getproPymData(editable.toString().trim(), etJM);
                    }
                }
            }
        });

        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SpUtil.putAndApply(context, "autoTM", "1");
                    etTM.setFocusable(false);
                    etTM.setEnabled(false);
                } else {
                    SpUtil.putAndApply(context, "autoTM", "0");
                    etTM.setFocusable(true);
                    etTM.setEnabled(true);
                    if (etTM.getText().toString().equals("-1")) {
                        etTM.setText("");
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
            getproPmAdd();
            input = true;
        }
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
            case R.id.fl_back:
                finish();
                break;
            case R.id.ll_check_photo:
                PhotoPickUtils.startPick().setPhotoCount(1).setShowCamera(true).start((GoodShopEditActivity) context, 505);
                break;
            case R.id.ll_clear_photo:
                pic = "";
                isclear = "1";
                imgShop.setVisibility(View.GONE);
                break;
            case R.id.img_selelte_gonghuoshang:
                startActivityForResult(new Intent(context, GongHuoShangActivity.class).putExtra("ischeck", true), 203);
                break;
            case R.id.tv_zxing:
                Acp.getInstance(context).
                        request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.VIBRATE
                                )
                                        .setDeniedMessage(getString(R.string.requstPerminssions))
                                        .build(),
                                new AcpListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onGranted() {
                                        BGAQRCodeUtil.setDebug(true);
                                        startActivityForResult(new Intent(context, ScanActivity.class), 201);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        CommomDialog.showMessage(context, getString(R.string.requstPerminssions));
                                        return;
                                    }
                                });
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
            case R.id.ll_Save_GOODSHOP:

                final String isIMAGE;

                if (TextUtils.isEmpty(pic)) {
                    isIMAGE = "0";
                } else if (!TextUtils.isEmpty(isclear)) {
                    isIMAGE = "1";
                } else {
                    isIMAGE = "1";
                }

                dialog.show();

                Log.e("GoodShopEditActivity", isIMAGE);
                if (checkMessage()) {
                    App.pausableThreadPoolExecutor.execute(new PriorityRunnable(0) {
                        @Override
                        public void doSth() {
                            try {
                                if (!TextUtils.isEmpty(type)) {
                                    getproPmUpData("pro_pm_update"
                                            , String.valueOf(SpUtil.get(context, "MC", ""))
                                            , (String) SpUtil.get(context, "androidIMEI", "")
                                            , UrlUtils.BBH
                                            , etGHS.getText().toString().trim()
                                            , yiji.getText().toString().trim()
                                            , erji.getText().toString().trim()
                                            , sanji.getText().toString().trim()
                                            , tvBH.getText().toString().trim()
                                            , etMC.getText().toString().trim()
                                            , etJM.getText().toString().trim()
                                            , etTYM.getText().toString().trim()
                                            , etTM.getText().toString().trim()
                                            , tvCGDJ.getText().toString().trim()
                                            , tvYSDJ.getText().toString().trim()
                                            , tvDDSL.getText().toString().trim()
                                            , etBeizhu.getText().toString().trim()
                                            , pic
                                            , isIMAGE);
                                } else {
                                    getproPmUpData("pro_pm_insert"
                                            , String.valueOf(SpUtil.get(context, "MC", ""))
                                            , (String) SpUtil.get(context, "androidIMEI", "")
                                            , UrlUtils.BBH
                                            , etGHS.getText().toString().trim()
                                            , yiji.getText().toString().trim()
                                            , erji.getText().toString().trim()
                                            , sanji.getText().toString().trim()
                                            , tvBH.getText().toString().trim()
                                            , etMC.getText().toString().trim()
                                            , etJM.getText().toString().trim()
                                            , etTYM.getText().toString().trim()
                                            , etTM.getText().toString().trim()
                                            , tvCGDJ.getText().toString().trim()
                                            , tvYSDJ.getText().toString().trim()
                                            , tvDDSL.getText().toString().trim()
                                            , etBeizhu.getText().toString().trim()
                                            , pic
                                            , isIMAGE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }
                    });
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

        String GHS = etGHS.getText().toString().trim();
        String Mc = etMC.getText().toString().trim();

        if (TextUtils.isEmpty(GHS)) {
            CommomDialog.showMessage(context, "请选择供货商");
            dialog.dismiss();
            return false;
        }

        if (TextUtils.isEmpty(Mc)) {
            CommomDialog.showMessage(context, "请输入名称信息");
            dialog.dismiss();
            return false;
        }

        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 201) {
            String result = data.getStringExtra("result");
            Log.e("GoodShopEditActivity", result);
            getProPmSmData(result);
        } else if (resultCode == 200) {
            String gonghuoshang = data.getStringExtra("gonghuoshang");
            String flmc1 = data.getStringExtra("FLMC1");
            String flmc2 = data.getStringExtra("FLMC2");
            String flmc3 = data.getStringExtra("FLMC3");
            etGHS.setText(gonghuoshang);
            yiji.setText(flmc1);
            erji.setText(flmc2);
            sanji.setText(flmc3);
        }

        PhotoPickUtils.onActivityResult(requestCode, resultCode, data, new PhotoPickUtils.PickHandler() {
            @Override
            public void onPickSuccess(final ArrayList<String> photos, int requestCode) {
                switch (requestCode) {
                    case 505:
                        isclear = "";
                        pic = photos.get(0);
                        Toast.makeText(GoodShopEditActivity.this, pic, Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeFile(pic);
                        bd = new BitmapDrawable(bitmap);
                        imgShop.setBackgroundDrawable(bd);
                        imgShop.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                Log.e("MyMessageActivity", photos.get(0));
            }

            @Override
            public void onPreviewBack(ArrayList<String> photos, int requestCode) {
            }

            @Override
            public void onPickFail(String error, int requestCode) {
            }

            @Override
            public void onPickCancle(int requestCode) {
            }
        });

    }

    public void getproPmUpData(final String host
            , final String strCZY
            , final String strSBID
            , final String strBBH
            , final String strCSMC
            , final String strFL1
            , final String strFL2
            , final String strFL3
            , final String strBH
            , final String strMC
            , final String strPYM
            , final String strTYM
            , final String strTM
            , final String intJHDJ
            , final String intXSDJ
            , final String intJHSL
            , final String strBZ
            , final String strIMAGE
            , final String isIMAGE
    ) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_pm_update = DBService.doConnectionIMG(host, strIMAGE, strCZY, strSBID, strBBH, strCSMC
                            , strFL1, strFL2, strFL3, strBH, strMC, strPYM, strTYM
                            , strTM, intJHDJ, intXSDJ, intJHSL, strBZ, isIMAGE
                    );

                    dialog.dismiss();
                    if (TextUtils.isEmpty(pro_pm_update)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proPmInsertBean> proPmInsertBeans = proPmInsertBean.arrayproPmInsertBeanFromData(pro_pm_update);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(proPmInsertBeans.get(0).getErr())) {
                                if (!TextUtils.isEmpty(type)) {
                                    EasyToast.showShort(context, "修改成功");
                                    finish();
                                } else {
                                    EasyToast.showShort(context, "添加成功");
                                    getproPmAdd();

                                    etMC.setText("");
                                    etJM.setText("");
                                    etTYM.setText("");
                                    etTM.setText("");
                                    tvCGDJ.setText("");
                                    tvYSDJ.setText("");
                                    tvDDSL.setText("");
                                    etBeizhu.setText("");
                                    pic = "";
                                    isclear = "";
                                    imgShop.setVisibility(View.GONE);

                                }

                            } else {
                                CommomDialog.showMessage(context, proPmInsertBeans.get(0).getErr());
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


    public void getProPmSmData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_pm_sm = DBService.doConnection("pro_pm_sm", key);
                    dialog.dismiss();
                    if (TextUtils.isEmpty(pro_pm_sm)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proPmSmBean> proPmSmBeans = proPmSmBean.arrayproPmSmBeanFromData(pro_pm_sm);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proPmSmBeans.get(0).getErr())) {
                                etMC.setText(proPmSmBeans.get(0).getMC());
                                etTM.setText(proPmSmBeans.get(0).getTM());
                                tvCGDJ.setText(proPmSmBeans.get(0).getDJ());
                            } else {
                                CommomDialog.showMessage(context, proPmSmBeans.get(0).getErr());
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

    public void getproPymData(final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    final String pro_pym = DBService.doConnection("pro_pym", key);
                    dialog.dismiss();
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
                                et.setText(proPymBeans.get(0).getPYM());
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

    private void getproPmAdd() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_add = DBService.doConnection("pro_pm_add");
                    dialog.dismiss();
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
                                tvBH.setText(proCsAddBeans.get(0).getBH());
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

                    final ResultSet pro_pm_edit = DBService.doConnectionForResultSet("pro_pm_edit", key);

                    if (null == pro_pm_edit) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    try {
                        Log.e("GoodShopEditActivity", "数据流转换");
                        while (pro_pm_edit.next()) {
                            Log.e("GoodShopEditActivity", "数据流寻找");

                            final String err = pro_pm_edit.getString("err");
                            final String col0 = pro_pm_edit.getString("col0");
                            final String col1 = pro_pm_edit.getString("col1");
                            final String col2 = pro_pm_edit.getString("col2");
                            final String col3 = pro_pm_edit.getString("col3");
                            final String col4 = pro_pm_edit.getString("col4");
                            final String col5 = pro_pm_edit.getString("col5");
                            final String col6 = pro_pm_edit.getString("col6");
                            final String col7 = pro_pm_edit.getString("col7");
                            final String col8 = pro_pm_edit.getString("col8");
                            final String col9 = pro_pm_edit.getString("col9");
                            final String col10 = pro_pm_edit.getString("col10");
                            final String col11 = pro_pm_edit.getString("col11");
                            final String col12 = pro_pm_edit.getString("col12");
                            final String col13 = pro_pm_edit.getString("col13");

                            InputStream in = pro_pm_edit.getBinaryStream("col14");

                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            bd = new BitmapDrawable(bitmap);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!TextUtils.isEmpty(err)) {
                                            CommomDialog.showMessage(context, err);
                                            return;
                                        }
                                        if (col0.equals("1")) {
                                            etMC.setFocusable(false);
                                            etMC.setEnabled(false);
                                            etJM.setEnabled(false);
                                            etJM.setFocusable(false);
                                            etGHS.setEnabled(false);
                                            etGHS.setFocusable(false);
                                        }
                                        etGHS.setText(col1);
                                        yiji.setText(col2);
                                        erji.setText(col3);
                                        sanji.setText(col4);
                                        tvBH.setText(col5);
                                        etMC.setText(col6);
                                        etJM.setText(col7);
                                        etTYM.setText(col8);
                                        etTM.setText(col9);
                                        tvCGDJ.setText(col10);
                                        tvYSDJ.setText(col11);
                                        tvDDSL.setText(col12);
                                        etBeizhu.setText(col13);

                                        if (bd != null) {
                                            imgShop.setVisibility(View.VISIBLE);
                                            imgShop.setBackgroundDrawable(bd);
                                            imgShop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(context, HDImageViewActivity.class));
                                                }
                                            });
                                        }
                                        dialog.dismiss();
                                        input = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        pro_pm_edit.close();
                    } catch (Exception e) {
                        Log.e("GoodShopEditActivity", "数据流转换失败");
                        e.printStackTrace();
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

    public void getproflData(final String host, final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection(host, key);
                    dialog.dismiss();
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
