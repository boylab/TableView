package com.boylab.tableview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.adapter.HeaderAdapter;
import com.boylab.tableview.adapter.TableViewAdapter;
import com.boylab.tableview.protocol.ItemRow;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class TableView extends RelativeLayout {

    /**
     * 视图结构
     */
    private TextView text_Heading;
    private CustomScrollView rv_HeadRow;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_TableView;

    /**
     * 视图附加类
     */
    private TableViewAdapter tableViewAdapter;
    private ArrayList<HorizontalScrollView> mScrollViews = new ArrayList<HorizontalScrollView>();

    /**
     * 下拉刷新、上拉加载监听
     */
    private OnTableRefreshListener mRefreshListener;
    private OnTableLoadMoreListener mLoadMoreListener;

    /**
     * 数据结构
     */
    private ItemRow headerRow;
    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    /**
     * 参数
     */





    public TableView(Context context) {
        this(context, null, 0);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.layout_tableview, this);

        text_Heading = inflate.findViewById(R.id.text_Heading);
        rv_HeadRow = inflate.findViewById(R.id.rv_HeadRow);
        refreshLayout = inflate.findViewById(R.id.refreshLayout);
        rv_TableView = inflate.findViewById(R.id.rv_TableView);

        rv_HeadRow.setLayoutManager(LinearLayoutManager.HORIZONTAL);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);

        LinearLayoutManager headLayoutManager = new LinearLayoutManager(context);
        headLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_TableView.setLayoutManager(headLayoutManager);
    }

    public void setHeaderRow(ItemRow headerRow) {
        this.headerRow = headerRow;
        HeaderAdapter headerAdapter = new HeaderAdapter(getContext(), headerRow);
        rv_HeadRow.setAdapter(headerAdapter);

        rv_HeadRow.setOnScrollChangeListener(new CustomScrollView.onScrollChangeListener() {
            @Override
            public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                for (HorizontalScrollView mScrollView : mScrollViews) {
                    mScrollView.scrollTo(x, y);
                }
            }

            @Override
            public void onScrollFarLeft(HorizontalScrollView scrollView) {

            }

            @Override
            public void onScrollFarRight(HorizontalScrollView scrollView) {

            }
        });

    }

    public void setTableDatas(ArrayList<ItemRow> mTableDatas) {
        this.mTableDatas = mTableDatas;
        tableViewAdapter = new TableViewAdapter(getContext(), this.mTableDatas);
        tableViewAdapter.setOnTableViewCreatedListener(new TableViewAdapter.OnTableViewCreatedListener() {
            @Override
            public void onTableViewCreatedCompleted(CustomScrollView mScrollView) {
                Log.i(">>>>>>", "onTableViewCreatedCompleted: 回调创建");
                mScrollViews.add(mScrollView);
            }
        });


        rv_TableView.setAdapter(tableViewAdapter);

        tableViewAdapter.setOnTableViewListener(new TableViewAdapter.OnTableViewListener() {
            @Override
            public void onTableScrollChange(int x, int y) {
                rv_HeadRow.scrollTo(x, y);
            }
        });


    }

     public void notifyDataSetChanged() {
        tableViewAdapter.notifyDataSetChanged();
    }

    public void setEnableLoadMore(boolean enabled){
        //调用 finishRefresh(boolean success);
        refreshLayout.setEnableLoadMore(enabled);
    }

    public void finishRefresh(){
        refreshLayout.finishRefresh(true);
    }

    public void finishLoadMore(){
        refreshLayout.finishLoadMore(true);
    }

    public void setOnRefreshListener(OnTableRefreshListener onRefreshListener){
        this.mRefreshListener = onRefreshListener;
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mRefreshListener != null){
                    mRefreshListener.onRefresh(TableView.this);
                }
            }
        });
        //调用 finishRefresh(boolean success);
    }

    public void setOnLoadMoreListener(OnTableLoadMoreListener onLoadMoreListener){
        this.mLoadMoreListener = onLoadMoreListener;
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreListener != null){
                    mLoadMoreListener.onLoadMore(TableView.this);
                }
            }
        });
        //调用 finishRefresh(boolean success);
    }

    /**
     * 下拉刷新监听
     */
    public interface OnTableRefreshListener{

        void onRefresh(TableView tableView);
    }

    /**
     * 上拉加载监听
     */
    public interface OnTableLoadMoreListener{

        void onLoadMore(TableView tableView);
    }

}
