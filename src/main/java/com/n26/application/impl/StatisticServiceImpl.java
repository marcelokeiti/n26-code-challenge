package com.n26.application.impl;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.n26.application.StatisticService;
import com.n26.application.TimeProvider;
import com.n26.domain.FinancialTransaction;
import com.n26.domain.Statistic;
import com.n26.exception.ExpiredTransactionException;

/**
 * <p>
 * This class is responsible to handle the in-memory cache with the statistic
 * based on the transactions which happened in the last 60 seconds.
 * </p>
 * 
 * The class is Thread-Safe. It uses a ReentrantReadWriteLock in which the read
 * operations are not blocked by another read operations. </br>
 * Since the interval between the date in which the transaction was created and
 * the current date matters, the ReentrantReadWriteLock is created with fair
 * policy.
 * 
 * The <code>@Scope</code> annotation is just for readability since It's the
 * default scope.
 */
@Service
@Scope(value = "singleton")
public class StatisticServiceImpl implements StatisticService {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private static final int SECONDS = (int) ChronoUnit.MINUTES.getDuration().getSeconds();

    // each element represents one second of the minute
    protected Statistic[] minuteStatistic;

    private TimeProvider timeProvider;

    @Autowired
    public StatisticServiceImpl(final TimeProvider timeProvider) {
	this.timeProvider = timeProvider;
    }

    protected StatisticServiceImpl() {
    }

    @PostConstruct
    public void initialize() {
	minuteStatistic = new Statistic[SECONDS];
	Arrays.setAll(minuteStatistic, p -> Statistic.newInstance());
    }

    @Override
    public void add(final FinancialTransaction transaction) {
	lock.writeLock().lock();

	try {
	    ZonedDateTime now = timeProvider.now();

	    ZonedDateTime start = now.minusSeconds(59);
	    ZonedDateTime end = now;

	    if (transaction.isBefore(start)) {
		throw new ExpiredTransactionException("The transaction is expired!");
	    }

	    if (transaction.isAfter(end)) {
		throw new IllegalArgumentException("The transaction cannot be after the current date!");
	    }

	    int index = transaction.getDate().getSecond();

	    Statistic statistic = minuteStatistic[index];
	    statistic.consolidate(transaction);
	} finally {
	    lock.writeLock().unlock();
	}
    }

    @Override
    public Statistic get() {
	lock.readLock().lock();

	try {
	    ZonedDateTime now = timeProvider.now();

	    Statistic statistic = Arrays.stream(minuteStatistic).filter(p -> p.isValidOnDate(now))
		    .reduce(Statistic.newInstance(), (stat1, stat2) -> stat1.consolidate(stat2));

	    return statistic;
	} finally {
	    lock.readLock().unlock();
	}
    }

}
