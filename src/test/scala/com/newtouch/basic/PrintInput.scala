package com.newtouch.basic

object PrintInput {
  def main(args: Array[String]): Unit = {
    //print和println：print打印时不会加换行符，而println打印时会加一个换行符。
    print("Hello World"); println("Hello World")

    //printf：printf可以用于进行格式化
    printf("Hi, my name is %s, I'm %d years old.\n", "Leo", 30)

    //readLine: readLine允许我们从控制台读取用户输入的数据，类似于java中的System.in和Scanner的作用。
    val name = readLine("Welcome to Game House. Please tell me your name: ")
    print("Thanks. Then please tell me your age: ")
    val age = readInt()
    if(age > 18) {
      printf("Hi, %s, you are %d years old, so you are legel to come here!", name, age)
    } else {
      printf("Sorry, boy, %s, you are only %d years old. you are illegal to come here!", name, age)
    }



  }

}
