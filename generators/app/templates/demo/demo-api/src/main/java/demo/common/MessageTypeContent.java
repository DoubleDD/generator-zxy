package <%= package %>.common;

/**
 * @author <%= user %>
 */
public class MessageTypeContent {
    private MessageTypeContent() {
    }

    /**
     * 系统管理 1
     * 人资模块(HR) 2
     * 考试 3
     * 课程模块(COURSE) 4
     * 资源平台 7
     */

    public static final int CONTENT_CUSTOM_ORDER_INSERT = 70001;

    //发送预警邮件
    public static final int SEND_MESSAGE_WARNING_EMAIL = 11209;
}