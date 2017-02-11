package com.webianks.exp.crimson.facts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webianks.exp.crimson.R;

import java.util.List;

public class FactRecyclerViewAdapter extends RecyclerView.Adapter<FactRecyclerViewAdapter.VH> {

    private Context context;
    private List<AllFacts> list;


    public FactRecyclerViewAdapter(Context context, List<AllFacts> list){

        this.context = context;
        this.list = list;

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.single_fact_row, parent, false);
        FactRecyclerViewAdapter.VH viewHolder = new FactRecyclerViewAdapter.VH(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        holder.fact_title.setText(list.get(position).getFact());
        holder.source.setText(list.get(position).getSource());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView fact_title ;
        TextView source ;


        public VH(View itemView) {
            super(itemView);

            fact_title = (TextView) itemView.findViewById(R.id.fact_title);
            source = (TextView) itemView.findViewById(R.id.source);

        }

    }


}
