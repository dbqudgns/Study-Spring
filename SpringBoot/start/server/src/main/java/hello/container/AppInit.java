package hello.container;

import jakarta.servlet.ServletContext;

// 애플리케이션 초기화를 진행하기 위해 인터페이스를 만들어야 한다.
public interface AppInit {
    void onStartup(ServletContext servletContext);
}
