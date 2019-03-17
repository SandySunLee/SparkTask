package com.newtouch.entity

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Fields262Parquet extends App{
  if(args.length != 2){
    println("目录不正确，退出程序")
    sys.exit()
  }
  //创建一个集合存储输入输出目录
  val conf = new SparkConf().setAppName(s"${this.getClass.getName}").setMaster("local[*]")
    .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    .registerKryoClasses(Array(classOf[SS]))
  val sc = new SparkContext(conf)
  val sQLContext = new SQLContext(sc)
  sQLContext.setConf("spark.sql.parquet.compression.codec","snappy")

  //开始读取数据
  val inputPath=args(0)
  val lines = sc.textFile(inputPath)

  val rowRDD = lines.map(t=>t.split(",",t.length))
    .filter(_.length >= 27)
    .map(arr=>{
      new SS(
        arr(0),
        arr(1).toInt,
        arr(2).toInt,
        arr(3).toInt,
        arr(4).toInt,
        arr(5),
        arr(6),
        arr(7).toInt,
        arr(8).toInt,
        arr(9).toDouble,
        arr(10).toDouble,
        arr(11),
        arr(12),
        arr(13),
        arr(14),
        arr(15),
        arr(16),
        arr(17).toInt,
        arr(18),
        arr(19),
        arr(20).toInt,
        arr(21).toInt,
        arr(22),
        arr(23),
        arr(24),
        arr(25),
        arr(26).toInt
      )
    })
  import sQLContext.implicits._
  val df = rowRDD.toDF()
  //存储parquet文件
  val outputPath=args(1)
  df.coalesce(1).write.parquet(outputPath)
  sc.stop()


}


class SS(
          sessionid:String,
          advertisersid:Int,
          adorderid:Int,
          adcreativeid:Int,
          adplatformproviderid:Int,
          sdkversion:String,
          adplatformkey:String,
          putinmodeltype:Int,
          requestmode:Int,
          adprice:Double,
          adppprice:Double,
          requestdate:String,
          ip:String,
          appid:String,
          appname:String,
          uuid:String,
          device:String,
          client:Int,
          osversion:String,
          density:String,
          pw:Int,
          ph:Int,
          long:String,
          lat:String,
          provincename:String,
          cityname:String,
          ispid:Int
        )extends Product() with Serializable{
  override def productElement(n: Int) = n match {
    case 0 =>sessionid
    case 1 =>advertisersid
    case 2 =>adorderid
    case 3 =>adcreativeid
    case 4 =>adplatformproviderid
    case 5 =>sdkversion
    case 6 =>adplatformkey
    case 7 =>putinmodeltype
    case 8 =>requestmode
    case 9 =>adprice
    case 10=>adppprice
    case 11=>requestdate
    case 12=>ip
    case 13=>appid
    case 14=>appname
    case 15=>uuid
    case 16=>device
    case 17=>client
    case 18=>osversion
    case 19=>density
    case 20=>pw
    case 21=>ph
    case 22=>long
    case 23=>lat
    case 24=>provincename
    case 25=>cityname
    case 26=>ispid
  }
  def canEqual(that: Any) = that.isInstanceOf[SS]
  def productArity = 27

}
