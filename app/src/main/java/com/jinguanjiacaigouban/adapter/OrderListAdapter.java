package com.jinguanjiacaigouban.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.activity.OrderEditActivity;
import com.jinguanjiacaigouban.bean.proCdBean;
import com.jinguanjiacaigouban.bean.proCsDeleteBean;
import com.jinguanjiacaigouban.bean.proDdFdBean;
import com.jinguanjiacaigouban.bean.proDdPmBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
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
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Activity mContext;

    private ArrayList<proCdBean> datas = new ArrayList();

    public ArrayList<proCdBean> getDatas() {
        return datas;
    }

    public OrderListAdapter(Activity context, List<proCdBean> homeBean) {
        this.mContext = context;
        datas.addAll(homeBean);
    }

    public void setDatas(ArrayList<proCdBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_layout, parent, false);
        ViewHolder vp = new ViewHolder(view);
        return vp;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvXSNR.setText(datas.get(position).getXSRN());

        holder.flShowGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("OrderListAdapter", "position:" + position);
                proDdPm(holder, datas.get(position).getBH());
                proDdFd(holder, datas.get(position).getBH());
                if (datas.get(position).getErr().equals("")) {
                    holder.llOrderFendian.setVisibility(View.VISIBLE);
                    holder.llOrderGoods.setVisibility(View.VISIBLE);
                    datas.get(position).setErr("1");
                } else {
                    holder.llOrderFendian.setVisibility(View.GONE);
                    holder.llOrderGoods.setVisibility(View.GONE);
                    datas.get(position).setErr("");
                }
            }
        });

        if (datas.get(position).getErr().equals("")) {
            holder.llOrderFendian.setVisibility(View.GONE);
            holder.llOrderGoods.setVisibility(View.GONE);
        } else {
            holder.llOrderFendian.setVisibility(View.VISIBLE);
            holder.llOrderGoods.setVisibility(View.VISIBLE);
        }

        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, OrderEditActivity.class).putExtra("strBH", datas.get(position).getBH()));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommomDialog(mContext, R.style.dialog, "确认删除该条信息？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, final boolean confirm) {
                        dialog.dismiss();
                        if (confirm) {
                            getData(position, datas.get(position).getBH(), String.valueOf(SpUtil.get(mContext, "MC", "")), (String) SpUtil.get(mContext, "androidIMEI", ""));
                        }
                    }
                }).setTitle("提示").show();
            }
        });

    }


    public void getData(final int position, final String strBH, final String strMC, final String androidIMEI) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_delete = DBService.doConnection("pro_dd_delete", (String) SpUtil.get(mContext, "MC", ""), androidIMEI, UrlUtils.BBH, strBH);
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


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_Check)
        CheckBox cbCheck;
        @BindView(R.id.tv_XSNR)
        TextView tvXSNR;
        @BindView(R.id.img_show_goods)
        ImageView imgShowGoods;
        @BindView(R.id.fl_show_goods)
        FrameLayout flShowGoods;
        @BindView(R.id.ll_order_goods)
        LinearLayout llOrderGoods;
        @BindView(R.id.ll_order_fendian)
        GridLayout llOrderFendian;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;
        @BindView(R.id.btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void getproDdEdit(final String... key) {

        final Dialog dialog = Utils.showLoadingDialog(mContext);

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_dd_edit = DBService.doConnection("pro_dd_edit", key[0], key[1], key[2]);

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_edit)) {

                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    private void proDdPm(final ViewHolder holder, final String... key) {

        final Dialog dialog = Utils.showLoadingDialog(mContext);

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    final String pro_dd_pm = DBService.doConnection("pro_dd_pm", key[0]);

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_pm)) {

                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<proDdPmBean> proDdPmBeans = proDdPmBean.arrayproDdPmBeanFromData(pro_dd_pm);
                            holder.llOrderGoods.removeAllViews();
                            for (int i = 0; i < proDdPmBeans.size(); i++) {
                                View item_proddpm_layout = View.inflate(mContext, R.layout.item_proddpm_layout, null);
                                TextView tv_title = item_proddpm_layout.findViewById(R.id.tv_title);
                                TextView tv_danjia = item_proddpm_layout.findViewById(R.id.tv_danjia);
                                TextView tv_shuliang = item_proddpm_layout.findViewById(R.id.tv_shuliang);
                                TextView tv_beizhu = item_proddpm_layout.findViewById(R.id.tv_beizhu);
                                tv_title.setText(proDdPmBeans.get(i).getCol1() + "." + proDdPmBeans.get(i).getCol2());
                                tv_danjia.setText("单价：" + Utils.subZeroAndDot(proDdPmBeans.get(i).getCol3()));
                                tv_shuliang.setText("数量：" + Utils.subZeroAndDot(proDdPmBeans.get(i).getCol4()));
                                tv_beizhu.setText("备注：" + proDdPmBeans.get(i).getCol5());
                                holder.llOrderGoods.addView(item_proddpm_layout);
                                Log.e("OrderListAdapter", proDdPmBeans.get(i).getCol2());
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }


    private void proDdFd(final ViewHolder holder, final String... key) {

        final Dialog dialog = Utils.showLoadingDialog(mContext);

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    final String pro_dd_fd = DBService.doConnection("pro_dd_fd", key[0]);

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_fd)) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<proDdFdBean> proDdFdBeans = proDdFdBean.arrayproDdFdBeanFromData(pro_dd_fd);
                            holder.llOrderFendian.removeAllViews();
                            for (int i = 0; i < proDdFdBeans.size(); i++) {
                                View item_proddfd_layout = View.inflate(mContext, R.layout.item_proddfd_layout, null);
                                TextView tv_title = item_proddfd_layout.findViewById(R.id.tv_title);
                                tv_title.setText(proDdFdBeans.get(i).getCol1());
                                holder.llOrderFendian.addView(item_proddfd_layout);
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(mContext, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }


}
