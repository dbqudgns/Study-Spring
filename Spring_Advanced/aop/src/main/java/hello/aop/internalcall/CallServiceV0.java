package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV0 {

    public void external() {
        log.info("call external");
        internal(); // 내부 메서드 호출 -> this.internal())

        /** 프록시 한계 :
         * 자기 자신의 내부 메서드를 호출하는 this.internal()이 되는데,
         * 여기서 this는 실제 대상 객체의 인스턴스를 뜻한다.
         * 결과적으로 이러한 내부 호출은 프록시를 거치지 않아 어드바이스도 적용할 수 없다.
         */
    }

    public void internal() {
        log.info("call internal");
    }

}
