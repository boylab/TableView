package com.boylab.tableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.adapter.HeaderAdapter;
import com.boylab.tableview.adapter.TableViewAdapter;
import com.boylab.tableview.protocol.ItemGravity;
import com.boylab.tableview.protocol.ItemParams;
import com.boylab.tableview.protocol.ItemRow;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private ItemRow headRow;
    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    /**
     * 参数
     */
    private ItemParams headParams, leftParams, contentParams;
    private int divider = getResources().getColor(android.R.color.holo_red_dark);
    private HashMap<Integer, Integer> itemWidth = new HashMap<>();
    private int itemHeight = ItemParams.HEIGHT;

    public TableView(Context context) {
        this(context, null, 0);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseRes(context, attrs);
        initView(context);
    }

    private void parseRes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            headParams = new ItemParams(getResources().getColor(android.R.color.holo_blue_light));
            leftParams = new ItemParams(getResources().getColor(android.R.color.holo_blue_light));
            contentParams = new ItemParams(getResources().getColor(android.R.color.white));
        } else {
            headParams = new ItemParams();
            leftParams = new ItemParams();
            contentParams = new ItemParams();
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TableView);

            CharSequence[] charWith = typedArray.getTextArray(R.styleable.TableView_itemWidth);
            if (charWith != null) {
                List<CharSequence> withList = Arrays.asList(charWith);
                for (int i = 0; i < withList.size(); i++) {
                    itemWidth.put(i, Integer.getInteger(String.valueOf(withList.get(i))));
                }
            }
            itemHeight = typedArray.getInt(R.styleable.TableView_itemHeight, ItemParams.HEIGHT);
            headParams.setHeight(itemHeight);
            headParams.setItemWidth(itemWidth);
            leftParams.setHeight(itemHeight);
            leftParams.setItemWidth(itemWidth);
            contentParams.setHeight(itemHeight);
            contentParams.setItemWidth(itemWidth);

            divider = typedArray.getColor(R.styleable.TableView_dividerColor, getResources().getColor(android.R.color.darker_gray));
            int foucsColor = typedArray.getColor(R.styleable.TableView_foucsColor, getResources().getColor(android.R.color.holo_blue_bright));
            headParams.setFoucsColor(foucsColor);
            leftParams.setFoucsColor(foucsColor);
            contentParams.setFoucsColor(foucsColor);

            int headTextSize = typedArray.getInt(R.styleable.TableView_headTextSize, ItemParams.TEXT_SIZE);
            int headTextColor = typedArray.getColor(R.styleable.TableView_headTextColor, getResources().getColor(android.R.color.black));
            int headBackgroundColor = typedArray.getColor(R.styleable.TableView_headBackgroundColor, getResources().getColor(android.R.color.holo_blue_light));
            int headPaddingTop = typedArray.getInt(R.styleable.TableView_headPaddingTop, ItemParams.PADDING);
            int headPaddingLeft = typedArray.getInt(R.styleable.TableView_headPaddingLeft, ItemParams.PADDING);
            int headPaddingBottom = typedArray.getInt(R.styleable.TableView_headPaddingBottom, ItemParams.PADDING);
            int headPaddingRight = typedArray.getInt(R.styleable.TableView_headPaddingRight, ItemParams.PADDING);
            ItemGravity headGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_headGravity, ItemGravity.CENTER.ordinal()));

            headParams.setTextSize(headTextSize);
            headParams.setTextColor(headTextColor);
            headParams.setBackgroundColor(headBackgroundColor);
            headParams.setPaddingTop(headPaddingTop);
            headParams.setPaddingLeft(headPaddingLeft);
            headParams.setPaddingBottom(headPaddingBottom);
            headParams.setPaddingRight(headPaddingRight);
            headParams.setItemGravity(headGravity);

            int leftTextSize = typedArray.getInt(R.styleable.TableView_leftTextSize, ItemParams.TEXT_SIZE);
            int leftTextColor = typedArray.getColor(R.styleable.TableView_leftTextColor, getResources().getColor(android.R.color.black));
            int leftBackgroundColor = typedArray.getColor(R.styleable.TableView_leftBackgroundColor, getResources().getColor(android.R.color.holo_blue_bright));
            int leftPaddingTop = typedArray.getInt(R.styleable.TableView_leftPaddingTop, ItemParams.PADDING);
            int leftPaddingLeft = typedArray.getInt(R.styleable.TableView_leftPaddingLeft, ItemParams.PADDING);
            int leftPaddingBottom = typedArray.getInt(R.styleable.TableView_leftPaddingBottom, ItemParams.PADDING);
            int leftPaddingRight = typedArray.getInt(R.styleable.TableView_leftPaddingRight, ItemParams.PADDING);
            ItemGravity leftGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_leftGravity, ItemGravity.CENTER.ordinal()));

            leftParams.setTextSize(leftTextSize);
            leftParams.setTextColor(leftTextColor);
            leftParams.setBackgroundColor(leftBackgroundColor);
            leftParams.setPaddingTop(leftPaddingTop);
            leftParams.setPaddingLeft(leftPaddingLeft);
            leftParams.setPaddingBottom(leftPaddingBottom);
            leftParams.setPaddingRight(leftPaddingRight);
            leftParams.setItemGravity(leftGravity);

            int contentTextSize = typedArray.getInt(R.styleable.TableView_contentTextSize, ItemParams.TEXT_SIZE);
            int contentTextColor = typedArray.getColor(R.styleable.TableView_contentTextColor, getResources().getColor(android.R.color.black));
            int contentBackgroundColor = typedArray.getColor(R.styleable.TableView_contentBackgroundColor, getResources().getColor(android.R.color.white));
            int contentPaddingTop = typedArray.getInt(R.styleable.TableView_contentPaddingTop, ItemParams.PADDING);
            int contentPaddingLeft = typedArray.getInt(R.styleable.TableView_contentPaddingLeft, ItemParams.PADDING);
            int contentPaddingBottom = typedArray.getInt(R.styleable.TableView_contentPaddingBottom, ItemParams.PADDING);
            int contentPaddingRight = typedArray.getInt(R.styleable.TableView_contentPaddingRight, ItemParams.PADDING);
            ItemGravity contentGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_contentGravity, ItemGravity.CENTER.ordinal()));

            contentParams.setTextSize(contentTextSize);
            contentParams.setTextColor(contentTextColor);
            contentParams.setBackgroundColor(contentBackgroundColor);
            contentParams.setPaddingTop(contentPaddingTop);
            contentParams.setPaddingLeft(contentPaddingLeft);
            contentParams.setPaddingBottom(contentPaddingBottom);
            contentParams.setPaddingRight(contentPaddingRight);
            contentParams.setItemGravity(contentGravity);

            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.layout_tableview, this);
        setBackgroundColor(divider);

        text_Heading = inflate.findViewById(R.id.text_Heading);
        rv_HeadRow = inflate.findViewById(R.id.rv_HeadRow);
        refreshLayout = inflate.findViewById(R.id.refreshLayout);
        rv_TableView = inflate.findViewById(R.id.rv_TableView);

        setTextHeading();
        rv_HeadRow.setLayoutManager(LinearLayoutManager.HORIZONTAL);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);

        LinearLayoutManager headLayoutManager = new LinearLayoutManager(context);
        headLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_TableView.setLayoutManager(headLayoutManager);
    }

    private void setTextHeading() {
        ViewGroup.LayoutParams layoutParams = text_Heading.getLayoutParams();
        layoutParams.height = headParams.getHeight();
        layoutParams.width = headParams.getWidth(0);
        text_Heading.setLayoutParams(layoutParams);

        text_Heading.setTextSize(headParams.getTextSize());
        text_Heading.setTextColor(headParams.getTextColor());
        text_Heading.setBackgroundColor(headParams.getBackgroundColor());
        text_Heading.setPadding(headParams.getPaddingLeft(), headParams.getPaddingLeft(), headParams.getPaddingLeft(), headParams.getPaddingLeft());

        text_Heading.setGravity(headParams.getItemGravity().getGravity());
    }


    /**
     * 注意：headParams相关参数须提前设置
     *
     * @param headRow
     */
    public void setHeadRow(ItemRow headRow) {
        this.headRow = headRow;

        text_Heading.setText(headRow.get(0));
        HeaderAdapter headerAdapter = new HeaderAdapter(getContext(), headRow, headParams, false);
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

    /**
     * 注意：leftParams, contentParams相关参数须提前设置
     *
     * @param mTableDatas
     */
    public void setTableDatas(ArrayList<ItemRow> mTableDatas) {
        this.mTableDatas = mTableDatas;
        tableViewAdapter = new TableViewAdapter(getContext(), this.mTableDatas, leftParams, contentParams);
        tableViewAdapter.setOnTableViewCreatedListener(new TableViewAdapter.OnTableViewCreatedListener() {
            @Override
            public void onTableViewCreatedCompleted(CustomScrollView mScrollView) {
                mScrollViews.add(mScrollView);
            }
        });
        rv_TableView.setAdapter(tableViewAdapter);

        tableViewAdapter.setOnItemClickListenter(new OnItemClickListenter() {
            @Override
            public void onItemClick(View item, int position) {
                if (mOnItemClickListenter != null) {
                    mOnItemClickListenter.onItemClick(item, position);
                }
            }
        });

        tableViewAdapter.setOnItemLongClickListenter(new OnItemLongClickListenter() {
            @Override
            public void onItemLongClick(View item, int position) {
                if (mOnItemLongClickListenter != null) {
                    mOnItemLongClickListenter.onItemLongClick(item, position);
                }
            }
        });

        tableViewAdapter.setOnTableViewListener(new TableViewAdapter.OnTableViewListener() {
            @Override
            public void onTableScrollChange(int x, int y) {
                rv_HeadRow.scrollTo(x, y);
            }
        });
    }

    private OnItemClickListenter mOnItemClickListenter;
    private OnItemLongClickListenter mOnItemLongClickListenter;

    public void setOnItemClickListenter(final OnItemClickListenter mOnItemClickListenter) {
        this.mOnItemClickListenter = mOnItemClickListenter;
    }

    public void setOnItemLongClickListenter(OnItemLongClickListenter mOnItemLongClickListenter) {
        this.mOnItemLongClickListenter = mOnItemLongClickListenter;
    }

    public void notifyDataSetChanged() {
        tableViewAdapter.notifyDataSetChanged();
    }

    public void finishRefresh() {
        refreshLayout.finishRefresh(true);
    }

    public void finishLoadMore() {
        refreshLayout.finishLoadMore(true);
    }

    public void setOnRefreshListener(OnTableRefreshListener onRefreshListener) {
        this.mRefreshListener = onRefreshListener;
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                tableViewAdapter.setFocusRow(-1);
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh(TableView.this);
                }
            }
        });
        //调用 finishRefresh(boolean success);
    }

    public void setOnLoadMoreListener(OnTableLoadMoreListener onLoadMoreListener) {
        this.mLoadMoreListener = onLoadMoreListener;
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore(TableView.this);
                }
            }
        });
        //调用 finishRefresh(boolean success);
    }

    /**
     * 下拉刷新监听
     */
    public interface OnTableRefreshListener {

        void onRefresh(TableView mTableView);
    }

    /**
     * 上拉加载监听
     */
    public interface OnTableLoadMoreListener {

        void onLoadMore(TableView mTableView);
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
