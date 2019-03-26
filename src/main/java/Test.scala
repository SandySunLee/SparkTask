import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

import com.cpic.udf.UserDefinedFunctions
import com.cpic.utils.{DateUtils, Dates, Files}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.actors.Actor
import scala.util.control.Breaks._


object Test extends App {
  val set1="hour to second".trim.toUpperCase
  println(set1.split("(\\s)+TO(\\s)+").toBuffer)
//    replaceAll("\\w+","x")

  //  println(UserDefinedFunctions.initcap("a aBc ab")     +"--------------")
  //  val logger = LoggerFactory.getLogger(this.getClass)
  //  val decimals = -3
  //  val number= "34512.345"
  //  val num= number.toString.split("\\.")
  //  val result=num(0).substring(0,num(0).length+decimals)+"0"*decimals.abs
  //  println(result)
//    println(UserDefinedFunctions.trunc(12345.6789,0))
  //  println(UserDefinedFunctions.trunc(12345.6789,1))
  //  println(UserDefinedFunctions.trunc(12345.6789,2))
  //  println(UserDefinedFunctions.trunc(12345.6789,3))
  //  println(UserDefinedFunctions.trunc(12345.6789,4))
  //  println(UserDefinedFunctions.trunc(12345.6789,5))
  //  println(UserDefinedFunctions.trunc(12345.6789,6))
  //  println(UserDefinedFunctions.trunc(12345.6789,-1))
  //  println(UserDefinedFunctions.trunc(12345.6789,-2))
  //  println(UserDefinedFunctions.trunc(12345.6789,-3))
  //  println(UserDefinedFunctions.trunc(12345.6789,-4))
  //  println(UserDefinedFunctions.trunc(12345.6789,-5))
  //  println(UserDefinedFunctions.trunc(12345.6789,-6))
  //  val files=Files.getAllFiles(new File("D:\\myjava"))
  //  for(file <- files){
  //    println(file)
  //  }

  //异步执行
  //  MyActor1.start()
  //  MyActor2.start()
  //  val actor = new YourActor
  //  actor.start()
  //  actor ! "start"
  //  actor ! "stop"
  //  logger.info("消息发送完成！")
}


//class MyActor extends Actor {
//  override def act(): Unit = {
//    loop {
//      react {
//        case "start" => {
//          println("starting ...")
//          Thread.sleep(5000)
//          println("started")
//        }
//        case "stop" => {
//          println("stopping ...")
//          Thread.sleep(8000)
//          println("stopped ...")
//        }
//      }
//    }
//  }
//
//}
//
//object MyActor {
//  def main(args: Array[String]) {
//    val actor = new MyActor
//    actor.start()
//    actor ! "start"
//    actor ! "stop"
//    println("消息发送完成！")
//  }
//}
//
//class YourActor extends Actor {
//  val logger = LoggerFactory.getLogger(this.getClass)
//
//  override def act(): Unit = {
//    loop {
//      react {
//        case "start" => {
//          for (i <- 0 to 10) {
//            logger.info("start" + i)
//            Thread.sleep(1000)
//          }
//        }
//        case "stop" => {
//          for (i <- 0 to 10) {
//            logger.info("stop" + i)
//            Thread.sleep(1000)
//          }
//        }
//      }
//    }
//  }
//}
//
//
//object MyActor1
//  extends Actor {
//  //重新act方法
//  def act() {
//    for (i <- 1 to 10) {
//      println("actor-1 " + i)
//      Thread.sleep(2000)
//    }
//  }
//}
//
//object MyActor2 extends Actor {
//  //重新act方法
//  def act() {
//    for (i <- 1 to 10) {
//      println("actor-2 " + i)
//      Thread.sleep(2000)
//    }
//  }
//}
//
//object MyActor3
//  extends Actor {
//  //重新act方法
//  def act() {
//    for (i <- 1 to 10) {
//      println("actor-3 " + i)
//      Thread.sleep(2000)
//    }
//  }
//}
//
//object MyActor4 extends Actor {
//  //重新act方法
//  def act() {
//    for (i <- 1 to 10) {
//      println("actor-4 " + i)
//      Thread.sleep(2000)
//    }
//  }
//}