package com.jinguanjiacaigouban.adapter;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.jinguanjiacaigouban.view.CommomDialog;

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

    private OrderEditActivity mContext;

    private ArrayList<proDdPmBean> datas = new ArrayList();

    public ArrayList<proDdPmBean> getDatas() {
        return datas;
    }

    public OrderGoodsListAdapter(OrderEditActivity context, List<proDdPmBean> homeBean) {
        this.mContext = context;
        datas.addAll(homeBean);
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
                            getDelete(position, OrderEditActivity.strBH, datas.get(position).getCol1(), (String) SpUtil.get(mContext, "androidIMEI", ""));
                        }
                    }
                }).setTitle("提示").show();
            }
        });

        holder.tvTitle.setText("编号：" + datas.get(position).getCol1() + " 品名：" + datas.get(position).getCol2());
        holder.etDanjia.setText(datas.get(position).getCol3()+"");
        holder.etShuliang.setText(datas.get(position).getCol4()+"");
        holder.etBeizhu.setText(datas.get(position).getCol5()+"");

        holder.etDanjia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                datas.get(position).setCol3(editable.toString());
                getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                        , (String) SpUtil.get(mContext, "androidIMEI", "")
                        , UrlUtils.BBH
                        , OrderEditActivity.strBH
                        , datas.get(position).getCol1()+""
                        , datas.get(position).getCol2()+""
                        , datas.get(position).getCol3()+""
                        , datas.get(position).getCol4()+""
                        , datas.get(position).getCol5()+"");
            }
        });

        holder.etShuliang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                datas.get(position).setCol4(editable.toString());
                getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                        , (String) SpUtil.get(mContext, "androidIMEI", "")
                        , UrlUtils.BBH
                        , OrderEditActivity.strBH
                        , datas.get(position).getCol1()+""
                        , datas.get(position).getCol2()+""
                        , datas.get(position).getCol3()+""
                        , datas.get(position).getCol4()+""
                        , datas.get(position).getCol5()+"");
            }
        });

        holder.etBeizhu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                datas.get(position).setCol5(editable.toString());
                getProDdPmUpdate(String.valueOf(SpUtil.get(mContext, "MC", ""))
                        , (String) SpUtil.get(mContext, "androidIMEI", "")
                        , UrlUtils.BBH
                        , OrderEditActivity.strBH
                        , datas.get(position).getCol1()+""
                        , datas.get(position).getCol2()+""
                        , datas.get(position).getCol3()+""
                        , datas.get(position).getCol4()+""
                        , datas.get(position).getCol5()+"");
            }
        });
    }

    public void getDelete(final int position, final String strBH, final String strXH, final String androidIMEI) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
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
                        final String err = proCsDeleteBeans.get(0).getErr();
                        if (TextUtils.isEmpty(err)) {
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    EasyToast.showShort(mContext, "删除成功");
                                    datas.remove(position);
                                    notifyDataSetChanged();
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
        EditText etDanjia;
        @BindView(R.id.tv_shuliang)
        TextView tvShuliang;
        @BindView(R.id.et_shuliang)
        EditText etShuliang;
        @BindView(R.id.tv_beizhu)
        TextView tvBeizhu;
        @BindView(R.id.et_beizhu)
        EditText etBeizhu;
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
