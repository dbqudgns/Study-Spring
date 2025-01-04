package hello.springtx.order;

//결제 잔고가 부족하면 발생하는 비즈니스 예외 : Exception을 상속 받아서 체크 예외에 해당된다.
public class NotEnoughMoneyException extends Exception{

    public NotEnoughMoneyException(String message) {
        super(message);
    }

}
