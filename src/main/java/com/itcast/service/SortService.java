package com.itcast.service;

import com.itcast.dao.SortDao;
import com.itcast.domain.Sort;

import java.util.List;

/**
 * 分类功能的业务层
 *
 * 分类的控制层controller调用services功能
 * services层调用dao层功能
 */

public class SortService {
    //创建dao层，SortDao类的对象
    private SortDao sortDao = new SortDao();
    /**
     * 定义一个方法调用dao层SortDao#querySortNameByParent(String parents)
     * 查询所有的分类名称，传递String参数，父分类
     */
    public List<Object> querySortNameByParent(String parent){
        return sortDao.querySortNameByParent(parent);
    }

    /**
     * 定义一个方法来调用dao层SortDao#querySortNameAll查询所有分离名称
     * 返回List集合
     */
    public List<Object> querySortNameAll(){
        return sortDao.querySortNameAll();
    }

    /**
     * 定义方法调用dao层SortDao#deleteSort()实现编辑分类
     * 传递sort对象
     * 是controller层调用service层传递Sort对象
     */
    public void deleteSort(Sort sort){
        sortDao.deleteSort(sort);
    }
    /**
     * 定义方法调用dao层SortDao#editSort()实现编辑分类
     * 传递sort对象
     * 是controller层调用service层传递Sort对象
     */
    public  void editSort(Sort sort){
        sortDao.editSort(sort);
    }

    /**
     * 定义方法，调用dao层SortDao#addSort()添加分类
     * 传递Sort对象
     * service层方法中的sort对象，是controller层传递的
     */
    public void addSort(Sort sort){
        sortDao.addSort(sort);
    }

    /**
     * 定义方法调用dao层SortDao#querySortAll获取所有的分类的数据
     * 返回的是List集合，储存Sort对象
     */
    public List<Sort> querySortAll(){
        return sortDao.querySortAll();
    }

}
