# 极客时间小马哥实战课记录

## homework

### week-1

- 通过自研 Web MVC 框架实现一个用户注册，forward 到一个成功的页面（JSP 用法）/register
- 通过 Controller -> Service -> Repository 实现（数据库实现）

### week-2

- 通过简易版依赖注入和依赖查找，实现用户注册功能

- 通过 UserService 实现用户注册注册用户需要校验

- Id：必须大于 0 的整数

- 密码：6-32 位 电话号码: 采用中国大陆方式（11 位校验）

### week-3

- 整合 https://jolokia.org/ 实现一个自定义 JMX MBean，通过 Jolokia 做 Servlet 代理
  1. 读取JMX信息 http://localhost:9696/jolokia/read/org.geektimes.projects.user.management:type=User
  2. 写入JMX信息 http://localhost:9696/jolokia/write/org.geektimes.projects.user.management:type=User/Email/masaiqi.com@gmail.com
- 完成 Microprofile config API 中的实现:

	1. 扩展 org.eclipse.microprofile.config.spi.ConfigSource 实现，包括 OS 环境变量，以及本地配置文件
	
	2. 扩展 org.eclipse.microprofile.config.spi.Converter 实现，提供 String 类型到简单类型
	
	3. 通过 org.eclipse.microprofile.config.Config 读取当前应用名称，应用名称 property name = “application.name”
	