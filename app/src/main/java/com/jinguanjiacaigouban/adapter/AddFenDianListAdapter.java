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
import com.jinguanjiacaigouban.bean.proFdFdlxBean;

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
public class AddFenDianListAdapter extends RecyclerView.Adapter<AddFenDianListAdapter.ViewHolder> {

    private Activity mContext;

    private ArrayList<proFdFdlxBean> datas = new ArrayList();

    public ArrayList<proFdFdlxBean> getDatas() {
        return datas;
    }

    public AddFenDianListAdapter(Activity context, List<proFdFdlxBean> homeBean) {
        this.mContext = context;
        datas.addAll(homeBean);
    }

    public void setDatas(ArrayList<proFdFdlxBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_fendian_layout, parent, false);
        ViewHolder vp = new ViewHolder(view);
        return vp;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.cbCheck.setVisibility(View.VISIBLE);

        holder.tvXSNR.setText(datas.get(position).getMC());

        if (datas.get(position).getErr().equals("1")) {
            holder.cbCheck.setChecked(true);
        } else {
            holder.cbCheck.setChecked(false);
        }

        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    datas.get(position).setErr("1");
                } else {
                    datas.get(position).setErr("0");
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
        TextView tvXSNR;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
