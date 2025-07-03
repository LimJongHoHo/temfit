package kr.co.limbin.temfit.configs;

import kr.co.limbin.temfit.interceptors.DevInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.devInterceptor())
                .excludePathPatterns("/assets/**");
    }

    @Bean
    public DevInterceptor devInterceptor() {
        return new DevInterceptor();
    }
}
