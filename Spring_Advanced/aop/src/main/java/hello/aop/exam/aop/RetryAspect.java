package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    // @Retry가 붙은 메서드 실행 시에 어드바이스가 실행되며 예외 발생시 예외처리를 한다.
    @Around("@annotation(retry)") // Retry retry를 사용해서 어드바이스에 애노테이션을 파라미터로 전달한다.
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;

        /**
         * 실행 예시
         * seq = 4 상태에서 save("data4") 호출
         * save() 진입 → seq++ → seq = 5
         * if (seq % 5 == 0) 조건 만족 → IllegalStateException 발생
         * RetryAspect가 잡고 joinPoint.proceed() 다시 호출
         * 다시 save() 진입 → seq++ → seq = 6
         * if (seq % 5 == 0) 조건 불만족 → "ok" 반환
         */

        // 재시도 호출 시 로그 출력 때 [trace]가 안뜨는 이유는 ?
        // return joinPoint.proceed() 호출 시 save() 메서드가 재호출 되는데 이때는 TraceAspect를 다시 타지 않는다.
        // 처음 save() 호출 시 : TraceAspect -> RetryAspect -> save()
        // 재시도로 save() 호출 시 : RetryAspect -> save()
    }
}