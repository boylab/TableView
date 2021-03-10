package com.boylab.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemRow;

import java.util.ArrayList;

public class LeftColumnAdapter extends RecyclerView.Adapter<LeftColumnAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    public LeftColumnAdapter(Context context, ArrayList<ItemRow> mTableDatas) {
        this.context = context;
        this.mTableDatas = mTableDatas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_row_text, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemRow itemRow = mTableDatas.get(position);
        holder.text_item.setText(itemRow.get(0));
    }

    @Override
    public int getItemCount() {
        return mTableDatas.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_item = itemView.findViewById(R.id.text_item);
        }
    }
}
