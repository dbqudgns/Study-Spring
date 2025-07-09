package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before : 조인 포인트 실행 이전에 실행
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature()); // 1
            Object result = joinPoint.proceed();
            // @AfterReturning : 조인 포인트가 정상 완료 후 실행
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            // @AfterThrowing : 메서드가 예외를 던지는 경우 실행
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After : 메서드 실행이 종료되면 실행된다. (finally와 똑같다.)
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    /** @Before()는 메서드 종료 시 자동으로 다음 타겟이 호출된다.
     * 예외가 발생하면 다음 코드가 호출되지는 않는다.
     * @param joinPoint (생략 가능)
     */
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[Before] {}", joinPoint.getSignature()); // 2
    }

    /**
     * @AfterReturning는 메서드 실행이 정상적으로 반환될 때 실행된다.
     * @param joinPoint (생략 가능 단, 사용시에는 적어줘야 한다.)
     * @param result returning 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
     *               returning절에 지정된 타입의 값을 반환하는 메서드만 대상으로 실행한다. (부모 타입을 지정하면 모든 자식 타입은 인정된다.)
     *               위 메서드에서는 Object에 해당 즉, void 반환 타입은 실행되지 않는다.
     * 반환 객체를 변경할 수는 없지만 조작할 수는 있다.
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result); // 3
    }

    /**
     * @AfterThrowing : 메서드 실행이 예외를 던져서 종료될 때 실행
     * @param joinPoint (생략 가능 단, 사용시에는 적어줘야 한다.)
     * @param ex throwing 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
     *           throwning절에 지정된 타입과 맞는 예외를 대상으로 실행한다. (부모 타입을 지정하면 모든 자식 타입은 인정된다.)
     */
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage()); // 예외 발생 시 바로 호출된다.
    }

    /**
     * @After : 정상 및 예외 반환 조건을 모두 처리한다.
     * 일반적으로 리소스를 해제하는데 사용된다.
     * @param joinPoint (생략 가능 단, 사용시에는 적어줘야 한다.)
     */
    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature()); // 4
    }


}
