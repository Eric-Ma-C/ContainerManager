package zju.cst.eric.network.message;

/**
 * @Date: 2020/1/9 21:59
 * @Author: EricMa
 * @Description: 容器与平台通信的消息体
 */
public class MessageBody {
        private Intent intent;
        private String value;

        public MessageBody(Intent intent, String value) {
                this.intent = intent;
                this.value = value;
        }

        public Intent getIntent() {
                return intent;
        }

        public void setIntent(Intent intent) {
                this.intent = intent;
        }

        public String getValue() {
                return value;
        }

        public void setValue(String value) {
                this.value = value;
        }

        @Override
        public String toString() {
                return "MessageBody{" +
                    "intent=" + intent +
                    ", value='" + value + '\'' +
                    '}';
        }
}
