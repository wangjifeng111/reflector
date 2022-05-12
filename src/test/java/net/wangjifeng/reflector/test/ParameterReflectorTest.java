package net.wangjifeng.reflector.test;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.ParameterReflector;
import net.wangjifeng.reflector.impl.ClassReflectorImpl;
import net.wangjifeng.reflector.pojo.Pojo;
import net.wangjifeng.reflector.pojo.User2;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author: wjf
 * @date: 2022/5/5
 */
public class ParameterReflectorTest {

    private ParameterReflector parameterReflector;

    @Before
    public void before() {
        ClassReflector<User2> classReflector = new ClassReflectorImpl<>(User2.class);
        parameterReflector = classReflector.methodReflector("setUser1s", List.class).parameterReflector(0);
    }

    @Test
    public void nameTest() {
        System.out.println(parameterReflector.name()); // arg0
    }

    @Test
    public void index() {
        System.out.println(parameterReflector.index());
    }

    @Test
    public void parameterTest() {
        System.out.println(parameterReflector.parameter());
    }

    @Test
    public void genericTest() {
        System.out.println(parameterReflector.generic());
    }

    @Test
    public void hasAnnotationTest() {
        boolean hasAnnotation = parameterReflector.hasAnnotation(Pojo.class);
        System.out.println(hasAnnotation);
    }

    @Test
    public void getAnnotationTest() {
        Pojo annotation = parameterReflector.getAnnotation(Pojo.class);
        System.out.println(annotation);
    }

    @Test
    public void isPublicTest() {
        System.out.println(parameterReflector.isPublic());
    }

    @Test
    public void isFinalTest() {
        System.out.println(parameterReflector.isFinal());
    }

}
