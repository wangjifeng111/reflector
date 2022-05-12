package net.wangjifeng.reflector;

import java.util.List;

/**
 * Class反射器，Class反射相关操作的核心接口。
 * @param <T> Java类。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public interface ClassReflector<T> extends Reflector {

    /**
     * 获取所有的构造器反射器。
     * @return 所有的构造器反射器。
     */
    List<ConstructorReflector<? extends T>> constructorReflectors();

    /***
     * 获取某一个构造器反射器，不存在则抛出异常。
     * @param constructorParameterTypes 构造器参数类型列表。
     * @return 某一个构造器反射器。
     */
    ConstructorReflector<? extends T> constructorReflector(Class<?>... constructorParameterTypes);

    /**
     * 获取所有(只获取本类的方法反射器)的方法反射器。
     * @return 所有的方法反射器。
     */
    List<MethodReflector<?>> methodReflectors();

    /**
     * 获取某一个方法反射器，不存在则抛出异常。
     * @param methodName 方法名称。
     * @param methodParameterTypes 方法参数类型列表。
     * @param <M> 返回值类型。
     * @return 某一个方法反射器。
     */
    <M> MethodReflector<M> methodReflector(String methodName, Class<?>... methodParameterTypes);

    /**
     * 获取所有(字段反射器包括超类的字段)的字段反射器。
     * @return 所有的字段反射器。
     */
    List<FieldReflector<?>> fieldReflectors();

    /**
     * 获取某一个字段反射器，不存在则抛出遗产。
     * @param fieldName 字段名称。
     * @param <F> 字段类型。
     * @return 某一个字段反射器。
     */
    <F> FieldReflector<F> fieldReflector(String fieldName);

    /**
     * 获取当前操作的class。
     * @return 当前操作的class。
     */
    Class<? extends T> klass();
}
