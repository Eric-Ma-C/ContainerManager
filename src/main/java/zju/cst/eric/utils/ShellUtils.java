package zju.cst.eric.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Date: 2020/1/11 19:42
 * @Author: EricMa
 * @Description: todo:
 */
public class ShellUtils {


    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit\n";
    private static final String COMMAND_LINE_END = "\n";

    static {
        if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
            System.out.println("window");
            COMMAND_SH = "cmd";
        } else {
            System.out.println("unix");
        }

    }



    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, true);
    }

    public static CommandResult execCommand(String command, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isNeedResultMsg);
    }

    /**
     * execute shell commands
     * {@link CommandResult#result} is -1, there maybe some excepiton.
     *
     * @param commands     command array
     * @param needResponse whether need result msg
     */
    public static CommandResult execCommand(String[] commands, final boolean needResponse) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, "空命令");
        }

        Process process = null;

        final StringBuilder successMsg = new StringBuilder();
        final StringBuilder errorMsg = new StringBuilder();

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            final BufferedReader successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final BufferedReader errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            //http://249wangmang.blog.163.com/blog/static/52630765201261334351635/
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (needResponse) {
                            String s;
                            while ((s = successResult.readLine()) != null) {
                                successMsg.append(s);
                                successMsg.append(LINE_SEPARATOR);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //启动两个线程,解决process.waitFor()阻塞问题
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (needResponse) {
                            String s;
                            while ((s = errorResult.readLine()) != null) {
                                errorMsg.append(s);
                                errorMsg.append(LINE_SEPARATOR);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            result = process.waitFor();
            if (errorResult != null) {
                errorResult.close();
            }
            if (successResult != null) {
                successResult.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }

        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
            : errorMsg.toString());
    }

    public static class CommandResult {

        public int result;
        public String responseMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String responseMsg, String errorMsg) {
            this.result = result;
            this.responseMsg = responseMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                "result=" + result +
                ", responseMsg='" + responseMsg + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
        }
    }

}
