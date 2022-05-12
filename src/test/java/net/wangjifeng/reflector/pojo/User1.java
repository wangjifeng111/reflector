package net.wangjifeng.reflector.pojo;

import java.util.List;

/**
 * 测试pojo，父pojo。
 *
 * @author: wjf
 * @date: 2022/5/5
 */
@Pojo
public class User1 {

    private String user1Name;

    private Integer user1Age;

    public User1(String name, Integer age) {
        this.user1Name = name;
        this.user1Age = age;
    }

    public User1(List<User1> list) {
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public Integer getUser1Age() {
        return user1Age;
    }

    public void setUser1Age(Integer user1Age) {
        this.user1Age = user1Age;
    }

    @Override
    public String toString() {
        return "User1{" +
                "user1Name='" + user1Name + '\'' +
                ", user1Age=" + user1Age +
                '}';
    }
}
