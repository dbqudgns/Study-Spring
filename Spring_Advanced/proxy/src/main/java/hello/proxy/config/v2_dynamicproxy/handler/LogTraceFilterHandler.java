package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// JDK 동적 프록시 적용 : 메서드 이름을 기준으로 특정 조건을 만족할 때 로그를 남기는 기능을 개발해보자
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target; // 프록시가 호출할 대상
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String ... patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터 : 특정 메서드 이름이 매칭되지 않으면 실제 로직을 바로 호출한다.
        String methodName = method.getName();
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "."
                    + method.getName() + "()"; // 메서드를 통해서 호출되는 메서드 정보와 클래스 정보를 동적으로 확인할 수 있다.
            status = logTrace.begin(message);

            // 로직 호출
            Object result = method.invoke(target, args);

            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
