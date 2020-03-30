# Diamond
### Redis和Mysql本地同步框架

#### 介绍
 Simple and practical redis and mysql data synchronization framework.  
 简单实用的redis和mysql的数据同步框架
#### 软件架构
![Image text](https://raw.githubusercontent.com/DarMi7/diamond/master/diamond-spring-boot-starter/img/%7D5%5D50K_V%5B_10XN%246S8%5BZXWV.png)  
本着不重复造轮子的原则，该框架借鉴了前辈们架构思想和组件。  
* 拦截  
基于aop的将需要同步的方法进行拦截
* 代理层   
  代理数据源、代理连接、代理sql执行器，通过代理的方式对我们的DML语句进行拦截。  
* 任务分发层  
  将拦截的任务通过disruptor进行分发给消费者进行处理。  
* 数据处理管道  
  一个数据流管道类似于NIO的pipeline，对前面从disruptor中获取到的任务进行解析,解析完成之后投递到阻塞队列中。  
* 阻塞队列数据和redis同步  
  采用线程池获取任务的形式，从阻塞队列中阻塞获取任务，将已经解析好的redis数据同步到指定的redis中。  

#### 规划展望
目前还处于开发阶段，还有更新解析没有完成。未来可能将会加入mq将服务解耦出来，适应那些任务量大的需求，减轻对我们业务逻辑的影响。
因为对于本地实现方式会开启2个线程，对任务进行处理，对我们的系统有一定的影响。

#### 使用说明

1.  配置redis连接这是必不可少的  
dm.config.redis-address: redis://127.0.0.1:6379
2.  配置我们需要同步的表  
dm.config.async-tables: user,t_order_1
3.  给需要同步的方法加上注解    
@EnableDmAsync  
详细可以参见sample项目


