package hello.order.v1;

import hello.order.OrderService;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV2 implements OrderService { ;

    private AtomicInteger stock = new AtomicInteger(100);

    // tag에 method를 기준으로 분류해서 적용한다.
    @Counted("my.order") // my.order 라는 이름의 메트릭 엔드포인트
    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
    }

    @Counted("my.order")
    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
