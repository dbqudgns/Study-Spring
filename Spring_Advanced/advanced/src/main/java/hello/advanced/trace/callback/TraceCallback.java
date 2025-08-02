package hello.advanced.trace.callback;

// 콜백 역할
public interface TraceCallback<T> {
    T call();
}
