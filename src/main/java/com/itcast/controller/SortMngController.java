package com.itcast.controller;

import com.itcast.domain.Sort;
import com.itcast.service.SortService;
import com.itcast.view.AbstractSortMngDialog;

import javax.swing.*;
import java.util.List;

/**
 * 分类管理对话框的子类
 * 实现分类管理功能
 * 显示分类管理界面，new该子类
 */
public class SortMngController extends AbstractSortMngDialog {
    //成员位置创建出services层SortService类的对象
    private SortService sortService = new SortService();

    public SortMngController(JFrame frame) {
        super(frame);
        //向表格中填充分类数据是在构造方法中实现
        //setTableModel方法，传递List<Sort>集合，就可以向表格中填充数据
        /**
         * 实现步骤：
         * 1.调用services层中的方法，获取出一个List集合
         * 2.services层调用dao层，获取出List集合
         * 3.dao层，查询数据库，数据表中的结果集变成List集合，返回List集合
         * 4.调用父类方法setTableModel，传递List集合
         * 5.抽取一个refresh方法调用即可
         */
        refresh();
    }

    /**
     * 添加分类按钮，点击后调用的方法
     * 开启添加分类的对话框
     */
    @Override
    public void addSort() {
        new AddSortController(this).setVisible(true);
        refresh();
    }
    /**
     * 编辑分类按钮，点击后调用的方法
     * 开启编辑分类的对话框
     *
     * 对用会选择的分类数据进行控制：不选择、空行
     * sortDataTable表格中的方法getSelectRow获取选择行，返回-1则没有选择
     * 选择的是空行
     * getSortByTableRow()父类方法，传递选中的行号，返回这一行的数据封装到Sort对象
     * (1)如果用户选择有效行，封装好Sort对象，传递给EditSortController对话框
     * 后面的实现和新增几乎一样
     */

    @Override
    public void editSort() {
        //1.获取用户选中的行号
        int row = sortDataTable.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this,"请选择数据");
            return;
        }
        //2.getByTableRow,传递行号，返回这一行的数据封装成Sort对象
        Sort sort = getSortByTableRow(row);
        if(sort == null){
            JOptionPane.showMessageDialog(this,"选择的是空行");
            return;
        }
        //3.选择的是有效数据，则将Sort对象传递到编辑分类的对话框中
        new EditSortController(this,sort).setVisible(true);
        refresh();
    }

    /**
     * 点击删除按钮，调用方法
     * 实现步骤：
     * 1.获取选择的哪一行
     * 2.提示对话框，询问用户是否真的要删除吗
     * 3.调用services层方法deleteSort，传递sort对象
     * 4.刷新数据
     */
    @Override
    public void deleteSort() {
        //获取用户选择行号
        int row = this.sortDataTable.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this,"请选择数据");
            return;
        }
        //2.getByTableRow,传递行号，返回这一行的数据封装成Sort对象
        Sort sort = getSortByTableRow(row);
        if(sort == null){
            JOptionPane.showMessageDialog(this,"选择的是空行");
            return;
        }
        //3.选择的是有效数据，则提示用户你真的要删除吗
        int i = JOptionPane.showConfirmDialog(this,"是否真的要删除吗","删除提示",JOptionPane.YES_NO_OPTION);
        if(i == JOptionPane.OK_OPTION){
            //调用services层方法deleteSort
            sortService.deleteSort(sort);
            JOptionPane.showMessageDialog(this,"删除成功");
            refresh();
        }
    }

    @Override
    public List<Sort> queryAllSort() {
        return null;
    }

    //刷新显示分类数据刷新
    private void refresh(){
        List<Sort> list = sortService.querySortAll();
        setTableModel(list);
    }
}
