# SystemConfig.xml

`SystemConfig.xml` now uses the normalized field names below, and runtime access should go through `SystemConfigLoader`.

## Runtime entry

- `SystemConfigLoader`: the current runtime loader for reading and reloading `SystemConfig.xml`
- `SystemConfigXml`: the single configuration model used for XML binding, API payloads, and runtime reads
- `SystemConfigXmlParse`: deprecated compatibility wrapper only; do not add new usages

## Root fields

- `logLevel`: system operation log level
- `logRetentionDays`: log retention days
- `adminUsers.userCode`: admin account codes
- `systemName`: display name of the system
- `cluster`: whether cluster mode is enabled
- `defaultRowPrivilegeLevel`: default row-level privilege scope

## JMX fields

- `jmx.tomcat.active`: whether Tomcat is the default JMX target
- `jmx.tomcat.jmxUrl`: JMX service URL
- `jmx.tomcat.mbeanName`: target MBean name
- `jmx.websphere.active`: whether WebSphere is the default JMX target
- `jmx.weblogic.active`: whether WebLogic is the default JMX target

## Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<systemConfig>
    <logLevel>1</logLevel>
    <logRetentionDays>30</logRetentionDays>
    <adminUsers>
        <userCode>admin</userCode>
    </adminUsers>
    <systemName>系统平台统一用户</systemName>
    <cluster>false</cluster>
    <defaultRowPrivilegeLevel>org</defaultRowPrivilegeLevel>
    <jmx>
        <tomcat active="true">
            <jmxUrl><![CDATA[service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi]]></jmxUrl>
            <mbeanName><![CDATA[Catalina:type=Manager,context=/mycuckoo,host=localhost]]></mbeanName>
        </tomcat>
        <websphere active="false" />
        <weblogic active="false" />
    </jmx>
</systemConfig>
```
