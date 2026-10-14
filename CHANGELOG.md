# 更新日志

## 3.3.0

### 重构（源与二进制兼容）

- 新增公共抽象基类 `PersonCredentialInfo`（生日、性别）与 `DomesticPersonCredentialInfo`（在其上增加地区）：身份证与港澳台居民居住证信息原本三份逐字段相同的实现上收至基类，永居证与护照信息继承生日/性别部分。各具体类型、相等性语义（按运行时类比较）与 `toString` 输出保持不变
- 新增包内 `AbstractResidencePermitProcessor`：港澳/台湾居民居住证的校验与解析链结构相同（18位结构同居民身份证，仅地区码前缀不同），统一到基类，两个处理器只保留各自的基础格式正则与地区码映射（包内类型，不属于公共API）
- 港澳台地区信息常量（H/HA/M/MA/810000/820000/830000）集中到包内 `CredentialRegionInfos`，不再由各处理器重复定义（包内类型，不属于公共API）
- 重复代码消除：主代码跨文件重复行占比从约14.5%降至约8.5%，剩余为各值类不同字段的访问器样板。以重构前后全量样例逐一比对验证：校验、解析、识别结果与 equals、toString 输出完全不变；含枚举字段（性别、机构类别）的信息对象 hashCode 数值因枚举按对象身份哈希而变化（hashCode 契约内允许，本就不保证跨版本数值稳定）

### 文档

- 对齐注释与代码：`DateUtil.validDateBeforeNow` 语义实为"不晚于今天（含当天）"；`getUnifiedSocialCreditCodeCheckDigit` 的 `@throws` 补充字符集外字符场景；`FileUtil` BOM 判断改显式 `uFEFF` 转义（行为不变）

## 3.2.0

### 新增

- 内置证件类型 `HK_MACAO_TRAVEL_PERMIT_NON_CHINESE`（港澳居民来往内地通行证（非中国籍），2024年7月10日起签发）：号码共9位、一人一号终身不变，HA/MA字头（HA=香港、MA=澳门）+ 7位数字，可解析持证人地区；换证次数是卡面独立登记项目、不属于号码，故不解析。与中国籍通行证号码（H/M + 8位数字）格式互斥，`detect` 不会混淆

## 3.1.0

### 修复

- 统一社会信用代码基础校验按 GB/T 32100-2015 字符集（不含 I、O、S、V、Z）收紧：含非法字符的输入返回 `BASIC_FORMAT_ERROR`，不再从 `validate`/`detect` 抛出 `IllegalArgumentException`（此前形如 `91330106MA27Y4UO0R` 的输入会击穿校验）
- 校验链兜底：校验器抛出的运行时异常统一视为基本格式错误，非法输入不再以异常形式抛出（同时保护抛异常的自定义处理器）
- `FileUtil` 读取 CSV 时去除首行 UTF-8 BOM，避免首列编码带上不可见字符
- `RegionUtil` 加载国内地区改为两遍处理，省市信息按编码前缀回填，不再依赖 CSV 行序；同时校验地区编码必须是 6 位数字，坏数据快速失败

### 变更

- 新增 `INTERNATIONAL_REGION_ERROR` 错误码（描述为"国籍/地区错误"）：国际地区编码校验失败与国内地区错误区分（永居证国籍码、护照机读码国籍/地区位原先返回 `REGION_ERROR`）。取值包含有独立编码但非国家的地区（香港/澳门/台湾）、国际组织（UNO/EUE等）及无国籍/难民类别，故不使用"国籍"表述
- 护照号解析去除尾部填充符 `<`（与个人号码处理一致），如 `E1234567<` 解析为 `E1234567`
- 不支持证件类型时的 `UnsupportedOperationException` 消息改用证件类型中文名
- 护照机读码扩展地区数据（26 条）从 `MachineReadablePassportProcessor` 内嵌静态块下沉到 `RegionUtil`：数据归地区数据层，护照处理器改为委托查找，后续支持 TD1/TD2 等其他机读证件时可直接复用（行为不变）
- `pom` 补充发布元数据（`name`/`description`/`url`/`licenses`/`developers`/`scm`）
- jar manifest 新增 `Automatic-Module-Name`（Java 9+ 模块路径下模块名稳定，不再由 jar 文件名派生）及实现信息条目；README 补充 GitHub Packages 消费方配置（仓库地址与 settings.xml 凭据）

### 新增

