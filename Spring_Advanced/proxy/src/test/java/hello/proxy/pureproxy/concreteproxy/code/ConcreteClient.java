package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {

    private ConcreteLogic concrteLogic;

    public ConcreteClient(ConcreteLogic concreateLogic) {
        this.concrteLogic = concreateLogic;
    }

    public void execute() {
        concrteLogic.operation();
    }
}
