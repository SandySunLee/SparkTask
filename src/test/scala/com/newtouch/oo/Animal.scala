package com.newtouch.oo

trait Animal {
  val name:String
}

abstract class Test{
  def call
}

class Dog (val name:String="",val age:Int=0){
  def getName = name
  protected val jj:String="11"
}

class HaShiQi extends Dog with Animal {
  private var score = "A"
  def getScore = score
  //重写方法
  override def getName = "Hi, I'm " + super.getName

  //重写字段
  override val name: String = "hashiqi"
  override val age: Int = 3

  //匿名内部类
//  val xx=new HaShiQi{
//
//  }

}

object Test {
  def main(args: Array[String]): Unit = {
    val hsq:Dog=new HaShiQi()
    val s:HaShiQi=null
//    if(hsq.isInstanceOf[Dog]) s=hsq.asInstanceOf[Dog]
    println(hsq.isInstanceOf[Dog])
    println(hsq.asInstanceOf[Dog])
    println(hsq.getClass)
    println(classOf[HaShiQi])
  }
}