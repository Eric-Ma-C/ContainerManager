package zju.cst.eric.controller;

import zju.cst.eric.ExceptionHandler;
import zju.cst.eric.network.ContainerTcpClient;
import zju.cst.eric.shell.ShellTask;
import zju.cst.eric.utils.ShellUtils;

/**
 * @Date: 2020/1/11 20:30
 * @Author: EricMa
 * @Description: 控制中心
 */
public class ControlCenter {
    private static final int MINIMUM_TCP_CONNECTION_INTERVAL = 100000;
    /** deamon检查tcp重连时间 60s*/
    private static final int DEAMON_RECONNECTION_INTERVAL = 60*1000;
    private ContainerTcpClient mContainerTcpClient;

    private static class ControlCenterHolder {
        private static final ControlCenter INSTANCE = new ControlCenter();
    }

    private ControlCenter() {
        mContainerTcpClient=ContainerTcpClient.getInstance();
    }

    public static ControlCenter getInstance() {
        return ControlCenterHolder.INSTANCE;
    }

    /** 测试任务 */
    public void task1(){
        while (true) {


            String path="";
            try {
                path=mContainerTcpClient.getCondaEnvFileByTaskId("123");
            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }

            path="/root/tmp/tf-gpu.yml";

//            ShellUtils.CommandResult result=ShellUtils.execCommand("conda env create -f "+path);
//            ShellUtils.CommandResult result=ShellUtils.execCommand("pwd");
//            System.out.println(result.toString());





            ShellTask shellTask=new ShellTask("conda env create -f "+path);
//            ShellTask shellTask=new ShellTask("ping www.baidu.com","/bin");

            shellTask.exec();


            /** 休眠 */
            try {
                Thread.sleep(MINIMUM_TCP_CONNECTION_INTERVAL);
            } catch (InterruptedException e) {
                ExceptionHandler.handle(e);
            }
        }
    }


    /** 守护进程deamon
     * 每隔1分钟检查一次tcp连接,
     * 若断开,则重新发起tcp连接 */
    public void deamon(){
        while (true) {

            if (mContainerTcpClient.isConnected())
            String path="";
            try {
                path=mContainerTcpClient.deamonReconnect("123");
            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }




            ShellTask shellTask=new ShellTask("conda env create -f "+path);
//            ShellTask shellTask=new ShellTask("ping www.baidu.com","/bin");

            shellTask.exec();


            /** 休眠 */
            try {
                Thread.sleep(DEAMON_RECONNECTION_INTERVAL);
            } catch (InterruptedException e) {
                ExceptionHandler.handle(e);
            }
        }
    }
}
