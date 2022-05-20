package net.wangjifeng.reflector;

import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.reflect.Type;

/**
 * 可泛化的，带泛型的类型部分需要实现。
 *
 * @author: wjf
 * @date: 2022/5/20
 */
@FunctionalInterface
public interface Generalizable {

    /**
     * 获取泛型实际类型，返回的类型可能是：
     * 1. 没有泛型时，返回{@link Class}。
     * 2. 如果是参数化类型{@link java.lang.reflect.ParameterizedType}，则返回实际的类型为{@link TypeParameterResolver.ParameterizedTypeImpl}。
     * 3. 如果是通配符类型{@link java.lang.reflect.WildcardType}，则返回实际的类型为{@link TypeParameterResolver.WildcardTypeImpl}。
     * 4. 如果是泛型数组类型{@link java.lang.reflect.GenericArrayType}，则返回实际的类型为{@link TypeParameterResolver.GenericArrayTypeImpl}。
     * @return 泛型类型。
     */
    Type generic();

}
