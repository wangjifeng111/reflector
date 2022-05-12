package net.wangjifeng.reflector.test;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.ConstructorReflector;
import net.wangjifeng.reflector.ParameterReflector;
import net.wangjifeng.reflector.impl.ClassReflectorImpl;
import net.wangjifeng.reflector.pojo.Pojo;
import net.wangjifeng.reflector.pojo.User1;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: wjf
 * @date: 2022/5/5
 */
public class ConstructorReflectorTest {

    private ConstructorReflector<? extends User1> constructorReflector;

    @Before
    public void before() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>("net.wangjifeng.reflector.pojo.User1");
        constructorReflector = classReflector.constructorReflector(String.class, Integer.class);
    }

    @Test
    public void newInstanceTest() {
        User1 tom = constructorReflector.newInstance("tom", 18);
        System.out.println(tom);
    }

    @Test
    public void isThisConstructorReflectorTest() {
        boolean isThisConstructorReflector = constructorReflector.isThisConstructorReflector(String.class, Integer.class);
        System.out.println(isThisConstructorReflector);
    }

    /**
     * 打印如下所示，则说明没有开启-parameter编译参数。(只要添加此项目，即默认开启-parameter编译参数)
     * <pre>{@code
     * java.lang.String arg0
     * java.lang.Integer arg1
     * }</pre>
     */
    @Test
    public void parameterReflectorsTest() {
        constructorReflector.parameterReflectors().forEach(System.out::println);
    }

    @Test
    public void parameterReflectorNameTest() {
        ParameterReflector user1Name = constructorReflector.parameterReflector("name");
        // ParameterReflector user1Name = constructorReflector.parameterReflector("arg0"); // throw exception
        System.out.println(user1Name);
    }

    @Test
    public void parameterReflectorIndexTest() {
        ParameterReflector user1Name = constructorReflector.parameterReflector(0);
        System.out.println(user1Name);
    }

    @Test
    public void constructorTest() {
        Constructor<? extends User1> constructor = constructorReflector.constructor();
        System.out.println(constructor);
    }

    @Test
    public void constructorParamGenericsTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<User1>(User1.class);
        ConstructorReflector<? extends User1> constructorReflector = classReflector.constructorReflector(List.class);
        Type[] types = constructorReflector.constructorParamGenerics();
        for (Type type : types) {
            System.out.println(type);
        }
    }

    @Test
    public void hasAnnotationTest() {
        boolean hasAnnotation = constructorReflector.hasAnnotation(Pojo.class);
        System.out.println(hasAnnotation);
    }

    @Test
    public void getAnnotationTest() {
        Pojo annotation = constructorReflector.getAnnotation(Pojo.class);
        System.out.println(annotation);
    }

    @Test
    public void isPublicTest() {
        System.out.println(constructorReflector.isPublic());
    }

    @Test
    public void isFinalTest() {
        System.out.println(constructorReflector.isFinal());
    }

}
