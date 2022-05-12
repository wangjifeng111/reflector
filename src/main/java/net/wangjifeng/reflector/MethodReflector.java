package net.wangjifeng.reflector;

import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Method反射器，Method反射相关操作的核心接口。
 * @param <M> 方法返回值类型。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public interface MethodReflector<M> extends Reflector {

    /**
     * 执行方法，当方法返回值为void或其包装类则返回null。
     * @param target 方法的执行者，当此方法为静态方法时，此参数应该为null。
     * @param methodParameters 方法参数。
     * @return 返回值。
     */
    M invoke(Object target, Object... methodParameters);

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
     * 是否是当前方法反射器。
     * @param methodName 方法名称。
     * @param methodParameterTypes 方法参数类型列表。
     * @return 是否是当前方法反射器。
     */
    boolean isThisMethodReflector(String methodName, Class<?>... methodParameterTypes);

    /**
     * 获取当前操作的method。
     * @return 当前操作的method。
     */
    Method method();

    /**
     * 获取方法返回值类型的泛型。
     * 如果是参数化类型{@link java.lang.reflect.ParameterizedType}，则返回实际的类型为{@link TypeParameterResolver.ParameterizedTypeImpl}
     * 如果是通配符类型{@link java.lang.reflect.WildcardType}，则返回实际的类型为{@link TypeParameterResolver.WildcardTypeImpl}
     * 如果是泛型数组类型{@link java.lang.reflect.GenericArrayType}，则返回实际的类型为{@link TypeParameterResolver.GenericArrayTypeImpl}
     * @return 方法返回值类型的泛型。
     */
    public Type returnTypeGeneric();

    /**
     * 获取方法参数的泛型。
     * 如果是参数化类型{@link java.lang.reflect.ParameterizedType}，则返回实际的类型为{@link TypeParameterResolver.ParameterizedTypeImpl}
     * 如果是通配符类型{@link java.lang.reflect.WildcardType}，则返回实际的类型为{@link TypeParameterResolver.WildcardTypeImpl}
     * 如果是泛型数组类型{@link java.lang.reflect.GenericArrayType}，则返回实际的类型为{@link TypeParameterResolver.GenericArrayTypeImpl}
     * @return 方法参数的泛型。
     */
    public Type[] methodParamGenerics();
}
