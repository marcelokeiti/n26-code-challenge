package com.n26;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.n26.application.TimeProvider;
import com.n26.application.impl.FakeTimeProvider;

@Configuration
@Profile("test")
public class FakeTimeConfiguration {

    @Bean
    public TimeProvider timeProvider() {
	return new FakeTimeProvider();
    }

}
