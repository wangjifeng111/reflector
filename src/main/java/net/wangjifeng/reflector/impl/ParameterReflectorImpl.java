package net.wangjifeng.reflector.impl;

import net.wangjifeng.reflector.ParameterReflector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * Parameter反射器的默认实现。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class ParameterReflectorImpl implements ParameterReflector {

    private final Parameter parameter;

    private final int index;

    private final Type generic;

    ParameterReflectorImpl(Parameter parameter, int index, Type generic) {
        this.parameter = parameter;
        this.index = index;
        this.generic = generic;
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> klass) {
        return this.parameter.isAnnotationPresent(klass);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> klass) {
        return this.parameter.getAnnotation(klass);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(this.parameter.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.parameter.getModifiers());
    }

    @Override
    public String name() {
        return this.parameter.getName();
    }

    @Override
    public int index() {
        return this.index;
    }

    @Override
    public Parameter parameter() {
        return this.parameter;
    }

    @Override
    public Type generic() {
        return this.generic;
    }

    @Override
    public String toString() {
        return this.parameter.toString();
    }
}
