package com.newtouch.basic

object Function {

  //方法重载
  def sayHello(name: String, age: Int) = {
    if (age > 18) {
      printf("hi %s, you are a big boy\n", name); age//返回值age不需要额外写return
    }
    else {
      printf("hi %s, you are a little boy\n", name); age
    }
  }
  //单行函数
  def sayHello(name: String) = print("Hello, " + name)
  //scala的方法不需要刻意去写返回值类型，系统会自动识别
  def sayHello(name: String,context:String) = "Hello, " + name+" "+context


  //递归实现经典的斐波那契数列
  def fab(n: Int): Int = {
    if(n <= 1) 1
    else fab(n - 1) + fab(n - 2)
  }
  //默认值方法
  def sayHello(firstName: String, middleName: String = "William", lastName: String = "Croft") = firstName + " " + middleName + " " + lastName

  //变长参数
  def sum(nums: Int*) = {
    var res = 0
    for (num <- nums) res += num
    res
  }
  def sum2(nums: Int*): Int = {
    if (nums.length == 0) 0
    else nums.head + sum2(nums.tail: _*)
  }

  def main(args: Array[String]): Unit = {
    sayHello("leo", 30)

    //在调用函数时，也可以不按照函数定义的参数顺序来传递参数，而是使用带名参数的方式来传递。
    sayHello(firstName = "Mick", lastName = "Nina", middleName = "Jack")

    //还可以混合使用未命名参数和带名参数，但是未命名参数必须排在带名参数前面。
    sayHello("Mick", lastName = "Nina", middleName = "Jack")

    //使用序列调用变长参数
    val ss = sum(1 to 5: _*)






  }


}







