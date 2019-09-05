package <%= package %>.web.config;

import com.zxy.common.message.CommonMessageConverter;
import com.zxy.common.message.provider.MessageSender;
import com.zxy.common.message.provider.MessageSenderFactory;
import com.zxy.common.serialize.Serializer;
import com.zxy.common.serialize.hessian.HessianSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @user tianjun
 * @date 16/6/21
 */
@Configuration
public class MessageConfig {

    @Bean
    public Serializer serializer() {
        return new HessianSerializer();
    }

    @Bean
    public CommonMessageConverter commonMessageConverter(Serializer serializer) {
        CommonMessageConverter converter = new CommonMessageConverter();
        converter.setSerializer(serializer);
        return converter;
    }

	@Bean
	public MessageSenderFactory messageSenderFactory(){
		return new MessageSenderFactory();
	}
	@Bean
	public MessageSender messageSender(Environment env, MessageSenderFactory messageSenderFactory){
		return messageSenderFactory.create(env.getProperty("spring.rabbitmq.default-exchange"));
	}

}
