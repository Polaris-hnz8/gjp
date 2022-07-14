package com.itcast.service;

import com.itcast.dao.LedgerDao;
import com.itcast.dao.SortDao;
import com.itcast.domain.Ledger;
import com.itcast.domain.QueryForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LedgerServices {
    private LedgerDao ledgerDao = new LedgerDao();
    private SortDao sortDao = new SortDao();
    /**
     * 方法1：
     * queryTotalMoneyByParent
     * 调用dao层方法queryTotalMoneyByParent，获取收入或者支出的所有金额的总和
     */
    public Double queryTotalMoneyByParent(String parent){
        return ledgerDao.getTotalMoney(parent);
    }

    /**
     * 方法2：
     * 调用dao层querySumMoneyBySort,传递父分类获取这个分类下的求和数据
     * 按照每个分类的名称进行分组
     * Map:键存储的是分类名称，值存储的是这个名称所有金额的总和
     */
    public Map<String, Double> querySumMoneyBySort(String parent){
        //1.调用dao层querySumMoneyBySort,传递父分类获取这个分类下的求和数据
        List<Object[]> list = ledgerDao.querySumMoneyBySort(parent);
        //2.创建Map集合:键存储的是分类名称，值存储的是这个名称所有金额的总和
        Map<String,Double> map = new HashMap<String,Double>();
        //3.遍历集合LIst，获取出Object数组(根据sid值，找sname值)
        for(Object[] objects : list){
            double money = (Double)objects[0];
            int sid = (Integer) objects[1];
            //(1)调用sortDao方法，传递sid获取sname
            String sname = sortDao.getSnameBySid(sid);
            //(2)将求和的值与分类名称，存储到Map集合中
            map.put(sname, money);
        }
        return map;
    }

    /**
     * 定义方法删除账务
     * deleteLedger传递lid值即可
     */
    public void deleteLedger(int lid){
        ledgerDao.deleteLedger(lid);
    }
    /**
     * 定义方法编辑账务
     * editLedger,传递Ledger对象
     */
    public void editLedger(Ledger ledger){
        ledgerDao.editLedger(ledger);
    }
    /**
     * 定义添加账务方法
     * addLedger，传递Ledger对象
     */
    public void addLedger(Ledger ledger){
        ledgerDao.addLedger(ledger);
    }

    /**
     * 定义方法sortDao中的getSidBySname方法
     * 传递sname获取sid
     * @return
     */
    public int getSidBySname(String sname){
        return sortDao.getSidBySname(sname);
    }

    /**
     * 定义方法，返回值是map集合
     * 方法的作用是，根据用户的条件查询数据库（List集合）
     * 遍历List集合，统计收入和支出的求和计算
     * 存储数据到Map集合中
     *
     * 调用dao层方法，查询结果的List集合
     */
    public Map<String,Object> queryLegerByQueryForm(QueryForm form){
        List<Ledger> list = ledgerDao.queryLedgerByQueryForm(form);
        double in = 0;
        double pay = 0;

        for(Ledger ledger : list) {
            int sid = ledger.getSid();
            //调用dao层SortDao方法getSnameBySid，传递sid获取出sname
            String sname = sortDao.getSnameBySid(sid);
            ledger.setSname(sname);
            if(ledger.getParent().equals("收入")){
                in += ledger.getMoney();
            }else{
                pay += ledger.getMoney();
            }
        }
        //创建一个Map集合，将数据List、in、pay存储到Map集合中
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("ledger",list);
        data.put("in",in);
        data.put("pay",pay);
        return data;
    }

    /**
     * 定义方法sortDao中的getAllByDb方法
     * @return
     */
    public List<Ledger> getAllByDb(){
        return ledgerDao.getAllByDb();
    }

}
