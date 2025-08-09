package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.Subject;

// Subject 인터페이스를 호출하는 클라이언트 코드이다.
public class ProxyPatternClient {

    private Subject subject;

    public ProxyPatternClient(Subject subject) {
        this.subject = subject;
    }

    public void execute() {
        subject.operation();
    }
}
