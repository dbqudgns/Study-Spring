package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

// 프록시와 내부 호출 대안2 : 지연 조회
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

    // ApplicationContext, ObjectProvider를 사용한 지연(LAZY) 조회
    private final ApplicationContext applicationContext;
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public void external() {
        log.info("call external");

        // 객체를 스프링 컨테이너에서 조회하는 것을 실제 객체를 사용하는 시점으로 지연할 수 있다.
        // CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();
        callServiceV2.internal();
    }

    public void internal() {
        log.info("call internal");
    }


}
