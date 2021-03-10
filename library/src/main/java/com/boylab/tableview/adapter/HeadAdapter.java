package com.boylab.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemParams;
import com.boylab.tableview.protocol.ItemRow;

public class HeadAdapter extends RecyclerView.Adapter<HeadAdapter.MyViewHolder> {

    private Context context;
    private ItemRow itemRow;
    private ItemParams headParams;

    private boolean isFocus = false;
    private boolean canFocus = false;

    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public HeadAdapter(Context context, ItemRow itemRow, ItemParams headParams, boolean isFocus) {
        this.context = context;
        this.itemRow = itemRow;
        this.headParams = headParams;
        this.isFocus = isFocus;
    }

    public void setCanFocus(boolean canFocus) {
        this.canFocus = canFocus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_row_text, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ViewGroup.LayoutParams layoutParams = holder.text_item.getLayoutParams();
        layoutParams.height = headParams.getHeight();
        layoutParams.width = headParams.getWidth(position);
        holder.text_item.setLayoutParams(layoutParams);

        holder.text_item.setText(itemRow.get(position + 1));

        holder.text_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canFocus){
                    holder.text_item.setBackgroundColor(headParams.getFoucsColor());
                }
                holder.text_item.setBackgroundColor(headParams.getFoucsColor());
                if (onClickListener != null){
                    onClickListener.onClick(holder.text_item);
                }
            }
        });

        holder.text_item.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (canFocus){
                    holder.text_item.setBackgroundColor(headParams.getFoucsColor());
                }
                if (onLongClickListener != null){
                    onLongClickListener.onLongClick(holder.text_item);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (itemRow.size() > 1){
            return itemRow.size() - 1;
        }
        return 0;
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_item = itemView.findViewById(R.id.text_item);

            text_item.setTextSize(headParams.getTextSize());
            text_item.setTextColor(headParams.getTextColor());
            text_item.setBackgroundColor(isFocus ? headParams.getFoucsColor() : headParams.getBackgroundColor());
            text_item.setPadding(headParams.getPaddingLeft(), headParams.getPaddingLeft(), headParams.getPaddingLeft(), headParams.getPaddingLeft());

            text_item.setGravity(headParams.getItemGravity().getGravity());
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
