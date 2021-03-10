package com.boylab.tableview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.tableview.R;

public class CustomScrollView extends HorizontalScrollView {
    //触摸前的点
    private float x;

    //手势抬起之后的点
    private float x1;

    private onScrollChangeListener onScrollChangeListener;
    private RecyclerView rv_horizonView;
    private RecyclerView.Adapter adapter;

    public CustomScrollView(Context context) {
        this(context, null, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.layout_customscrollview, this);

        rv_horizonView = inflate.findViewById(R.id.rv_horizonView);
    }

    public void setLayoutManager(@RecyclerView.Orientation int orientation) {
        LinearLayoutManager headLayoutManager = new LinearLayoutManager(getContext());
        headLayoutManager.setOrientation(orientation);
        rv_horizonView.setLayoutManager(headLayoutManager);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        rv_horizonView.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public interface onScrollChangeListener {
        /**
         * 滚动监听
         *
         * @param scrollView
         * @param x
         * @param y
         */
        void onScrollChanged(HorizontalScrollView scrollView, int x, int y);

        /**
         * 滑动到最左侧
         *
         * @param scrollView
         */
        void onScrollFarLeft(HorizontalScrollView scrollView);

        /**
         * 滑动到最右侧
         *
         * @param scrollView
         */
        void onScrollFarRight(HorizontalScrollView scrollView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                x1 = ev.getX();
                if (computeHorizontalScrollOffset() == 0 && x - x1 < 0) {
                    //滑动最左边
                    if (onScrollChangeListener != null) {
                        onScrollChangeListener.onScrollFarLeft(this);
                    }
                } else if (computeHorizontalScrollRange() - computeHorizontalScrollOffset() <= computeHorizontalScrollExtent() && x - x1 > 0) {
                    //滑动最右边
                    if (onScrollChangeListener != null) {
                        onScrollChangeListener.onScrollFarRight(this);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置监听
     *
     * @param mOnScrollChangeListener
     */
    public void setOnScrollChangeListener(CustomScrollView.onScrollChangeListener mOnScrollChangeListener) {
        this.onScrollChangeListener = mOnScrollChangeListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //回调
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(this, l, t);
        }
    }

}
