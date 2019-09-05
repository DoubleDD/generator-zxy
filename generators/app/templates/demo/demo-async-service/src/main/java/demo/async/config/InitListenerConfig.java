package <%= package %>.async.config;

import com.zxy.common.message.consumer.AbstractMessageListener;
import com.zxy.common.message.consumer.MessageException;
import com.zxy.common.message.provider.MessageSender;
import <%= package %>.common.MessageHeaderContent;
import <%= package %>.common.MessageTypeContent;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ErrorHandler;

import java.util.Arrays;

/**
 * @author <%= user %>
 */
@Configuration
public class InitListenerConfig implements BeanFactoryAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitListenerConfig.class);

    private Environment                env;
    private DefaultListableBeanFactory beanFactory;
    private MessageSender              messageSender;

    private String  envName;
    private Integer prefetchCount;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;

    @Autowired
    public void setEnv(Environment env) {
        this.env               = env;
        envName                = env.getProperty("application.env.name", String.class, "rasdev9");
        prefetchCount          = env.getProperty("spring.rabbitmq.listener.simple.prefetch", Integer.class, SimpleMessageListenerContainer.DEFAULT_PREFETCH_COUNT);
        concurrentConsumers    = env.getProperty("spring.rabbitmq.listener.simple.concurrency", Integer.class, 1);
        maxConcurrentConsumers = env.getProperty("spring.rabbitmq.listener.simple.max-concurrency", Integer.class, 1);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    private SimpleMessageListenerContainer createListen(ConnectionFactory connectionFactory, MessageListener listener, String queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listener);
        container.setPrefetchCount(prefetchCount);
        container.setConcurrentConsumers(concurrentConsumers);
        container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        container.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable throwable) {
                if (causeChainContainsARADRE(throwable)) {
                    LOGGER.error("message listener shutdown: {}", throwable.getCause().getMessage());
                    // 发送邮件消息
                    String errorMessage = envName + "环境异步监听服务出错: " +
                            throwable.getCause().getMessage();
                    messageSender.send(MessageTypeContent.SEND_MESSAGE_WARNING_EMAIL, (Object) errorMessage,
                            MessageHeaderContent.SUBJECT, "system project " + listener.getClass().getSimpleName() + "服务挂起");
                    // 停止监听
                    container.shutdown();
                }
            }

            private boolean causeChainContainsARADRE(Throwable t) {
                for (Throwable cause = t.getCause(); cause != null; cause = cause.getCause()) {
                    if (cause instanceof MessageException) {
                        return true;
                    }
                }
                return false;
            }
        });
        return container;
    }


    /**
     * 注册所有的监听器
     */
    @Override
    public void afterPropertiesSet() {
        // 获取所有实现了 RabbitConsumer 接口的类
        String[]       beanNamesForType = beanFactory.getBeanNamesForType(RabbitConsumer.class);
        RabbitConsumer listener;
        // 遍历所有的RabbitConsumer实现类，为每个监听器创建 SimpleMessageListenerContainer 实例
        for (String beanName : beanNamesForType) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("正在注册监听器：{}", beanName);
            }

            listener = beanFactory.getBean(beanName, RabbitConsumer.class);

            if (!(listener instanceof AbstractMessageListener)) {
                LOGGER.error("{} 没有继承 AbstractMessageListener", beanName);
                return;
            }

            QueueConf queueConf = listener.getClass().getAnnotation(QueueConf.class);
            if (queueConf == null) {
                LOGGER.error("{} 没有配置队列信息（QueueConf）", beanName);
                return;
            }

            String queueBeanName = beanName + "Queue";
            String containerName = beanName + "Container";
            String queueName;
            String queueKey      = queueConf.key();
            if (!StringUtils.isBlank(queueKey)) {
                queueName = env.getProperty(queueConf.key());
            } else {
                queueName = queueConf.queue();
            }

            if (StringUtils.isBlank(queueName)) {
                LOGGER.error("{} 的队列名称为空，检查配置文件或者 QueueConf#queue的值", beanName);
                return;
            }

            Queue                   queue             = new Queue(queueName, true);
            DirectExchange          exchange          = beanFactory.getBean(DirectExchange.class);
            ConnectionFactory       connectionFactory = beanFactory.getBean(ConnectionFactory.class);
            AbstractMessageListener messageListener   = (AbstractMessageListener) listener;

            // 注册队列
            beanFactory.registerSingleton(queueBeanName, queue);
            // 绑定路由键
            Arrays.stream(messageListener.getTypes()).forEach(type -> {
                String  name    = queueBeanName + "#" + type;
                Binding binding = BindingBuilder.bind(queue).to(exchange).with(type + "");

                beanFactory.registerSingleton(name, binding);
            });

            // 创建监听器
            SimpleMessageListenerContainer listenerContainer = createListen(connectionFactory, messageListener, queueName);
            // 注册监听器 SimpleMessageListenerContainer
            beanFactory.registerSingleton(containerName, listenerContainer);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("监听器注册完成：{}", beanName);
            }
        }
    }


}
