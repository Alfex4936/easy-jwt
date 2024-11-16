package com.seok.easyjwt.jwt;


import com.seok.easyjwt.configuration.EasyJwtAutoConfiguration;
import com.seok.easyjwt.user.QueryJwtUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;

public class EasyJwtAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withConfiguration(AutoConfigurations.of(EasyJwtAutoConfiguration.class));

    @Test
    public void testAutoConfigurationEnabled() {
        contextRunner
                .withPropertyValues("easy-jwt.enabled=true", "easy-jwt.secret=467fc8a59f7ea1275f68d3f09f85935901d32ba65a3ab9af9a90dfd62ccfc2cd")
                .run(context -> {
                    assertThat(context).hasSingleBean(JwtProperties.class);
                    assertThat(context).hasSingleBean(JwtTokenProvider.class);
                    assertThat(context).hasSingleBean(JwtFilter.class);
                });
    }

    @Test
    public void testAutoConfigurationDisabled() {
        contextRunner
                .withPropertyValues("easy-jwt.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(JwtProperties.class);
                    assertThat(context).doesNotHaveBean(JwtTokenProvider.class);
                    assertThat(context).doesNotHaveBean(JwtFilter.class);
                });
    }

    // Test configuration to provide required beans
    @Configuration
    static class TestConfig {

        @Bean
        public QueryJwtUserService queryJwtUserService() {
            return Mockito.mock(QueryJwtUserService.class);
        }

        @Bean
        @Primary
        public UserDetailsService userDetailsService() {
            return Mockito.mock(UserDetailsService.class);
        }
    }
}