package com.newtouch.basic

object CellectionDemo extends App {
  /////////////////////////////////////////////////////////////////////////////////////////////////////////
  //List
  val list1 = List(1, 2, 3, 4)
  def decorator(l: List[Int], prefix: String) {
    if (l != Nil) {
      println(prefix + l.head)
      decorator(l.tail, prefix)
    }
  }

  //linkedList
  // LinkedList代表一个可变的列表，使用elem可以引用其头部，使用next可以引用其尾部
  // val l = scala.collection.mutable.LinkedList(1, 2, 3, 4, 5); l.elem; l.next

  // 案例：使用while循环将LinkedList中的每个元素都乘以2
  val list2 = scala.collection.mutable.LinkedList(1, 2, 3, 4, 5)
  var currentList = list2
  while (currentList != Nil) {
    currentList.elem = currentList.elem * 2
    currentList = currentList.next
  }
  // 案例：使用while循环将LinkedList中，从第一个元素开始，每隔一个元素，乘以2
  val list3 = scala.collection.mutable.LinkedList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  var currentList3 = list3
  var first = true
  while (currentList3!= Nil && currentList3.next != Nil) {
    if (first) { currentList3.elem = currentList3.elem * 2; first = false }
    currentList3  = currentList3.next.next
    if (currentList3 != Nil) currentList3.elem = currentList3.elem * 2
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////
  //Set
  val s1 = new scala.collection.mutable.HashSet[Int](); s1 += 1; s1 += 2; s1 += 5

  // LinkedHashSet会用一个链表维护插入顺序，
  val s2 = new scala.collection.mutable.LinkedHashSet[Int](); s2 += 1; s2 += 2; s2 += 5

  // SrotedSet会自动根据key来进行排序，
  val s3 = scala.collection.mutable.SortedSet("orange", "apple", "banana")

  //统计多个文本内的单词总数
  try {
    // 使用scala的io包将文本文件内的数据读取出来
    val lines01 = scala.io.Source.fromFile("C://Users//Administrator//Desktop//test01.txt").mkString
    val lines02 = scala.io.Source.fromFile("C://Users//Administrator//Desktop//test02.txt").mkString
    // 使用List的伴生对象，将多个文件内的内容创建为一个List
    val lines = List(lines01, lines02)
    lines.flatMap(_.split(" ")).map((_, 1)).map(_._2).reduceLeft(_ + _)
  }catch {
    case _ :Exception =>println("IO 异常")
  }




}
