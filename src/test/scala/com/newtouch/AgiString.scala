//package com.newtouch
//
//import java.io._
//import java.nio.channels._
//import java.nio._
//import java.net.{ URLEncoder, URLDecoder }
////import com.spreada.utils.chinese.ZHConverter
//import scala.collection.mutable.HashMap
//
//case class AgiString(value : String) {
//
//  private val base64encoder = new sun.misc.BASE64Encoder
//  private val base64decoder = new sun.misc.BASE64Decoder
//
//  private val desRegister = HashMap[String, Des]()
//  private val systemEncode = System.getProperty("file.encoding")
//
//  private def trueIndexOf(index : Int) : Int = {
//    val length = value.length
//    if (index < 0)
//      index + length
//    else if (index > length)
//      index - length
//    else
//      index
//  }
//
//  // 默认的urlencode按照UTF8
//  def encode : String = URLEncoder.encode(value, "UTF8")
//  def encode(enc : String) : String = URLEncoder.encode(value, enc)
//  def decode : String = URLDecoder.decode(value, "UTF8")
//  def decode(enc : String) : String = URLDecoder.decode(value, enc)
//  // 根据系统默认的编码进行encode，用于gui开发使用
//  def encodeSys : String = URLEncoder.encode(value, systemEncode)
//  def decodeSys : String = URLDecoder.decode(value, systemEncode)
//
//  def base64encode : String = base64encoder.encode(value.getBytes)
//  def base64decode : String = new String(base64decoder.decodeBuffer(value))
//
//  // 简繁转换
//  // 基于ZhConverter：http://code.google.com/p/java-zhconverter/
////  def toTraditional : String = ZHConverter.convert(value, ZHConverter.TRADITIONAL)
////  def toSimplified : String = ZHConverter.convert(value, ZHConverter.SIMPLIFIED)
//
//  // 获取DesUtil实例
//  private def getDesUtil(key : String) : Des = {
//    if (!desRegister.contains(key))
//      desRegister(key) = new Des(key)
//    desRegister(key)
//  }
//
//  // Des加密
//  def desEncrypt(key : String) : String = {
//    new String(getDesUtil(key).DesEncrypt(value.getBytes, 1))
//  }
//  def desEncryptBase64(key : String) : String = {
//    base64encoder.encode(getDesUtil(key).DesEncrypt(value.getBytes, 1))
//  }
//
//  // Des解密
//  def desDecrypt(key : String) : String = new String(getDesUtil(key).DesEncrypt(new String(value.getBytes, "utf8").getBytes, 0))
//  def desDecryptBase64(key : String) : String = new String(getDesUtil(key).DesEncrypt(base64decoder.decodeBuffer(value), 0))
//
//  // 驼峰命名
//  def toCamelCase : String = toCamelCase(true)
//  def toCamelCase(isFirstUpper : Boolean = true) : String = {
//    val b = new StringBuilder
//    var toUpperPos = -1
//    for (i <- 0.to(value.length - 1)) {
//      if (!value(i).isLetterOrDigit)
//        toUpperPos = i + 1
//      else
//        b.append(if (toUpperPos == i) value(i).toUpperCase else value(i))
//    }
//    val first = b.charAt(0)
//    if (isFirstUpper)
//      if (!first.isUpperCase)
//        b.setCharAt(0, first.toUpperCase)
//      else if (!first.isLowerCase)
//        b.setCharAt(0, first.toLowerCase)
//    b.mkString
//  }
//
//  def toUnCamcelCase(implicit sp : String = "_") : String = {
//    val b = new StringBuilder
//    var pos = -1
//    value.foreach { char =>
//      pos += 1
//      if (char.isLetterOrDigit) {
//        if (char.isUpperCase)
//          (if (pos == 0) b else b.append(sp)).append(char.toLowerCase)
//        else
//          b.append(char)
//      }
//    }
//    b.mkString
//  }
//
//  // 指定位置大写
//  def toUpperCase(index : Int) : String = {
//    val trueIndex = trueIndexOf(index)
//    val char = value.charAt(trueIndex)
//    if (!char.isUpper) {
//      value.updated(trueIndex, char.toUpperCase).mkString
//    } else
//      value
//  }
//  // 指定位置小写
//  def toLowerCase(index : Int) : String = {
//    val trueIndex = trueIndexOf(index)
//    val char = value.charAt(trueIndex)
//    if (!char.isLower) {
//      value.updated(trueIndex, char.toLowerCase).mkString
//    } else
//      value
//  }
//
//  def dump(file : File) : Boolean = {
//    try {
//      val opChannel = new FileOutputStream(file).getChannel
//      opChannel.write(ByteBuffer.wrap(value.getBytes))
//      opChannel.close
//      true
//    } catch {
//      case _ =>
//        System.err.println("Can't dump I/O error!")
//        false
//    }
//  }
//  def dump(name : String) : Boolean = dump(new File(name))
//
//  lazy private val md5handle = java.security.MessageDigest.getInstance("MD5")
//  private val hexDigits = Array[Char]('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
//
//  def md5 : String = {
//    val encrypt = md5handle.digest(value.getBytes)
//    val b = new StringBuilder(32)
//    for (i <- 0.to(15)) {
//      b.append(hexDigits(encrypt(i) >>> 4 & 0xf)).append(hexDigits(encrypt(i) & 0xf))
//    }
//    b.mkString
//  }
//
//  def jsonDecode : JsValue = JsValue.fromString(value)
//
//  def toEventName : String = {
//    val name = value.toLowerCase()
//    if (name.indexOf("on") == 0)
//      name.replace("on", "")
//    else
//      name
//  }
//}
