package com.itcast.controller;

import com.itcast.domain.Ledger;
import com.itcast.domain.QueryForm;
import com.itcast.service.LedgerServices;
import com.itcast.service.SortService;
import com.itcast.view.AbstractLedgerMngDialog;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * 账务管理对话框控制层，从主窗体中点击账务管理按钮显示
 *
 */
public class LedgerMngController extends AbstractLedgerMngDialog {
    public LedgerMngController(JFrame frame) {
        super(frame);
    }

    private SortService sortService = new SortService();

    private LedgerServices ledgerServices = new LedgerServices();

    /**
     * 点击添加账务按钮，弹出添加账务对话框
     */
    @Override
    public void addLedger() {
        new AddLedgerController(this).setVisible(true);
    }

    /**
     * 点击编辑账务按钮，弹出编辑账务对话框
     */
    @Override
    public void editLedger() {
        //获取到选择的行
        int row = ledgerDataTable.getSelectedRow();
        //1.没有选择任何行
        if(row < 0){
            JOptionPane.showMessageDialog(this,"请选择要编辑的数据");
            return;
        }
        //调用父类方法getLedgerByTableRow，传递行号，获取到一个Ledger对象
        Ledger ledger = getLedgerByTableRow(row);
        //2.选择的是空行
        if(ledger == null) {
            JOptionPane.showMessageDialog(this, "您选择的是空行");
            return;
        }
        //3.选择的是有内容的行：弹出对话框进行编辑操作
        new EditLedgerController(this,ledger).setVisible(true);
        //4.更新数据
        queryLedger();

    }

    @Override
    public void deleteLedger() {
        //1.获取选中的行号
        int row = this.ledgerDataTable.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this,"没有选择数据");
            return;
        }
        //2.将选择中的行封装成Ledger对象
        Ledger ledger = getLedgerByTableRow(row);
        if(ledger == null){
            JOptionPane.showMessageDialog(this,"选择的是空行");
            return;
        }
        //3.确认删除的提示对话框
        int result = JOptionPane.showConfirmDialog(this,"是否确认删除吗");
        if(result == JOptionPane.OK_OPTION){
            //调用services层方法deleteLedger，传递lid值数据表主键列
            ledgerServices.deleteLedger(ledger.getLid());
            JOptionPane.showMessageDialog(this,"删除成功");
        }
        //4.更新数据
        queryLedger();
    }

    /**
     * 点击查询按钮，实现查询功能
     * 获取的是services层的查询结果
     * 结果做成Map集合
     * key:键名、value:存储查询数据的List结合
     * key:键名、value:所有收入的总和
     * key:键名、value:所有支出的总和
     * map.put("",数据库结果集List集合)
     * map.put("",inMoney)
     * map.put("",payMoney)
     */
    @Override
    public void queryLedger() {
        //1.用户选择的查询条件封装到QueryForm对象中
        String begin = beginDateTxt.getText();
        String end = endDateTxt.getText();
        String parent = parentBox.getSelectedItem().toString();
        String sname = sortBox.getSelectedItem().toString();
        QueryForm form = new QueryForm(begin, end, parent, sname);
        //2.调用Services层方法，queryLegerByQueryForm
        //3.获取到Map集合，键:ledger 值:List集合，填充在表格中
        Map<String,Object> data = ledgerServices.queryLegerByQueryForm(form);
        List<Ledger> list = (List<Ledger>) data.get("ledger");
        double in = (Double) data.get("in");
        double pay = (Double) data.get("pay");
        //4.将查询数据填充到表格JTable中
        this.setTableModel(list);
        //5.计算后的求和数据填充到Lable中
        inMoneyTotalLabel.setText("总收入" + in + "元");
        payMoneyTotalLabel.setText("总支出" + pay + "元");
    }

    /**
     * 菜单联动,调用的方法
     * 收支菜单选择之后，分类菜单会跟随它进行选择
     * 情况1：收支：请选择、分类：请选择
     * 情况2：收支：收入/支出、分类：所有收入和支出
     * 		根据收支的选择，去数据库中查询选择的内容
     * 情况3：收支：收入or支出、分类：显示对应的分类
     * 		根据收支的选择，去数据库中查询选择的内容
     *
     */
    @Override
    public void parentChange() {
        //获取出收支的选项
        String parent = parentBox.getSelectedItem().toString();
        //情况1：如果parent选择的是“-请选择-”，则后面的分类也为“请选择”
        if(parent.equals("-请选择-")){
            sortBox.setModel(new DefaultComboBoxModel(new String[]{"-请选择-"}));
        }
        //情况2：根据数据库进行查询，收支：收入/支出，查询所有的内容
        if(parent.equals("收入/支出")){
            //调用services层方法querySortNameAll()查询所有分类名称
            //获取到一个List集合.toArray()，将集合中的数据填充到下拉菜单中
            List<Object> list = sortService.querySortNameAll();
            list.add(0,"-请选择-");
            sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
        }
        //情况3：根据数据库进行查询，查询分类的具体内容
        if(parent.equals("收入") || parent.equals("支出")){
            //调用services层方法querySortNameByParent(String parent)查询所有分类名称
            //获取到一个List集合.toArray()，将集合中的数据填充到下拉菜单中
            List<Object> list = sortService.querySortNameByParent(parent);
            list.add(0,"-请选择-");
            sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
        }
    }
    public void pie(){
        new ShapeController(this).setVisible(true);
    }

    @Override
    public void Ring() {
        new RingShapeController(this).setVisible(true);
    }

    public void barGraph(){
        new barShapeController(this).setVisible(true);
    }

    public  void outPutExcel(){
        new FromDbToExcelController().fromDbToExcel();
    }
}
