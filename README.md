[TOC]
### 1、简要概述
该工程基于maven module管理，对于依赖方可以引入任何一个业务模块，如果期望引入所有模块，可以引入
```xml
<dependency>
    <groupId>com.mljr.commons</groupId>
    <artifactId>car-commons-all</artifactId>
    <version>2.2.10-SNAPSHOTs</version>
</dependency>
```
### 2、模块介绍
#### 2.1、car-commons-all：包含所有子模块
1. car-commons-aop

> 提供基于aop切面，实现日志输出和基于oval的DTO参数校验功能。
+ @LogMonitors

示例如下：
```java
@OvalValidator("GPS设备商认证")
@LogMonitor("GPS设备商认证")
public Result<List<GpsDeviceInfoRe>> approve(@RequestBody GpsDeviceAgencyDTO deviceAgencyDTO) {
    GpsDeviceAgencyType agencyType = GpsDeviceAgencyType.getByName(deviceAgencyDTO.getAgencyType());
    return gpsDeviceInfoComponent.getGpsInfos(deviceAgencyDTO.getDeviceList(),agencyType);
}
```

+ @OvalValidator

示例如下：
```java
@OvalValidator("GPS设备商认证")
@LogMonitor("GPS设备商认证")
public Result<List<GpsDeviceInfoRe>> approve(@RequestBody GpsDeviceAgencyDTO deviceAgencyDTO) {
    GpsDeviceAgencyType agencyType = GpsDeviceAgencyType.getByName(deviceAgencyDTO.getAgencyType());
    return gpsDeviceInfoComponent.getGpsInfos(deviceAgencyDTO.getDeviceList(),agencyType);
}
```

#### 2.2、car-commons-aviator：提供基于aviator计算引擎相关封装扩展。
示例如下：
```java
/**
 * @description: 基于Aviator测试类
 * @Date : 2018/9/7 下午6:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class AviatorTest {

    Map<String,Object> evn = new HashMap<String,Object>(){{
        put("a","1.6");
        put("b",6.44);
        put("c",3.2);
        put("d",-5.4);
    }};

    @Test
    public void nvl(){
        String expression = "nvl(a,0)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("nvl(a,0)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void sum(){
        String expression = "sum(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("sum(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void max(){
        String expression = "max(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("max(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void min(){
        String expression = "min(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("min(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void round(){
        String expression = "round(a)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("round(a)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void test1(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("a",3);
            put("b",2);
            put("c",5);
            put("d",-2);
        }};
        String expression = " (a > b) && (c > 0 || d > 0) ? a : b";
        System.out.println(AviatorEvaluator.execute(expression,evn));
    }

    @Test
    public void in(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("rateLevel",15);
        }};
        String expression = "in(rateLevel, ',' , '15')";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println(AviatorExecutor.executeBoolean(ctx));
    }

    @Test
    public void scale(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("x",15.344);
        }};
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,0)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,1)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,2)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,-2,1)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,-2,2)").env(evn).cached(true).build()));
    }
}
```
#### 2.3、car-commons-base：提供相关基类以及公共模块
#### 2.4、car-commons-cache：提供Redis缓存接口，由依赖方实现接口。
示例如下：
```java
/**
 * @description: 统一Redis Service接口
 * @Date : 2018/4/23 下午3:30
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface RedisService {
    /**
     * 获取统一的业务应用前缀，用于生成RedisKey
     * @return
     */
    KeyPrefix getKeyPrefix();
    /**
     * 根据key，获取如下真正的要存储的key，以避免覆盖
     * @param key
     * @return
     */
    String getKeyWithSystemCode(String key);
    /**
     * 加锁
     * @param lockKey
     * @param liveTime 失效时间
     */
    boolean lock(String lockKey,long liveTime);
    /**
     * 加锁
     * @param lockKey
     */
    boolean lock(String lockKey);
    /**
     * 解锁
     * @param lockKey
     */
    void unlock(String lockKey);
    /**
     * 通过key删除
     */
    Object del(String... keys);
    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    void set(byte[] key, byte[] value, long liveTime);
    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime  单位秒
     */
    <V> void set(String key, V value, long liveTime);
    /**
     * 添加key value
     * @param key
     * @param value
     */
    <V> void set(String key, V value);
    /**
     * 添加key value，如果存在返回false,否则返回true 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime  单位秒
     * @return 如果存在返回false,否则返回true
     */
    <V> boolean setNx(String key, V value, long liveTime);
    /**
     * 添加key value，如果存在返回false,否则返回true
     * @param key
     * @param value
     * @return 如果存在返回false,否则返回true
     */
    <V> boolean setNx(String key, V value);

    /**
     * 添加key value（该key通过接口内部再次加工）
     * @param key
     * @param value
     */
    <V> void setWithPrefix(String key, V value);
    /**
     * 添加key value（该key通过接口内部再次加工）
     * @param key
     * @param value
     * @param liveTime  单位秒
     */
    <V> void setWithPrefix(String key, V value, long liveTime);
    /**
     * 添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    void set(byte[] key, byte[] value);
    /**
     * 自增长
     * @param key
     * @param liveTime
     */
    void incr(final byte[] key,final long liveTime);
    /**
     * 返回增长值
     * @param key
     * @return
     */
    long getIncrValue(final String key);
    /**
     * 获取redis value
     * @param key
     * @return
     */
    <T> T get(String key, Class<T> clazz);
    /**
     * 获取redis value（该key通过接口内部再次加工）
     * @param key
     * @return
     */
    <T> T getWithPrefix(String key, Class<T> clazz);
    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    Set<String> setkeys(String pattern);
    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    boolean exists(String key);
    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    boolean existsWithPrefix(String key);
    /**
     * 清空redis 所有数据
     * @return
     */
    String flushDB();
    /**
     * 查看redis里有多少数据
     */
    long dbSize();
    /**
     * 检查是否连接成功
     *
     * @return
     */
    String ping();
}
```
#### 2.5、car-commons-component

提供相关功能模块，包括影像件操作、GPS设备验证、全局参数查询。
#### 2.6、car-commons-ding

提供基于钉钉通知报警，包括Text、markdown相关类型包装。

示例如下：
```java
/**
 * @description: 钉钉机器人接口
 * @Date : 2018/8/5 下午4:04
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface DingRobotService {

    /**
     * 发送text类型
     */
    DingBaseRobotResp sendText(String robotToken, TextDingRobotReq req);

    /**
     * 发送link类型
     */
    DingBaseRobotResp sendLink(String robotToken, LinkDingRobotReq req);

    /**
     * 发送markdown类型
     */
    DingBaseRobotResp sendMarkdown(String robotToken, MarkdownDingRobotReq req);
}
```
可以通过工厂类创建
```java
DingRobotService service = DingRobotClientFactory.create("A");
```
#### 2.7、car-commons-file
excel导出相关功能
#### 2.8、car-commons-model
相关model类
#### 2.9、car-commons-util
> 相关工具类，包括ArithUti、Base64、CollectionsTools、ComputeTools、DESUtil、HtppUtils
MD5、NumberUtil、PropertiesReader、StringTools、TimeTools