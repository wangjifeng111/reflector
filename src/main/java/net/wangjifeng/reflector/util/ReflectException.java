package net.wangjifeng.reflector.util;

/**
 * 自定义反射异常。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class ReflectException extends RuntimeException {

    public ReflectException(Throwable throwable) {
        this(throwable.getMessage());
    }

    public ReflectException(String msg) {
        super(msg);
    }

    /**
     * 无感知异常。
     * @param unaware 无感知。
     * @param <R> 结果。
     * @return 结果。
     */
    public static <R> R unawareException(Unaware<R> unaware) {
        return unaware.defaultUnaware();
    }

    /**
     * 无感知接口，统一处理异常。
     * @param <R> 结果。
     */
    @FunctionalInterface
    public interface Unaware<R> {

        /**
         * 具体的处理逻辑。
         * @return 结果。
         * @throws Throwable 抛出的异常。
         */
        @SuppressWarnings("all")
        R unaware() throws Throwable;

        /**
         * 默认无感知。
         * @return 结果。
         */
        default R defaultUnaware() {
            try {
                return unaware();
            } catch (Throwable throwable) {
                throw new ReflectException(throwable);
            }
        }

    }

}
