package com.boylab.example.bean;

import com.boylab.tableview.protocol.ItemRow;

import java.util.ArrayList;
import java.util.List;

public class StudentLabel implements ItemRow {

    private final List<String> label = new ArrayList<String>(){{
        add("序号");
        add("姓名");
        add("年龄");
        add("性别");
        add("身高");
        add("体重");
        add("语文");
        add("数学");
        add("英语");
        add("语文老师");
        add("数学老师");
        add("英语老师");
        add("备注");
    }};

    @Override
    public int size() {
        return 13;
    }

    @Override
    public String get(int position) {
        if (position < size()){
            return label.get(position);
        }
        return "N/A";
    }
}
