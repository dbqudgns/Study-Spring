package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.util.Set;

@HandlesTypes(AppInit.class) // 1. @HandleTypes 애노테이션에 애플리케이션 초기화 인터페이스(AppInit.class)를 지정한다.
public class MyContainerInitV2 implements ServletContainerInitializer {

    @Override // 2. AppInit.class의 구현체인 AppInitV1Servlet.class 정보가 Set<Class<?>> c에 전달된다.
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("MyContainerInitV2 c = " + c);
        System.out.println("MyContainerInitV2 container = " + ctx);

        for (Class<?> appInitClass : c) {
            try {
                // new AppInitServlet()과 같은 코드
                AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();
                appInit.onStartup(ctx); // 3. 애플리케이션 초기화 코드를 직접 실행하면서 서블릿 컨테이너 정보가 담긴 ctx도 함께 전달한다.
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
