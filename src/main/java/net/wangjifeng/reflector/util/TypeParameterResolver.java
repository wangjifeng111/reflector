package net.wangjifeng.reflector.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类型参数解析器。
 *
 * @author: wjf
 * @date: 2022/4/21
 */
@SuppressWarnings("all")
public class TypeParameterResolver {

    /**
     * 基本数据类型及其包装类。
     */
    public static final List<Class<?>> Primitive_Type;
    /**
     * 特殊的引用类型。
     */
    public static final List<Class<?>> Object_Type;

    static {
        Primitive_Type = new ArrayList<>();
        Primitive_Type.add(byte.class);
        Primitive_Type.add(short.class);
        Primitive_Type.add(int.class);
        Primitive_Type.add(long.class);
        Primitive_Type.add(float.class);
        Primitive_Type.add(double.class);
        Primitive_Type.add(char.class);
        Primitive_Type.add(boolean.class);
        Primitive_Type.add(void.class);
        Primitive_Type.add(Byte.class);
        Primitive_Type.add(Short.class);
        Primitive_Type.add(Integer.class);
        Primitive_Type.add(Long.class);
        Primitive_Type.add(Float.class);
        Primitive_Type.add(Double.class);
        Primitive_Type.add(Character.class);
        Primitive_Type.add(Boolean.class);
        Primitive_Type.add(Void.class);

        Object_Type = new ArrayList<>();
        Object_Type.add(Object.class);
    }

    private TypeParameterResolver() {
        super();
    }

    /**
     * 解析字段的类型。
     *
     * @param field   字段。
     * @param srcType 字段所属class。
     * @return 字段的类型，如果字段是泛型，则获取此类型的泛型上限。
     */
    public static Type resolveFieldType(Field field, Type srcType) {
        var fieldType = field.getGenericType();
        var declaringClass = field.getDeclaringClass();
        return resolveType(fieldType, srcType, declaringClass);
    }

    /**
     * 解析方法的返回值的类型。
     *
     * @param method  方法。
     * @param srcType 方法所属class。
     * @return 方法的返回值的类型，如果字段是泛型，则获取此类型的泛型上限。
     */
    public static Type resolveReturnType(Method method, Type srcType) {
        var returnType = method.getGenericReturnType();
        var declaringClass = method.getDeclaringClass();
        return resolveType(returnType, srcType, declaringClass);
    }

    /**
     * 解析方法的参数的类型。
     *
     * @param method  方法。
     * @param srcType 方法所属class。
     * @return 方法的参数的类型，如果字段是泛型，则获取此类型的泛型上限。
     */
    public static Type[] resolveMethodParamTypes(Method method, Type srcType) {
        var paramTypes = method.getGenericParameterTypes();
        var declaringClass = method.getDeclaringClass();
        var result = new Type[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            result[i] = resolveType(paramTypes[i], srcType, declaringClass);
        }
        return result;
    }

    /**
     * 解析构造器的参数的类型。
     *
     * @param constructor  构造器。
     * @param srcType 构造器所属class。
     * @return 构造器的参数的类型，如果字段是泛型，则获取此类型的泛型上限。
     */
    public static Type[] resolveConstructorParamTypes(Constructor<?> constructor, Type srcType) {
        var paramTypes = constructor.getGenericParameterTypes();
        var declaringClass = constructor.getDeclaringClass();
        var result = new Type[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            result[i] = resolveType(paramTypes[i], srcType, declaringClass);
        }
        return result;
    }

    private static Type resolveType(Type type, Type srcType, Class<?> declaringClass) {
        if (type instanceof TypeVariable typeVariable) {
            return resolveTypeVar(typeVariable, srcType, declaringClass);
        } else if (type instanceof ParameterizedType parameterizedType) {
            return resolveParameterizedType(parameterizedType, srcType, declaringClass);
        } else if (type instanceof GenericArrayType genericArrayType) {
            return resolveGenericArrayType(genericArrayType, srcType, declaringClass);
        } else {
            return type;
        }
    }

    private static Type resolveGenericArrayType(GenericArrayType genericArrayType, Type srcType, Class<?> declaringClass) {
        var componentType = genericArrayType.getGenericComponentType();
        Type resolvedComponentType = null;
        if (componentType instanceof TypeVariable typeVariable) {
            resolvedComponentType = resolveTypeVar(typeVariable, srcType, declaringClass);
        } else if (componentType instanceof GenericArrayType newGenericArrayType) {
            resolvedComponentType = resolveGenericArrayType(newGenericArrayType, srcType, declaringClass);
        } else if (componentType instanceof ParameterizedType parameterizedType) {
            resolvedComponentType = resolveParameterizedType(parameterizedType, srcType, declaringClass);
        }
        if (resolvedComponentType instanceof Class klass) {
            return Array.newInstance(klass, 0).getClass();
        } else {
            return new GenericArrayTypeImpl(resolvedComponentType);
        }
    }

