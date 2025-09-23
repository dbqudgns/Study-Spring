package hello.order.v1;

import hello.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV1 implements OrderService {

    /**
     * MeterRegistry : 마이크로미터 기능을 제공하는 핵심 컴포넌트
     * 스프링을 통해서 주입 받아서 사용하고 카운터, 게이지 메트릭 등을 등록해준다.
     */
    private final MeterRegistry registry;

    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV1(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();

        Counter.builder("my.order") // Counter.builder(name)을 통해 카운터를 생성한다. name에는 메트릭 이름을 지정한다.
                .tag("class", this.getClass().getName()) // tag을 통해 필터할 수 있는 레이블로 사용된다.
                .tag("method", "order")
                .description("order")
                .register(registry).increment(); // 만든 카운터를 MeterRegistry에 등록한다. 그래야 실제로 동작한다.
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();

        Counter.builder("my.order") // Counter.builder(name)을 통해 카운터를 생성한다. name에는 메트릭 이름을 지정한다.
                .tag("class", this.getClass().getName()) // tag을 통해 필터할 수 있는 레이블로 사용된다.
                .tag("method", "cancel")
                .description("order")
                .register(registry).increment(); // 만든 카운터를 MeterRegistry에 등록한다. 그래야 실제로 동작한다.


    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
