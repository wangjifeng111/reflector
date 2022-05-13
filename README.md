# Reflector

![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)

Java标准库的反射api使用起来很难受，有各种编译期异常需要我们自己处理，而且使用起来也不够丝滑，不够简便。本项目针对这些问题进行了优化，还额外完善了关于泛型的支持，相对Java本身提供的泛型api要好用的多，使用起来非常丝滑。

## 主要概念

本项目定义了很多接口，提出了`Reflector`(反射器)的概念，所有的反射相关操作都需要使用反射器。反射元素的概念，即：`Class`，`Constructor`，`Field`，`Method`，`Parameter`，反射元素和反射器始终一一对应，一个反射器的行为只受反射元素的影响。

下面是反射器相关接口关系表：

|          名称           |   类型    |                             功能                             |
| :---------------------: | :-------: | :----------------------------------------------------------: |
|        Reflector        | interface |     反射器的顶级接口，所有其他反射器都需要实现这个接口。     |
|    ClassReflector<T>    | interface | Class反射器，Class反射相关操作的核心接口。`<T>`为被操作类的类型。 |
| ConstructorReflector<T> | interface | Constructor反射器，Constructor反射相关操作的核心接口。`<T>`为被操作类的类型。 |
|    FieldReflector<F>    | interface | Field反射器，Field反射相关操作的核心接口。`<F>`为此字段的类型。 |
|   MethodReflector<M>    | interface | Method反射器，Method反射相关操作的核心接口。`<M>`为此方法的返回值类型。 |
|   ParameterReflector    | interface |      Parameter反射器，Parameter反射相关操作的核心接口。      |

## 反射器相关Api的使用样例

以下是样例使用的实体类：

```java
// 测试使用的注解
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pojo {
}

// 测试样例使用的实体类
@Pojo
public class User {
    @Pojo
    private String name;

    private Integer age;
    
    private List<? extends User> users;

    public User() {}
    
    public User(@Pojo String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    
    public User(List<User> users) {
        this.users = users;
    }
    
    // getter 和 setter 方法
    
   	// toString 方法
}
```

- Reflector

  Reflector作为顶级的反射器接口，它拥有一些公共的接口，检查反射元素是否存在注解等一系列操作，以类举例：

  ```Java
  ClassReflector<User> classReflector = new ClassReflectorImpl<>(User.class);
  // 检查User类是否存在注解@Pojo
  boolean hasAnnotation = classReflector.hasAnnotation(Pojo.class);
  System.out.println(hasAnnotation); // true
  // 获取User类上的注解@Pojo
  Pojo annotation = classReflector.getAnnotation(Pojo.class);
  System.out.println(annotation != null); // true
  ```

- ClassReflector<T>

  ```java
  // 获取User类的ClassReflector
  ClassReflector<User> classReflector = new ClassReflectorImpl<>(User.class);
  // 获取User类的三个构造方法
  // 无参构造方法的构造反射器
  ConstructorReflector<? extends User> constructorReflector1 = classReflector.constructorReflector();
  // name和age作为参数的构造方法的构造反射器
  ConstructorReflector<? extends User> constructorReflector2 = classReflector.constructorReflector(String.class, Integer.class);
  // list作为参数的构造方法的构造反射器
  ConstructorReflector<? extends User> constructorReflector3 = classReflector.constructorReflector(List.class);
  // 获取setName方法所对应的方法反射器
  classReflector.methodReflector("setName", String.class);
  // 获取name字段所对应的字段反射器
  FieldReflector<String> nameFieldReflector = classReflector.fieldReflector("name");
  ```

