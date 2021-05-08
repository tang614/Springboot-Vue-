package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel写操作
        //设置写入文件的地址和名称
        //String fileName = "F:\\1.xlsx";
        //调用方法实现写操作
        //EasyExcel.write(fileName,DemoData.class).sheet("学生列表").doWrite(getData());

        //实现excel读操作
        String file = "F:\\1.xlsx";
        EasyExcel.read(file,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    public static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("luck"+i);
            list.add(demoData);
        }
        return list;
    }
}
