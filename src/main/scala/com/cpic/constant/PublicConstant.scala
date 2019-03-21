package com.cpic.constant

import java.io.FileInputStream
import java.util.Properties

import org.slf4j.LoggerFactory

object PublicConstant {
  val logger = LoggerFactory.getLogger(this.getClass)
  val path = System.getenv("cfg_file")
  logger.info(s"配置文件路径${path}")

  val properties = new Properties()
  properties.load(new FileInputStream(path))

  val idsrpt_home=properties.getProperty("IDSRPT_HOME")
  val userPrincipal = properties.getProperty("userPrincipal") //"sparkuser"
  val userKeytabPath = properties.getProperty("userKeytabPath") // "/opt/FIclient/user.keytab"
  val krb5ConfPath = properties.getProperty("krb5ConfPath") //"/opt/FIclient/KrbClient/kerberos/var/krb5kdc/krb5.conf"
  val sleeptime = properties.getProperty("sleeptime")
  val tab_cnt = properties.getProperty("tab_cnt").toInt
  val mydb = properties.getProperty("mydb")
  val autodb = properties.getProperty("autodb")
  val veidoo=properties.getProperty("veidoo")//维度层加载的脚本名称
  val edw=properties.getProperty("edw")//宽表层加载的脚本名称
  val index=properties.getProperty("index")//指标层加载的脚本名称
  val report=properties.getProperty("report")//接口层加载的脚本名称
  val readuser:String=properties.getProperty("readuser")//只读账号用户名
  val zookeeperips=properties.getProperty("zookeeperips")//zookeeperIP
  val zkport=properties.getProperty("zkport")//zookeeperPort
  val hivePrincipal=properties.getProperty("hivePrincipal")//登录hive验证kerberos的principal

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
}
