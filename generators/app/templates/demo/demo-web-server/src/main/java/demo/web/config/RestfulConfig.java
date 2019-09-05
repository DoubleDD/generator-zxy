package <%= package %>.web.config;


import com.zxy.common.base.exception.UnprocessableException;
import com.zxy.common.message.provider.MessageSender;
import com.zxy.common.restful.audit.ControllerAdvice;
import com.zxy.common.restful.exception.DefaultHandlerExceptionResolver;
import com.zxy.common.restful.exception.ExceptionMappingEntry;
import com.zxy.common.restful.json.spring.CustomizedHandlerAdapter;
import com.zxy.common.restful.json.spring.JsonReturnValueHandler;
import com.zxy.common.restful.multipart.support.DefaultAttachmentResolver;
import com.zxy.common.restful.resolver.RequestContextResolver;
import com.zxy.common.restful.resolver.SubjectResolver;
import com.zxy.common.restful.security.AuthenticationException;
import com.zxy.common.restful.security.AuthorizationException;
import com.zxy.common.restful.validation.ValidationException;
import com.zxy.common.restful.validation.support.DefaultValidateProcessor;
import com.zxy.common.restful.validation.support.RequiredValidator;
import org.apache.catalina.connector.Connector;
import org.jooq.exception.NoDataFoundException;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RestfulConfig {

    @Bean
    public JsonReturnValueHandler getJsonReturnValueHandler() {
        return new JsonReturnValueHandler();
    }

    @Bean
    public CustomizedHandlerAdapter getCustomizedHandlerAdapter() {
        CustomizedHandlerAdapter cchl = new CustomizedHandlerAdapter();
        cchl.setCustomReturnValueHandlers(Arrays.asList(getJsonReturnValueHandler()));
        cchl.setCustomArgumentResolvers(Arrays.asList(getRequestContextResolver(), getSubjectResolver()));
        return cchl;
    }

    @Bean
    public RequestContextResolver getRequestContextResolver() {
        return new RequestContextResolver();
    }

    @Bean
    public DefaultAttachmentResolver getDefaultAttachmentResolver() {
        return new DefaultAttachmentResolver();
    }

    @Bean
    public DefaultValidateProcessor getDefaultValidateProcessor() {
        DefaultValidateProcessor dvp = new DefaultValidateProcessor();
        dvp.setValidators(Arrays.asList(new RequiredValidator()));
        return dvp;
    }

    @Bean
    public DefaultHandlerExceptionResolver getDefaultHandlerExceptionResolver() {
        DefaultHandlerExceptionResolver resolver = new DefaultHandlerExceptionResolver();
        List<ExceptionMappingEntry> entries = new ArrayList<ExceptionMappingEntry>();

        ExceptionMappingEntry entry = new ExceptionMappingEntry();
        entry.setExceptionClass(UnprocessableException.class);
        entry.setExposeErrorMessage(true);
        entry.setStatusCode(422);
        entry.setMessage("Invalid input.");
        entries.add(entry);

        entry = new ExceptionMappingEntry();
        entry.setExceptionClass(ValidationException.class);
        entry.setExposeErrorMessage(true);
        entry.setStatusCode(422);
        entry.setMessage("Invalid input.");
        entries.add(entry);

        entry = new ExceptionMappingEntry();
        entry.setExceptionClass(AuthenticationException.class);
        entry.setStatusCode(401);
        entry.setMessage("Access is Denied");
        entries.add(entry);

        entry = new ExceptionMappingEntry();
        entry.setExceptionClass(AuthorizationException.class);
        entry.setStatusCode(403);
        entry.setMessage("Request is not permitted");
        entries.add(entry);

        entry = new ExceptionMappingEntry();
        entry.setExceptionClass(NoDataFoundException.class);
        entry.setStatusCode(404);
        entry.setMessage("date not find");


        entry = new ExceptionMappingEntry();
        entry.setExceptionClass(Exception.class);
        entry.setStatusCode(500);
        entry.setMessage("Internal Error");
        entries.add(entry);

        resolver.setEntries(entries);
        return resolver;
    }


    @Bean
    public MemberSecurityManager securityManager(){
        return new MemberSecurityManager();
    }

    @Bean
    public SubjectResolver getSubjectResolver() {
        return new SubjectResolver();
    }

    @Bean
    public ControllerAdvice controllerAdvice(MessageSender messageSender, MemberSecurityManager memberSecurityManager) {
        ControllerAdvice controllerAdvice = new ControllerAdvice();
        controllerAdvice.setMessageSender(messageSender);
        controllerAdvice.setSecurityManager(memberSecurityManager);
        return controllerAdvice;
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory(){
            @Override
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                connector.setParseBodyMethods("POST,PUT,DELETE");
            }
        };
    }
}
