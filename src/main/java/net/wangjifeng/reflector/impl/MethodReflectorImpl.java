package net.wangjifeng.reflector.impl;

import net.wangjifeng.reflector.MethodReflector;
import net.wangjifeng.reflector.ParameterReflector;
import net.wangjifeng.reflector.util.ReflectException;
import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Method反射器的默认实现。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class MethodReflectorImpl<M> implements MethodReflector<M> {

    private final Method method;

    private final Type[] methodParamGenerics;

    private final Type returnTypeGeneric;

    private final List<ParameterReflector> parameterReflectors;

    public MethodReflectorImpl(Method method) {
        this.method = method;
        this.methodParamGenerics = TypeParameterResolver.resolveMethodParamTypes(this.method, this.method.getDeclaringClass());
        this.returnTypeGeneric = TypeParameterResolver.resolveReturnType(this.method, this.method.getDeclaringClass());
        this.parameterReflectors = initParameterReflectors();
    }

    @Override
    public Type returnTypeGeneric() {
        return this.returnTypeGeneric;
    }

    @Override
    public Type[] methodParamGenerics() {
        return this.methodParamGenerics;
    }

    @Override
    @SuppressWarnings("unchecked")
    public M invoke(Object target, Object... methodParameters) {
        return ReflectException.unawareException(() -> {
            if (!isPublic()) {
                this.method.setAccessible(true);
            }
            return (M) this.method.invoke(target, methodParameters);
        });
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
    public boolean isThisMethodReflector(String methodName, Class<?>... methodParameterTypes) {
        return this.method.getName().equals(methodName) && Arrays.equals(this.method.getParameterTypes(), methodParameterTypes);
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> klass) {
        return this.method.isAnnotationPresent(klass);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> klass) {
        return this.method.getAnnotation(klass);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(this.method.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.method.getModifiers());
    }

    @Override
    public String toString() {
        return this.method.toString();
    }

    private List<ParameterReflector> initParameterReflectors() {
        Parameter[] parameters = this.method.getParameters();
        List<ParameterReflector> parameterReflectorList = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            parameterReflectorList.add(new ParameterReflectorImpl(parameters[i], i, this.methodParamGenerics[i]));
        }
        return parameterReflectorList;
    }

}
