package zju.cst.eric.shell;

import zju.cst.eric.ExceptionHandler;

import java.util.List;

/**
 * @Date: 2020/1/11 21:49
 * @Author: EricMa
 * @Description: 非阻塞 execute shell command
 */
public class ShellTask implements RealtimeProcessInterface {

    private RealtimeProcess mRealtimeProcess = null;
    private String[] command;
    private String cmdDir;

    public ShellTask(String cmd,String cmdDir) {
        this.command = cmd.split("\\s+");
        this.cmdDir=cmdDir;
    }

    public ShellTask(String cmd) {
        this.command = cmd.split("\\s+");
        this.cmdDir="/";
    }

    public void exec() {
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setDirectory(cmdDir);
        mRealtimeProcess.setCommand(command);

        try {
            mRealtimeProcess.start();
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        //System.out.println(mRealtimeProcess.getAllResult());

    }


    @Override
    public void onNewStdoutListener(String newStdout) {
        System.out.println(newStdout);
    }

    @Override
    public void onNewStderrListener(String newStderr) {
        System.out.println("\n*** Error ***:"+newStderr);
    }

    @Override
    public void onProcessFinish(int resultCode) {
        System.out.println("\nFinished:"+resultCode);
    }
}
