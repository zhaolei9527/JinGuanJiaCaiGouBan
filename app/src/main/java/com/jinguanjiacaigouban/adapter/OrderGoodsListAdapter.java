package com.jinguanjiacaigouban.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.activity.OrderEditActivity;
import com.jinguanjiacaigouban.bean.proCsDeleteBean;
import com.jinguanjiacaigouban.bean.proDdInsertBean;
import com.jinguanjiacaigouban.bean.proDdPmBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.LastInputEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.wenguoyi.Adapter
 *
 * @author 赵磊
 * @date 2018/5/15
 * 功能描述：首页商品列表适配器，包括了头部，轮播，和列表
 */
public class OrderGoodsListAdapter extends RecyclerView.Adapter<OrderGoodsListAdapter.ViewHolder> {

    private Activity mContext;
    TextView tvCont;
    EditText etSearchHonghuoshang;
    ImageView imgSelelteGonghuoshang;
    boolean share = false;
    String strBH = "";


    private ArrayList<proDdPmBean> datas = new ArrayList();

    public ArrayList<proDdPmBean> getDatas() {
        return datas;
    }

    public OrderGoodsListAdapter(Activity context, List<proDdPmBean> homeBean) {
        this.mContext = context;
        share = true;
        datas.addAll(homeBean);
    }

    public OrderGoodsListAdapter(OrderEditActivity context, List<proDdPmBean> homeBean, TextView tvCont, EditText etSearchHonghuoshang, ImageView imgSelelteGonghuoshang) {
        this.mContext = context;
        this.etSearchHonghuoshang = etSearchHonghuoshang;
        this.imgSelelteGonghuoshang = imgSelelteGonghuoshang;
        this.tvCont = tvCont;
        datas.addAll(homeBean);
        this.strBH = OrderEditActivity.strBH;
    }

