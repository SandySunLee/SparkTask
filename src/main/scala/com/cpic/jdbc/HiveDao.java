package com.cpic.jdbc;

import com.cpic.constant.PublicConstant;
import com.cpic.utils.LoginUtil;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

/**
 * 本类是对sparkSQL的补充，比如读取hive所建的分区表的时候执行效率会比sparkSQL快
 */
public class HiveDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
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
        StringBuffer sb=new StringBuffer();
        for(String ip:PublicConstant.zookeeperips().split(",")){
            sb.append(ip+":"+PublicConstant.zkport());
        }
        zkQuorum = sb.toString().substring(0,sb.toString().length()-1);
        // 设置新建用户的USER_NAME，其中"xxx"指代之前创建的用户名，例如创建的用户为user，则USER_NAME为user
        USER_NAME = PublicConstant.userPrincipal();
        if (isSecureVerson) {
            // 设置客户端的keytab和krb5文件路径
            LoginUtil.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, USER_NAME, PublicConstant.userKeytabPath());
            LoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY, ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
            // 安全模式
            // Zookeeper登录认证
            LoginUtil.login(USER_NAME, PublicConstant.userKeytabPath(), PublicConstant.krb5ConfPath(), CONF);
        }
    }

    /**
     * 采用beeline方式执行hql数组
     * @param sqls
     */
    public  void beeline(String[] sqls){
        Connection connection = null;
        try {
            init();
            // 定义HQL，HQL为单条语句，不能包含“;”
            // 拼接JDBC URL
            StringBuilder sBuilder = new StringBuilder("jdbc:hive2://").append(zkQuorum).append("/").append(PublicConstant.mydb());//默认进入自己的库
            if (isSecureVerson) {
                // 在使用多实例特性时append("hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM")的"hiveserver2"与"hive/hadoop.hadoop.com@HADOOP.COM"根据使用不同的实例进行变更
                // 例如使用Hive1实例则改成"hiveserver2_1"与"hive1/hadoop.hadoop.com@HADOOP.COM"，Hive2实例为"hiveserver2_2",以此类推。
                sBuilder.append(";serviceDiscoveryMode=").append("zooKeeper;")
                        .append("zooKeeperNamespace=").append("hiveserver2;")
                        .append("sasl.qop=").append("auth-conf;")
                        .append("auth=").append("KERBEROS;")
                        .append("principal=").append("hive/hadoop.").append(PublicConstant.hivePrincipal())
                        .append(";");
            } else {
                // 普通模式
                // 使用多实例特性的"hiveserver2"变更参照安全模式
                sBuilder.append(";serviceDiscoveryMode=").append("zooKeeper;")
                        .append("zooKeeperNamespace=").append("hiveserver2;")
                        .append("auth=none");
            }
             String url = sBuilder.toString();
            //String url = "jdbc:hive2://ha-cluster/default;user.principal=spark/hadoop.hadoop.com@HADOOP.COM;saslQop=auth-conf;auth=KERBEROS;principal=spark/hadoop.hadoop.com@HADOOP.COM;";

            // 加载Hive JDBC驱动
            Class.forName(HIVE_DRIVER);
            // 获取JDBC连接
            // 如果使用的是普通模式，那么第二个参数需要填写正确的用户名，否则会以匿名用户(anonymous)登录
            // 这边加个断电调试看一下呢，你弄吧.我不会用这个ide，这不是eclipse嘛。平时用idea:你要在这里打断电吗是的，我要进驱动程序里面看一下，进不去jar里面的。好吧
            logger.info(url);
            connection = DriverManager.getConnection(url, "", "");
            logger.info(connection.toString());
            for(String sql:sqls){
                execDDL(connection, sql);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }finally {
            // 关闭JDBC连接
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warn(e.getMessage());
                }
            }
        }
    }

    public void execDDL(Connection connection, String sql) throws SQLException {
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

    public void execDML(Connection connection, String sql) throws SQLException {
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
