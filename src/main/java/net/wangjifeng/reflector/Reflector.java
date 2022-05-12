package net.wangjifeng.reflector;

import java.lang.annotation.Annotation;

/**
 * 反射器，反射相关操作的核心接口。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public interface Reflector {

    /**
     * 是否包含此注解。
     * @param klass 注解的class对象。
     * @param <A> 注解。
     * @return 是否包含此注解。
     */
    <A extends Annotation> boolean hasAnnotation(Class<A> klass);

    /**
     * 获取此注解。
     * @param klass 注解的class对象。
     * @param <A> 注解。
     * @return 注解。
     */
    <A extends Annotation> A getAnnotation(Class<A> klass);

    /**
     * 当前反射器是否是public修饰。
     * @return 当前反射器是否是public修饰。
     */
    boolean isPublic();

    /**
     * 当前反射器是否是final修饰。
     * @return 当前反射器是否是final修饰。
     */
    boolean isFinal();

}
