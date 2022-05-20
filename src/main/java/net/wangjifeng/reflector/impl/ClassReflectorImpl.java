package net.wangjifeng.reflector.impl;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.ConstructorReflector;
import net.wangjifeng.reflector.FieldReflector;
import net.wangjifeng.reflector.MethodReflector;
import net.wangjifeng.reflector.util.ReflectException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Class反射器的默认实现。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
public class ClassReflectorImpl<T> implements ClassReflector<T> {

    private final Class<T> klass;

    private final List<ConstructorReflector<T>> constructorReflectors;

    private final List<MethodReflector<?>> methodReflectors;

    private final List<FieldReflector<?>> fieldReflectors;

    @SuppressWarnings("unchecked")
    public ClassReflectorImpl(String className) {
        this((Class<T>) ReflectException.unawareException(() -> Class.forName(className)));
    }

    public ClassReflectorImpl(Class<T> klass) {
        this.klass = klass;
        this.constructorReflectors = initConstructorReflectors();
        this.methodReflectors = initMethodReflectors();
        this.fieldReflectors = initFieldReflectors();
    }

    @Override
    public List<ConstructorReflector<T>> constructorReflectors() {
        return this.constructorReflectors;
    }

    @Override
    public ConstructorReflector<T> constructorReflector(Class<?>... constructorParameterTypes) {
        return this.constructorReflectors.stream()
                .filter(constructorReflector -> constructorReflector.isThisConstructorReflector(constructorParameterTypes))
                .findFirst()
                .orElseThrow(() -> {
                    var typeName = this.klass.getTypeName();
                    var parameters = new StringJoiner(", ", "(", ")");
                    Arrays.stream(constructorParameterTypes).forEach(constructorParameterType -> parameters.add(constructorParameterType.getTypeName()));
                    return new ReflectException(String.format("不存在一个构造方法: [%s%s]", typeName, parameters));
                });
    }

    @Override
    public List<MethodReflector<?>> methodReflectors() {
         return this.methodReflectors;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> MethodReflector<M> methodReflector(String methodName, Class<?>... methodParameterTypes) {
        return (MethodReflector<M>) this.methodReflectors.stream()
                .filter(methodReflector -> methodReflector.isThisMethodReflector(methodName, methodParameterTypes))
                .findFirst()
                .orElseThrow(() -> {
                    var typeName = this.klass.getTypeName();
                    var parameters = new StringJoiner(", ", "(", ")");
                    Arrays.stream(methodParameterTypes).forEach(methodParameterType -> parameters.add(methodParameterType.getTypeName()));
                    return new ReflectException(String.format("不存在一个方法: [%s.%s%s]", typeName, methodName, parameters));
                });
    }

    @Override
    public List<FieldReflector<?>> fieldReflectors() {
        return this.fieldReflectors;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> FieldReflector<F> fieldReflector(String fieldName) {
        return (FieldReflector<F>) this.fieldReflectors.stream()
                .filter(fieldReflector -> fieldReflector.isThisFieldReflector(fieldName))
                .findFirst()
                .orElseThrow(() -> {
                    var typeName = this.klass.getTypeName();
                    return new ReflectException(String.format("不存在一个字段: [%s.%s]", typeName, fieldName));
                });
    }

    @Override
    public Class<? extends T> klass() {
        return this.klass;
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> klass) {
        return this.klass.isAnnotationPresent(klass);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> klass) {
        return this.klass.getAnnotation(klass);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(this.klass.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.klass.getModifiers());
    }

    @Override
    public String toString() {
        return this.klass.toString();
    }

    @SuppressWarnings("unchecked")
    private List<ConstructorReflector<T>> initConstructorReflectors() {
        var constructors = this.klass.getDeclaredConstructors();
        List<ConstructorReflector<T>> constructorReflectorList = new ArrayList<>(constructors.length);
        for (Constructor<?> constructor : constructors) {
            constructorReflectorList.add(new ConstructorReflectorImpl<>((Constructor<T>) constructor));
        }
        return constructorReflectorList;
    }

    private List<MethodReflector<?>> initMethodReflectors() {
        var methods = this.klass.getDeclaredMethods();
        List<MethodReflector<?>> methodReflectorList = new ArrayList<>(methods.length);
        for (Method method : methods) {
            methodReflectorList.add(new MethodReflectorImpl<>(method));
        }
        return methodReflectorList;
    }

    private List<FieldReflector<?>> initFieldReflectors() {
        var fields = this.klass.getDeclaredFields();
        List<FieldReflector<?>> fieldReflectorList = new ArrayList<>(fields.length);
        for (Field field : fields) {
            fieldReflectorList.add(new FieldReflectorImpl<>(field));
        }

        Class<?> superClass = this.klass;

        while (superClass != Object.class) {
            superClass = superClass.getSuperclass();
            for (Field superField : superClass.getDeclaredFields()) {
                fieldReflectorList.add(new FieldReflectorImpl<>(superField));
            }
        }

        return fieldReflectorList;
    }

}
