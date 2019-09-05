package <%= package %>.web.config;

import com.zxy.common.message.provider.MessageSender;
import com.zxy.common.restful.security.freq.support.DefaultFrequencyHandler;
import com.zxy.common.restful.security.interceptor.CachedResultInterceptor;
import com.zxy.common.restful.security.interceptor.FrequencyCheckInterceptor;
import com.zxy.common.restful.security.interceptor.PermissionCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @user tianjun
 * @date 16/7/12
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {



	private PermissionCheckInterceptor permissionCheckInterceptor;
	private CachedResultInterceptor cachedResultInterceptor;
	private FrequencyCheckInterceptor frequencyCheckInterceptor;

	@Autowired
	public void setFrequencyCheckInterceptor(FrequencyCheckInterceptor frequencyCheckInterceptor) {
		this.frequencyCheckInterceptor = frequencyCheckInterceptor;
	}

	@Autowired
    public void setPermissionCheckInterceptor(PermissionCheckInterceptor permissionCheckInterceptor) {
        this.permissionCheckInterceptor = permissionCheckInterceptor;
    }

	@Autowired
	public void setCachedResultInterceptor(CachedResultInterceptor cachedResultInterceptor) {
		this.cachedResultInterceptor = cachedResultInterceptor;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(cachedResultInterceptor);
		registry.addInterceptor(permissionCheckInterceptor);
		registry.addInterceptor(frequencyCheckInterceptor);
	}


	@Bean
	public PermissionCheckInterceptor permissionCheckInterceptor(){
	    return new PermissionCheckInterceptor();
	}

	@Bean
	public CachedResultInterceptor cachedResultInterceptor() {
		return new CachedResultInterceptor();
	}

	@Bean
	public DefaultFrequencyHandler defaultFrequencyHandler(MessageSender messageSender) {
		DefaultFrequencyHandler frequencyHandler = new DefaultFrequencyHandler();
		frequencyHandler.setMessageSender(messageSender);
		return frequencyHandler;
	}
	@Bean
	public FrequencyCheckInterceptor frequencyCheckInterceptor(DefaultFrequencyHandler frequencyHandler,
			   MemberSecurityManager memberSecurityManager){
		FrequencyCheckInterceptor frequencyCheckInterceptor = new FrequencyCheckInterceptor();
		frequencyCheckInterceptor.setFrequencyHandler(frequencyHandler);
		frequencyCheckInterceptor.setSecurityManager(memberSecurityManager);
		return frequencyCheckInterceptor;
	}

}