    public void setDatas(ArrayList<proDdPmBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_goods_layout, parent, false);
        ViewHolder vp = new ViewHolder(view);
        return vp;
    }

    boolean isdelete = false;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommomDialog(mContext, R.style.dialog, "确认删除该条信息？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, final boolean confirm) {
                        dialog.dismiss();
                        if (confirm) {
                            isdelete = true;
                            getDelete(position, OrderEditActivity.strBH, datas.get(position).getCol1(), (String) SpUtil.get(mContext, "androidIMEI", ""));
                        }
                    }
                }).setTitle("提示").show();
            }
        });

        holder.tvTitle.setText(datas.get(position).getCol1() + "." + datas.get(position).getCol2());
        holder.etDanjia.setText(Utils.subZeroAndDot(datas.get(position).getCol3() + ""));
        holder.etShuliang.setText(Utils.subZeroAndDot(datas.get(position).getCol4() + ""));
        holder.etBeizhu.setText(Utils.subZeroAndDot(datas.get(position).getCol5() + ""));

        holder.etDanjia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {
                    try {
                        if (strBH.equals(OrderEditActivity.strBH) && !isdelete) {
                            datas.get(position).setCol3(holder.etDanjia.getText().toString());
                            getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                                    , (String) SpUtil.get(mContext, "androidIMEI", "")
                                    , UrlUtils.BBH
                                    , OrderEditActivity.strBH
                                    , String.valueOf(datas.get(position).getCol1())
                                    , String.valueOf(datas.get(position).getCol2())
                                    , String.valueOf(datas.get(position).getCol4())
                                    , String.valueOf(datas.get(position).getCol3())
                                    , String.valueOf(datas.get(position).getCol5()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        holder.etShuliang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {
                    try {
                        if (strBH.equals(OrderEditActivity.strBH) && !isdelete) {
                            datas.get(position).setCol4(holder.etShuliang.getText().toString());
                            getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                                    , (String) SpUtil.get(mContext, "androidIMEI", "")
                                    , UrlUtils.BBH
                                    , OrderEditActivity.strBH
                                    , String.valueOf(datas.get(position).getCol1())
                                    , String.valueOf(datas.get(position).getCol2())
                                    , String.valueOf(datas.get(position).getCol4())
                                    , String.valueOf(datas.get(position).getCol3())
                                    , String.valueOf(datas.get(position).getCol5()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                }

            }
        });

        holder.etBeizhu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    try {
                        if (strBH.equals(OrderEditActivity.strBH) && !isdelete) {
                            datas.get(position).setCol5(holder.etBeizhu.getText().toString());
                            getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                                    , (String) SpUtil.get(mContext, "androidIMEI", "")
                                    , UrlUtils.BBH
                                    , OrderEditActivity.strBH
                                    , datas.get(position).getCol1() + ""
                                    , datas.get(position).getCol2() + ""
                                    , String.valueOf(datas.get(position).getCol4())
                                    , String.valueOf(datas.get(position).getCol3())
                                    , String.valueOf(datas.get(position).getCol5()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (share == true) {
            holder.etBeizhu.setFocusable(false);
            holder.etBeizhu.setEnabled(false);
            holder.etShuliang.setFocusable(false);
            holder.etShuliang.setEnabled(false);
            holder.etDanjia.setFocusable(false);
            holder.etDanjia.setEnabled(false);
            holder.btnDelete.setVisibility(View.GONE);
        }

    }

    public void getDelete(final int position, final String strBH, final String strXH, final String androidIMEI) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(500);
                                isdelete = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();


                    String pro_cs_delete = DBService.doConnection("pro_dd_pm_delete", (String) SpUtil.get(mContext, "MC", ""), androidIMEI, UrlUtils.BBH, strBH, strXH);
                    if (TextUtils.isEmpty(pro_cs_delete)) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                                return;
                            }
                        });

                    } else {
                        List<proCsDeleteBean> proCsDeleteBeans = proCsDeleteBean.arrayproCsDeleteBeanFromData(pro_cs_delete);

                        if (proCsDeleteBeans.isEmpty()){
                            return;
                        }


                        final String err = proCsDeleteBeans.get(0).getErr();
                        if (TextUtils.isEmpty(err)) {
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    EasyToast.showShort(mContext, "删除成功");
                                    datas.remove(position);
                                    notifyDataSetChanged();
                                    tvCont.setText("总计：" + datas.size());

                                    if (datas.size() == 0) {
                                        etSearchHonghuoshang.setFocusable(true);
                                        imgSelelteGonghuoshang.setVisibility(View.VISIBLE);
                                    } else {
                                        etSearchHonghuoshang.setFocusable(false);
                                        imgSelelteGonghuoshang.setVisibility(View.GONE);
                                    }


                                }
                            });
                        } else {
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommomDialog.showMessage(mContext, err);
                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    private void getProDdPmUpdate(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_dd_insert = DBService.doConnection("pro_dd_pm_update", key[0], key[1], key[2], key[3], key[4], key[5], key[6], key[7], key[8]);
                    if (TextUtils.isEmpty(pro_dd_insert)) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proDdInsertBean> proDdInsertBeans = proDdInsertBean.arrayproDdInsertBeanFromData(pro_dd_insert);

                    if (proDdInsertBeans.isEmpty()){
                        return;
                    }


                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(proDdInsertBeans.get(0).getErr())) {
                                EasyToast.showShort(mContext, "保存成功");
                            } else {
                                CommomDialog.showMessage(mContext, proDdInsertBeans.get(0).getErr());
                            }

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_danjia)
        TextView tvDanjia;
        @BindView(R.id.et_danjia)
        LastInputEditText etDanjia;
        @BindView(R.id.tv_shuliang)
        TextView tvShuliang;
        @BindView(R.id.et_shuliang)
        LastInputEditText etShuliang;
        @BindView(R.id.tv_beizhu)
        TextView tvBeizhu;
        @BindView(R.id.et_beizhu)
        LastInputEditText etBeizhu;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;
        @BindView(R.id.btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
