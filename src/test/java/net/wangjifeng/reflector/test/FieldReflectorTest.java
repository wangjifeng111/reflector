package net.wangjifeng.reflector.test;

import net.wangjifeng.reflector.ClassReflector;
import net.wangjifeng.reflector.FieldReflector;
import net.wangjifeng.reflector.impl.ClassReflectorImpl;
import net.wangjifeng.reflector.pojo.Pojo;
import net.wangjifeng.reflector.pojo.User1;
import net.wangjifeng.reflector.pojo.User2;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wjf
 * @date: 2022/5/5
 */
public class FieldReflectorTest {

    private FieldReflector<List<User1>> fieldReflector;

    @Before
    public void before() {
        ClassReflector<User2> classReflector = new ClassReflectorImpl<>(User2.class);
        fieldReflector = classReflector.fieldReflector("user1s");
    }

    @Test
    public void readValueTest() {
        User2 user2 = new User2("tom", 18);
        List<User1> user1s = new ArrayList<>();
        user1s.add(new User1("jack", 20));
        user2.setUser1s(user1s);
        List<User1> user1List = fieldReflector.readValue(user2);
        user1List.forEach(System.out::println);
    }

    @Test
    public void writeValueTest() {
        User2 user2 = new User2("tom", 18);

        List<User1> user1s = new ArrayList<>();
        user1s.add(new User1("jack", 20));

        fieldReflector.writeValue(user2, user1s);
        user2.getUser1s().forEach(System.out::println);
    }

    @Test
    public void isThisFieldReflectorTest() {
        boolean isThisFieldReflector = fieldReflector.isThisFieldReflector("user1s");
        System.out.println(isThisFieldReflector);
    }

    @Test
    public void fieldTest() {
        Field field = fieldReflector.field();
        System.out.println(field);
    }

    @Test
    public void genericTest() {
        Type generic = fieldReflector.generic();
        System.out.println(generic);
    }

    @Test
    public void hasAnnotationTest() {
        boolean hasAnnotation = fieldReflector.hasAnnotation(Pojo.class);
        System.out.println(hasAnnotation);
    }

    @Test
    public void getAnnotationTest() {
        Pojo annotation = fieldReflector.getAnnotation(Pojo.class);
        System.out.println(annotation);
    }

    @Test
    public void isPublicTest() {
        System.out.println(fieldReflector.isPublic());
    }

    @Test
    public void isFinalTest() {
        System.out.println(fieldReflector.isFinal());
    }

}
