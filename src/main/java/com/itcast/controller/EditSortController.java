package com.itcast.controller;

import com.itcast.domain.Sort;
import com.itcast.service.SortService;
import com.itcast.view.AbstractOperationSortDialog;

import javax.swing.*;

/**
 * 编辑分类的对话框
 */

public class EditSortController extends AbstractOperationSortDialog {
    private Sort sort;
    public EditSortController(JDialog dialog, Sort sort) {
        super(dialog);
        titleLabel.setText("编辑分类");
        super.setTitle("编辑分类");
        this.sort = sort;
        //获取Sort对象中的数据，填充到对话框中
        //将Sort对象中封装的分类，填充到下拉菜单中
        //setSelectedIndex将菜单中已有的项目作为默认项出现
        parentBox.setSelectedItem(sort.getParent());
        snameTxt.setText(sort.getSname());
        sdescArea.setText(sort.getSdesc());
    }

    /**
     * 用户点击确认编辑按钮按钮，调用该方法
     */
    @Override
    public void confirm() {
        //1.获取分类下拉菜单内容
        String parent = parentBox.getSelectedItem().toString();
        //2.获取分类名称
        String sname = snameTxt.getText().trim();
        //3.获取分类描述
        String sdesc = sdescArea.getText();
        //4.修改数据是否符合要求，验证方式与添加分类验证方式相同
        if(parent.equals("=请选择=")){
            JOptionPane.showMessageDialog(this,"请选择分类");
            return;
        }
        if(sname == null || sname.isEmpty()){
            JOptionPane.showMessageDialog(this,"请输入分类名称");
            return;
        }
        //5.获取到的数据封装成Sort对象
        sort.setParent(parent);
        sort.setSdesc(sdesc);
        sort.setSname(sname);
        //6.调用service层内容，传递Sort对象进行修改
        SortService sortService = new SortService();
        sortService.editSort(sort);
        //7.关闭自己的窗口
        this.dispose();
        JOptionPane.showMessageDialog(this,"编辑成功");



    }
}
