package com.itcast.controller;

import com.itcast.domain.Ledger;
import com.itcast.service.LedgerServices;
import com.itcast.service.SortService;
import com.itcast.view.AbstractOperationLedgerDialog;

import javax.swing.*;
import java.util.List;

public class EditLedgerController extends AbstractOperationLedgerDialog {
    private Ledger ledger;
    private SortService sortService = new SortService();
    private LedgerServices ledgerServices = new LedgerServices();
    public EditLedgerController(JDialog dialog, Ledger ledger) {
        super(dialog);
        super.titleLabel.setText("编辑账务");
        super.setTitle("编辑账务");
        this.ledger = ledger;
        //编辑对话框中，显示填充全部数据
        parentBox.setSelectedItem(ledger.getParent());
        //setSelectedItem方法将菜单原有的内容，设置一个默认选项
        //(1)获得到分类名称，转为字符串数组
        String[] items = {ledger.getSname()};
        //(2)调用菜单的setModel方法，重新设置菜单数据
        sortBox.setModel(new DefaultComboBoxModel(items));

        accountTxt.setText(ledger.getAccount());
        moneyTxt.setText(ledger.getMoney()+"");
        createtimeTxt.setText(ledger.getCreatetime());
        ldescTxt.setText(ledger.getLdesc());
    }

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
     * 点击确定编辑按钮后调用的方法
     * 1.获取用户选择的数据，对数据进行验证
     * 2.将数据封装成Ledger对象
     * 3.调用services层方法
     */
    @Override
    public void confirm() {
        //1.获取用户选择的数据，对数据进行验证
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

        //2.将已有的数据封装成到Ledger对象中
        ledger.setAccount(account);
        ledger.setCreatetime(createtime);
        ledger.setLdesc(ldesc);
        ledger.setMoney(money);
        ledger.setParent(parent);

        int sid = ledgerServices.getSidBySname(sname);
        ledger.setSid(sid);

        //3.调用services层方法
        ledgerServices.editLedger(ledger);
        this.dispose();
        JOptionPane.showMessageDialog(this,"编辑成功");

    }
}
