package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // @ClassAop는 클래스, 인터페이스, enum, record에서 사용 가능하다.
@Retention(RetentionPolicy.RUNTIME) // @ClassAop는 런타임까지 활성화 되어있다. (사실상 안 사라진다.)
public @interface ClassAop {
}
