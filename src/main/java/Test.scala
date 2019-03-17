import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.actors.Actor
import scala.util.control.Breaks._

class Test

object Test extends App {
  val logger = LoggerFactory.getLogger(this.getClass)
  //异步执行
  //  MyActor1.start()
  //  MyActor2.start()
  val actor = new YourActor
  actor.start()
  actor ! "start"
  actor ! "stop"
  logger.info("消息发送完成！")
}


class MyActor extends Actor {
  override def act(): Unit = {
    loop {
      react {
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(8000)
          println("stopped ...")
        }
      }
    }
  }

}

object MyActor {
  def main(args: Array[String]) {
    val actor = new MyActor
    actor.start()
    actor ! "start"
    actor ! "stop"
    println("消息发送完成！")
  }
}

class YourActor extends Actor {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def act(): Unit = {
    loop {
      react {
        case "start" => {
          for (i <- 0 to 10) {
            logger.info("start" + i)
            Thread.sleep(1000)
          }
        }
        case "stop" => {
          for (i <- 0 to 10) {
            logger.info("stop" + i)
            Thread.sleep(1000)
          }
        }
      }
    }
  }
}


object MyActor1
  extends Actor {
  //重新act方法
  def act() {
    for (i <- 1 to 10) {
      println("actor-1 " + i)
      Thread.sleep(2000)
    }
  }
}

object MyActor2 extends Actor {
  //重新act方法
  def act() {
    for (i <- 1 to 10) {
      println("actor-2 " + i)
      Thread.sleep(2000)
    }
  }
}

object MyActor3
  extends Actor {
  //重新act方法
  def act() {
    for (i <- 1 to 10) {
      println("actor-3 " + i)
      Thread.sleep(2000)
    }
  }
}

object MyActor4 extends Actor {
  //重新act方法
  def act() {
    for (i <- 1 to 10) {
      println("actor-4 " + i)
      Thread.sleep(2000)
    }
  }
}