package com.webianks.exp.crimson.tips;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webianks.exp.crimson.R;

import java.util.List;

public class SleepBetterRecyclerViewAdapter extends RecyclerView.Adapter<SleepBetterRecyclerViewAdapter.VH> {

    private Context context;
    private List<SleepBetterDetails> list;


    public SleepBetterRecyclerViewAdapter(Context context, List<SleepBetterDetails> list){

        this.context = context;
        this.list = list;

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.single_sleep_row, parent, false);
        SleepBetterRecyclerViewAdapter.VH viewHolder = new SleepBetterRecyclerViewAdapter.VH(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        holder.Sleep_title.setText(list.get(position).getSleep_title());
        holder.Sleep_Desc.setText(list.get(position).getSleep_desc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView Sleep_title , Sleep_Desc ;


        public VH(View itemView) {
            super(itemView);

            Sleep_title = (TextView) itemView.findViewById(R.id.sleep_title);
            Sleep_Desc =(TextView) itemView.findViewById(R.id.sleep_description);


        }


    }


}
