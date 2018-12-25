package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.OrderListAdapter;
import com.jinguanjiacaigouban.bean.proCdBean;
import com.jinguanjiacaigouban.bean.proYgBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.DateUtils;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;
import com.jinguanjiacaigouban.view.SakuraLinearLayoutManager;
import com.jinguanjiacaigouban.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/11
 * 功能描述：
 */
public class OrderSearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.ll_Add)
    LinearLayout llAdd;
    @BindView(R.id.tv_search_start_time)
    TextView tvSearchStartTime;
    @BindView(R.id.tv_search_end_time)
    TextView tvSearchEndTime;
    @BindView(R.id.et_search_honghuoshang)
    EditText etSearchHonghuoshang;
    @BindView(R.id.img_selelte_gonghuoshang)
    ImageView imgSelelteGonghuoshang;
    @BindView(R.id.et_search_CGY)
    EditText etSearchCGY;
    @BindView(R.id.img_selelte_CGY)
    ImageView imgSelelteCGY;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.rv_order_list)
    JinGuanJiaRecycleView rvOrderList;
    @BindView(R.id.ll_start_time)
    LinearLayout llStartTime;
    @BindView(R.id.ll_end_time)
    LinearLayout llEndTime;
    private SakuraLinearLayoutManager line;
    private OrderListAdapter adapter;
    private TimePickerView pvTime;
    private long start = 0l;
    private long end = 0l;
    private Dialog dialog;
    SpinerPopWindow<String> stringSpinerPopWindow;
    private ArrayList<String> proYgList = new ArrayList<>();
    boolean input = false;
    public static String bh;


    @Override
    protected int setthislayout() {
        return R.layout.activity_ordersearch_layout;
    }

    @Override
    protected void initview() {
        line = new SakuraLinearLayoutManager(context);
        line.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderList.setLayoutManager(line);
        rvOrderList.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallRotate);
        progressView.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvOrderList.setFootLoadingView(progressView);
        rvOrderList.loadMoreComplete();
        rvOrderList.setCanloadMore(false);

        tvSearchStartTime.setText(DateUtils.getDay(System.currentTimeMillis()));
        tvSearchEndTime.setText(DateUtils.getDay(System.currentTimeMillis()));

        etSearchCGY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input) {
                    getDataFtime(tvSearchStartTime.getText().toString(), tvSearchEndTime.getText().toString(), etSearchHonghuoshang.getText().toString(), etSearchCGY.getText().toString());
                }
            }
        });

        etSearchHonghuoshang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input) {
                    getDataFtime(tvSearchStartTime.getText().toString(), tvSearchEndTime.getText().toString(), etSearchHonghuoshang.getText().toString(), etSearchCGY.getText().toString());
                }
            }
        });

    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        tvSearchEndTime.setOnClickListener(this);
        tvSearchStartTime.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        imgSelelteCGY.setOnClickListener(this);
        imgSelelteGonghuoshang.setOnClickListener(this);
        llEndTime.setOnClickListener(this);
        llStartTime.setOnClickListener(this);
        imgSelelteGonghuoshang.setOnClickListener(this);
        llAdd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        dialog = Utils.showLoadingDialog(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    private void getdata() {
        dialog.show();
        getDataFtime(tvSearchStartTime.getText().toString(), tvSearchEndTime.getText().toString(), etSearchHonghuoshang.getText().toString(), etSearchCGY.getText().toString());
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
            case R.id.ll_end_time:
                initTimePicker(getString(R.string.Theendtime), "end");
                pvTime.show();
                break;
            case R.id.ll_start_time:
                initTimePicker(getString(R.string.Thestarttime), "start");
                pvTime.show();
                break;
            case R.id.img_selelte_CGY:
                if (proYgList.isEmpty()) {
                    dialog.show();
                    getDataFCGY();
                } else {
                    showSponer(proYgList, etSearchCGY);
                }
                break;
            case R.id.img_selelte_gonghuoshang:
                startActivityForResult(new Intent(context, GongHuoShangActivity.class).putExtra("ischeck", true), 203);
                break;
            case R.id.ll_Add:
                startActivity(new Intent(context, OrderAddActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e( "OrderSearchActivity", "页面返回");

        if (resultCode == 200 && requestCode == 203) {
            etSearchHonghuoshang.setText(data.getStringExtra("gonghuoshang"));
        } else if (resultCode == 800 && requestCode == 800) {
            bh = data.getStringExtra("MC");
            Toast.makeText(context, bh, Toast.LENGTH_SHORT).show();
        }

    }

    private void initTimePicker(String title, final String TYPE) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2022, 12, 31);
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if ("start".equals(TYPE)) {
                    tvSearchStartTime.setText(DateUtils.getDay(date.getTime()));
                    start = date.getTime();
                } else {
                    end = date.getTime();
                    tvSearchEndTime.setText(DateUtils.getDay(date.getTime()));
                }
                getdata();
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setTitleBgColor(getResources().getColor(R.color.bgtitle))
                .setCancelColor(getResources().getColor(R.color.text))
                .setSubmitColor(getResources().getColor(R.color.text))
                .setTitleText(title)
                .setTitleColor(getResources().getColor(R.color.text))
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

    }

    public void getDataFtime(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    input = true;
                    String pro_cd = DBService.doConnection("pro_cd", String.valueOf(SpUtil.get(context, "MC", "")), key[0], key[1], key[2], key[3]);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cd)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proCdBean> proCdBeans = proCdBean.arrayproCdBeanFromData(pro_cd);
                    adapter = new OrderListAdapter(OrderSearchActivity.this, proCdBeans);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(bh)) {
                                for (int i = 0; i < adapter.getDatas().size(); i++) {
                                    if (adapter.getDatas().get(i).getBH().contains(bh)) {
                                        adapter.getDatas().get(i).setErr("1");
                                        rvOrderList.scrollToPosition(i);
                                        EasyToast.showShort(context, adapter.getDatas().get(i).getBH());
                                    }
                                }
                            }

                            rvOrderList.setAdapter(adapter);
                            tvCont.setText("总计：" + proCdBeans.size());
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
        });
    }

    public void getDataFCGY() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_yg = DBService.doConnection("pro_yg");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_yg)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    List<proYgBean> proYgBeans = proYgBean.arrayproYgBeanFromData(pro_yg);

                    proYgList = new ArrayList<>();

                    for (int i = 0; i < proYgBeans.size(); i++) {
                        proYgList.add(proYgBeans.get(i).getMC());
                    }

                    showSponer(proYgList, etSearchCGY);
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
        });
    }

    private void showSponer(final List<String> profl1list, final EditText et) {
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

}
