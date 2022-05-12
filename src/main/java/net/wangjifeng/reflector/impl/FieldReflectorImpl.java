package net.wangjifeng.reflector.impl;

import net.wangjifeng.reflector.FieldReflector;
import net.wangjifeng.reflector.util.ReflectException;
import net.wangjifeng.reflector.util.TypeParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Field反射器的默认实现。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class FieldReflectorImpl<F> implements FieldReflector<F> {

    private final Field field;

    private final Type generic;

    public FieldReflectorImpl(Field field) {
        this.field = field;
        this.generic = TypeParameterResolver.resolveFieldType(this.field, this.field.getDeclaringClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public F readValue(Object target) {
        return ReflectException.unawareException(() -> {
            if (!isPublic()) {
                this.field.setAccessible(true);
            }
            return (F) this.field.get(target);
        });
    }

    @Override
    public void writeValue(Object target, F value) {
        ReflectException.<Void>unawareException(() -> {
            if (!isPublic()) {
                this.field.setAccessible(true);
            }
            this.field.set(target, value);
            return null;
        });
    }

    @Override
    public boolean isThisFieldReflector(String fieldName) {
        return this.field.getName().equals(fieldName);
    }

    @Override
    public Field field() {
        return this.field;
    }

    @Override
    public Type generic() {
        return this.generic;
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> klass) {
        return this.field.isAnnotationPresent(klass);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> klass) {
        return this.field.getAnnotation(klass);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(this.field.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.field.getModifiers());
    }

    @Override
    public String toString() {
        return this.field.toString();
    }

}
