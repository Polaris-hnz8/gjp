package com.itcast.controller;

import com.itcast.domain.Ledger;
import com.itcast.service.LedgerServices;
import com.itcast.service.SortService;
import com.itcast.view.AbstractOperationLedgerDialog;

import javax.swing.*;
import java.util.List;

/**
 * 添加账务对话框控制层
 */

public class AddLedgerController extends AbstractOperationLedgerDialog {
    private SortService sortService = new SortService();
    private LedgerServices ledgerServices = new LedgerServices();

    public AddLedgerController(JDialog dialog) {
        super(dialog);
        titleLabel.setText("添加账务");
        super.setTitle("添加账务");
    }

    /**
     * 点击添加账务按钮，弹出添加账务对话框
     */
    @Override
    public void changeParent() {
        //菜单联动效果实现:获取出收支的选项
        String parent = parentBox.getSelectedItem().toString();
        //情况1：如果parent选择的是“-请选择-”，则后面的分类也为“请选择”
        if(parent.equals("-请选择-")){
            sortBox.setModel(new DefaultComboBoxModel(new String[]{"-请选择-"}));
        }
        //情况2：根据数据库进行查询，查询分类的具体内容
        if(parent.equals("收入") || parent.equals("支出")){
            //调用services层方法querySortNameByParent(String parent)查询所有分类名称
            //获取到一个List集合.toArray()，将集合中的数据填充到下拉菜单中
            List<Object> list = sortService.querySortNameByParent(parent);
            list.add(0,"-请选择-");
            sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
        }
    }

    /**
     * 点击添加按钮，实现功能
     * 验证用户填写数据：
     * 分类、分类名称、金额>0必须是数字
     * 账户必须填写
     * 1.用户所有填写的数据，封装成Ledger的对象
     * 2.传递给services层进行添加
     */
    @Override
    public void confirm() {
        String parent = parentBox.getSelectedItem().toString();
        String sname = sortBox.getSelectedItem().toString();
        String account = accountTxt.getText();
        String createtime = createtimeTxt.getText();
        String sMoney = moneyTxt.getText();
        String ldesc = ldescTxt.getText();
        if(parent.equals("-请选择-")){
            JOptionPane.showMessageDialog(this,"请选择收/支");
            return;
        }
        if(sname.equals("-请选择-")){
            JOptionPane.showMessageDialog(this,"请选择分类名称");
            return;
        }
        if(account==null || account.isEmpty()){
            JOptionPane.showMessageDialog(this,"请填写账户");
            return;
        }
        //将获取到的金额string转为double类型
        double money = 0;
        try{
            money = Double.parseDouble(sMoney);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"必须填写数字");
            return;
        }
        if(money <= 0){
            JOptionPane.showMessageDialog(this,"金额必须大于0");
            return;
        }
        //1.用户所有填写的数据，封装成Ledger的对象:0,数据主键假数据、sid是通过sname查询sort表查询获得，假数据
        //2.传递给services层进行添加
        //调用service层方法getSidBySname，获取sid值
        int sid = ledgerServices.getSidBySname(sname);
        Ledger ledger = new Ledger(0,parent,money,sid,account,createtime,ldesc,sname);
        ledgerServices.addLedger(ledger);

        this.dispose();
        JOptionPane.showMessageDialog(this,"添加账务成功");
    }
}
