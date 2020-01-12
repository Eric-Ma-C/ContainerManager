package zju.cst.eric.shell;

/**
 * @Date: 2020/1/11 21:48
 * @Author: EricMa
 * @Description: 一条指令
 */
class RealtimeProcessCommand{
    private String directory = null;
    private String[] cmdWords = null;
    public RealtimeProcessCommand(){}

    public void setDirectory(String directory){
        this.directory = directory;
    }
    public void setCmdWords(String[] cmdWords){
        this.cmdWords = cmdWords;
    }
    public String getDirectory(){
        return this.directory;
    }
    public String[] getCmdWords(){
        return this.cmdWords;
    }

}