    private static ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType, Type srcType, Class<?> declaringClass) {
        var rawType = (Class<?>) parameterizedType.getRawType();
        var typeArgs = parameterizedType.getActualTypeArguments();
        var args = new Type[typeArgs.length];
        for (int i = 0; i < typeArgs.length; i++) {
            if (typeArgs[i] instanceof TypeVariable typeVariable) {
                args[i] = resolveTypeVar(typeVariable, srcType, declaringClass);
            } else if (typeArgs[i] instanceof ParameterizedType newParameterizedType) {
                args[i] = resolveParameterizedType(newParameterizedType, srcType, declaringClass);
            } else if (typeArgs[i] instanceof WildcardType wildcardType) {
                args[i] = resolveWildcardType(wildcardType, srcType, declaringClass);
            } else {
                args[i] = typeArgs[i];
            }
        }
        return new ParameterizedTypeImpl(rawType, null, args);
    }

    private static Type resolveWildcardType(WildcardType wildcardType, Type srcType, Class<?> declaringClass) {
        var lowerBounds = resolveWildcardTypeBounds(wildcardType.getLowerBounds(), srcType, declaringClass);
        var upperBounds = resolveWildcardTypeBounds(wildcardType.getUpperBounds(), srcType, declaringClass);
        return new WildcardTypeImpl(lowerBounds, upperBounds);
    }

    private static Type[] resolveWildcardTypeBounds(Type[] bounds, Type srcType, Class<?> declaringClass) {
        var result = new Type[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i] instanceof TypeVariable typeVariable) {
                result[i] = resolveTypeVar(typeVariable, srcType, declaringClass);
            } else if (bounds[i] instanceof ParameterizedType parameterizedType) {
                result[i] = resolveParameterizedType(parameterizedType, srcType, declaringClass);
            } else if (bounds[i] instanceof WildcardType wildcardType) {
                result[i] = resolveWildcardType(wildcardType, srcType, declaringClass);
            } else {
                result[i] = bounds[i];
            }
        }
        return result;
    }

    private static Type resolveTypeVar(TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass) {
        Type result;
        Class<?> clazz;
        if (srcType instanceof Class) {
            clazz = (Class<?>) srcType;
        } else if (srcType instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) srcType;
            clazz = (Class<?>) parameterizedType.getRawType();
        } else {
            throw new IllegalArgumentException("The 2nd arg must be Class or ParameterizedType, but was: " + srcType.getClass());
        }

        if (clazz == declaringClass) {
            var bounds = typeVar.getBounds();
            if (bounds.length > 0) {
                return bounds[0];
            }
            return Object.class;
        }

        var superclass = clazz.getGenericSuperclass();
        result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superclass);
        if (result != null) {
            return result;
        }

        var superInterfaces = clazz.getGenericInterfaces();
        for (Type superInterface : superInterfaces) {
            result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superInterface);
            if (result != null) {
                return result;
            }
        }
        return Object.class;
    }

    private static Type scanSuperTypes(TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass, Class<?> clazz, Type superclass) {
        if (superclass instanceof ParameterizedType parentAsType) {
            Class<?> parentAsClass = (Class<?>) parentAsType.getRawType();
            TypeVariable<?>[] parentTypeVars = parentAsClass.getTypeParameters();
            if (srcType instanceof ParameterizedType parameterizedType) {
                parentAsType = translateParentTypeVars(parameterizedType, clazz, parentAsType);
            }
            if (declaringClass == parentAsClass) {
                for (int i = 0; i < parentTypeVars.length; i++) {
                    if (typeVar.equals(parentTypeVars[i])) {
                        return parentAsType.getActualTypeArguments()[i];
                    }
                }
            }
            if (declaringClass.isAssignableFrom(parentAsClass)) {
                return resolveTypeVar(typeVar, parentAsType, declaringClass);
            }
        } else if (superclass instanceof Class && declaringClass.isAssignableFrom((Class<?>) superclass)) {
            return resolveTypeVar(typeVar, superclass, declaringClass);
        }
        return null;
    }

    private static ParameterizedType translateParentTypeVars(ParameterizedType srcType, Class<?> srcClass, ParameterizedType parentType) {
        var parentTypeArgs = parentType.getActualTypeArguments();
        var srcTypeArgs = srcType.getActualTypeArguments();
        var srcTypeVars = srcClass.getTypeParameters();
        var newParentArgs = new Type[parentTypeArgs.length];
        boolean noChange = true;
        for (int i = 0; i < parentTypeArgs.length; i++) {
            if (parentTypeArgs[i] instanceof TypeVariable) {
                for (int j = 0; j < srcTypeVars.length; j++) {
                    if (srcTypeVars[j].equals(parentTypeArgs[i])) {
                        noChange = false;
                        newParentArgs[i] = srcTypeArgs[j];
                    }
                }
            } else {
                newParentArgs[i] = parentTypeArgs[i];
            }
        }
        return noChange ? parentType : new ParameterizedTypeImpl((Class<?>) parentType.getRawType(), null, newParentArgs);
    }

    /**
     * 参数化类型。
     */
    public static class ParameterizedTypeImpl implements ParameterizedType {

        private Type rawType;

        private Type ownerType;

        private Type[] actualTypeArguments;

        public ParameterizedTypeImpl(Type rawType, Type ownerType, Type[] actualTypeArguments) {
            super();
            this.rawType = rawType;
            this.ownerType = ownerType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public String toString() {
            return "ParameterizedTypeImpl [rawType=" + rawType + ", ownerType=" + ownerType + ", actualTypeArguments=" + Arrays.toString(actualTypeArguments) + "]";
        }
    }

    /**
     * 通配符类型。
     */
    public static class WildcardTypeImpl implements WildcardType {

        private Type[] lowerBounds;

        private Type[] upperBounds;

        public WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            super();
            this.lowerBounds = lowerBounds;
            this.upperBounds = upperBounds;
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds;
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds;
        }

        @Override
        public String toString() {
            return "WildcardTypeImpl{" +
                    "lowerBounds=" + Arrays.toString(lowerBounds) +
                    ", upperBounds=" + Arrays.toString(upperBounds) +
                    '}';
        }
    }

    /**
     * 泛型数组类型。
     */
    public static class GenericArrayTypeImpl implements GenericArrayType {

        private Type genericComponentType;

        public GenericArrayTypeImpl(Type genericComponentType) {
            super();
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }

        @Override
        public String toString() {
            return "GenericArrayTypeImpl{" +
                    "genericComponentType=" + genericComponentType +
                    '}';
        }
    }
}
