package zju.cst.eric;

import zju.cst.eric.utils.TimeUtils;

/**
 * @Date: 2020/1/11 15:27
 * @Author: EricMa
 * @Description:集中异常处理
 */
public class ExceptionHandler {

    public static void handle(Exception e) {
        /** 得到异常棧的首个元素 */
        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        System.out.println("Exception at "
            + stackTraceElement.getClassName() + "-->"
            + stackTraceElement.getMethodName() + "  "
            + "line " + stackTraceElement.getLineNumber()
            + "    " + TimeUtils.getTimeStr());

        e.printStackTrace();
    }

    public static void handle(Exception e, String info) {
        System.out.println(info);
        handle(e);
    }
}
