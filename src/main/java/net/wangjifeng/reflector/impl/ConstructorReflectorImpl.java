package net.wangjifeng.reflector.impl;

import net.wangjifeng.reflector.ConstructorReflector;
import net.wangjifeng.reflector.ParameterReflector;
import net.wangjifeng.reflector.util.ReflectException;
import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constructor反射器的默认实现。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class ConstructorReflectorImpl<T> implements ConstructorReflector<T> {

    private final Constructor<T> constructor;

    private final Type[] constructorParamGenerics;

    private final List<ParameterReflector> parameterReflectors;

    public ConstructorReflectorImpl(Constructor<T> constructor) {
        this.constructor = constructor;
        this.constructorParamGenerics = TypeParameterResolver.resolveConstructorParamTypes(this.constructor, this.constructor.getDeclaringClass());
        this.parameterReflectors = initParameterReflectors();
    }

    @Override
    public T newInstance(Object... constructorParameters) {
        return ReflectException.unawareException(() -> {
            if (!isPublic()) {
                this.constructor.setAccessible(true);
            }
            return this.constructor.newInstance(constructorParameters);
        });
    }

    @Override
    public boolean isThisConstructorReflector(Class<?>... constructorParameterTypes) {
        return Arrays.equals(this.constructor.getParameterTypes(), constructorParameterTypes);
    }

    @Override
    public List<ParameterReflector> parameterReflectors() {
        return this.parameterReflectors;
    }

    @Override
    public ParameterReflector parameterReflector(String name) {
        return this.parameterReflectors.stream()
                .filter(parameterReflector -> parameterReflector.name().equals(name))
                .findFirst().orElseThrow(() -> new ReflectException(String.format(
                        "不存在一个参数名称为[%s]的参数，如若此参数确实存在，则出现此问题的原因可能是在class进行编译的时候没有开启-parameters编译参数",
                        name
                )));
    }

    @Override
    public ParameterReflector parameterReflector(int index) {
        return this.parameterReflectors.stream()
                .filter(parameterReflector -> parameterReflector.index() == index)
                .findFirst().orElseThrow(() ->  new ReflectException(String.format("不存在一个参数索引为[%d]的参数", index)));
    }

    @Override
    public Constructor<T> constructor() {
        return this.constructor;
    }

    @Override
    public Type[] constructorParamGenerics() {
        return this.constructorParamGenerics;
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> klass) {
        return this.constructor.isAnnotationPresent(klass);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> klass) {
        return this.constructor.getAnnotation(klass);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(this.constructor.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.constructor.getModifiers());
    }

    @Override
    public String toString() {
        return this.constructor.toString();
    }

    private List<ParameterReflector> initParameterReflectors() {
        Parameter[] parameters = this.constructor.getParameters();
        List<ParameterReflector> parameterReflectorList = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            parameterReflectorList.add(new ParameterReflectorImpl(parameters[i], i, this.constructorParamGenerics[i]));
        }
        return parameterReflectorList;
    }

}
