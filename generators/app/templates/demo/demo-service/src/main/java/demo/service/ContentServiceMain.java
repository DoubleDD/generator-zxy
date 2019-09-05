package <%= package %>.service;

import com.zxy.common.dao.spring.CommonDaoConfig;
import com.zxy.common.rpc.spring.config.RPCServerConfig;
import <%= package %>.service.config.CacheConfig;
import <%= package %>.service.config.MessageConfig;
import <%= package %>.service.config.RpcConfig;
import org.jooq.Schema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <%= user %>
 */
@EnableTransactionManagement
@Configuration
@ComponentScan
@Import(value = {
        CacheConfig.class,
        MessageConfig.class,
        RpcConfig.class,
        RPCServerConfig.class,
        CommonDaoConfig.class,
        RabbitAutoConfiguration.class,
        TransactionAutoConfiguration.EnableTransactionManagementConfiguration.JdkDynamicAutoProxyConfiguration.class,
        TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        TransactionAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        JooqAutoConfiguration.class
})
public class ContentServiceMain {

    @Bean
    public Schema schema() {
        return null;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ContentServiceMain.class, args);
        synchronized (ContentServiceMain.class) {
            ContentServiceMain.class.wait();
        }
    }
}
