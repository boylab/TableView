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

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {

    private Context context;
    private ItemRow itemRow;

    public HeaderAdapter(Context context, ItemRow itemRow) {
        this.context = context;
        this.itemRow = itemRow;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_row_text, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_item.setText(itemRow.get(position));
    }

    @Override
    public int getItemCount() {
        return itemRow.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_item = itemView.findViewById(R.id.text_item);
        }
    }
}
