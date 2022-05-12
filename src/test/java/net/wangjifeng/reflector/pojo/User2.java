package net.wangjifeng.reflector.pojo;

import java.util.List;

/**
 * 测试pojo，子pojo。
 *
 * @author: wjf
 * @date: 2022/5/5
 */
public class User2 {

    private String user2Name;

    private Integer user2Age;

    private List<? extends User1> user1s;

    public User2(String name, Integer age) {
        this.user2Name = name;
        this.user2Age = age;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public Integer getUser2Age() {
        return user2Age;
    }

    public void setUser2Age(Integer user2Age) {
        this.user2Age = user2Age;
    }

    public List<? extends User1> getUser1s() {
        return user1s;
    }

    public void setUser1s(@Pojo List<? extends User1> user1s) {
        this.user1s = user1s;
    }

    @Override
    public String toString() {
        return "User2{" +
                "user2Name='" + user2Name + '\'' +
                ", user2Age=" + user2Age +
                ", user1s=" + user1s +
                '}';
    }
}
