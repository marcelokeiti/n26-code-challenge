package com.n26.application.impl;

import java.time.ZonedDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.n26.application.TimeProvider;

@Component
@Profile("test")
public class FakeTimeProvider implements TimeProvider {

    private ZonedDateTime time;

    @Override
    public ZonedDateTime now() {
	return time;
    }

    public void setTime(ZonedDateTime time) {
	this.time = time;
    }

}
