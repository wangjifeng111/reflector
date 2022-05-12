package net.wangjifeng.reflector;

import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Constructor反射器，Constructor反射相关操作的核心接口。
 * @param <T> Java类。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public interface ConstructorReflector<T> extends Reflector {

    /**
     * 创建新实例。
     * @param constructorParameters 构造器参数列表。
     * @return 新实例。
     */
    T newInstance(Object... constructorParameters);

    /**
     * 是否是当前构造反射器。
     * @param constructorParameterTypes 构造器参数类型列表。
     * @return 是否是当前构造反射器。
     */
    boolean isThisConstructorReflector(Class<?>... constructorParameterTypes);

    /**
     * 获取所有的参数反射器。
     * @return 所有的参数反射器。
     */
    List<ParameterReflector> parameterReflectors();

    /**
     * 根据名称获取某一个参数反射器。不存在则抛出异常，具体请参考{@link ParameterReflector#name()}。
     * @param name 参数名称。
     * @return 某一个参数反射器。
     */
    ParameterReflector parameterReflector(String name);

    /**
     * 根据参数在参数列表的索引位置获取某一个参数反射器。
     * @param index 参数在参数列表的索引位置。
     * @return 某一个参数反射器。
     */
    ParameterReflector parameterReflector(int index);

    /**
     * 获取当前操作的constructor。
     * @return 当前操作的constructor。
     */
    Constructor<T> constructor();

    /**
     * 获取构造器参数的泛型。
     * 如果是参数化类型{@link java.lang.reflect.ParameterizedType}，则返回实际的类型为{@link TypeParameterResolver.ParameterizedTypeImpl}
     * 如果是通配符类型{@link java.lang.reflect.WildcardType}，则返回实际的类型为{@link TypeParameterResolver.WildcardTypeImpl}
     * 如果是泛型数组类型{@link java.lang.reflect.GenericArrayType}，则返回实际的类型为{@link TypeParameterResolver.GenericArrayTypeImpl}
     * @return 构造器参数的泛型。
     */
    public Type[] constructorParamGenerics();
}
