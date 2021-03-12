package com.boylab.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemParams;
import com.boylab.tableview.protocol.ItemRow;
import com.boylab.tableview.view.TableView;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<? extends ItemRow> mTableDatas = new ArrayList<ItemRow>();
    private ItemParams contentParams;

    private int focusRow = -1;

    private TableView.OnItemClickListenter mOnItemClickListenter;
    private TableView.OnItemLongClickListenter mOnItemLongClickListenter;

    public ContentAdapter(Context context, ArrayList<? extends ItemRow> mTableDatas, ItemParams contentParams) {
        this.context = context;
        this.mTableDatas = mTableDatas;
        this.contentParams = contentParams;
    }

    public void setFocusRow(int focusRow) {
        this.focusRow = focusRow;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_row, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ItemRow itemRow = mTableDatas.get(position);

        boolean isFocus = (focusRow == position);
        final HeadAdapter headAdapter = new HeadAdapter(context, itemRow, contentParams, isFocus);
        headAdapter.setCanFocus(true);
        holder.rv_Table_Row.setAdapter(headAdapter);

        headAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewClick.isFastDoubleClick()){
                    return;
                }
                if (mOnItemClickListenter != null){
                    mOnItemClickListenter.onItemClick(holder.rv_Table_Row, position);
                }
            }
        });

        headAdapter.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListenter != null){
                    mOnItemLongClickListenter.onItemLongClick(holder.rv_Table_Row, position);
                }
                return true;
            }
        });

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

    public void setOnItemClickListenter(TableView.OnItemClickListenter mOnItemClickListenter) {
        this.mOnItemClickListenter = mOnItemClickListenter;
    }

    public void setOnItemLongClickListenter(TableView.OnItemLongClickListenter mOnItemLongClickListenter) {
        this.mOnItemLongClickListenter = mOnItemLongClickListenter;
    }
}
