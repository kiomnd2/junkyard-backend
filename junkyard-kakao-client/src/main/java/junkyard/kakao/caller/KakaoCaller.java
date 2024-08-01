package junkyard.kakao.caller;

public interface KakaoCaller<T, R> {
    R call(T t);
}
