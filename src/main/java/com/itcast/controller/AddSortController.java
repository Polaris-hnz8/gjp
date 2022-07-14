package com.itcast.controller;

import com.itcast.domain.Sort;
import com.itcast.service.SortService;
import com.itcast.view.AbstractOperationSortDialog;

import javax.swing.*;

/**
 * 添加分类对话框分控制器
 */
public class AddSortController extends AbstractOperationSortDialog {
    public AddSortController(JDialog dialog) {
        super(dialog);
        titleLabel.setText("添加分类");
        super.setTitle("添加分裂");
    }

    /**
     * 添加确定按钮confirm方法
     * 实现步骤：
     * 1.做数据验证：
     * （1）验证分类选项
     * （2）验证分类名称
     * 2.将获取到的数据，封装成Sort对象,lid成员不需要设置值
     * 3.将Sort对象传递给services层处理
     * 4.services获取Sort对象后，对象传递到dao层
     * 5.dao层中，将Sort对象中的数据写入数据表中insert
     * 6.提示用户添加成功
     * 7.重新加载分类功能——SortMngController这个对话框的addSort方法中实现
     */
    @Override
    public void confirm() {
        //1.对添加功能的数据进行验证
        //首先获取分类下拉菜单用户所选择的值
        //getSelectedItem()获取下拉菜单中选择的内容转换成字符串
        String parent = parentBox.getSelectedItem().toString();
        //获取分类名称
        String sname = snameTxt.getText().trim();
        //获取分裂的描述
        String sdesc = sdescArea.getText();
        //验证用户选择的下拉菜单：选中的内容！=“请选择”就可以
        if("=请选择=".equals(parent)){
            //弹出一个对话框，选择错误
            JOptionPane.showMessageDialog(this,"请选择分类","错误提示",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //验证分类名称，不是空即可
        if(sname == null || sname.equals("")){
            JOptionPane.showMessageDialog(this,"请填写分类名称");
            return;
        }

        //2.将获取到的数据，封装成Sort对象
        Sort sort = new Sort();
        sort.setParent(parent);
        sort.setSname(sname);
        sort.setSdesc(sdesc);

        //3.将Sort对象传递给services层处理:调用service层的SortService方法addSort()传递Sort对象
        //(1)创建service层的SortService类的对象
        SortService sortService = new SortService();
        //(2)调用对象方法addSort传递封装好的Sort对象
        sortService.addSort(sort);

        //4.添加结果用户提示
        JOptionPane.showMessageDialog(this,"添加分类成功","操作成功",JOptionPane.PLAIN_MESSAGE);
        //关闭添加分类自己的窗体
        this.dispose();
    }

}
