package <%= package %>.<%= project %>.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * RestTemplate配置
 *
 * @author <%= user %>
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public SimpleClientHttpRequestFactory requestFactory(Environment env) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // 设置底层的URLConnection的读取超时
        requestFactory.setReadTimeout(env.getProperty("resttemplate.timeout.read", Integer.class, 10000));
        // 设置底层URLConnection的连接超时
        requestFactory.setConnectTimeout(env.getProperty("resttemplate.timeout.connect", Integer.class, 10000));
        return requestFactory;
    }

    @Bean
    public ResponseErrorHandler errorHandler() {
        return new DefaultResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory requestFactory, ResponseErrorHandler errorHandler, Environment env) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setMessageConverters(Arrays.asList(
                new StringHttpMessageConverter(Charset.forName(env.getProperty("resttemplate.charset", "UTF-8"))),
                new FormHttpMessageConverter(), new MappingJackson2HttpMessageConverter()));
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate;
    }
}
