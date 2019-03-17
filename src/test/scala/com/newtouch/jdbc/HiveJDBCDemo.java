package com.newtouch.jdbc;

import java.sql.*;

public class HiveJDBCDemo {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//1.注册数据库驱动
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			//2.获取连接
//jdbc:hive2://ha-cluster/default
//jdbc:hive2://ha-cluster/default;user.keytab=/home/userkeytab/adminuser.keytab;user.principal=adminuser@HADOOP.COM;saslQop=auth-conf;auth=KERBEROS;principal=spark/hadoop.hadoop.com@HADOOP.COM
conn = DriverManager.getConnection("jdbc:hive2://ha-cluster/default;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM;","","");
			//3.获取传输器
			ps = conn.prepareStatement("SELECT COUNT(*) FROM  idsrpt.WD_USAGE");
//			ps.setInt(1, 2);
			//4.传输sql执行
			rs = ps.executeQuery();
			//5.获取结果
//			while(rs.next()){
//				String name = rs.getString("name");
//				System.out.println(name);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//6.关闭资源
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					ps = null;
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn = null;
				}
			}
		}
	}
}
