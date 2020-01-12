package zju.cst.eric.network.message;

import java.util.PrimitiveIterator;

/**
 * @Date: 2020/1/9 22:02
 * @Author: EricMa
 * @Description: 消息意图,主题
 */
public enum Intent {
        /** deamon */
        deamonReconnect("deamonReconnect"),
        /** 请求conda环境文件 */
        getCondaEnvFileByTaskId("getCondaEnvFileByTaskId"),
        /** 环境配置失败信息 */
        condaInstallError("condaInstallError"),
        /** 指令执行结果 */
        executeInstruction("executeInstruction");

        private String action;


        private Intent(String action) {
                this.action=action;
        }

        public String getAction() {
                return action;
        }
}
