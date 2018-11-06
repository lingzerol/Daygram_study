package com.example.zero.daygram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{
    private List<String> date_list;
    private int type;
    private Context context;
    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView option;
        public ViewHolder(View itemView) {
            super(itemView);
            option=(TextView)itemView.findViewById(R.id.daygram_recycler_option);
        }
    }

    public TimeAdapter(List<String> date_list,int type,Context context) {
        this.date_list = date_list;
        this.type=type;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String temp=date_list.get(position);
        holder.option.setText(temp);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.time_recycler_option,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity temp=(MainActivity)context;
                int position=holder.getAdapterPosition();
                DAIRY t=null;
                int m=0,y=0;
                temp.save_item();
                try{
                    if(type==0){
                        t=temp.memory_operator.read_item_with_exception(temp.content_year,position);
                        m=position;
                        y=temp.content_year;
                    }else{
                        t=temp.memory_operator.read_item_with_exception(1970+position,temp.content_month);
                        m=temp.content_month;
                        y=1970+position;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
             temp.refresh(t,y,m);
            }
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return date_list.size();
    }

}