package com.webianks.exp.crimson.reports;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.webianks.exp.crimson.R;

import java.util.List;


public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.VH> {

    private Context context;
    private List<AllReports> list;
    private OnItemClickListener onItemClickListener;


    public ReportsRecyclerViewAdapter(Context context, List<AllReports> list){

        this.context = context;
        this.list = list;

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.single_report_row, parent, false);
        ReportsRecyclerViewAdapter.VH viewHolder = new ReportsRecyclerViewAdapter.VH(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        Glide.with(context).load(list.get(position).getUrl()).into(holder.report);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView report;


        public VH(View itemView) {
            super(itemView);

            report = (ImageView) itemView.findViewById(R.id.eye_report);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {

            if (onItemClickListener!=null)
                onItemClickListener.itemClicked(getAdapterPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void itemClicked(int position);
    }


}
