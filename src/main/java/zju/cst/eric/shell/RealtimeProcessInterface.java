package zju.cst.eric.shell;

/**
 * @Date: 2020/1/11 21:47
 * @Author: EricMa
 * @Description: todo:
 */
interface RealtimeProcessInterface{
    void onNewStdoutListener(String newStdout);
    void onNewStderrListener(String newStderr);
    void onProcessFinish(int resultCode);
    //void execCommand(String ...commands);
}