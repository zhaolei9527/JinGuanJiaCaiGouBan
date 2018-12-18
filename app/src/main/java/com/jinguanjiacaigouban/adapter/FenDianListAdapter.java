package com.jinguanjiacaigouban.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.activity.FenDianActivity;
import com.jinguanjiacaigouban.activity.FenDianEditActivity;
import com.jinguanjiacaigouban.bean.proCsDeleteBean;
import com.jinguanjiacaigouban.bean.proFdlxSelectBean;
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
 * 功能描述：首页商品列表适配器
 */
public class FenDianListAdapter extends RecyclerView.Adapter<FenDianListAdapter.ViewHolder> {

    private Activity mContext;

    private ArrayList<proFdlxSelectBean> datas = new ArrayList();

    public ArrayList<proFdlxSelectBean> getDatas() {
        return datas;
    }

    public FenDianListAdapter(Activity context, List<proFdlxSelectBean> homeBean) {
        this.mContext = context;
        datas.addAll(homeBean);
    }

    public void setDatas(ArrayList<proFdlxSelectBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fendian_layout, parent, false);
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
                            getData(position, datas.get(position).getBH(), datas.get(position).getMC(), (String) SpUtil.get(mContext, "androidIMEI", ""));
                        }
                    }
                }).setTitle("提示").show();
            }
        });

        if (((FenDianActivity) mContext).isCheck) {
            holder.cbCheck.setEnabled(true);
        } else {
            holder.cbCheck.setEnabled(false);
        }

        holder.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.setResult(204, new Intent().putExtra("MC", datas.get(position).getMC()).putExtra("BH", datas.get(position).getBH()));
                mContext.finish();

            }
        });

        holder.tvXSNR.setText(datas.get(position).getXSNR());

        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, FenDianEditActivity.class)
                        .putExtra("strBH", datas.get(position).getBH())
                        .putExtra("type", "edit")
                );
            }
        });

    }

    public void getData(final int position, final String strBH, final String strMC, final String androidIMEI) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_delete = DBService.doConnection("pro_fdlx_delete", (String) SpUtil.get(mContext, "MC", ""), androidIMEI, UrlUtils.BBH, strBH, strMC);
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
        @BindView(R.id.btn_delete)
        Button btnDelete;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
