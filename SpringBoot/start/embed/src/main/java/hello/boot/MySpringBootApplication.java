package hello.boot;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // ElementType.TYPE : 클래스, 인터페이스, 열거형에 적용 가능하다.
@Retention(RetentionPolicy.RUNTIME) // RUNTIME : 싫행 시점까지 어노테이션 정보가 유지된다.
@Documented // 문서화 도구로 API 문서를 생성할 때 이 어노테이션을 표시하도록 한다.
@ComponentScan // 해당 어노테이션이 붙은 클래스의 현재 패키지 부터 그 하위 패키지를 컴포넌트 스캔의 대상으로 사용한다.
// @ComponetScan : 스프링이 특정 패키지를 탐색하면서 @Component가 붙은 클래스를 자동으로 스캔하여 빈으로 등록하도록 지시하는 어노테이션
public @interface MySpringBootApplication {
}
