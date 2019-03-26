package com.cpic.utils

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext

object Load {
  /**
    * 传入需要序列化的类
    * @param classes
    * @return
    */
  def loadConf(classes : scala.Array[scala.Predef.Class[_]]):SparkConf={
    //spark配置及其优化方案
    val conf = new SparkConf().setAppName(this.getClass.getName)
      //本地调试用上生产关闭
      .setMaster("local[2]")
    //修改序列化方式，增加序列化缓存，注册序列化类
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.registerKryoClasses(classes)
    conf.set("spark.kryoserializer.buffer.mb","10")
    //并行度设置成core数的2~3倍
    conf.set("spark.default.parallelism","6")
    //数据本地化
    conf.set("spark.locality.wait","10")
    //JVM调优--优化执行器内存占比
    conf.set("spark.storage.memoryFraction", "0.5")
    //shuffle调优
    conf.set("spark.shuffle.consolidateFiles", "true")
      .set("spark.reducer.maxSizeInFlight","96")
      .set("spark.shuffle.file.buffer","64")
      .set("spark.shuffle.io.maxRetries","5")
      .set("spark.shuffle.io.retryWait","3")
      .set("spark.shuffle.memoryFraction","0.3")
    //关闭parquet文件的自动分区数据类型推断（一般不设置）
    conf.set("spark.sql.sources.partitionColumnTypeInference.enabled","false")
    //开启parquet文件自动合并元数据（一般不设置）
    conf.set("spark.sql.parquet.mergeSchema","true")
    //压缩策略
    conf.set("spark.sql.parquet.compression.codec","snappy")
    //缓存表大小
    conf.set("spark.sql.inMemoryColumnarStorage.batchSize","20000")
    //广播阈值
    conf.set("spark.sql.autoBroadcastJoinThreshold","20971520")
    conf
  }

  /**
    * 普通的sparkconf
    * @return
    */
  def loadConf={
    new SparkConf()
  }
  /**
    * 获取SparkContext
    * @param conf
    * @return
    */
  def loadSc(conf:SparkConf):SparkContext={
    new SparkContext(conf)
  }
  /**
    * 获取SQLContext
    * @param conf
    * @return
    */
  def loadSC(conf:SparkConf):SQLContext={
    //SQLContext功能
    val sqlContext=new SQLContext(loadSc(conf))
    //    //缓存表大小
    //    sqlContext.setConf("spark.sql.inMemoryColumnarStorage.batchSize","20000")
    //    //广播阈值
    //    sqlContext.setConf("spark.sql.autoBroadcastJoinThreshold","20971520")
    sqlContext
  }

  /**
    * 获取HiveContext
    * @param conf
    * @return
    */
  def loadHc(conf:SparkConf):HiveContext={
    new HiveContext(loadSc(conf))
  }
}
