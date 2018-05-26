package com.mofangyouxuan.wx.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
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
    		registry.addInterceptor(new AllInterceptor()).addPathPatterns("/**");
        // addPathPatterns 用于添加拦截规则, 
        // excludePathPatterns 用户排除拦截
    		//registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/**");
    	    //registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/shop/**");
    		InterceptorRegistration  sessionReg = registry.addInterceptor(getSessionInInterceptor());
    		sessionReg.addPathPatterns("/user/**");
        sessionReg.addPathPatterns("/vip/**");
        sessionReg.addPathPatterns("/partner/**");
        sessionReg.addPathPatterns("/goods/**");
        sessionReg.addPathPatterns("/order/**");
        sessionReg.addPathPatterns("/srvcenter/**");
        sessionReg.addPathPatterns("/image/**");
        sessionReg.addPathPatterns("/postage/**");
        sessionReg.addPathPatterns("/receiver/**");
        sessionReg.addPathPatterns("/aftersales/**");
        super.addInterceptors(registry);
    }
}
