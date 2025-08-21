package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    // 빈 후처리기를 등록하지 않아도 된다. 스프링은 자동 프록시 생성기라는 빈 후처리기를 자동으로 등록해준다.
   // @Bean
    public Advisor advisor1(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);

    }

    // no-log 호출 시 로그가 출력된다.
    // @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // AspectJExpressionPointcut : AspectJ 포인트컷 표현식을 적용할 수 있다.
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        // hello.proxy.app 패키지와 하위 패키지의 모든 메서드는 포인트컷에 매칭하되, noLog() 메서드는 제외하라는 뜻 !
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }


}
