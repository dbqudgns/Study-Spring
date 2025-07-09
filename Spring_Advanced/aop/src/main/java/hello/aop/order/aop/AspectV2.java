package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") // 포인트컷 표현식
    private void allOrder() {} // 포인트컷 시그니처 : void + 메서드 이름 + 파라미터 + 코드 내용X

    @Around("allOrder()") // 포인트컷을 직접 지정해도 되지만, 포인트컷 시그니처를 사용해도 된다.
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

}
