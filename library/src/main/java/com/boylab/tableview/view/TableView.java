package com.boylab.tableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;
import com.boylab.tableview.adapter.HeadAdapter;
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
import java.util.Set;

public class TableView extends RelativeLayout {

    /**
     * 视图结构
     */
    private LinearLayout linear_Heading;
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
    private ArrayList<? extends ItemRow> mTableDatas = new ArrayList<ItemRow>();

    /**
     * 参数
     */
    private HashMap<Integer, Integer> itemWidth = new HashMap<>();
    private float itemHeight = ItemParams.HEIGHT;
    private int divider = getResources().getColor(android.R.color.holo_red_dark);
    private ItemParams headParams, leftParams, contentParams;

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

            int resourceId = typedArray.getResourceId(R.styleable.TableView_itemRowWidth, 0);
            if (resourceId != 0){
                int[] intArray = getResources().getIntArray(resourceId);
                if (intArray != null) {
                    for (int i = 0; i < intArray.length; i++) {
                        itemWidth.put(i, intArray[i]);
                    }
                }
            }

            itemHeight = typedArray.getDimension(R.styleable.TableView_itemRowHeight, ItemParams.HEIGHT);
            headParams.setHeight((int) itemHeight);
            headParams.setItemWidth(itemWidth);
            leftParams.setHeight((int) itemHeight);
            leftParams.setItemWidth(itemWidth);
            contentParams.setHeight((int) itemHeight);
            contentParams.setItemWidth(itemWidth);

            divider = typedArray.getColor(R.styleable.TableView_dividerColor, getResources().getColor(android.R.color.darker_gray));
            int foucsColor = typedArray.getColor(R.styleable.TableView_foucsColor, getResources().getColor(android.R.color.holo_blue_bright));
            headParams.setFoucsColor(foucsColor);
            leftParams.setFoucsColor(foucsColor);
            contentParams.setFoucsColor(foucsColor);

