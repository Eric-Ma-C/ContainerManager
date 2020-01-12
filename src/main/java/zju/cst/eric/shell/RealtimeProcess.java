package zju.cst.eric.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * @Date: 2020/1/11 21:45
 * @Author: EricMa
 * @Description:
 */

public class RealtimeProcess{
    /** 是否在执行 */
    private boolean isRunning = false;
    /** 存放命令行 */
    private RealtimeProcessCommand mRealtimeProcessCommand = new RealtimeProcessCommand();
    /** 保存所有的输出信息 */
    private StringBuffer mStringBuffer = new StringBuffer();
    private ProcessBuilder mProcessBuilder = null;
    private BufferedReader readStdout = null;
    private BufferedReader readStderr = null;
    /** 回调用到的接口 */
    private RealtimeProcessInterface mInterface = null;
    private int resultCode = 0;
    private String ROOT_DIR = null;
    private String tmp1 = null;
    private String tmp2 = null;


    public RealtimeProcess(RealtimeProcessInterface mInterface){
        // 实例化接口对象
        this.mInterface = mInterface;
    }
    public void setCommand(String ...commands){
        // 遍历命令
//        for(String cmd : commands){
//            RealtimeProcessCommand mRealtimeProcessCommand = new RealtimeProcessCommand();
            if(ROOT_DIR != null) {
                mRealtimeProcessCommand.setDirectory(ROOT_DIR);
            }
            mRealtimeProcessCommand.setCmdWords(commands);

//        }
    }
    public void setDirectory(String directory){
        this.ROOT_DIR = directory;
    }
    public void start() throws IOException, InterruptedException{
        isRunning = true;
//        for(RealtimeProcessCommand mRealtimeProcessCommand : commandList){
            mProcessBuilder = new ProcessBuilder();
            if(ROOT_DIR != null) {
                mProcessBuilder.directory(new File(mRealtimeProcessCommand.getDirectory()));
            }
            // 不重定向错误输出
            mProcessBuilder.redirectErrorStream(false);
            mProcessBuilder.command(mRealtimeProcessCommand.getCmdWords());
            exec(mProcessBuilder.start());
//        }
    }
    public String getAllResult(){
        return mStringBuffer.toString();
    }

    private void exec(final Process process) throws InterruptedException{
        // 获取标准输出
        readStdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 获取错误输出
        readStderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        // 创建线程执行
        Thread execThread = new Thread(){
            @Override
            public void run(){
                try {
                    // 逐行读取
                    while((tmp1 = readStdout.readLine()) != null || (tmp2 = readStderr.readLine()) != null){
                        if(tmp1 != null){
                            mStringBuffer.append(tmp1 + "\n");
                            // 回调接口方法
                            mInterface.onNewStdoutListener(tmp1);
                        }
                        if(tmp2 != null){
                            mStringBuffer.append(tmp2 + "\n");
                            mInterface.onNewStderrListener(tmp2);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                resultCode = process.exitValue();
            }
        };
        execThread.start();
        execThread.join();
        isRunning = false;
        mInterface.onProcessFinish(resultCode);
    }
    public boolean isRunning(){
        return this.isRunning;
    }

}


