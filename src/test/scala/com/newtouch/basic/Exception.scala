package com.newtouch.basic

import java.io.IOException

object Exception {

  def main(args: Array[String]): Unit = {
    try {
      throw new IllegalArgumentException("x should not be negative")
    } catch {
      case _: IllegalArgumentException => println("Illegal Argument!")
    } finally {
      print("release resources!")
    }

    try {
      throw new IOException("user defined exception")
    } catch {
      case e1: IllegalArgumentException => println("illegal argument")
      case e2: IOException => println("io exception")
    }

  }
}
