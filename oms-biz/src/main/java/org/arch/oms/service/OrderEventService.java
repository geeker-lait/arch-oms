package org.arch.oms.service;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
//@Service
public abstract class OrderEventService {



//    @Autowired
//    private RedisDistributeLock redisDistributeLock;

//    protected abstract <T, R> Supplier<R> execute(OrderEventDto<T> orderEventDto);
//
//    public <T, R> LockExecuteResult<R> executeByEvent(OrderEventDto<T> orderEventDto) {
//        preExecute();
//        LockExecuteResult<R> lock = redisDistributeLock.lock(Constant.ORDER_EVENT_REDIS_LOCK_PREFIX + orderEventDto.getLockKey(), () -> {
//            return execute(orderEventDto).get();
//        });
//        afterExecute();
//        return lock;
//    }
//
//    private void afterExecute() {
//    }
//
//    protected void preExecute() {
//
//    }

}
