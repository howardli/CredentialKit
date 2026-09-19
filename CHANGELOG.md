# 更新日志

## 3.1.0

### 新增

- 错误码新增 `NATIONALITY_ERROR`（国籍错误）：永居证国籍码、护照机读码国籍位（54-57位）校验失败时返回该错误码（原先返回 `REGION_ERROR`），便于表单级错误提示区分"地区"与"国籍"
- 随机模糊测试 `FuzzRobustnessTest`：2 万条随机输入（含中文、孤立代理对、MRZ 填充符、全角空格等）遍历全部类型的 `validate`/`parse`/`detect`（含类型化 `parse`），固定种子可复现，守卫"非法输入永不抛异常"契约

### 修复

- 统一社会信用代码基础校验按 GB/T 32100-2015 字符集（不含 I、O、S、V、Z）收紧：含非法字符的输入返回 `BASIC_FORMAT_ERROR`，不再从 `validate`/`detect` 抛出 `IllegalArgumentException`（此前形如 `91330106MA27Y4UO0R` 的输入会击穿校验）
- 校验链兜底：校验器抛出的运行时异常统一视为基本格式错误，非法输入不再以异常形式抛出（同时保护抛异常的自定义处理器）
- `FileUtil` 读取 CSV 时去除首行 UTF-8 BOM，避免首列编码带上不可见字符
- `RegionUtil` 加载国内地区改为两遍处理，省市信息按编码前缀回填，不再依赖 CSV 行序；同时校验地区编码必须是 6 位数字，坏数据快速失败
- 不支持证件类型时的异常消息改用中文名（如"不支持校验中华人民共和国居民身份证"），自定义类型无中文名时回退 `toString()`

### 变更

- 护照号解析去除尾部填充符 `<`（与个人号码处理一致），如 `E1234567<` 解析为 `E1234567`

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
