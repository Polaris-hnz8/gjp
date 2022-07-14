package com.itcast.controller;

import com.itcast.view.AbstractMainFrame;

public class MainFrameController extends AbstractMainFrame {
    /**
     * 重写主窗体类的抽象方法
     * 打开账务管理对话框
     */
    public void ledgerMng(){
        //创建账务管理对话框的子类对象
        new LedgerMngController(this).setVisible(true);
    }
    /**
     * 重写主窗体类的抽象方法
     * 打开分类管理对话框
     */
    public void sortMng(){
        //创建分类对话框的子类对象
        new SortMngController(this).setVisible(true);

    }
}
