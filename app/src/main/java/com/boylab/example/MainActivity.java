package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.boylab.example.bean.StudentLabel;
import com.boylab.example.bean.Student;
import com.boylab.tableview.protocol.ItemRow;
import com.boylab.tableview.view.TableView;

import java.util.ArrayList;

/**
 * 1、在.gradle文件添加库依赖
 *
 * 2、在.xml布局文件中添加
 *  <com.boylab.tableview.view.TableView
 *      android:id="@+id/tableview"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent"/>
 *
 * 3、创建一个Lable类，实现ItemRow接口
 * 调用tableview.setHeaderRow(new Label());
 *
 * 4、创建一个数据集合ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();
 * 要展示的数据实体类实现ItemRow接口
 * 调用tableview.setHeaderRow(new Label());
 *
 * 5、更新mTableDatas集合数据，
 * 调用tableview.notifyDataSetChanged();就可以刷新数据
 *
 **********************其他重要扩展功能******************************************
 *
 * 6、下拉刷新《一般少用》
 * tableview.setOnRefreshListener(OnRefreshListener onRefreshListener)
 *
 * 6、上拉加载
 * tableview.setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
 *
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<ItemRow> mTableDatas = new ArrayList<ItemRow>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 查询所得数据
         */
        for (int i = 0; i < 20; i++) {
            mTableDatas.add(new Student(i));
        }


        final TableView tableview = findViewById(R.id.tableview);
        tableview.setHeaderRow(new StudentLabel());
        tableview.setTableDatas(mTableDatas);

        tableview.setOnRefreshListener(new TableView.OnTableRefreshListener(){

            @Override
            public void onRefresh(TableView mTableView) {

                //数据更新
                mTableDatas.clear();
                for (int i = 0; i < 20; i++) {
                    mTableDatas.add(new Student(i));
                }

                //视图更新
                tableview.notifyDataSetChanged();

                //结束刷新
                mTableView.finishRefresh();
            }
        });

        tableview.setOnLoadMoreListener(new TableView.OnTableLoadMoreListener(){

            @Override
            public void onLoadMore(TableView mTableView) {

                //数据添加
                for (int i = 20; i < 20; i++) {
                    mTableDatas.add(new Student(i));
                }

                //视图更新
                tableview.notifyDataSetChanged();

                mTableView.finishLoadMore();
            }
        });

    }
}