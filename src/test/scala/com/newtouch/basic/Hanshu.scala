package com.newtouch.basic

object Hanshu extends App {
  //  将函数赋值给变量
  def sayHello(name: String) {
    println("Hello, " + name)
  }

  val sayHelloFunc1 = sayHello _
  sayHelloFunc1("leo")

  //匿名函数
  val sayHelloFunc2 = (name: String) => println("Hello, " + name)

  //高阶函数
  val sayHelloFunc3 = (name: String) => println("Hello, " + name)

  def greeting(func: (String) => Unit, name: String) {
    func(name)
  }

  greeting(sayHelloFunc3, "leo")

  Array(1, 2, 3, 4, 5).map((num: Int) => num * num)

  // 高阶函数的另外一个功能是将函数作为返回值
  def getGreetingFunc(msg: String) = (name: String) => println(msg + ", " + name)

  val greetingFunc = getGreetingFunc("hello")
  greetingFunc("leo")

  def greeting1(func: (String) => Unit, name: String) {
    func(name)
  }

  greeting1((name: String) => println("Hello, " + name), "leo")
  greeting1((name) => println("Hello, " + name), "leo")
  greeting1(name => println("Hello, " + name), "leo")

  def triple(func: (Int) => Int) = {
    func(3)
  }

  triple(3 * _)

  //常用高阶函数
  // map: 对传入的每个元素都进行映射，返回一个处理后的元素
  Array(1, 2, 3, 4, 5).map(2 * _)

  // foreach: 对传入的每个元素都进行处理，但是没有返回值
  (1 to 9).map("*" * _).foreach(println _)

  // filter: 对传入的每个元素都进行条件判断，如果对元素返回true，则保留该元素，否则过滤掉该元素
  (1 to 20).filter(_ % 2 == 0)

  // reduceLeft: 从左侧元素开始，进行reduce操作，即先对元素1和元素2进行处理，然后将结果与元素3处理，再将结果与元素4处理，依次类推，即为reduce；reduce操作必须掌握！spark编程的重点！！！
  // 下面这个操作就相当于1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9
  (1 to 9).reduceLeft(_ * _)

  // sortWith: 对元素进行两两相比，进行排序
  Array(3, 2, 5, 4, 10, 1).sortWith(_ < _)


  //闭包
  def getGreetingFunc1(msg: String) = (name: String) => println(msg + ", " + name)

  getGreetingFunc1("hello")("小明")


  //SAM转换
  import javax.swing._
  import java.awt.event._

  val button = new JButton("Click")
  button.addActionListener(new ActionListener {
    override def actionPerformed(event: ActionEvent) {
      println("Click Me!!!")
    }
  })

  implicit def getActionListener(actionProcessFunc: (ActionEvent) => Unit) = new ActionListener {
    override def actionPerformed(event: ActionEvent) {
      actionProcessFunc(event)
    }
  }

  button.addActionListener((event: ActionEvent) => println("Click Me!!!"))

  //柯理化
  def sum(a: Int, b: Int) = a + b
  sum(1, 1)

  def sum2(a: Int) = (b: Int) => a + b
  sum2(1)(1)

  def sum3(a: Int)(b: Int) = a + b
  sum3(1)(1)

  //return  用于匿名函数中
  def greeting(name: String) = {
    def sayHello(name: String):String = {
      return "Hello, " + name
    }
    sayHello(name)
  }


}
