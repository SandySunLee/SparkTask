package com.cpic.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.cpic.constant.PublicConstant;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.hadoop.hbase.util.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件变化监听器
 * <p>
 * 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
 * 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
 * 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
 * 修改原来文件名，则会调用修改新增删除三个方法
 */
public class FileListener extends FileAlterationListenerAdaptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String readuser;

    public void setReaduser() {
        this.readuser = PublicConstant.readuser();
    }

    /**
     * 监控的目录
     */
    private String dir = "/home/" + readuser;

    /**
     * 新增文件
     * @param file
     */
    public void onFileCreate(File file) {
        String newpath=file.getAbsolutePath();
        logger.warn("新增文件" + newpath);
        moveFile(newpath);
    }

    /**
     * 修改文件
     * @param file
     */
    public void onFileChange(File file) {
        String newpath=file.getAbsolutePath();
        logger.warn("修改文件" + newpath);
        moveFile(newpath);
    }

    /**
     * 移动文件并赋权
     * @param newpath
     */
    private void moveFile(String newpath){
        String childpath=newpath.replaceAll(dir,"");
        String fileName=Files.getFileName(childpath);
        String newnewpath=PublicConstant.idsrpt_home()+File.separator+childpath.replaceAll(fileName,"")+fileName;
        if(new File(newnewpath).exists()){
            Files.fixFileName(newnewpath);
        }
        Files.copyFile(newpath,newnewpath);
        ThreadUtil.chmod(newnewpath);
        logger.info(newpath+"has been copy to "+newnewpath);
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        logger.warn("删除文件" + file.getAbsolutePath());
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        logger.warn("新建目录" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        logger.warn("修改目录" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        logger.warn("删除目录" + directory.getAbsolutePath());
    }

    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStart(observer);
    }

    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStop(observer);
    }

    public static void main(String[] args) {
        // 监控目录
        String rootDir = "D:\\myjava";
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(1);
        // 创建过滤器
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"));
        IOFileFilter filter = FileFilterUtils.or(directories, files);
        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
        //不使用过滤器
//        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
        observer.addListener(new FileListener());
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
