package com.itcast.controller;

import com.itcast.domain.Ledger;
import com.itcast.service.LedgerServices;
import jxl.write.Label;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FromDbToExcelController extends Component {
    private LedgerServices ledgerServices = new LedgerServices();
    //static String dir_path;
    /**
     * 选择的文件路径获取
     */
    /*public void start(final Stage stage){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            dir_path = file.getPath();
        }
        System.out.println(dir_path);
        stage.show();
    }
    */

    /**
     * 输出excel表格的方法
     */
    public void fromDbToExcel() {
        try {
            //Application.launch();
            //1.创建可写入的Excel的工作簿
            WritableWorkbook writableWorkbook = null;
            File file = new File("D://student.xls");
            if (!file.exists()) {
                file.createNewFile();
            }
            //以fileName为文件名创建一个Workbook
            writableWorkbook = Workbook.createWorkbook(file);

            //2.创建可写入的工作表
            WritableSheet writableSheet = writableWorkbook.createSheet("Test Sheet1", 0);

            //3.查询数据库中的所有数据
            List<Ledger> list = ledgerServices.getAllByDb();

            //4.要插入到的excel表格的行号，默认从0开始
            Label labellid = new Label(0, 0, "lid");
            Label labelparent = new Label(1, 0, "parent");
            Label labelmoney = new Label(2, 0, "money");
            Label labelsid = new Label(3, 0, "sid");
            Label labelaccount = new Label(4, 0, "account");
            Label labelcreatetime = new Label(5, 0, "createtime");
            Label labelldesc = new Label(6, 0, "ldesc");

            writableSheet.addCell(labellid);
            writableSheet.addCell(labelparent);
            writableSheet.addCell(labelmoney);
            writableSheet.addCell(labelsid);
            writableSheet.addCell(labelaccount);
            writableSheet.addCell(labelcreatetime);
            writableSheet.addCell(labelldesc);

            for (int i = 0; i < list.size(); i ++) {
                Label labellid_i = new Label(0, i + 1, list.get(i).getLid()+"");
                Label labelparent_i = new Label(1, i + 1, list.get(i).getParent()+"");
                Label labelmoney_i = new Label(2, i + 1, list.get(i).getMoney()+"");
                Label labelsid_i = new Label(3, i + 1, list.get(i).getSid()+"");
                Label labelaccount_i = new Label(4, i + 1, list.get(i).getAccount()+"");
                Label labelcreatetime_i = new Label(5, i + 1, list.get(i).getCreatetime()+"");
                Label labelldesc_i = new Label(6, i + 1, list.get(i).getLdesc()+"");
                writableSheet.addCell(labellid_i);
                writableSheet.addCell(labelparent_i);
                writableSheet.addCell(labelmoney_i);
                writableSheet.addCell(labelsid_i);
                writableSheet.addCell(labelaccount_i);
                writableSheet.addCell(labelcreatetime_i);
                writableSheet.addCell(labelldesc_i);
            }

            //5.将数据写入excel文档
            writableWorkbook.write();

            //6.输出excel反馈信息
            JOptionPane.showMessageDialog(this,"Excel数据导出成功","操作成功",JOptionPane.PLAIN_MESSAGE);

            //7.关闭excel工作簿对象
            writableWorkbook.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
