mycuckoo应用基于MVC模式, 使用Spring和Mybatis构建.
Spring: 使用restful风格的SpringMVC, Spring IOC, 依赖包:
	spring-webmvc-${version}.jar
	spring-aop-${version}.jar(面向切面, 依赖aopalliance aspectjrt aspectjweaver)
	spring-asm-${version}.jar(依赖asm)
	spring-context-support-${version}.jar
	spring-expressing-${version}.jar
	spring-jdbc-${version}.jar
	spring-test-${version}.jar(测试包, 依赖于junit. 本应用的单元测试用到)
Mybatis: 依赖包 mybatis-3.2.7.jar mybatis-spring-1.1.1.jar
单元测试: 依赖包 junit-4.1.0.jar junit-dep-4.8.2.jar
数据库: 数据库使用postgresql, 依赖包 postgresql-8.4-701.jdbc3.jar
日志: slf4j已经成为Logger的事实标准API, 依赖包 slf4j-api-1.7.5.jar. slf4j只是一个日志外壳，
	需要添加依赖包如slf4j-jdk14.jar, slf4j-log4j12.jar或logback.jar, 这里添加slf4j-log4j12-1.7.5.jar,
	注意不同版本号的兼容性. 对于一些jar包早已使用log4j, 这里添加log4j-1.2.17.jar jcl-over-slf4j-1.7.5.jar
	以此替代apache common-logging的日志API, 本应用统一使用slf4j记录日志.
工具: 依赖apache的常用工具，依赖包 commons-dbcp-1.4.jar commons-pool-1.5.jar commons-lang3-3.1.jar
代理: 依赖包 cglib-2.2.2.jar
集合: 依赖google的guava-1.2.0.jar
XML: 依赖dom4j-1.6.1.jar和支持xpath功能的jaxen-1.1.3.jar
JSON: json数据序列化和反序列化依赖jackson, 依赖包 jackson-annotations-2.4.0.jar jackson-core-2.4.1.1.jar jackson-databind-2.4.1.3.jar