package <%= package %>.<%= project %>.service.config;


import com.zxy.common.rpc.spring.config.RemoteServicePackageConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <%= user %>
 */
@Configuration
public class RpcConfig {

    @Bean
    public RemoteServicePackageConfig remoteServicePackageConfig() {
        return new RemoteServicePackageConfig(new String[]{"<%= package %>.<%= project %>.api"});
    }
}
