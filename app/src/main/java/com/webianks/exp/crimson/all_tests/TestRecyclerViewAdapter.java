package com.webianks.exp.crimson.all_tests;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webianks.exp.crimson.R;

import java.util.List;



public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.VH>{


    private Context context;
    private List<TestNames> list;
    RecyclerClickListener listener;
    String colors [] ={"#2196F3","#4CAF50","#009688","#9C27B0","#03A9F4","#673AB7"};

    public TestRecyclerViewAdapter(Context context, List<TestNames> list){

        this.context = context;
        this.list = list;

    }

    public void setRecyclerViewListener(RecyclerClickListener listener){
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.single_test, parent, false);
        VH viewHolder = new VH(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        holder.testName.setText(list.get(position).getNames());
        holder.background.setBackgroundColor(Color.parseColor(colors[position]));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView testName;
        RelativeLayout background;

        public VH(View itemView) {
            super(itemView);

             testName = (TextView) itemView.findViewById(R.id.text);
             background = (RelativeLayout) itemView.findViewById(R.id.background);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            listener.onClick(list.get(getAdapterPosition()).getNames(),getAdapterPosition());
        }

    }




    public interface RecyclerClickListener{

        void onClick(String name, int index);

    }
}
