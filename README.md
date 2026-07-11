# CredentialKit

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-8+-green.svg)](https://www.oracle.com/java/)

中国证件号码校验与解析工具库，支持多种常用证件的合法性校验和信息解析。

## 功能特性

- 校验证件号码合法性
- 解析证件号码中的信息（地区、生日、性别等）
- 支持自定义证件类型扩展
- 线程安全设计
- 无外部依赖

## 支持的证件类型

| 证件类型 | 枚举值 | 解析信息 |
|---------|--------|---------|
| 中华人民共和国居民身份证 | `MAINLAND_RESIDENT_ID` | 地区、生日、性别 |
| 港澳居民来往内地通行证 | `HK_MACAO_TRAVEL_PERMIT` | 地区、换证次数 |
| 台湾居民来往大陆通行证 | `TAIWAN_TRAVEL_PERMIT` | 换证次数 |
| 港澳居民居住证 | `HK_MACAO_RESIDENCE_PERMIT` | 地区、生日、性别 |
| 台湾居民居住证 | `TAIWAN_RESIDENCE_PERMIT` | 地区、生日、性别 |
| 外国人永久居留身份证 | `FOREIGNER_PERMANENT_RESIDENCE_ID` | 国籍、地区、生日、性别 |
| 可机读护照 | `MACHINE_READABLE_PASSPORT` | 签发国、姓名、护照号、生日、性别、有效期 |
| 统一社会信用代码 | `UNIFIED_SOCIAL_CREDIT` | 机构类别、地区、组织机构代码 |

## 安装

### Maven

```xml
<dependency>
    <groupId>com.xiahaimoyu</groupId>
    <artifactId>credentialkit</artifactId>
    <version>2.5</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.xiahaimoyu:credentialkit:2.5'
```

## 使用方法

### 快速开始

```java
import com.xiahaimoyu.credentialkit.CredentialKit;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;

// 校验证件合法性
boolean isValid = CredentialKit.validate(
    DefaultCredentialType.MAINLAND_RESIDENT_ID,
    "330105197810270025"
).isValid();
```

### 校验证件

```java
// 详细校验 - 返回校验结果
ValidationResult result = CredentialKit.validate(
    DefaultCredentialType.MAINLAND_RESIDENT_ID,
    "330105197810270024"
);

if (!result.isValid()) {
    // 获取错误码
    Optional<ErrorCode> errorCode = result.getErrorCode();
    // 获取错误详情，例如: "[CHECK_DIGIT_ERROR] 校验位错误"
    String description = result.getErrorDescription();
}
```

### 解析证件信息

```java
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdInfo;
import com.xiahaimoyu.credentialkit.enums.Gender;

Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(
    DefaultCredentialType.MAINLAND_RESIDENT_ID,
    "330105197810270025"
);

if (infoOpt.isPresent()) {
    MainlandResidentIdInfo info = (MainlandResidentIdInfo) infoOpt.get();
    
    // 获取地区信息
    DomesticRegionInfo region = info.getRegion();
    System.out.println("省份: " + region.getProvince());  // 浙江省
    System.out.println("城市: " + region.getCity());      // 杭州市
    System.out.println("区县: " + region.getCounty());    // 拱墅区
    
    // 获取生日
    System.out.println("生日: " + info.getBirthDate());   // 19781027
    
    // 获取性别
    System.out.println("性别: " + info.getGender());      // FEMALE
}
```

### 智能识别证件类型

```java
// 自动识别证件类型（遍历已注册处理器，返回所有校验通过的类型）
List<CredentialType> types = CredentialKit.detect("330105197810270025");
for (CredentialType type : types) {
    System.out.println("证件类型: " + type.getChineseName());
}
```

> 说明：`detect` 会返回所有校验通过的类型列表。绝大多数证件号码只会匹配唯一类型，但理论上可能存在多个候选（返回多元素列表）；无任何匹配时返回空列表。

### 管理处理器

```java
// 注册处理器
CredentialKit.register(MyCredentialType.MY_DOCUMENT, new MyCredentialProcessor());

// 注销处理器
CredentialKit.unregister(MyCredentialType.MY_DOCUMENT);
```

### 扩展自定义证件类型

```java
import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;

// 定义证件类型
public enum MyCredentialType implements CredentialType {
    MY_DOCUMENT("我的证件", "My Document");
    
    private final String chineseName;
    private final String englishName;
    
    MyCredentialType(String chineseName, String englishName) {
        this.chineseName = chineseName;
        this.englishName = englishName;
    }
    
    @Override
    public String getChineseName() {
        return chineseName;
    }
    
    @Override
    public String getEnglishName() {
        return englishName;
    }
}

// 注册处理器
CredentialKit.register(MyCredentialType.MY_DOCUMENT, new MyCredentialProcessor());

// 使用
CredentialKit.validate(MyCredentialType.MY_DOCUMENT, "documentNumber");
```

### 动态添加地区数据

```java
import com.xiahaimoyu.credentialkit.util.RegionUtil;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;

// 添加自定义国内地区（GB/T 2260标准）
RegionUtil.addDomesticRegionData(
    new DomesticRegionInfo("330199", "浙江省", "杭州市", "自定义区")
);
```

## 解析信息详解

### 国内地区信息 (DomesticRegionInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `code` | `String` | 地区编码（6位数字，GB/T 2260标准） |
| `province` | `String` | 省份名称 |
| `city` | `String` | 城市名称 |
| `county` | `String` | 区县名称 |

### 国际地区信息 (InternationalRegionInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `chineseShortName` | `String` | 中文简称 |
| `englishShortName` | `String` | 英文简称 |
| `chineseFullName` | `String` | 中文全称 |
| `englishFullName` | `String` | 英文全称 |
| `alpha3` | `String` | 三位字母编码（ISO 3166-1） |
| `alpha2` | `String` | 两位字母编码（ISO 3166-1） |
| `numeric` | `String` | 数字编码（ISO 3166-1） |

### 居民身份证 (MainlandResidentIdInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `region` | `DomesticRegionInfo` | 首次签发地区（省/市/区） |
| `birthDate` | `String` | 生日（YYYYMMDD格式） |
| `gender` | `Gender` | 性别（MALE/FEMALE） |

### 港澳居民来往内地通行证 (HkMacaoTravelPermitInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `region` | `DomesticRegionInfo` | 签发地区 |
| `replacementTime` | `int` | 换证次数（未知时为-1） |

### 港澳/台湾居民居住证 (HkMacaoResidencePermitInfo/TaiwanResidencePermitInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `region` | `DomesticRegionInfo` | 签发地区 |
| `birthDate` | `String` | 生日（YYYYMMDD格式） |
| `gender` | `Gender` | 性别（MALE/FEMALE） |

### 外国人永久居留身份证 (ForeignerPermanentResidenceIdInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `internationalRegionInfo` | `InternationalRegionInfo` | 国籍 |
| `domesticRegionInfo` | `DomesticRegionInfo` | 签发地区 |
| `birthDate` | `String` | 生日（18位YYYYMMDD / 15位YYMMDD） |
| `gender` | `Gender` | 性别（MALE/FEMALE） |

### 可机读护照 (MachineReadablePassportInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `issuingRegion` | `InternationalRegionInfo` | 签发国家/地区 |
| `surname` | `String` | 姓 |
| `givenName` | `String` | 名 |
| `passportNumber` | `String` | 护照号码 |
| `region` | `InternationalRegionInfo` | 持有人国籍/地区 |
| `birthDate` | `String` | 生日（YYMMDD格式） |
| `gender` | `Gender` | 性别（MALE/FEMALE） |
| `expirationDate` | `String` | 有效期（YYMMDD格式） |
| `personalNumber` | `String` | 个人号码 |

### 统一社会信用代码 (UnifiedSocialCreditInfo)

| 字段 | 类型 | 说明 |
|-----|------|-----|
| `orgCategory` | `OrgCategory` | 机构类别 |
| `region` | `DomesticRegionInfo` | 登记地区 |
| `organizationCode` | `String` | 组织机构代码 |

## 错误码

| 错误码 | 说明 |
|-------|-----|
| `BASIC_FORMAT_ERROR` | 基本格式错误 |
| `REGION_ERROR` | 地区错误 |
| `NAME_ERROR` | 名字错误 |
| `BIRTH_DATE_ERROR` | 生日错误 |
| `EXPIRATION_DATE_ERROR` | 有效期错误 |
| `CHECK_DIGIT_ERROR` | 校验位错误 |
| `ORG_CATEGORY_ERROR` | 机构类别错误 |

## 数据标准

- 国内地区：GB/T 2260《中华人民共和国行政区划代码》
- 国际地区：ISO 3166-1 国际标准

**注意**：台湾(158/TWN/TW)、香港(344/HKG/HK)、澳门(446/MAC/MO)是中国的一部分，使用ISO编码是为了处理护照等国际证件的机读码格式。

## 技术要求

- Java 8 或更高版本
- 无外部依赖

## 许可证

本项目基于 Apache License 2.0 开源协议发布。详见 [LICENSE](LICENSE) 文件。

## 作者

Howard.Li

## 贡献

欢迎提交 Issue 和 Pull Request。