            float headTextSize = typedArray.getDimension(R.styleable.TableView_headTextSize, ItemParams.TEXT_SIZE);
            int headTextColor = typedArray.getColor(R.styleable.TableView_headTextColor, getResources().getColor(android.R.color.black));
            int headBackgroundColor = typedArray.getColor(R.styleable.TableView_headBackgroundColor, getResources().getColor(android.R.color.holo_blue_light));
            float headPaddingTop = typedArray.getDimension(R.styleable.TableView_headPaddingTop, ItemParams.PADDING);
            float headPaddingLeft = typedArray.getDimension(R.styleable.TableView_headPaddingLeft, ItemParams.PADDING);
            float headPaddingBottom = typedArray.getDimension(R.styleable.TableView_headPaddingBottom, ItemParams.PADDING);
            float headPaddingRight = typedArray.getDimension(R.styleable.TableView_headPaddingRight, ItemParams.PADDING);
            ItemGravity headGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_headGravity, ItemGravity.CENTER.ordinal()));

            headParams.setTextSize(headTextSize);
            headParams.setTextColor(headTextColor);
            headParams.setBackgroundColor(headBackgroundColor);
            headParams.setPaddingTop((int) headPaddingTop);
            headParams.setPaddingLeft((int) headPaddingLeft);
            headParams.setPaddingBottom((int) headPaddingBottom);
            headParams.setPaddingRight((int) headPaddingRight);
            headParams.setItemGravity(headGravity);

            float leftTextSize = typedArray.getDimension(R.styleable.TableView_leftTextSize, ItemParams.TEXT_SIZE);
            int leftTextColor = typedArray.getColor(R.styleable.TableView_leftTextColor, getResources().getColor(android.R.color.black));
            int leftBackgroundColor = typedArray.getColor(R.styleable.TableView_leftBackgroundColor, getResources().getColor(android.R.color.holo_blue_bright));
            float leftPaddingTop = typedArray.getDimension(R.styleable.TableView_leftPaddingTop, ItemParams.PADDING);
            float leftPaddingLeft = typedArray.getDimension(R.styleable.TableView_leftPaddingLeft, ItemParams.PADDING);
            float leftPaddingBottom = typedArray.getDimension(R.styleable.TableView_leftPaddingBottom, ItemParams.PADDING);
            float leftPaddingRight = typedArray.getDimension(R.styleable.TableView_leftPaddingRight, ItemParams.PADDING);
            ItemGravity leftGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_leftGravity, ItemGravity.CENTER.ordinal()));

            leftParams.setTextSize(leftTextSize);
            leftParams.setTextColor(leftTextColor);
            leftParams.setBackgroundColor(leftBackgroundColor);
            leftParams.setPaddingTop((int) leftPaddingTop);
            leftParams.setPaddingLeft((int) leftPaddingLeft);
            leftParams.setPaddingBottom((int) leftPaddingBottom);
            leftParams.setPaddingRight((int) leftPaddingRight);
            leftParams.setItemGravity(leftGravity);

            float contentTextSize = typedArray.getDimension(R.styleable.TableView_contentTextSize, ItemParams.TEXT_SIZE);
            int contentTextColor = typedArray.getColor(R.styleable.TableView_contentTextColor, getResources().getColor(android.R.color.black));
            int contentBackgroundColor = typedArray.getColor(R.styleable.TableView_contentBackgroundColor, getResources().getColor(android.R.color.white));
            float contentPaddingTop = typedArray.getDimension(R.styleable.TableView_contentPaddingTop, ItemParams.PADDING);
            float contentPaddingLeft = typedArray.getDimension(R.styleable.TableView_contentPaddingLeft, ItemParams.PADDING);
            float contentPaddingBottom = typedArray.getDimension(R.styleable.TableView_contentPaddingBottom, ItemParams.PADDING);
            float contentPaddingRight = typedArray.getDimension(R.styleable.TableView_contentPaddingRight, ItemParams.PADDING);
            ItemGravity contentGravity = ItemGravity.fromId(typedArray.getInt(R.styleable.TableView_contentGravity, ItemGravity.CENTER.ordinal()));

            contentParams.setTextSize(contentTextSize);
            contentParams.setTextColor(contentTextColor);
            contentParams.setBackgroundColor(contentBackgroundColor);
            contentParams.setPaddingTop((int) contentPaddingTop);
            contentParams.setPaddingLeft((int) contentPaddingLeft);
            contentParams.setPaddingBottom((int) contentPaddingBottom);
            contentParams.setPaddingRight((int) contentPaddingRight);
            contentParams.setItemGravity(contentGravity);

            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.layout_tableview, this);

        linear_Heading = inflate.findViewById(R.id.linear_Heading);
        text_Heading = inflate.findViewById(R.id.text_Heading);
        rv_HeadRow = inflate.findViewById(R.id.rv_HeadRow);
        refreshLayout = inflate.findViewById(R.id.refreshLayout);
        rv_TableView = inflate.findViewById(R.id.rv_TableView);

        linear_Heading.setBackgroundColor(divider);
        rv_HeadRow.setBackgroundColor(divider);
        rv_TableView.setBackgroundColor(divider);

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
        HeadAdapter headAdapter = new HeadAdapter(getContext(), headRow, headParams, false);
        rv_HeadRow.setAdapter(headAdapter);

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
    public void setTableDatas(ArrayList<? extends ItemRow> mTableDatas) {
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
                //Log.i(">>>>>>table", "onScrollChanged: x = "+x + "  >>>> y = "+y);
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


    public void setEnableRefresh(boolean enabled) {
        refreshLayout.setEnableRefresh(enabled);
    }

    public void setEnableLoadMore(boolean enabled) {
        refreshLayout.setEnableLoadMore(enabled);
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

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
        linear_Heading.setBackgroundColor(divider);
        rv_HeadRow.setBackgroundColor(divider);
        rv_TableView.setBackgroundColor(divider);
    }



    public int getItemHeight() {
        return (int) itemHeight;
    }

    public void setItemHeight(int itemHeight, HashMap<Integer, Integer> itemWidth) {
        this.itemHeight = itemHeight;

        Set<Integer> keySet = itemWidth.keySet();
        for(int key : keySet){
            this.itemWidth.put(key, itemWidth.get(key));
        }

        headParams.setHeight((int) this.itemHeight);
        leftParams.setHeight((int) this.itemHeight);
        contentParams.setHeight((int) this.itemHeight);

        headParams.setItemWidth(itemWidth);
        leftParams.setItemWidth(itemWidth);
        contentParams.setItemWidth(itemWidth);

        setTextHeading();
    }

    public int itemWidth(int column) {
        if (itemWidth.containsKey(column)){
            return itemWidth.get(column);
        }
        return ItemParams.HEIGHT;
    }

    public int getFoucsColor() {
        return contentParams.getFoucsColor();
    }

    public void setFoucsColor(int foucsColor) {
        headParams.setFoucsColor(foucsColor);
        leftParams.setFoucsColor(foucsColor);
        contentParams.setFoucsColor(foucsColor);
    }

    public float getHeadTextSize() {
        return headParams.getTextSize();
    }

    public void setHeadTextSize(int headTextSize) {
        headParams.setTextSize(headTextSize);
    }

    public int getHeadTextColor() {
        return headParams.getTextColor();
    }

    public void setHeadTextColor(int headTextColor) {
        headParams.setTextColor(headTextColor);
    }

    public int getHeadBackgroundColor() {
        return headParams.getBackgroundColor();
    }

    public void setHeadBackgroundColor(int headBackgroundColor) {
        headParams.setBackgroundColor(headBackgroundColor);
    }

    public int getHeadPaddingTop() {
        return headParams.getPaddingTop();
    }

    public int getHeadPaddingLeft() {
        return headParams.getPaddingLeft();
    }

    public int getHeadPaddingBottom() {
        return headParams.getPaddingBottom();
    }

    public int getHeadPaddingRight() {
        return headParams.getPaddingRight();
    }

    public void setHeadPadding(int top, int left, int bottom, int right) {
        headParams.setPaddingTop(top);
        headParams.setPaddingLeft(left);
        headParams.setPaddingBottom(bottom);
        headParams.setPaddingRight(right);
    }

    public ItemGravity getHeadGravity() {
        return headParams.getItemGravity();
    }

    public void setHeadGravity(ItemGravity headGravity) {
        headParams.setItemGravity(headGravity);
    }

    public float getLeftTextSize() {
        return leftParams.getTextSize();
    }

    public void setLeftTextSize(int leftTextSize) {
        leftParams.setTextSize(leftTextSize);
    }

    public int getLeftTextColor() {
        return leftParams.getTextColor();
    }

    public void setLeftTextColor(int leftTextColor) {
        leftParams.setTextColor(leftTextColor);
    }

    public int getLeftBackgroundColor() {
        return leftParams.getBackgroundColor();
    }

    public void setLeftBackgroundColor(int leftBackgroundColor) {
        leftParams.setBackgroundColor(leftBackgroundColor);
    }

    public int getLeftPaddingTop() {
        return leftParams.getPaddingTop();
    }

    public int getLeftPaddingLeft() {
        return leftParams.getPaddingLeft();
    }

    public int getLeftPaddingBottom() {
        return leftParams.getPaddingBottom();
    }

    public int getLeftPaddingRight() {
        return leftParams.getPaddingRight();
    }

    public void setLeftPadding(int top, int left, int bottom, int right) {
        leftParams.setPaddingTop(top);
        leftParams.setPaddingLeft(left);
        leftParams.setPaddingBottom(bottom);
        leftParams.setPaddingRight(right);
    }

    public ItemGravity getLeftGravity() {
        return leftParams.getItemGravity();
    }

    public void setLeftGravity(ItemGravity leftGravity) {
        leftParams.setItemGravity(leftGravity);
    }

    public float getContentTextSize() {
        return contentParams.getTextSize();
    }

    public void setContentTextSize(int contentTextSize) {
        contentParams.setTextSize(contentTextSize);

    }

    public int getContentTextColor() {
        return contentParams.getTextColor();
    }

    public void setContentTextColor(int contentTextColor) {
        contentParams.setTextColor(contentTextColor);

    }

    public int getContentBackgroundColor() {
        return contentParams.getBackgroundColor();
    }

    public void setContentBackgroundColor(int contentBackgroundColor) {
        contentParams.setBackgroundColor(contentBackgroundColor);
    }

    public int getContentPaddingTop() {
        return contentParams.getPaddingTop();
    }

    public int getContentPaddingLeft() {
        return contentParams.getPaddingLeft();
    }

    public int getContentPaddingBottom() {
        return contentParams.getPaddingBottom();
    }

    public int getContentPaddingRight() {
        return contentParams.getPaddingRight();
    }

    public void setContentPadding(int top, int left, int bottom, int right) {
        contentParams.setPaddingTop(top);
        contentParams.setPaddingLeft(left);
        contentParams.setPaddingBottom(bottom);
        contentParams.setPaddingRight(right);
    }

    public ItemGravity getContentGravity() {
        return contentParams.getItemGravity();
    }

    public void setContentGravity(ItemGravity contentGravity) {
        contentParams.setItemGravity(contentGravity);
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
