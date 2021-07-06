package org.arch.oms.common;

import org.arch.oms.dto.LockExecuteResult;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * redisson 锁工具类
 * @author junboXiang
 * @version V1.0
 * 2021-07-06
 */
@Component
public class RedisDistributeLock {

    @Autowired
    private RedissonClient redissonClient;

    public <T> LockExecuteResult<T> lock(String key, int waitTime, int leaseTime, Supplier<T> success) throws Exception {
        RLock lock = redissonClient.getLock(key);
        boolean b = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        try {
            return b ? new LockExecuteResult<>(true, success.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (b && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public <T> LockExecuteResult<T> lock(String key, Supplier<T> success) throws Exception {
        RLock lock = redissonClient.getLock(key);
        boolean b = lock.tryLock();
        try {
            return b ? new LockExecuteResult<>(true, success.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (b && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <T> LockExecuteResult<T> lockByDbTransactional(String key, int waitTime, int leaseTime, Supplier<T> success, Supplier<T> fail) throws Exception {
        RLock lock = redissonClient.getLock(key);
        boolean b = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        try {
            return b ? new LockExecuteResult<>(true, success.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (b && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <T> LockExecuteResult<T> lockByDbTransactional(String key, Supplier<T> success, Supplier<T> fail) throws Exception {
        RLock lock = redissonClient.getLock(key);
        boolean b = lock.tryLock();
        try {
            return b ? new LockExecuteResult<>(true, success.get()) : new LockExecuteResult<>(false, null);
        } finally {
            if (b && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
