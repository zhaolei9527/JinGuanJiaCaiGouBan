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
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.activity.GongHuoShangActivity;
import com.jinguanjiacaigouban.activity.GongHuoShangEditActivity;
import com.jinguanjiacaigouban.bean.proCsDeleteBean;
import com.jinguanjiacaigouban.bean.proCsSelectBean;
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
public class GongHuoShangListAdapter extends RecyclerView.Adapter<GongHuoShangListAdapter.ViewHolder> {

    private Activity mContext;
    private TextView tvCont;

    private ArrayList<proCsSelectBean> datas = new ArrayList();

    public ArrayList<proCsSelectBean> getDatas() {
        return datas;
    }

    public GongHuoShangListAdapter(Activity context, ArrayList<proCsSelectBean> homeBean, TextView tvCont) {
        this.mContext = context;
        this.tvCont = tvCont;
        datas.addAll(homeBean);
    }

    public void setDatas(ArrayList<proCsSelectBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gonghuoshang_layout, parent, false);
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

        if (((GongHuoShangActivity) mContext).isCheck) {
            holder.cbCheck.setEnabled(true);
        } else {
            holder.cbCheck.setEnabled(false);
        }

        if (datas.get(position).getErr().equals("1")) {
            holder.cbCheck.setEnabled(true);
            holder.cbCheck.setChecked(true);
        } else {
            holder.cbCheck.setChecked(false);
        }

        holder.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.setResult(200, new Intent()
                        .putExtra("FLMC1", datas.get(position).getFLMC1())
                        .putExtra("FLMC2", datas.get(position).getFLMC2())
                        .putExtra("FLMC3", datas.get(position).getFLMC3())
                        .putExtra("gonghuoshang", datas.get(position).getMC()));
                mContext.finish();
            }
        });

        holder.tvTitle.setText(datas.get(position).getXSNR());

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivityForResult(new Intent(mContext, GongHuoShangEditActivity.class)
                        .putExtra("strBH", datas.get(position).getBH())
                        .putExtra("type", "edit"), 800
                );
            }
        });

    }

    public void getData(final int position, final String strBH, final String strMC, final String androidIMEI) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_delete = DBService.doConnection("pro_cs_delete", (String) SpUtil.get(mContext, "MC", ""), androidIMEI, UrlUtils.BBH, strBH, strMC);
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
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
