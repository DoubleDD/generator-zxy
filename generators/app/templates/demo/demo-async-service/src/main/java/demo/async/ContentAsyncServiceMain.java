package <%= package %>.<%= project %>.async;

import com.zxy.common.rpc.spring.config.RPCClientConfig;
import <%= package %>.<%= project %>.async.config.CacheConfig;
import <%= package %>.<%= project %>.async.config.InitListenerConfig;
import <%= package %>.<%= project %>.async.config.MessageConfig;
import <%= package %>.<%= project %>.async.config.TransactionConfig;
import <%= package %>.<%= project %>.utils.SpringUtil;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <%= user %>
 */
@Configuration
@ComponentScan
@Import(value = {
        CacheConfig.class,
        MessageConfig.class,
        InitListenerConfig.class,
        RPCClientConfig.class,
        MessageConfig.class,
        DataSourceAutoConfiguration.class,
        JooqAutoConfiguration.class,
        TransactionConfig.class,
        RabbitAutoConfiguration.class,
        SpringUtil.class
})
public class ContentAsyncServiceMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ContentAsyncServiceMain.class).web(false).run(args);
    }
}
