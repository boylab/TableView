package com.boylab.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.protocol.ItemRow;
import com.boylab.tableview.view.CustomScrollView;

import java.util.ArrayList;

public class TableViewAdapter extends RecyclerView.Adapter<TableViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    private OnTableViewCreatedListener mOnTableViewCreatedListener;
    private OnTableViewListener mTableViewListener;
    private OnTableViewRangeListener mTableViewRangeListener;

    /**
     * Item点击事件
     */
    private OnItemClickListenter mOnItemClickListenter;

    /**
     * Item长按事件
     */
    private OnItemLongClickListenter mOnItemLongClickListenter;

    public TableViewAdapter(Context context, ArrayList<ItemRow> mTableDatas) {
        this.context = context;
        this.mTableDatas = mTableDatas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_table_view, null);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);

        if (mOnTableViewCreatedListener != null) {
            mOnTableViewCreatedListener.onTableViewCreatedCompleted(myViewHolder.rv_Content);
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LeftColumnAdapter leftColumnAdapter = new LeftColumnAdapter(context, mTableDatas);
        holder.rv_LeftColumn.setAdapter(leftColumnAdapter);

        ContentAdapter contentAdapter = new ContentAdapter(context, mTableDatas);
        holder.rv_Content.setAdapter(contentAdapter);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rv_LeftColumn;
        private CustomScrollView rv_Content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rv_LeftColumn = itemView.findViewById(R.id.rv_LeftColumn);
            LinearLayoutManager headLayoutManager = new LinearLayoutManager(context);
            headLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_LeftColumn.setLayoutManager(headLayoutManager);

            rv_Content = itemView.findViewById(R.id.rv_Content);
            rv_Content.setLayoutManager(LinearLayoutManager.VERTICAL);
            rv_Content.setOnScrollChangeListener(new CustomScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    if (mTableViewListener != null) {
                        mTableViewListener.onTableScrollChange(x, y);
                    }
                }

                @Override
                public void onScrollFarLeft(HorizontalScrollView scrollView) {
                    if (mTableViewRangeListener != null) {
                        mTableViewRangeListener.onLeft(scrollView);
                    }
                }

                @Override
                public void onScrollFarRight(HorizontalScrollView scrollView) {
                    if (mTableViewRangeListener != null) {
                        mTableViewRangeListener.onRight(scrollView);
                    }
                }
            });

        }
    }

    public void setOnTableViewCreatedListener(OnTableViewCreatedListener mOnTableViewCreatedListener) {
        this.mOnTableViewCreatedListener = mOnTableViewCreatedListener;
    }

    public void setOnTableViewListener(OnTableViewListener onTableViewListener) {
        this.mTableViewListener = onTableViewListener;
    }

    public void setTableViewRangeListener(OnTableViewRangeListener mTableViewRangeListener) {
        this.mTableViewRangeListener = mTableViewRangeListener;
    }

    public void setOnItemClickListenter(OnItemClickListenter mOnItemClickListenter) {
        this.mOnItemClickListenter = mOnItemClickListenter;
    }

    public void setOnItemLongClickListenter(OnItemLongClickListenter mOnItemLongClickListenter) {
        this.mOnItemLongClickListenter = mOnItemLongClickListenter;
    }

    /**
     * 表格创建完成回调
     */
    public interface OnTableViewCreatedListener {
        /**
         * 返回当前横向滚动视图给上个界面
         */
        void onTableViewCreatedCompleted(CustomScrollView mScrollView);
    }

    /**
     * 横向滚动监听
     */
    public interface OnTableViewListener {
        /**
         * 滚动监听
         * @param x
         * @param y
         */
        void onTableScrollChange(int x, int y);
    }

    /**
     * 横向滚动视图滑动到边界的监听
     */
    public interface OnTableViewRangeListener {

        /**
         * 说明 最左侧
         * 作者 郭翰林
         * 创建时间 2017/12/14 下午4:45
         *
         * @param view
         */
        void onLeft(HorizontalScrollView view);

        /**
         * 说明 最右侧
         * 作者 郭翰林
         * 创建时间 2017/12/14 下午4:45
         *
         * @param view
         */
        void onRight(HorizontalScrollView view);

    }

    /**
     * 说明 Item点击事件
     * 作者 郭翰林
     * 创建时间 2018/2/2 下午4:50
     */
    public interface OnItemClickListenter {

        /**
         * @param item     点击项
         * @param position 点击位置
         */
        void onItemClick(View item, int position);
    }

    /**
     * 说明 Item长按事件
     * 作者 郭翰林
     * 创建时间 2018/2/2 下午4:50
     */
    public interface OnItemLongClickListenter {

        /**
         * @param item     点击项
         * @param position 点击位置
         */
        void onItemLongClick(View item, int position);
    }
}
