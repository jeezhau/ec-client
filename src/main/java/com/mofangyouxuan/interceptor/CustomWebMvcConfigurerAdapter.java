package com.mofangyouxuan.interceptor;

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
    public HandlerInterceptor getUserSessionInInterceptor(){
        return new UserSessionInterceptor();
    }

    @Bean   
    public HandlerInterceptor getPartnerSessionInInterceptor(){
        return new PartnerSessionInterceptor();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
    		registry.addInterceptor(new AllInterceptor()).addPathPatterns("/**");
        // addPathPatterns 用于添加拦截规则, 
        // excludePathPatterns 用户排除拦截
    		//registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/**");
    	    //registry.addInterceptor(getSessionInInterceptor()).addPathPatterns("/shop/**");
    		InterceptorRegistration  userSessionReg = registry.addInterceptor(getUserSessionInInterceptor());
    		userSessionReg.addPathPatterns("/user/**");
    		userSessionReg.addPathPatterns("/vip/**");
        userSessionReg.addPathPatterns("/order/**");
        userSessionReg.addPathPatterns("/complain/**");
        userSessionReg.addPathPatterns("/receiver/**");
        userSessionReg.addPathPatterns("/aftersale/**");
 
		InterceptorRegistration  partnerSessionReg = registry.addInterceptor(getPartnerSessionInInterceptor());
		partnerSessionReg.addPathPatterns("/partner/**");
		partnerSessionReg.addPathPatterns("/goods/**");
		partnerSessionReg.addPathPatterns("/psaleorder/**");
	    partnerSessionReg.addPathPatterns("/pimage/**");
	    partnerSessionReg.addPathPatterns("/postage/**");
	    partnerSessionReg.addPathPatterns("/paftersale/**");
	    partnerSessionReg.addPathPatterns("/pstaff/**");
	    partnerSessionReg.addPathPatterns("/mypartners/**");
	    partnerSessionReg.addPathPatterns("/review/**");
	    partnerSessionReg.addPathPatterns("/pcomplain/**");
	    partnerSessionReg.addPathPatterns("/pcash/**");
	   
        super.addInterceptors(registry);
    }
}
