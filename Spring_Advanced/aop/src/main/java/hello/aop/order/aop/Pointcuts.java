package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // hello.aop.order 패키지와 그 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder(){} // 외부에서 호출하기 위해 포인트 컷의 접근 제어자를 public으로 열어두어야 한다.

    // 타입 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    // allOrder && allService
    @Pointcut("allOrder() && allService()")
    public void orderAndService(){}

}
