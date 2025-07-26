# CredentialKit

证件工具，目前主要用于常用证件的校验合法性和解析号码里面的内容。

## 使用方法

CredentialKit，目前只支持DefaultCredentialType里面的类型，如果不能满足需求，请自己继承CredentialType，然后使用register方法注册自己的处理器。

### valid 验证合法性

入参：CredentialType证件类型，credential证件

出参：为了使用的便利性，没有使用result再包装，直接返回boolean，合法返回true，不合法返回false。

### parse

入参：CredentialType证件类型，credential证件

出参：为了使用的便利性，没有使用result再包装，直接返回信息，强转即可。

| 说明             | 证件类型                                    | 返回对象                                    |
|----------------|-----------------------------------------|-----------------------------------------|
| 中华人民共和国居民身份证号码 | MAINLAND_RESIDENT_ID_NUMBER             | MainlandResidentIdNumberInfo            |
| 港澳居民来往内地通行证号码  | HONGKONG_MACAO_TRAVEL_PERMIT_NUMBER     | HkMoTravelPermitNumberInfo              |
| 台湾居民来往大陆通行证号码  | TAIWAN_TRAVEL_PERMIT_NUMBER             | TwTravelPermitNumberInfo                |
| 港澳居民居住证号码      | HONGKONG_MACAO_RESIDENCE_PERMIT_NUMBER  | HkMoResidencePermitNumberInfo           |
| 台湾居民居住证号码      | TAIWAN_RESIDENCE_PERMIT_NUMBER          | TwResidencePermitNumberInfo             |
| 外国人永久居留身份证号码   | FOREIGNER_PERMANENT_RESIDENCE_ID_NUMBER | ForeignerPermanentResidenceIdNumberInfo |
| 可机读护照编码        | MACHINE_READABLE_PASSPORT_CODE          | MachineReadablePassportCodeInfo         |
| 统一社会信用代码       | UNIFIED_SOCIAL_CREDIT_CODE              | UnifiedSocialCreditCodeInfo             |
