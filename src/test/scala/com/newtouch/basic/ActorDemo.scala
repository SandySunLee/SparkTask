package com.newtouch.basic

//import scala.actors.Actor
import akka.actor.{Actor, ActorSystem, Props}

import scala.util.control.Breaks._
//普通Actor
//class ActorDemo extends Actor{
//  override def act(): Unit = {
//    breakable{
//      while (true) {
//        receive {
//          case name: String => println("Hello, " + name)
//          case _ => break
//        }
//      }
//    }
//  }
//}
//
//object ActorDemo extends App{
//  val actor=new ActorDemo
//  actor.start()
//  actor ! "leo"
//  actor ! "liu"
//  actor ! 0
//}
/////////////////////////////////////////////////////////////////////////////////////////////
//Akka的Actor
class ActorDemo extends Actor{
  override def receive: Receive = {
    case "START" => {
      println("开始")
      Thread.sleep(10000)
      println("started")
    }
    case "STOP" => {
      println("结束！")
      System.exit(0)
    }
  }
}

object ActorDemo extends App{
  val system = ActorSystem("HelloSystem")
  // 缺省的Actor构造函数
  val helloActor = system.actorOf(Props[ActorDemo], name = "helloactor")

  helloActor ! "START"
  helloActor ! "STOP"

}