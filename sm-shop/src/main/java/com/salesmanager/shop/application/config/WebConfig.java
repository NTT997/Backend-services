package com.salesmanager.shop.application.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	
    @Autowired
    private MerchantStoreArgumentResolver merchantStoreArgumentResolver;
    
    @Autowired
    private LanguageArgumentResolver languageArgumentResolver;

	
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(merchantStoreArgumentResolver);
        argumentResolvers.add(languageArgumentResolver);
    }
	@Bean
    public FilterRegistrationBean<CachingRequestFilter> cachingRequestFilter() {
        FilterRegistrationBean<CachingRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingRequestFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // make sure it runs early
        return registrationBean;
    }
    

}
