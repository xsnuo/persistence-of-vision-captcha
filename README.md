# xsn-tool

这是一个私人的Java工具包，里面包含了一些非常简单且基础的Java工具。由于这个小工具在很多项目中都使用到了，所以单独把它开源发布，方便引用。

## Maven引入

```xml
<dependency>
    <groupId>com.xuesinuo</groupId>
    <artifactId>xtool</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Np工具类介绍

Np是一个融入了常用判断条件的，忽略空指针的对象操作工具类。

类似Optional的用法，功能上做了简化并扩展了业务场景常用的判断条件。

### e.g.0 基本用法

一个最简单的例子：忽略空指针连续调用多级Getter获取嵌套的属性值：

```java
Np.i(MainPojo).x(x -> x.getChildPojo()).x(x -> x.getName()).o("Default Name");
```

### e.g.1 相等比较、数值比较

`.eq()`, `.notEq()`, `.in()`, `.notIn()`用于对象比较，NULL值比较，数值比较。
`.gt()`, `.ge()`, `.lt()`, `.le()`用于数值比较。

```java
BigDecimal a = new BigDecimal("12");
BigDecimal b = new BigDecimal("12.0");
a.equals(b);// False
Np.i(a).eq(b);// True
Np.i(a).gt(b);// False
Np.i(a).lt(b);// False
Np.i(a).ge(b);// True
Np.i(a).ge(b);// True
```

```java
Obejct a = new Object();
Object b = new Object();
a.equals(b);// False
Np.i(a).eq(b);// False
Np.i(a).gt(b);// False
Np.i(a).lt(b);// False
Np.i(a).ge(b);// False
Np.i(a).ge(b);// False
```

```java
Obejct a = null;
Object b = null;
a.equals(b);// NullPointerException
Np.i(a).eq(b);// True
Np.i(a).gt(b);// False
Np.i(a).lt(b);// False
Np.i(a).ge(b);// False
Np.i(a).ge(b);// False
```

```java
BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("1.0");
BigDecimal c = new BigDecimal("2");
BigDecimal d = new BigDecimal("3");
BigDecimal e = null;
Collection<BigDecimal> list = Arrays.asList(b,c);

Np.i(a).in(list);// True
Np.i(a).in(list);// True
Np.i(a).in(list);// True
Np.i(d).in(list);// False
Np.i(e).in(list);// False

Np.i(a).in(b,c,null);// True
Np.i(d).in(b,c,null);// False
Np.i(e).in(b,c,null);// True
```
