package net.wangjifeng.reflector;

import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * Parameter反射器，Parameter反射相关操作的核心接口。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public interface ParameterReflector extends Reflector {

    /**
     * 返回参数的名称。对于Jdk1.8及以上，编译时加入参数-parameters参数，否则实际参数名称不可获取，此时请使用{@link #index()}方法获取。
     * @return 参数名称。对于编译时加入参数-parameters参数获取到的参数名称是实际参数名称，对于没有加此编译参数的获取到的参数名称则是[arg + index]。
     */
    String name();

    /**
     * 返回参数在参数列表中的位置。
     * @return 参数在参数列表中的位置。
     */
    int index();

    /**
     * 获取当前操作的parameter。
     * @return 当前操作的parameter。
     */
    Parameter parameter();

    /**
     * 获取字段的泛型类型。
     * 如果是参数化类型{@link java.lang.reflect.ParameterizedType}，则返回实际的类型为{@link TypeParameterResolver.ParameterizedTypeImpl}
     * 如果是通配符类型{@link java.lang.reflect.WildcardType}，则返回实际的类型为{@link TypeParameterResolver.WildcardTypeImpl}
     * 如果是泛型数组类型{@link java.lang.reflect.GenericArrayType}，则返回实际的类型为{@link TypeParameterResolver.GenericArrayTypeImpl}
     * @return 字段的泛型类型。
     */
    public Type generic();

}