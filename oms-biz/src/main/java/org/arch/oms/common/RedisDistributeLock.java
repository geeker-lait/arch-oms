package org.arch.oms.common;

import lombok.extern.slf4j.Slf4j;
import org.arch.oms.common.dto.LockExecuteResult;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * redisson 锁工具类
 * @author junboXiang
 * @version V1.0
 * 2021-07-06
 */
@Slf4j
@Component
public class RedisDistributeLock {

    @Autowired
    private RedissonClient redissonClient;

    public <T> LockExecuteResult<T> lock(String key, int waitTime, int leaseTime, Supplier<T> business) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        try {
            return lockSuccess ? new LockExecuteResult<>(true, business.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (lockSuccess && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public <T> LockExecuteResult<T> lock(String key, Supplier<T> business) {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock();
        try {
            return lockSuccess ? new LockExecuteResult<>(true, business.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (lockSuccess && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /**
     * 加锁执行有返回值
     * @param key
     * @param business
     * @param t
     * @param <T>
     * @param <R>
     * @return
     */
    public <T,R> LockExecuteResult lock(String key, Function<T,LockExecuteResult<R>> business, T t) {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock();
        try {
            return lockSuccess ? business.apply(t) : new LockExecuteResult<>(false, null);
        } finally {
            if (lockSuccess && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <T> LockExecuteResult<T> lockByDbTransactional(String key, int waitTime, int leaseTime, Supplier<T> business) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        try {
            return lockSuccess ? new LockExecuteResult<>(true, business.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (lockSuccess && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <T> LockExecuteResult<T> lockByDbTransactional(String key, Supplier<T> business) {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock();
        try {
            return lockSuccess ? new LockExecuteResult<>(true, business.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (lockSuccess && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
