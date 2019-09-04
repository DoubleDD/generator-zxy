package <%= package %>.<%= project %>.async.config;

import java.lang.annotation.*;

/**
 * 队列名称的配置
 *
 * @author <%= user %>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QueueConf {

    /**
     * 配置文件中队列对应的key
     *
     * @return
     */
    String key();

    /**
     * 直接指定queue
     *
     * @return
     */
    String queue() default "";
}
