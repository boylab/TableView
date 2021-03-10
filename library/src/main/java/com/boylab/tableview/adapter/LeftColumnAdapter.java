package com.boylab.tableview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemParams;
import com.boylab.tableview.protocol.ItemRow;

import java.util.ArrayList;

public class LeftColumnAdapter extends RecyclerView.Adapter<LeftColumnAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<? extends ItemRow> mTableDatas = new ArrayList<ItemRow>();
    private ItemParams leftParams;

    public LeftColumnAdapter(Context context, ArrayList<? extends ItemRow> mTableDatas, ItemParams leftParams) {
        this.context = context;
        this.mTableDatas = mTableDatas;
        this.leftParams = leftParams;
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

            ViewGroup.LayoutParams layoutParams = text_item.getLayoutParams();
            layoutParams.height = leftParams.getHeight();
            layoutParams.width = leftParams.getWidth(0);
            text_item.setLayoutParams(layoutParams);

            text_item.setTextSize(leftParams.getTextSize());
            text_item.setTextColor(leftParams.getTextColor());
            text_item.setBackgroundColor(leftParams.getBackgroundColor());
            text_item.setPadding(leftParams.getPaddingLeft(), leftParams.getPaddingLeft(), leftParams.getPaddingLeft(), leftParams.getPaddingLeft());

            text_item.setGravity(leftParams.getItemGravity().getGravity());
        }
    }
}
