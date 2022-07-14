package com.itcast.dao;

import com.itcast.domain.Ledger;
import com.itcast.domain.QueryForm;
import com.itcast.tools.DateUtils;
import com.itcast.tools.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LedgerDao {
    private QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
    private SortDao sortDao = new SortDao();
    /**
     * 数据获取001
     * getTotalMoney
     * 通过分类查询今年的所有数据总和，传递参数，支出还是收入
     */
    public Double getTotalMoney(String parent){
        try {
            String sql = "SELECT SUM(money) FROM gjp_ledger WHERE parent = ? AND createtime LIKE ?";
            Object[] params = {parent, DateUtils.getYear() + "%"};
            return (Double)qr.query(sql, new ScalarHandler(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据获取002
     * querySumMoneyBySort
     * 通过分类的名称查询所有分类数据的总和，传递参数，收入还是支出
     * 返回值为List<Object>数组
     */
    public List<Object[]>querySumMoneyBySort(String parent){
        try {
            //对所属于parent分类中的数据，进行求和运算，带上分组
            String sql = "SELECT SUM(money), sid FROM gjp_ledger WHERE " +
                    "parent = ? AND createtime LIKE ? GROUP BY sid";
            Object[] params = {parent, DateUtils.getYear() + "%"};
            return qr.query(sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 定义方法用于删除账务
     * 传递lid值
     */
    public void deleteLedger(int lid){
        try {
            //拼写sql语句
            qr.update("DELETE FROM gjp_ledger WHERE lid = ?", lid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义一个方法用于编辑账务
     * 传递Ledger对象
     */
    public void editLedger(Ledger ledger){
        try {
            //拼写更新账务sql语句、
            String sql = "UPDATE gjp_ledger SET parent = ?, money = ?, sid = ?, account = ?,createtime = ?, ldesc = ?" +
                    " WHERE lid = ?";
            Object[] params = {ledger.getParent(),ledger.getMoney(),ledger.getSid(),ledger.getAccount(),ledger.getCreatetime(),
                    ledger.getLdesc(),ledger.getLid()};
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 定义一个方法，添加账务
     * 传递一个Ledger对象
     * @param ledger
     */
    public void addLedger(Ledger ledger){
        try {
            //拼写sql语句
            String sql = "INSERT INTO gjp_ledger (parent,money,sid,account,createtime,ldesc)" +
                    "values(?,?,?,?,?,?)";
            Object[] params = {ledger.getParent(),ledger.getMoney(), ledger.getSid(),ledger.getAccount(), ledger.getCreatetime(),ledger.getLdesc()};
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 定义方法，实现组合查询功能
     * 传递QueryForm对象
     * 返回List<Ledger>集合
     *
     * 根据QueryForm封装的数据，进行SQL语句的编写
     */
    public List<Ledger> queryLedgerByQueryForm(QueryForm form){
        //查询语句中？占位符，也是一个不确定因素，参数选择容器进行存储
        List<String> params = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();//利用字符串缓冲区StringBuilder拼接sql命令字符串
        //1.查询条件，开始日期和结束日期，不需要理会必选
        builder.append("SELECT * FROM gjp_ledger WHERE createtime BETWEEN ? AND ?");
        params.add(form.getBegin());
        params.add(form.getEnd());
        //builder === SELECT * FROM gjp_ledger WHERE createtime BETWEEN ? AND ?
        //params === beginTime endTime

        //2.对查询条件收入or支出的选择，组合sql语句
        if(form.getParent().equals("收入") || form.getParent().equals("支出")){
            builder.append(" AND parent = ?");
            params.add(form.getParent());
        }
        //builder === SELECT * FROM gjp_ledger WHERE createtime BETWEEN ? AND ? AND parent = ?
        //params === beginTime endTime 收入/支出

        //3.对查询条件，分类名称的选择，进行sql语句组合
        if(!form.getSname().equals("-请选择-")){
            //获取sname的值，去数据表中查找sid的值
            //调用方法sortDao类的方法getSidBySname，传递sname获取Sid
            int sid = sortDao.getSidBySname(form.getSname());
            //组合数据库语句
            builder.append(" AND sid=?");
            params.add(sid+"");
        }
        //builder === SELECT * FROM gjp_ledger WHERE createtime BETWEEN ? AND ? AND parent = ? AND sid = ？
        //params === beginTime endTime 收入/支出 sid

        try {
            List<Ledger> list = qr.query(builder.toString(),new BeanListHandler<Ledger>(Ledger.class),params.toArray());
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     *  查询数据库中gjp_ledger表中所有的数据
     */
    public List<Ledger> getAllByDb(){
        String sql = "select * from gjp_ledger";
        try {
            List<Ledger> list = qr.query(sql,new BeanListHandler<Ledger>(Ledger.class));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}

