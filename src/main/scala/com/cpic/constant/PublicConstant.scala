package com.cpic.constant

import java.io.FileInputStream
import java.util.Properties

import org.slf4j.LoggerFactory

object PublicConstant {
  private val logger = LoggerFactory.getLogger(this.getClass)
  //export cfg_file=/cpic/cpicapp/idsrpt/config/xx.properties
  private val path = System.getenv("cfg_file")
  logger.info(s"配置文件路径${path}")

  val properties = new Properties()
  properties.load(new FileInputStream(path))

  val idsrpt_home=properties.getProperty("idsrpt_home")
  val userPrincipal = properties.getProperty("userPrincipal") //"sparkuser"
  val userKeytabPath = properties.getProperty("userKeytabPath") // "/opt/FIclient/user.keytab"
  val krb5ConfPath = properties.getProperty("krb5ConfPath") //"/opt/FIclient/KrbClient/kerberos/var/krb5kdc/krb5.conf"
  val sleeptime = properties.getProperty("sleeptime")//层间等待时间
  val tab_cnt = properties.getProperty("tab_cnt").toInt//需要的表数目
  val mydb = properties.getProperty("mydb")
  val autodb = properties.getProperty("autodb")

  val veidoo=properties.getProperty("veidoo")//维度层加载的脚本名称
  val edw=properties.getProperty("edw")//宽表层加载的脚本名称
  val index=properties.getProperty("index")//指标层加载的脚本名称
  val report=properties.getProperty("report")//接口层加载的脚本名称

  val readuser:String=properties.getProperty("readuser")//linux只读账号用户名

  val zookeeperips=properties.getProperty("zookeeperips")//zookeeperIP
  val zkport=properties.getProperty("zkport")//zookeeperPort
  val hivePrincipal=properties.getProperty("hivePrincipal")//登录hive验证kerberos的principal

  val ousername=properties.getProperty("ousername")//oracle用户名
  val opassword=properties.getProperty("opassword")//oracle密码
  val otable_name=properties.getProperty("otable_name")//访问oracle表名
  val oracleDriverUrl=properties.getProperty("oracleDriverUrl")//访问oracle的jdbcURL
  val driver="oracle.jdbc.driver.OracleDriver"//odbc driver
  //oracle 连接池信息
  val initialSize=1
  val maxActive=8
  val maxWait=3000
  val minIdle=1

  logger.info(s"主目录路径 ${idsrpt_home}")
  logger.info(s"工作库名   ${mydb} ")
  logger.info(s"原子层库名   ${autodb} ")
  logger.info(s"用户名  ${userPrincipal}")
  logger.info(s"kerberos文件路径  ${userKeytabPath}")
  logger.info(s"krb5配置文件路径   ${krb5ConfPath}")
  logger.info(s"睡眠时间sleeptime   ${sleeptime} 秒")
  logger.info(s"加载的表数目   ${tab_cnt} ")
  logger.info(s"维度层加载的脚本名称   ${veidoo} ")
  logger.info(s"宽表层加载的脚本名称   ${edw} ")
  logger.info(s"指标层加载的脚本名称   ${index} ")
  logger.info(s"接口层加载的脚本名称   ${report} ")
  logger.info(s"只读账号用户名   ${readuser} ")
  logger.info(s"zookeeperIP地址   ${zookeeperips} ")
  logger.info(s"zookeeper端口   ${zkport} ")
  logger.info(s"登录hive验证kerberos的principal   ${hivePrincipal} ")
  logger.info(s"oracle用户名   ${ousername} ")
  logger.info(s"oracle密码   ${opassword} ")
  logger.info(s"访问oracle表名   ${otable_name} ")
  logger.info(s"访问oracle的jdbcURL   ${oracleDriverUrl} ")



}
