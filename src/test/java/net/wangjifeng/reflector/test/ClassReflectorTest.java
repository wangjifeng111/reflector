package net.wangjifeng.reflector.test;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.ConstructorReflector;
import net.wangjifeng.reflector.FieldReflector;
import net.wangjifeng.reflector.MethodReflector;
import net.wangjifeng.reflector.impl.ClassReflectorImpl;
import net.wangjifeng.reflector.pojo.Pojo;
import net.wangjifeng.reflector.pojo.User1;
import org.junit.Test;

/**
 * @author: wjf
 * @date: 2022/5/5
 */
public class ClassReflectorTest {

    @Test
    public void constructorReflectorsTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        classReflector.constructorReflectors().forEach(System.out::println);
    }

    @Test
    public void constructorReflectorTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        ConstructorReflector<? extends User1> constructorReflector = classReflector.constructorReflector(String.class, Integer.class);
        System.out.println(constructorReflector);
    }

    @Test
    public void methodReflectorsTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        classReflector.methodReflectors().forEach(System.out::println);
    }

    @Test
    public void methodReflectorTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        MethodReflector<Object> getUser1Name = classReflector.methodReflector("getUser1Name");
        System.out.println(getUser1Name);
    }

    @Test
    public void fieldReflectorsTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        classReflector.fieldReflectors().forEach(System.out::println);
    }

    @Test
    public void fieldReflectorTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        FieldReflector<Object> user1Name = classReflector.fieldReflector("user1Name");
        System.out.println(user1Name);
    }

    @Test
    public void klassTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        Class<? extends User1> klass = classReflector.klass();
        System.out.println(klass);
    }

    @Test
    public void hasAnnotationTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        boolean hasAnnotation = classReflector.hasAnnotation(Pojo.class);
        System.out.println(hasAnnotation);
    }

    @Test
    public void getAnnotationTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        Pojo annotation = classReflector.getAnnotation(Pojo.class);
        System.out.println(annotation);
    }

    @Test
    public void isPublicTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        System.out.println(classReflector.isPublic());
    }

    @Test
    public void isFinalTest() {
        ClassReflector<User1> classReflector = new ClassReflectorImpl<>(User1.class);
        System.out.println(classReflector.isFinal());
    }

}
