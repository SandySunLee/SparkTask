package com.newtouch.rdd

import org.apache.spark.{SparkConf, SparkContext}

object RDDdemo extends App {
  val conf=new SparkConf
  conf.setAppName("RDDtest").setMaster("local[4]")
  val sc=new SparkContext(conf)
  //创建RDD的两种方式
//  val rdd1=sc.textFile("file:\\D:\\test\\words.txt") //生产用
//  val rdd2=sc.parallelize(Array(1,2,3,4,5,6,7,8,9),2)//测试用
//
//  rdd1.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).sortBy(_._2,false).foreach(println(_))
  /**
    * 1、SparkContext.wholeTextFiles()方法，可以针对一个目录中的大量小文件，返回<filename, fileContent>组成的pair，作为一个PairRDD，而不是普通的RDD。普通的textFile()返回的RDD中，每个元素就是文件中的一行文本。
    * 2、SparkContext.sequenceFile[K, V]()方法，可以针对SequenceFile创建RDD，K和V泛型类型就是SequenceFile的key和value的类型。K和V要求必须是Hadoop的序列化类型，比如IntWritable、Text等。
    * 3、SparkContext.hadoopRDD()方法，对于Hadoop的自定义输入类型，可以创建RDD。该方法接收JobConf、InputFormatClass、Key和Value的Class。
    * 4、SparkContext.objectFile()方法，可以针对之前调用RDD.saveAsObjectFile()创建的对象序列化的文件，反序列化文件中的数据，并创建一个RDD。
    */

//  val rdd1 = sc.parallelize(List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10))
//  //对rdd1里的每一个元素乘2然后排序
//  val rdd2 = rdd1.map(_ * 2).sortBy(x => x, true)
//  //过滤出大于等于十的元素
//  val rdd3 = rdd2.filter(_ >= 10).toArray().toBuffer
//  println(rdd3)

//  val rdd1 = sc.parallelize(Array("a b c", "d e f", "h i j"))
//  //将rdd1里面的每一个元素先切分在压平
//  val rdd2 = rdd1.flatMap(_.split(' '))
//  rdd2.collect

//  val rdd1 = sc.parallelize(List(5, 6, 4, 3))
//  val rdd2 = sc.parallelize(List(1, 2, 3, 4))
//  //求并集
//  val rdd3 = rdd1.union(rdd2)
//    println(rdd3.toArray().toBuffer)
//  //求交集
//  val rdd4 = rdd1.intersection(rdd2)
//  println(rdd3.toArray().toBuffer)
//  //去重
//  rdd3.distinct.foreach(println(_))

//  val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 3), ("kitty", 2)))
//  val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
//  //求jion
//  val rdd3 = rdd1.join(rdd2).map(x=>(x._1,x._2)).toArray().toBuffer
//  println(rdd3)
//  //求并集
//  val rdd4 = rdd1 union rdd2
//  println(rdd4.reduceByKey(_+_).toArray().toBuffer)
//  //按key进行分组
//  val rdd5=rdd4.groupByKey.map(x=>(x._1,x._2.toArray.sum))
//    .toArray().toBuffer
//  println(rdd5)

//  val rdd1 = sc.parallelize(List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2)))
//  val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
//  //cogroup
//  val rdd3 = rdd1.cogroup(rdd2).toArray().toBuffer
//  //注意cogroup与groupByKey的区别
//  println(rdd3)

//  val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5))
//  //reduce聚合
//  val rdd2 = rdd1.reduce(_ + _)

  val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 3), ("kitty", 2),  ("shuke", 1)))
  val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 3), ("shuke", 2), ("kitty", 5)))
  val rdd3 = rdd1.union(rdd2)
  //按key进行聚合
  val rdd4 = rdd3.reduceByKey(_ + _)
  rdd4.collect
  //按value的降序排序
  val rdd5 = rdd4.map(t => (t._2, t._1)).sortByKey(false).map(t => (t._2, t._1))
  rdd5.collect


  //rdd保存
//  rdd1.saveAsObjectFile("")
//  rdd1.saveAsTextFile("")


}