- `RegionUtil.preload()`：显式预热地区数据，替代此前"调用一次真实查询"的预热方式，对冷启动延迟敏感的服务可在启动阶段调用
- `RegionUtil.getMrzRegionInfoByAlpha3(alpha3)`：机读码（MRZ）语境的地区查找，ISO 3166 优先、未命中回退 ICAO 9303 机读码扩展编码（国际组织、无国籍/难民类别、德国 `D<<` 等），一般业务仍使用 `getInternationalRegionInfoByAlpha3`

- 模糊健壮性测试：固定种子对合法样本做 3 万次随机变异（替换/插入/删除/截断/大小写翻转）加 3 万次纯随机字符串，批量守护"任意输入不抛异常"的核心契约
- `ValidationResult` 新增 `validIf(condition, errorCode)` 条件式结果工厂：条件成立返回成功结果，否则返回指定错误码的失败结果，简化自定义校验器的书写

### 性能

- USCI 校验位字符查表改为 `int` 数组，去除装箱
- 护照姓名解析从 `split("<<")` 改为 `indexOf` 定位，去除每次调用的正则编译

## 3.0.0

### 新增

- `CredentialRegistry`：实例化注册中心，支持独立注册表（测试隔离、多租户），`create()` 含全部内置处理器并自动加载 SPI，`createEmpty()` 为空白实例
- SPI 扩展点 `com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider`，通过 `ServiceLoader` 自动发现第三方处理器（依次扫描线程上下文类加载器和库自身的类加载器，按提供者类名去重，兼容容器环境下提供者与库不在同一类加载器的场景）
- `parse` 类型化重载：`parse(type, credential, MainlandResidentIdInfo.class)`，调用方无需强转
- `getSupportedTypes()`：枚举已注册的证件类型
- `CredentialType.getDetectPriority()`：智能识别优先级（默认 0 = 最高，自定义类型天然排在内置类型之前）
- `DateUtil.setClock(Clock)` / `resetClock()`：注入时钟，可测试跨临界日期的行为
- `RegionUtil.removeDomesticRegionData` / `removeInternationalRegionData`：撤销自定义地区数据
- animal-sniffer 校验 Java 8 API 兼容性、JaCoCo 测试覆盖率报告（`mvn verify`；当前指令覆盖 97%、分支覆盖 85%）

### 修复

- `normalize()` 使用 `Locale.ROOT` 转大写，修复土耳其语等 locale 下 `i` 被转为 `İ` 导致合法输入被误判（回归测试：`EnvironmentRobustnessTest`）
- `detect()` 排序比较器空安全，修复中文名为 null 的自定义类型触发 `NullPointerException`
- SPI 提供者加载/执行失败时抛出携带提供者信息的 `IllegalStateException`（原先为裸 `ServiceConfigurationError`）
- `CredentialInfo.getType()` 改为由注册中心在解析后写入实际注册的证件类型：把内置处理器注册到自定义类型下时，`getType()` 不再返回硬编码的内置类型（直接使用处理器解析时为 null，可手动 `setType`）
- `CredentialKit` 默认注册中心改为懒加载：SPI 提供者有缺陷时，每次调用抛出清晰的 `IllegalStateException`（含提供者信息），而不是类初始化失败后的 `NoClassDefFoundError`

### 性能

- 地区数据改为「不可变快照 + 复制写入」：读取路径零锁 `HashMap` 查找；原先为 `ConcurrentHashMap`
- `ValidationResult.failure()` 按错误码缓存实例，高频校验零分配
- 护照 `rightTrim` 由正则替换改为尾部扫描
- `detect()` 排序比较器提升为静态常量，避免每次识别重复构建（实测 1.99µs -> 1.71µs）
- 护照 `expirationDate` 与 `birthDate` 统一为 `YYYYMMDD` 格式（新增 `DateUtil.toFullYearExpirationDate`）

### 破坏性变更

仅影响自定义处理器作者，静态调用方（`CredentialKit.validate/parse/detect/register/unregister`）不受影响：

- `CredentialProcessor` 改为构造器注入 `List<CredentialValidator>` 和 `List<CredentialParser<T>>`，删除 `buildValidators()` / `buildParsers()`（消除"父类构造器调用可覆盖方法"的隐患）；校验器列表不允许为空列表
- `CredentialInfo.getType()` 由抽象方法改为注册中心写入的实例字段（final，不可再被子类覆写）；自定义信息类需删除 `getType()` 覆写
- `is18DigitCredential` 从 `CredentialProcessor` 移入 `MainlandResidentIdProcessor`（私有）
- `MachineReadablePassportInfo.getExpirationDate()` 从 `YYMMDD` 改为 `YYYYMMDD`

## 2.6

- 上一稳定版本
