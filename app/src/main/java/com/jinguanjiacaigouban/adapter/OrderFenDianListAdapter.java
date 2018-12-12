package com.jinguanjiacaigouban.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.bean.proDdFdBean;

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
public class OrderFenDianListAdapter extends RecyclerView.Adapter<OrderFenDianListAdapter.ViewHolder> {

    private Activity mContext;

    private ArrayList<proDdFdBean> datas = new ArrayList();

    public ArrayList<proDdFdBean> getDatas() {
        return datas;
    }

    public OrderFenDianListAdapter(Activity context, List<proDdFdBean> homeBean) {
        this.mContext = context;
        datas.addAll(homeBean);
    }

    public void setDatas(ArrayList<proDdFdBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_fendian_layout, parent, false);
        ViewHolder vp = new ViewHolder(view);
        return vp;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(datas.get(position).getCol1());

        if (datas.get(position).getErr().equals("1")) {
            holder.cbCheck.setChecked(false);
        } else {
            holder.cbCheck.setChecked(true);
        }


        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    datas.get(position).setErr("");
                } else {
                    datas.get(position).setErr("1");
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
