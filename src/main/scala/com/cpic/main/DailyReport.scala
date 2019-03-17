package com.cpic.main

import scala.actors.Actor

object DailyReport extends App{
  // TODO: 校验数据
  Verify.verify()
  // TODO: 并行执行维度表
  // TODO: 并行执行宽表层
  // TODO: 并行执行指标层
  // TODO: 并行执行接口层

  // TODO: 异常自动重跑/成功则清掉日志
  // TODO: 钉钉通知
  // TODO: 重跑超时则全流程断掉



  // TODO: 下发数据

}
class ExeVeidoo extends Actor{
  override def act(): Unit = {
    // TODO: 并行执行维度表脚本
  }
}
class ExeEdw extends Actor{
  override def act(): Unit = {
    // TODO: 并行执行维度表脚本
  }
}
class ExeIndex extends Actor{
  override def act(): Unit = {
    // TODO: 并行执行维度表脚本
  }
}
class ExeReport extends Actor{
  override def act(): Unit = {
    // TODO: 并行执行维度表脚本
  }
}