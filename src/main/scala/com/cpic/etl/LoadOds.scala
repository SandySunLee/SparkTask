package com.cpic.etl

import com.cpic.utils.Load
import org.apache.spark.sql.hive.HiveContext

object LoadOds extends App{

  /**
    * 加载原子层的各个类
    * @param classes
    * @tparam T
    * @return
    */
  def load[T](classes : scala.Array[scala.Predef.Class[_]]):HiveContext={
      Load.loadHc(Load.loadConf(classes))
}

  /**
    * 将一个javaRDD注册成一个hiveContext的临时表
    * @param hc
    * @param javaRdd
    * @param beanClass
    * @param tableName
    * @tparam T
    */
  def registerTmpTable[T](hc:HiveContext,javaRdd:org.apache.spark.api.java.JavaRDD[T], beanClass : scala.Predef.Class[T],tableName:String)={
    hc.createDataFrame(javaRdd,beanClass).registerTempTable(tableName)
  }

}
