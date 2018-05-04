package com.mofangyouxuan.wx.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 自定义拦截器配置
 * @author jeekhan
 *
 */
@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter{
    @Bean   
    public HandlerInterceptor getSessionInInterceptor(){
        return new SessionInterceptor();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 
        // excludePathPatterns 用户排除拦截
    		//registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/**");
    	    registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/shop/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/user/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/partner/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/goods/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/order/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/srvcenter/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/image/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/postage/**");
        registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/receiver/**");
        super.addInterceptors(registry);
    }
}