- ConstructorReflector<T>

  ```java
  // 使用name和age作为参数的构造方法的构造反射器创建实例
  User user = constructorReflector2.newInstance("tom", 18);
  // 获取当前构造方法的name参数的参数反射器，如果调用此方式失败，可能是因为编译maven项目的时候没有添加-parameter编译参数。
  // 使用此项目即默认开启-parameter编译参数
  ParameterReflector nameParameterReflector = constructorReflector2.parameterReflector("name");
  // 如果项目没有添加-parameter编译参数，那么参数名称将默认为arg + index(参数的在参数列表的索引位置)，那么上面的调用将变
  // 成如下模样：
  ParameterReflector nameParameterReflector = constructorReflector2.parameterReflector("arg0");
  // 也可以使用重载方法，通过index获取参数反射器
  ParameterReflector nameParameterReflector = constructorReflector2.parameterReflector(0);
  // 获取构造反射器的参数列表的泛型类型，此处使用第三个构造方法
  Type[] types = constructorReflector.constructorParamGenerics();
  Arrays.stream(types).forEach(System.out::println);
  // 打印结果是一个ParameterizedTypeImpl参数化类型，不了解的可以去搜一搜
  // ParameterizedTypeImpl [rawType=interface java.util.List, ownerType=null, actualTypeArguments=[class net.wangjifeng.reflector.test.pojo.User]]
  ```

- FieldReflector<F>

  ```java
  // 读取字段
  User user = new User("tom", 18);
  String name = nameFieldReflector.readValue(user);
  System.out.println(name); // tom
  // 写入字段
  nameFieldReflector.writeValue(user, "jack");
  System.out.println(user.getName()); // jack
  // 获取字段的泛型
  FieldReflector<List<? extends User>> listFieldReflector = classReflector.fieldReflector("users");
  Type generic = listFieldReflector.generic();
  System.out.println(generic);
  // 打印结果：ParameterizedTypeImpl [rawType=interface java.util.List, ownerType=null, actualTypeArguments=[WildcardTypeImpl{lowerBounds=[], upperBounds=[class net.wangjifeng.reflector.test.pojo.User]}]]
  // 打印结果是一个参数化类型内部包含一个通配符类型
  ```

- MethodReflector<M>

  ```java
  User user = new User("tom", 18);
  // 调用getName方法
  MethodReflector<String> getNameMethodReflector = classReflector.methodReflector("getName");
  String name = getNameMethodReflector.invoke();
  System.out.println(name); // tom
  // 调用setName方法
  MethodReflector<String> setNameMethodReflector = classReflector.methodReflector("setName", String.class);
  setNameMethodReflector.invoke(user, "jack");
  System.out.println(user.getName()); // jack
  // 获取参数反射器相关api和构造方法类似，此处省略
  // 获取参数列表泛型和构造方法类型，此处省略
  // 获取返回值类型
  MethodReflector<List<? extends User>> getUsersMethodReflector = classReflector.methodReflector("getUsers");
  Type type = getUsersMethodReflector.returnTypeGeneric();
  System.out.println(type);
  // 打印结果如下：ParameterizedTypeImpl [rawType=interface java.util.List, ownerType=null, actualTypeArguments=[WildcardTypeImpl{lowerBounds=[], upperBounds=[class net.wangjifeng.reflector.test.pojo.User]}]]
  ```

- ParameterReflector

  参数反射器的构造方法不可以自主调用，因为参数不会凭空产生。

  ```java
  // 以setUsers方法为例
  MethodReflector<String> setUsersMethodReflector = 
      classReflector.methodReflector("setUsers", String.class);
  ParameterReflector parameterReflector = setUsersMethodReflector.parameterReflector("users");
  // 获取users参数的泛型
  Type generic = parameterReflector.generic();
  System.out.println(generic);
  // 打印结果：ParameterizedTypeImpl [rawType=interface java.util.List, ownerType=null, actualTypeArguments=[WildcardTypeImpl{lowerBounds=[], upperBounds=[class net.wangjifeng.reflector.test.pojo.User]}]]
  ```

## 使用

此项目当前没有推送到maven中央仓库，后期会推送，当前使用只能下载编译后引入项目。