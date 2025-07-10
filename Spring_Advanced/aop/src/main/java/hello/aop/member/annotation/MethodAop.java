package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // @MethodAop는 메서드에서만 사용 가능하다.
@Retention(RetentionPolicy.RUNTIME) // @MethodAop는 런타임까지 활성화 되어있다. (사실상 안 사라진다.)
public @interface MethodAop {
    String value();
}
