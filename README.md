# 方圆整理的高并发秒杀系统

## 1. 介绍
高并发秒杀系统实战项目，记录每一分成长

## 2. 软件架构

- flash-sale-domain: 领域层，包含实体类
- flash-sale-infrastructure: 基础设施层，包含mapper文件
- flash-sale-app: 应用层，包含service服务
- flash-sale-controller: controller层
- environment-mysql: 保存数据库脚本
- environment-postman: postman测试脚本

## 3. 分支说明

### 3.1 base-framework
- 想在这个分支搭建一个基础框架，什么项目代码都没有，能从这个分支上从0到1的开发

### 3.2 base-function
> 基础功能开发，包括秒杀活动的操作和秒杀品操作

#### 3.2.1 开发日志

- 22/02/20 新增 秒杀活动发布功能


## 巨人的肩膀
- [Nacos启动报错解决：which: no javac in ](https://blog.csdn.net/qq_44895681/article/details/105515025)
- [centos7下 Nacos安装 以及可能会出现的问题启动成功无法访问](https://blog.csdn.net/dagedeshu/article/details/109209157)
- [Error creating bean with name 'flow-sentinel-nacos-datasource'](https://www.yuque.com/yuqueyonghu4gseak/yc/gyft35)
- [Sentinel 官方文档](https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel)
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)
- [NacosConfig 摆脱本地配置参考文档](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-config)
- [Nacos Spring-Cloud @RefreshScope 配置不生效问题](https://blog.csdn.net/fly_leopard/article/details/107489937)