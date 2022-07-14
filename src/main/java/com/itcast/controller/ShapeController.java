package com.itcast.controller;

import com.itcast.service.LedgerServices;
import com.itcast.tools.DateUtils;
import com.itcast.tools.JFreeChartUtils;
import com.itcast.view.AbstractShapeDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShapeController extends AbstractShapeDialog {
    public ShapeController(JDialog dialog) {
        super(dialog);
        initDialog();
    }

    private LedgerServices ledgerServices = new LedgerServices();

    /**
     * 获取生成图片的路径，路径存储到List集合中
     * JFreeChartUtils静态方法pie()实现
     *
     * 1.调用services层方法，获取需要的数据
     */
    @Override
    public List<String> getImagePaths() {
        /**
         * part1:moneyPay
         */
        //创建图片存储路径
        List<String> listPath = new ArrayList<String>();

        //调用services层中的queryTotalMoneyByParent，获取所有支出或者支出的总金额
        Double moneyPay = ledgerServices.queryTotalMoneyByParent("支出");
        //调用services方法querySumMoneyBySort，获取的是支出或者收入中的每一项的总金额，如工资、股票收入
        Map<String,Double> mapPay = ledgerServices.querySumMoneyBySort("支出");

        //调用工具类JFreeChartUtils工具类中的方法pie生成图片
        String title = "支出 占比图("+moneyPay+")(+"+ DateUtils.getYear() +"年)";
        String pathPay = "pay.jpg";
        JFreeChartUtils.pie(title, mapPay, moneyPay, pathPay);

        listPath.add(pathPay);

        /**
         * part2:moneyIn
         */
        //调用services层中的queryTotalMoneyByParent，获取所有支出或者支出的总金额
        Double moneyIn = ledgerServices.queryTotalMoneyByParent("收入");
        //调用services方法querySumMoneyBySort，获取的是支出或者收入中的每一项的总金额，如工资、股票收入
        Map<String,Double> mapIn = ledgerServices.querySumMoneyBySort("收入");

        //调用工具类JFreeChartUtils工具类中的方法pie生成图片
        String titleIn = "收入 占比图("+moneyIn+")(+"+DateUtils.getYear() +"年)";
        String pathIn = "in.jpg";
        JFreeChartUtils.pie(titleIn, mapIn, moneyIn, pathIn);

        listPath.add(pathIn);
        return listPath;
    }
}
