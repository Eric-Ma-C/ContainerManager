package zju.cst.eric;

import zju.cst.eric.controller.ControlCenter;
import zju.cst.eric.network.ContainerTcpClient;
import zju.cst.eric.utils.TimeUtils;

/**
 * @Date: 2020/1/11 14:57
 * @Author: EricMa
 * @Description: 主入口
 */
public class ContainerManager {
    public static void main(String[] args) {
        ControlCenter.task1();
    }
}
