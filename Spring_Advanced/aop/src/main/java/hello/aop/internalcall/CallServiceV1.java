package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 프록시와 내부 호출 대안1 : 자기 자신 주입
@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    /**
     * 생성자 주입은 본인을 생성하면서 주입해야 하기 때문에 순환 사이클이 만들어져 오류가 발생한다.
     * 따라서, 아래와 같이 수정자(Setter) 주입을 통해 스프링 빈이 생성된 이후에 주입할 수 있기 때문에 정상 진행이 된다.
     */
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 먼저 프록시를 호출하고 internal을 호출하게 된다.
    }

    public void internal() {
        log.info("call internal");
    }


}
