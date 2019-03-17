package com.newtouch.oo

import java.io.IOException
//类
class Student{//主构造器
  //主构造器会执行类定义中的所有语句
  println("执行主构造器")

  try {
    println("读取文件")
    throw new IOException("io exception")
  } catch {
    case e: NullPointerException => println("打印异常Exception : " + e)
    case e: IOException => println("打印异常Exception : " + e)
  } finally {
    println("执行finally部分")
  }

  private var gender = "male"

  //用this关键字定义辅助构造器
//  def this(name: String, age: Int, gender: String){
//    //每个辅助构造器必须以主构造器或其他的辅助构造器的调用开始
//    this(name, age)
//    println("执行辅助构造器")
//    this.gender = gender
//  }
  //自定义get、set方法
  private var myName = "leo"
  def name = "your name is " + myName
  def name_=(newValue: String)  {
    print("you cannot edit your name!!!")
  }

  //仅暴露field的getter方法
  private var myAddress = "hubei"
  def updateName(newAddress: String) {
    if(myAddress == "hubei") myAddress = newAddress
    else print("not accept this new address!!!")
  }
  def address = "your address is " + myAddress

  //对象私有的field，只有本对象内可以访问到
  private[this] val favirate:String="play"
}
//单例对象  //伴生对象
object Student extends App {//应用程序对象

}