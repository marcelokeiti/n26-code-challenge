package com.n26.application.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import com.n26.application.TimeProvider;

@Component
public class RealTimeProvider implements TimeProvider {

    @Override
    public ZonedDateTime now() {
	return ZonedDateTime.now(ZoneOffset.UTC);
    }

}
