package com.newtouch.basic

object ViewBounds extends App{
  //泛型函数
  def getCard[T](content: T) = {
    if(content.isInstanceOf[Int]) "card: 001, " + content
    else if(content.isInstanceOf[String]) "card: this is your card, " + content
    else "card: " + content
  }


}
//泛型
//class Student[T](val localId: T) {
//  def getSchoolId(hukouId: T) = "S-" + hukouId + "-" + localId
//}

//上界
class Person(val name: String) {
  def sayHello = println("Hello, I'm " + name)
  def makeFriends(p: Person) {
    sayHello
    p.sayHello
  }
}
class Student(name: String) extends Person(name)
class Party[T <: Person](p1: T, p2: T) {
  def play = p1.makeFriends(p2)
}
//下界
class Father(val name: String)
class Child(name: String) extends Father(name){
  def getIDCard[R >: Child](person: R) {
    if (person.getClass == classOf[Child]) println("please tell us your parents' names.")
    else if (person.getClass == classOf[Father]) println("sign your name for your child's id card.")
    else println("sorry, you are not allowed to get id card.")
  }
}
//内置比较器
class Calculator[T: Ordering] (val number1: T, val number2: T) {
  def max(implicit order: Ordering[T]) = if(order.compare(number1, number2) > 0) number1 else number2
}



object ImplicitContext{
  //implicit def girl2Ordered(g : Girl) = new Ordered[Girl]{
  //  override def compare(that: Girl): Int = if (g.faceValue > that.faceValue) 1 else -1
  //}

  implicit object OrderingGirl extends Ordering[Girl]{
    override def compare(x: Girl, y: Girl): Int = if (x.faceValue > y.faceValue) 1 else -1
  }
}

class Girl(var name: String, var faceValue: Double){
  override def toString: String = s"name : $name, faveValue : $faceValue"
}

//class MissRight[T <% Ordered[T]](f: T, s: T){
//  def choose() = if(f > s) f else s
//}
//class MissRight[T](f: T, s: T){
//  def choose()(implicit ord: T => Ordered[T]) = if (f > s) f else s
//}

class MissRight[T: Ordering](val f: T, val s: T){
  def choose()(implicit ord: Ordering[T]) = if(ord.gt(f, s)) f else s
}

object MissRight {
  def main(args: Array[String]) {
    import ImplicitContext.OrderingGirl
    val g1 = new Girl("yuihatano", 99)
    val g2 = new Girl("jzmb", 98)
    val mr = new MissRight(g1, g2)
    val result = mr.choose()
    println(result)
  }
}
