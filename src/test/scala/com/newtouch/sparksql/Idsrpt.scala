package com.newtouch.sparksql

import com.newtouch.entity.Baodan
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
object Idsrpt extends App{

  //spark配置及其优化方案
  val conf = new SparkConf().setAppName("test").setMaster("local[2]")
  //修改序列化方式，增加序列化缓存，注册序列化类
  conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  conf.registerKryoClasses(Array(classOf[Baodan]))
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

  val sparkContext = new SparkContext(conf)
  //SQLContext功能
  val sc=new SQLContext(sparkContext)
  //缓存表大小
  sc.setConf("spark.sql.inMemoryColumnarStorage.batchSize","20000")
  //广播阈值
  sc.setConf("spark.sql.autoBroadcastJoinThreshold","20971520")

  //  val df=sc.read.load("file:\\\\D:\\myjava\\text.parquet")
  //创建DataFrame的第一种方式：构造DataFrame的元数据
  //  val rdd=sparkContext.textFile("file:\\\\\\\\D:\\\\myjava\\\\*.parquet").map(_.split(","))
  //  val schema = StructType(
  //    List(
  //      StructField("id", IntegerType, true),
  //      StructField("name", StringType, true),
  //      StructField("age", IntegerType, true)
  //    )
  //  )
  //  val rowRDD = rdd.map(p => Row(p(0).toInt, p(1).toString.trim, p(2).toInt))
  //  val personDataFrame = sc.createDataFrame(rowRDD, schema)
  //  personDataFrame.registerTempTable("t_person")
  //  val df = sc.sql("select * from t_person order by age desc")
  //  df.show()
  //  df.printSchema()
  //构建DataFrame的第二种方式：对象反射
   //case class Student(id:Int,name:String,age:Int)
  //scala的类最多只允许22个字段，要超过22个字段则要引入数组操作，比较麻烦，我们选择java类做替代
  //想要用sqlContext的内置函数，必须引入这个隐式转换
  //想要用对象反射DataFrame，必须引入这个隐式转换
  import sc.implicits._
  val javaRDD=sparkContext.textFile("file:\\\\\\\\D:\\test\\test.txt").map(_.split("\t"))
//    .map(x=>Student(x(0).toInt,x(1).toString.trim,x(2).toInt))
    .map(line=>new Baodan(line(0).toInt,
    line(1).toInt,
    line(2).toInt,
    line(3).toInt,
    line(4).toInt,
    line(5).toInt,
    line(6).toInt,
    line(7).toInt,
    line(8).toInt,
    line(9).toInt,
    line(10).toInt,
    line(11).toInt,
    line(12).toInt,
    line(13).toInt,
    line(14).toInt,
    line(15).toInt,
    line(16).toInt,
    line(17).toInt,
    line(18).toInt,
    line(19).toInt,
    line(20).toInt,
    line(21).toInt,
    line(22).toInt,
    line(23).toInt,
    line(24).toInt,
    line(25).toInt,
    line(26).toInt,
    line(27).toInt,
    line(28).toInt,
    line(29).toInt,
    line(30).toInt,
    line(31).toInt,
    line(32).toInt,
    line(33).toInt,
    line(34).toInt,
    line(35).toInt,
    line(36).toInt,
    line(37).toInt,
    line(38).toInt,
    line(39).toInt,
    line(40).toInt,
    line(41).toInt,
    line(42).toInt,
    line(43).toInt,
    line(44).toInt,
    line(45).toInt,
    line(46).toInt,
    line(47).toInt,
    line(48).toInt,
    line(49).toInt) ).toJavaRDD()

  val df=sc.createDataFrame(javaRDD,classOf[Baodan]).registerTempTable("baodan")
//    .persist(StorageLevel.MEMORY_ONLY_SER)
//    .toDF().select("id","name","age").filter("age>12").show()
  //    .registerTempTable("student_t")
  sc.sql("select field1,field2,field3,field4 from baodan where field5 > 100").show
  //  //广播变量
  //  sparkContext.broadcast(df)
  //
  //  sc.sql("select * from student_t").show()

//  val autodb="dmgr"
//  val mydb="idsrpt"
  val hc=new HiveContext(sparkContext)
//  val result=hc.sql(s"select * from $autodb.ids_agreement_t where workdate > 20190101")
//  //注册临时表，存在内存中，Application结束就没了
//  result.registerTempTable("baodan_tmp")
//  //在hive中建物理表
//  result.saveAsTable(s"$mydb.baodan_t")
//  //直接访问hive表
//  hc.table(s"$mydb.baodan_t")

  //jdbc
//  val jdbcDF = sc.read.format("jdbc").options(
//    Map("url" -> "jdbc:mysql://spark1:3306/testdb",
//      "dbtable" -> "students")).load()



  sparkContext.stop()
}
