package hello.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportSelector : 설정 정보를 동적으로 선택할 수 있게 해주는 인터페이스
 */
public class HelloImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 이곳에 설정 정보로 사용할 클래스를 동적으로 프로그래밍하면 된다.
        // 예를 들어, 특정 조건일 때는 어느 설정 정보를 등록하고 다른 조건일 때는 다른 설정 정보를 등록한다.
        return new String[]{"hello.selector.HelloConfig"};
    }
}
