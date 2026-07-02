package com.payguard.core.infra.config;

import com.payguard.core.infra.filter.IdempotencyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final IdempotencyFilter idempotencyFilter;

    public FilterConfig(IdempotencyFilter idempotencyFilter) {
        this.idempotencyFilter = idempotencyFilter;
    }

    @Bean
    public FilterRegistrationBean<IdempotencyFilter> loggingFilterInternalToBeRegistered() {
        FilterRegistrationBean<IdempotencyFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(idempotencyFilter);
        registrationBean.addUrlPatterns("/api/v1/transactions/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}