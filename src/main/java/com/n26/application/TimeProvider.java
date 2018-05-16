package com.n26.application;

import java.time.ZonedDateTime;

public interface TimeProvider {

    ZonedDateTime now();

}
