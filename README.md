# CredentialKit

证件号码校验、解析与智能识别的 Java 工具库。零第三方运行时依赖，支持 Java 8+。

## 支持的证件类型

| 证件 | 解析信息 |
|---|---|
| 中华人民共和国居民身份证（15/18位） | 地区、生日、性别 |
| 港澳居民来往内地通行证 | 地区、换证次数 |
| 台湾居民来往大陆通行证 | 换证次数 |
| 港澳居民居住证 | 地区、生日、性别 |
| 台湾居民居住证 | 地区、生日、性别 |
| 外国人永久居留身份证（15/18位） | 地区、国籍、生日、性别 |
| 可机读护照（TD3，ICAO 9303） | 签发地区、姓名、护照号、地区、生日、性别、有效期、个人号码 |
| 统一社会信用代码 | 机构类别、地区、组织机构代码 |

## 快速开始

```xml
<dependency>
    <groupId>com.xiahaimoyu</groupId>
    <artifactId>credentialkit</artifactId>
    <version>3.0.0</version>
</dependency>
```

```java
// 校验（返回详细结果）
ValidationResult result = CredentialKit.validate(
        DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025");
if (!result.isValid()) {
    System.out.println(result.getErrorDescription()); // [CHECK_DIGIT_ERROR] 校验位错误
}

// 解析（类型化，无需强转；getType()返回实际注册的证件类型）
Optional<MainlandResidentIdInfo> info = CredentialKit.parse(
        DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025",
        MainlandResidentIdInfo.class);
info.ifPresent(i -> System.out.println(i.getBirthDate() + " " + i.getGender()));

// 智能识别（返回按优先级排序的候选类型）
List<CredentialType> types = CredentialKit.detect("H12345678");
```

## 扩展

### 手动注册

```java
CredentialKit.register(MyCredentialType.MY_ID, new MyCredentialProcessor());
```

### SPI 自动发现

实现 `com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider`，
在 jar 包中添加 `META-INF/services/com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider`
文件（内容为实现类全限定名）。`CredentialRegistry.create()` 创建的实例会自动加载。

### 独立注册表

```java
CredentialRegistry registry = CredentialRegistry.createEmpty(); // 不含内置处理器
registry.register(MyCredentialType.MY_ID, new MyCredentialProcessor());
```

适用于测试隔离、多租户等场景。`CredentialKit` 的静态方法等价于操作默认注册中心。

## 性能特征（实测，单线程）

| 操作 | 耗时 |
|---|---|
| `validate`（成功/失败路径） | ~1.2 µs/次（约 85 万次/秒） |
| `detect`（遍历全部类型） | ~1.7 µs/次（约 58 万次/秒） |
| `parse` | ~1.4 µs/次 |

首次访问会懒加载地区数据（约 100~150ms，主要是资源 I/O），对冷启动延迟敏感的服务
可在启动阶段预热（见 `RegionUtil` 的 Javadoc）。测试覆盖率：指令 97%、分支 85%（JaCoCo，`mvn verify`）。

## 升级到 3.0.0

破坏性变更（仅影响自定义处理器作者，静态调用方不受影响）：

- `CredentialProcessor` 改为构造器注入校验器/解析器列表，删除 `buildValidators()`/`buildParsers()`
- `is18DigitCredential` 从基类移入 `MainlandResidentIdProcessor`
- `MachineReadablePassportInfo.getExpirationDate()` 从 `YYMMDD` 改为 `YYYYMMDD`
- `CredentialInfo.getType()` 改为由注册中心写入实际注册的证件类型（final，子类不可覆写）
- 空校验器列表的处理器构造时直接抛 `IllegalArgumentException`

详见 [CHANGELOG.md](CHANGELOG.md)。
