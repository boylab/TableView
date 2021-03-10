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
import com.boylab.tableview.protocol.ItemParams;
import com.boylab.tableview.protocol.ItemRow;
import com.boylab.tableview.view.CustomScrollView;
import com.boylab.tableview.view.TableView;

import java.util.ArrayList;

public class TableViewAdapter extends RecyclerView.Adapter<TableViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<? extends ItemRow> mTableDatas = new ArrayList<ItemRow>();
    private ItemParams leftParams, contentParams;

    private int focusRow = -1;
    private int focusColumn = -1;

    private OnTableViewCreatedListener mOnTableViewCreatedListener;
    private OnTableViewListener mTableViewListener;
    private OnTableViewRangeListener mTableViewRangeListener;

    /**
     * Item点击事件
     */
    private TableView.OnItemClickListenter mOnItemClickListenter;

    /**
     * Item长按事件
     */
    private TableView.OnItemLongClickListenter mOnItemLongClickListenter;

    public TableViewAdapter(Context context, ArrayList<? extends ItemRow> mTableDatas, ItemParams leftParams, ItemParams contentParams) {
        this.context = context;
        this.mTableDatas = mTableDatas;
        this.leftParams = leftParams;
        this.contentParams = contentParams;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        LeftColumnAdapter leftColumnAdapter = new LeftColumnAdapter(context, mTableDatas, leftParams);
        holder.rv_LeftColumn.setAdapter(leftColumnAdapter);

        final ContentAdapter contentAdapter = new ContentAdapter(context, mTableDatas, contentParams);
        contentAdapter.setFocusRow(focusRow);
        holder.rv_Content.setAdapter(contentAdapter);
        contentAdapter.setOnItemClickListenter(new TableView.OnItemClickListenter() {
            @Override
            public void onItemClick(View item, int mPosition) {
                contentAdapter.setFocusRow(mPosition);
                contentAdapter.notifyItemChanged(focusRow);
                focusRow = mPosition;
                contentAdapter.notifyItemChanged(focusRow);

                if (mOnItemClickListenter != null){
                    mOnItemClickListenter.onItemClick(item, mPosition);
                }
            }
        });
        contentAdapter.setOnItemLongClickListenter(new TableView.OnItemLongClickListenter(){

            @Override
            public void onItemLongClick(View item, int mPosition) {
                contentAdapter.setFocusRow(mPosition);
                contentAdapter.notifyItemChanged(focusRow);
                focusRow = mPosition;
                contentAdapter.notifyItemChanged(focusRow);
                if (mOnItemLongClickListenter != null){
                    mOnItemLongClickListenter.onItemLongClick(item, mPosition);
                }
            }
        });
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

    public void setFocusRow(int focusRow) {
        this.focusRow = focusRow;
    }

    public void setFocusColumn(int focusColumn) {
        this.focusColumn = focusColumn;
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

    public void setOnItemClickListenter(TableView.OnItemClickListenter mOnItemClickListenter) {
        this.mOnItemClickListenter = mOnItemClickListenter;
    }

    public void setOnItemLongClickListenter(TableView.OnItemLongClickListenter mOnItemLongClickListenter) {
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

}
