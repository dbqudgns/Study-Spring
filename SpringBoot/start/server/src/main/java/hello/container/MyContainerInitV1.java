package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

// ServletContainerInitializer : 서블릿 컨테이너를 초기화하는 기능을 제공
public class MyContainerInitV1 implements ServletContainerInitializer {

    /**
     * 애플리케이션에 필요한 기능들을 초기화하거나 등록할 수 있다.
     * @param set : @HandleTypes에 선언된 인터페이스의 구현체들의 클래스 정보를 담는다.
     * @param servletContext : 서블릿 컨테이너를 접근할 수 있는 수단
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyContainerInitV1.onStartup");
        System.out.println("MyContainerInitV1 set = " + set);
        System.out.println("MyContainerInitV1 servletContext = " + servletContext);
    }

    // 추가로 WAS에게 실행할 초기화 클래스를 알려줘야 한다.
    // resources/META-INF/services/jakarta.servlet.ServletContainerInitializer
    // 위 경로명에 hello.container.MyContainerInitV1을 작성하면 WAS를 실행할 때 해당 클래스를 서블릿 컨테이너 초기화 클래스로 인식하고 로딩 시점에 실행한다.
}
