package com.cpic.main;

import com.cpic.constant.PublicConstant;
import com.cpic.utils.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Timers implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    /**
     * 每日定时任务
     */
    public void showDayTime() {
        Date sendDate = new Date();
        Timer dTimer = new Timer();
        dTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                if (hour == 6 && minutes == 0) {
                    logger.info("每日任务已执行");
                    ThreadUtil.exeShell1(PublicConstant.idsrpt_home() + File.separator + "crontab/exe_daily.sh");
                }
            }
        }, sendDate, 24 * 60 * 60 * 1000);//设置24小时执行一次
    }

    /**
     * 每月定时任务
     */
    public void showMonthTime() {
        Date sendDate = new Date();

        Timer dTimer = new Timer();
        dTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                if (day == 1 && hour == 6 && minutes == 0) {
                    logger.info("每月定时任务已执行");
                    ThreadUtil.exeShell1(PublicConstant.idsrpt_home() + File.separator + "crontab/exe_month.sh");
                }
            }
        }, sendDate, 24 * 60 * 60 * 1000);//24* 60* 60 * 1000

    }

}
