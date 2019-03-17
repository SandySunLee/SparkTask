package com.newtouch.basic

import scala.collection.mutable.ArrayBuffer

object ArrayDemo {

  def main(args: Array[String]): Unit = {
    //初始化一个长度为8的定长数组，其所有元素均为0
    val arr1 = new Array[Int](8)
    //直接打印定长数组，内容为数组的hashcode值
    println(arr1)

    //将数组转换成数组缓冲，就可以看到原数组中的内容了
    //toBuffer会将数组转换长数组缓冲
    println(arr1.toBuffer)

    //注意：如果new，相当于调用了数组的apply方法，直接为数组赋值
    //初始化一个长度为1的定长数组
    val arr2 = Array[Int](10)
    println(arr2.toBuffer)

    //定义一个长度为3的定长数组
    val arr3 = Array("hadoop", "storm", "spark")
    //使用()来访问元素
    println(arr3(2))

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //变长数组（数组缓冲）
    //如果想使用数组缓冲，需要导入import scala.collection.mutable.ArrayBuffer包
    val ab = ArrayBuffer[Int]()
    //向数组缓冲的尾部追加一个元素
    //+=尾部追加元素
    ab += 1
    //追加多个元素
    ab += (2, 3, 4, 5)
    //追加一个数组++=
    ab ++= Array(6, 7)
    //追加一个数组缓冲
    ab ++= ArrayBuffer(8,9)
    //打印数组缓冲ab

    //在数组某个位置插入元素用insert
    ab.insert(0, -1, 0)
    //删除数组某个位置的元素用remove
    ab.remove(8, 2)
    println(ab)

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //遍历数组
    //初始化一个数组
    val arr = Array(1,2,3,4,5,6,7,8)
    //增强for循环
    for(i <- arr)
      println(i)
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //好用的until会生成一个Range
    //reverse是将前面生成的Range反转
    //数组逆序输出
    for(i <- (0 until arr.length).reverse)
      println(arr(i))
    println(arr.sortWith(_ > _).toBuffer)

    //数组快速排序
    import scala.util.Sorting.quickSort
    quickSort(arr)
    println(arr.toBuffer.reverse)

    println(arr.sortBy(x=>x).toBuffer)

    //常用方法
    arr.max
    arr.min
    arr.sum
    // 获取数组中所有元素内容
    arr.mkString
    arr.mkString(", ")
    arr.mkString("<", ",", ">")
    // toString函数
    arr.toString
    arr.toString

    arr.length

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //数组转化
    // 对Array进行转换，获取的还是Array
    val a = Array(1, 2, 3, 4, 5)
    val a2 = for (ele <- a) yield ele * ele
    // 对ArrayBuffer进行转换，获取的还是ArrayBuffer
    val b = ArrayBuffer[Int]()
    b += (1, 2, 3, 4, 5)
    val b2 = for (ele <- b) yield ele * ele
    // 结合if守卫，仅转换需要的元素
//    val a3 = for (ele <- if ele % 2 == 0) yield ele * ele

//    val a3=for(i <- arr){
//      var result=ArrayBuffer[Int]()
//      if(i %2 ==0) result+=i*2;
//      result
//    }

    // 使用函数式编程转换数组（通常使用第一种方式）
    a.filter(_ % 2 == 0).map(2 * _)
    a.filter { _ % 2 == 0 } map { 2 * _ }

  }

  //移除第一个负数之后的所有负数(效率低)
//  def removeFirstFs(arr:ArrayBuffer[Int])={
//    var foundFirstNegative = false
//    var arrayLength = arr.length
//    var index = 0
//    while (index < arrayLength) {
//      if (arr(index) >= 0) {
//        index += 1
//      } else {
//        if (!foundFirstNegative) { foundFirstNegative = true; index += 1 }
//        else { arr.remove(index); arrayLength -= 1 }
//      }
//    }
//    arr
//  }

  //改良版
  def removeFirstFs(a:ArrayBuffer[Int])={
    var foundFirstNegative = false
    val keepIndexes = for (i <- 0 until a.length if !foundFirstNegative || a(i) >= 0) yield {
      if (a(i) < 0) foundFirstNegative = true
      i
    }
    for (i <- 0 until keepIndexes.length) { a(i) = a(keepIndexes(i)) }
    a.trimEnd(a.length - keepIndexes.length)
    a
  }
}
















