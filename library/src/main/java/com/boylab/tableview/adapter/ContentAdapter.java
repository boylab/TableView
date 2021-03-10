package com.boylab.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemRow;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    public ContentAdapter(Context context, ArrayList<ItemRow> mTableDatas) {
        this.context = context;
        this.mTableDatas = mTableDatas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_row, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemRow itemRow = mTableDatas.get(position);

        HeaderAdapter headerAdapter = new HeaderAdapter(context, itemRow);
        holder.rv_Table_Row.setAdapter(headerAdapter);

    }

    @Override
    public int getItemCount() {
        return mTableDatas.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_Table_Row;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_Table_Row = itemView.findViewById(R.id.rv_Table_Row);

            LinearLayoutManager headLayoutManager = new LinearLayoutManager(context);
            headLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_Table_Row.setLayoutManager(headLayoutManager);


        }
    }
}
