package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 빈 후처리기(BeanPostProcessor) 사용
public class BeanPostProcessorTest {

    @Test
    void postProcessor() {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // beanA 이름으로 B 객체가 빈으로 등록된다.
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // A는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A(); // beanA라는 이름으로 A 타입의 스프링 빈을 찾을 수 있다.
        }

        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor(); // 빈 후처리기리를 사용하려면 BeanPostProcessor 인터페이스를 구현하고 스프링 빈으로 등록해야 한다.
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    // 빈 후처리기
    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {

        @Override // 객체 생성 이후에 @PostConstruct 같은 초기화가 발생한 다음에 호출되는 포스트 프로세서이다.
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);

            if (bean instanceof A) {
                return new B(); // A 객체를 B 객체로 바꿔치기 하여 스프링 컨테이너에 등록된다.
            }

            return bean;
        }
    }

}
