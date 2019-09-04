package <%= package %>.<%= project %>.web;

import com.zxy.common.rpc.spring.config.RPCClientConfig;
import <%= package %>.<%= project %>.web.config.CacheConfig;
import <%= package %>.<%= project %>.web.config.MessageConfig;
import <%= package %>.<%= project %>.web.config.RestfulConfig;
import <%= package %>.<%= project %>.web.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(value = {
        CacheConfig.class,
        MessageConfig.class,
        RestfulConfig.class,
        RPCClientConfig.class,
        WebConfig.class,
        RabbitAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class

})
public class ContentWebServerMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ContentWebServerMain.class, args);
    }
}
