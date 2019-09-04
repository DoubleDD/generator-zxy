package <%= package %>.<%= project %>.async.config;

import com.zxy.common.message.CommonMessageConverter;
import com.zxy.common.message.provider.MessageSender;
import com.zxy.common.message.provider.MessageSenderFactory;
import com.zxy.common.serialize.Serializer;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * @author <%= user %>
 */
@Configuration
public class MessageConfig {

    @Bean
    public CommonMessageConverter commonMessageConverter(Serializer serializer) {
        CommonMessageConverter converter = new CommonMessageConverter();
        converter.setSerializer(serializer);
        return converter;
    }

    @Bean
    DirectExchange exchange(Environment env) {
        return new DirectExchange(env.getProperty("spring.rabbitmq.default-exchange"));
    }

    @Bean
    public MessageSenderFactory messageSenderFactory() {
        return new MessageSenderFactory();
    }

    @Bean
    public MessageSender messageSender(MessageSenderFactory messageSenderFactory, Environment env) {
        return messageSenderFactory.create(env.getProperty("spring.rabbitmq.default-exchange"));
    }

}