package zju.cst.eric.utils;

import com.alibaba.fastjson.JSON;
import zju.cst.eric.ExceptionHandler;
import zju.cst.eric.network.message.MessageBody;

/**
 * @Date: 2020/1/11 16:48
 * @Author: EricMa
 * @Description: todo:
 */
public class JsonUtils {

    public static String toJSONString(Object obj) {

        String json = "";
        try {
            /**  序列化 */
            json = JSON.toJSONString(obj);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        return json;
    }

    public static <T> T parseObject(String json, Class<T> tClass) {

        T obj = null;

        try {
            /** 反序列化 */
            obj = JSON.parseObject(json, tClass);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        return obj;
    }
}
