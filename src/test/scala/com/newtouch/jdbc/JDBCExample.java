package com.newtouch.jdbc;

import com.cpic.utils.LoginUtil;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.sql.*;

public class JDBCExample {
	/**
	 * 所连接的集群是否为安全模式
	 */
	private static final boolean isSecureVerson = true;

	private static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";

	private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
	private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
	private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop";

	private static Configuration CONF = null;
	private static String USER_NAME = null;
	private static String zkQuorum = null;

	private static void init() throws IOException {
		CONF = new Configuration();
		// 其中，zkQuorum的"xxx.xxx.xxx.xxx"为集群中Zookeeper所在节点的IP，端口默认是24002
		zkQuorum = "10.182.83.86:24002,10.182.83.85:24002,10.182.83.84:24002";
		// 设置新建用户的USER_NAME，其中"xxx"指代之前创建的用户名，例如创建的用户为user，则USER_NAME为user
		USER_NAME = "idsrpt";

		if (isSecureVerson) {
			// 设置客户端的keytab和krb5文件路径

			LoginUtil.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, USER_NAME, "/cpic/cpicapp/idsrpt/kerberos/user.keytab");
			LoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY, ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);

			// 安全模式
			// Zookeeper登录认证
			LoginUtil.login(USER_NAME, "/cpic/cpicapp/idsrpt/kerberos/user.keytab", "/cpic/cpicapp/idsrpt/kerberos/krb5.conf", CONF);
		}
	}

	/**
	 * 本示例演示了如何使用Hive JDBC接口来执行HQL命令<br>
	 * <br>
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		Connection connection = null;
		try {

			// 参数初始化
			init();
			// 这个代码是没有问题的，你可以登终端看这边认证是成功的，但是抛了sql异常，
			// 定义HQL，HQL为单条语句，不能包含“;”
			// 拼接JDBC URL
			StringBuilder sBuilder = new StringBuilder("jdbc:hive2://").append(zkQuorum).append("/");

			if (isSecureVerson) {
				// 在使用多实例特性时append("hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM")的"hiveserver2"与"hive/hadoop.hadoop.com@HADOOP.COM"根据使用不同的实例进行变更
				// 例如使用Hive1实例则改成"hiveserver2_1"与"hive1/hadoop.hadoop.com@HADOOP.COM"，Hive2实例为"hiveserver2_2",以此类推。
				sBuilder.append(";serviceDiscoveryMode=").append("zooKeeper").append(";zooKeeperNamespace=")
						.append("hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM").append(";");
			} else {
				// 普通模式
				// 使用多实例特性的"hiveserver2"变更参照安全模式
				sBuilder.append(";serviceDiscoveryMode=").append("zooKeeper").append(";zooKeeperNamespace=").append("hiveserver2;auth=none");
			}
			// String url = sBuilder.toString();
			String url = "jdbc:hive2://ha-cluster/default;user.principal=spark/hadoop.hadoop.com@HADOOP.COM;saslQop=auth-conf;auth=KERBEROS;principal=spark/hadoop.hadoop.com@HADOOP.COM;";

			// 加载Hive JDBC驱动
			Class.forName(HIVE_DRIVER);

			// 获取JDBC连接
			// 如果使用的是普通模式，那么第二个参数需要填写正确的用户名，否则会以匿名用户(anonymous)登录
			// 这边加个断电调试看一下呢，你弄吧.我不会用这个ide，这不是eclipse嘛。平时用idea:你要在这里打断电吗是的，我要进驱动程序里面看一下，进不去jar里面的。好吧
			System.out.println(url);
			connection = DriverManager.getConnection(url, "", "");
			System.out.println(connection.toString());
			// 建表
			// 表建完之后，如果要往表中导数据，可以使用LOAD语句将数据导入表中，比如从HDFS上将数据导入表:
			// load data inpath '/tmp/employees.txt' overwrite into table employees_info;
			// execDDL(connection,"SELECT COUNT(*) FROM  idsrpt.WD_USAGE");
			execDDL(connection, "set role=admin");
			// execDDL(connection,"show databases");
			execDDL(connection, "set role admin");
//			 execDDL(connection,"list jars");
			 execDDL(connection,"drop function idsrpt.udf_datediff_trunc") ;
			 execDDL(connection,"drop function idsrpt.udf_upper") ;
			 execDDL(connection,"drop function idsrpt.udf_eq") ;
			 execDDL(connection,"drop function idsrpt.udf_nvl") ;
			 execDDL(connection,"drop function idsrpt.udf_datediff") ;
			 execDDL(connection,"drop function idsrpt.udf_year_first_day") ;
			 execDDL(connection,"drop function idsrpt.udf_year_last_day") ;
			 execDDL(connection,"drop function idsrpt.udf_month_first_day") ;
			 execDDL(connection,"drop function idsrpt.udf_month_last_day") ;
			 execDDL(connection,"drop function idsrpt.udf_date_add") ;
			 execDDL(connection,"drop function idsrpt.udf_nvl2") ;

			execDDL(connection, "create function idsrpt.udf_datediff_trunc as 'com.cpic.udf.MyDateTrunc'  using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_upper as 'com.cpic.udf.ToUpperUDF' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_eq as 'com.cpic.udf.MyEqual' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_nvl as 'com.cpic.udf.MyNull' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_datediff as 'com.cpic.udf.MyDatediff' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_year_first_day as 'com.cpic.udf.MyYearfirstDay' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_year_last_day as 'com.cpic.udf.MyYearLastDay' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_month_first_day as 'com.cpic.udf.MyMonthFirstDay' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_month_last_day as 'com.cpic.udf.MyMonthLastDay' using JAR 'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_date_add as 'com.cpic.udf.MyDateAdd' using JAR  'hdfs://hacluster/user/hive/udf/cx_udf.jar'");
			execDDL(connection, "create function idsrpt.udf_nvl2 as 'com.cpic.udf.MyNvl2' using JAR  'hdfs://hacluster/user/hive/udf/cx_udf.jar'");

			
			
			
			// execDDL(connection,"select idsrpt.udf_datediff_trunc('20181111', '2018-11-11 22:22:22')") ;
			System.out.println("create function success!");

			// 查询
			// execDML(connection,sqls[1]);

			// 删表
			// execDDL(connection,sqls[2]);
			// System.out.println("Delete table success!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭JDBC连接
			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void execDDL(Connection connection, String sql) throws SQLException {
		PreparedStatement statement = null;
		try {
			System.out.println(sql);
			statement = connection.prepareStatement(sql);
			statement.execute();
		} finally {
			if (null != statement) {
				statement.close();
			}
		}
	}

	public static void execDML(Connection connection, String sql) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultMetaData = null;

		try {
			// 执行HQL
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();

			// 输出查询的列名到控制台
			resultMetaData = resultSet.getMetaData();
			int columnCount = resultMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(resultMetaData.getColumnLabel(i) + '\t');
			}
			System.out.println();

			// 输出查询结果到控制台
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					System.out.print(resultSet.getString(i) + '\t');
				}
				System.out.println();
			}
		} finally {
			if (null != resultSet) {
				resultSet.close();
			}

			if (null != statement) {
				statement.close();
			}
		}
	}

}
