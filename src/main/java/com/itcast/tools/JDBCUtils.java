package com.itcast.tools;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * 使用数据库的连接池
 * DBCP连接池
 * BasicDataSource 实现javax.sql.DataSource接口
 */

public class JDBCUtils {
	public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/gjp";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "20001201";
	
	private static final int MAX_IDLE = 3;
	private static final long MAX_WAIT = 5000;
	private static final int MAX_ACTIVE = 5;
	private static final int INITIAL_SIZE = 10;

	//1.使用数据库连接池对象连接数据库
	private static BasicDataSource dataSource = new BasicDataSource();
	//BasicDataSource方法，设置必要参数
	static {
		//(1)设置数据库驱动
		dataSource.setDriverClassName(DRIVER_CLASS_NAME);
		//(2)设置数据库连接URL
		dataSource.setUrl(URL);
		//(3)设置用户数据库名称和密码
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);

		dataSource.setMaxIdle(MAX_IDLE);//连接池最大空闲连接个数
		dataSource.setMaxWaitMillis(MAX_WAIT);
		dataSource.setMaxTotal(MAX_ACTIVE);
		dataSource.setInitialSize(INITIAL_SIZE);//连接池初始化连接个数
	}

	/**
	 * 2.通过静态调用拿到DataSource对象，从而实现对数据库的操作
	 * 通过QueryRunner对象，构造方法中传入BasicDataSource类对象
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
}

