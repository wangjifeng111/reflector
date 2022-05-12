package net.wangjifeng.reflector.test;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.MethodReflector;
import net.wangjifeng.reflector.ParameterReflector;
import net.wangjifeng.reflector.impl.ClassReflectorImpl;
import net.wangjifeng.reflector.pojo.Pojo;
import net.wangjifeng.reflector.pojo.User1;
import net.wangjifeng.reflector.pojo.User2;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wjf
 * @date: 2022/5/5
 */
public class MethodReflectorTest {

    private MethodReflector<List<? extends User1>> getUser1sMethodReflector;

    private MethodReflector<List<? extends User1>> setUser1sMethodReflector;

    @Before
    public void before() {
        ClassReflector<User2> classReflector = new ClassReflectorImpl<>(User2.class);
        getUser1sMethodReflector = classReflector.methodReflector("getUser1s");
        setUser1sMethodReflector = classReflector.methodReflector("setUser1s", List.class);
    }

    @Test
    public void invokeTest() {
        User2 user2 = new User2("tom", 18);
        List<User1> user1s = new ArrayList<>();
        user1s.add(new User1("jack", 20));
        user2.setUser1s(user1s);

        List<? extends User1> list = getUser1sMethodReflector.invoke(user2);
        list.forEach(System.out::println);

        setUser1sMethodReflector.invoke(user2, new ArrayList<>());
        System.out.println(user2.getUser1s());
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
        setUser1sMethodReflector.parameterReflectors().forEach(System.out::println);
    }

    @Test
    public void parameterReflectorNameTest() {
        ParameterReflector user1s = setUser1sMethodReflector.parameterReflector("user1s");
        // ParameterReflector user1s = setUser1sMethodReflector.parameterReflector("arg0"); // throw exception
        System.out.println(user1s);
    }

    @Test
    public void parameterReflectorIndexTest() {
        ParameterReflector arg0 = setUser1sMethodReflector.parameterReflector(0);
        System.out.println(arg0);
    }

    @Test
    public void isThisMethodReflectorTest() {
        boolean isThisMethodReflector = setUser1sMethodReflector.isThisMethodReflector("setUser1s", List.class);
        System.out.println(isThisMethodReflector);
    }

    @Test
    public void methodTest() {
        Method method = getUser1sMethodReflector.method();
        System.out.println(method);
    }

    @Test
    public void returnTypeGenericTest() {
        Type type = getUser1sMethodReflector.returnTypeGeneric();
        System.out.println(type);
    }

    @Test
    public void methodParamGenericsTest() {
        Type[] types = setUser1sMethodReflector.methodParamGenerics();
        for (Type type : types) {
            System.out.println(type);
        }
    }

    @Test
    public void hasAnnotationTest() {
        boolean hasAnnotation = setUser1sMethodReflector.hasAnnotation(Pojo.class);
        System.out.println(hasAnnotation);
    }

    @Test
    public void getAnnotationTest() {
        Pojo annotation = setUser1sMethodReflector.getAnnotation(Pojo.class);
        System.out.println(annotation);
    }

    @Test
    public void isPublicTest() {
        System.out.println(setUser1sMethodReflector.isPublic());
    }

    @Test
    public void isFinalTest() {
        System.out.println(setUser1sMethodReflector.isFinal());
    }

}
