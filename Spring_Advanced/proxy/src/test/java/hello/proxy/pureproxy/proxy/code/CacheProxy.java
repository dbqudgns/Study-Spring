package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private Subject target; // 클라이언트가 프록시를 호출하면 프록시가 최종적으로 실제 객체를 호출해야 한다. 따라서 내부에 객체의 참조를 가지고 있어야 한다.
    private String cacheValue; // 값이 없으면 실제 객체(target)를 호출해서 값을 구한 후 저장하고 반환한다. 값이 있으면 실제 객체를 전혀 호출하지 않고 그대로 반환한다.

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}