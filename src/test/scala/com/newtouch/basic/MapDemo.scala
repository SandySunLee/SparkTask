package com.newtouch.basic

object MapDemo {
  def main(args: Array[String]): Unit = {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //不可变映射
    val ages1 = Map("Leo" -> 30, "Jen" -> 25, "Jack" -> 23)


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //可变映射
    val ages2 = scala.collection.mutable.Map("Leo" -> 30, "Jen" -> 25, "Jack" -> 23)
    ages2("Leo") = 31

    // 使用另外一种方式定义Map元素
    val ages3 = Map(("Leo", 30), ("Jen", 25), ("Jack", 23))

    // 创建一个空的HashMap
    val ages4 = new scala.collection.mutable.HashMap[String, Int]



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 获取指定key对应的value，如果key不存在，会报错
    try {
      val leoAge2 = ages1("leo")
    } catch {
      case _ : Exception => println("Exception in thread \"main\" java.util.NoSuchElementException: key not found: leo")
    }


    // 使用contains函数检查key是否存在
    val leoAge3 = if (ages1.contains("leo")) ages1("leo") else 0
    // getOrElse函数
    val leoAge4 = ages1.getOrElse("leo", 0)

    // 更新Map的元素
    ages2("Leo") = 31
    // 增加多个元素
    ages2 += ("Mike" -> 35, "Tom" -> 40)
    // 移除元素
    ages2 -= "Mike"

    // 更新不可变的map
    val ages5 = ages2 + ("Mike" -> 36, "Tom" -> 40)
    // 移除不可变map的元素
    val ages6 = ages2 - "Tom"



    // 遍历map的entrySet
    for ((key, value) <- ages1) println(key + " " + value)
    // 遍历map的key
    for (key <- ages1.keySet) println(key)
    for (key <- ages1.keys) println(key)
    // 遍历map的value
    for (value <- ages1.values) println(value)
    // 生成新map，反转key和value
    for ((key, value) <- ages1) yield (value, key)

    //toMap
    val arr=Array(('a',1),('b',2),('c',3))
    arr.toMap
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // SortedMap可以自动对Map的key的排序
    val ages = scala.collection.immutable.SortedMap("leo" -> 30, "alice" -> 15, "jen" -> 25)

    // LinkedHashMap可以记住插入entry的顺序
    val ages0 = new scala.collection.mutable.LinkedHashMap[String, Int]
    ages0("leo") = 30
    ages0("alice") = 15
    ages0("jen") = 25

  }

}










