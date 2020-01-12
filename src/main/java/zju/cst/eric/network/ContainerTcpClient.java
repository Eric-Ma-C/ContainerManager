package zju.cst.eric.network;

import zju.cst.eric.ExceptionHandler;
import zju.cst.eric.network.message.Intent;
import zju.cst.eric.network.message.MessageBody;
import zju.cst.eric.utils.JsonUtils;
import zju.cst.eric.utils.TimeUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Date: 2020/1/7 15:26
 * @Author: EricMa
 * @Description: 容器端主动发起tcp请求
 */
public class ContainerTcpClient {
    private static final String SERVER_IP = "112.124.46.179";
    private static final int SERVER_PORT = 10001;
    /**
     * socket超时时间
     */
    private static final int SOCKET_TIMEOUT = 10 * 1000;
    /**
     * 消息字符编码格式
     */
    private static final String CHARSET_NAME = "UTF-8";
    /**
     * 结束字符串
     */
    private static final String END_STRING = "eof";


    /** tcp连接是否保持 */
    public boolean isConnected() {
    }

    private static class ContainerTcpClientHolder {
        private static final ContainerTcpClient INSTANCE = new ContainerTcpClient();
    }

    private ContainerTcpClient() {
    }

    public static ContainerTcpClient getInstance() {
        return ContainerTcpClientHolder.INSTANCE;
    }


    public String getCondaEnvFileByTaskId(String taskId) {

        MessageBody messageBody = new MessageBody(Intent.getCondaEnvFileByTaskId, taskId);
        String res="";
        try {
            res=connect(messageBody);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        MessageBody body=JsonUtils.parseObject(res,MessageBody.class);

        return "";

    }

    /**
     * tcp断开后,守护进程发起的tcp重连接
     * @param:
     * @return:
     */
    public String deamonReconnect(String taskId) {

        MessageBody messageBody = new MessageBody(Intent.deamonReconnect,"");
        String res="";
        try {
            res=connect(messageBody);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        MessageBody body=JsonUtils.parseObject(res,MessageBody.class);

        return body.toString();

    }

    /**
     * 发起一次tcp连接,拿到返回消息即断开连接
     */
    private String connect(MessageBody messageBody) throws Exception {
        //创建Socket对象
        Socket socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);


        //与服务端建立连接
        //建立连接后就可以往服务端写数据了
        Writer writer = new OutputStreamWriter(socket.getOutputStream(), CHARSET_NAME);
        writer.write(JsonUtils.toJSONString(messageBody));
        writer.write(END_STRING + "\n");
        writer.flush();


        //写完以后进行读操作
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
        //设置超时间
        socket.setSoTimeout(SOCKET_TIMEOUT);
        StringBuffer sb = new StringBuffer();
        String temp;
        int index;
        try {
            while ((temp = br.readLine()) != null) {
                if ((index = temp.indexOf(END_STRING)) != -1) {
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
        } catch (SocketTimeoutException e) {
            ExceptionHandler.handle(e,"数据读取超时。");
        }


//        System.out.println("服务端: " + sb);

        writer.close();
        br.close();
        socket.close();

        return sb.toString();
    }


    /**
     * 发起tcp长连接,直到主动断开连接,或服务器,网络异常断开
     */
    private String connectKeepAlive(MessageBody messageBody) throws Exception {
        //创建Socket对象
        Socket socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);


        //与服务端建立连接
        //建立连接后就可以往服务端写数据了
        Writer writer = new OutputStreamWriter(socket.getOutputStream(), CHARSET_NAME);
        writer.write(JsonUtils.toJSONString(messageBody));
        writer.write(END_STRING + "\n");
        writer.flush();


        //写完以后进行读操作
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
        //设置超时间
        socket.setSoTimeout(SOCKET_TIMEOUT);
        StringBuffer sb = new StringBuffer();
        String temp;
        int index;
        try {
            while ((temp = br.readLine()) != null) {
                if ((index = temp.indexOf(END_STRING)) != -1) {
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
        } catch (SocketTimeoutException e) {
            ExceptionHandler.handle(e,"数据读取超时。");
        }


//        System.out.println("服务端: " + sb);

        writer.close();
        br.close();
        socket.close();

        return sb.toString();
    }


//        /** 发起一次tcp连接 */
//        private static void connect(MessageBody messageBody) throws Exception {
//                //创建Socket对象
//                Socket socket = new Socket(InetAddress.getByName(SERVER_IP),SERVER_PORT);
//
//
//                //与服务端建立连接
//                //建立连接后就可以往服务端写数据了
//                Writer writer = new OutputStreamWriter(socket.getOutputStream(),CHARSET_NAME );
//                writer.write("你好，服务端。   "+ TimeUtils.getTimeStr());
//                writer.write(END_STRING+"\n");
//                writer.flush();
//                //写完以后进行读操作
//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
//                //设置超时间
//                socket.setSoTimeout(SOCKET_TIMEOUT);
//                StringBuffer sb = new StringBuffer();
//                String temp;
//                int index;
//                try {
//                        while ((temp=br.readLine()) != null) {
//                                if ((index = temp.indexOf(END_STRING)) != -1) {
//                                        sb.append(temp.substring(0, index));
//                                        break;
//                                }
//                                sb.append(temp);
//                        }
//                } catch (SocketTimeoutException e) {
//                        System.out.println("数据读取超时。");
//                }
//                System.out.println("服务端: " + sb);
//
//                writer.close();
//                br.close();
//                socket.close();
//
//
//        }


}
