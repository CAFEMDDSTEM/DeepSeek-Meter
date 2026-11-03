# DeepSeek Meter

仅面向 Android 的 DeepSeek Token / 费用监控 App。技术栈为 Kotlin、Jetpack Compose、Material 3、ViewModel、StateFlow、Coroutines、DataStore 和 Android WebView。

## 构建

要求：

- JDK 21
- Android SDK Platform 35
- Build Tools 36.0.0
- Gradle 8.14.4

```powershell
.\gradlew.bat assembleDebug
```

APK 输出：

`app/build/outputs/apk/debug/app-debug.apk`

## 官方数据来源

登录页：`https://platform.deepseek.com/sign_in`

登录成功后从官方页面的 `localStorage["userToken"]` 读取 Session，并以：

`Authorization: Bearer <session>`

访问官方前端正在使用的接口：

- `GET /api/v0/users/get_user_summary`
- `GET /api/v0/usage/by_api_key/amount?start=&end=&tz=`
- `GET /api/v0/usage/by_api_key/cost?start=&end=&tz=`

充值按钮打开官方 `https://platform.deepseek.com/top_up`，不调用或虚构充值接口。

## 统计规则

- 剩余余额：官方 `normal_wallets` + `bonus_wallets` 的 `balance`。
- 已使用：官方 `total_costs.amount`。
- 总额度：由 `剩余余额 + 已使用` 计算，保证环形图 100% 关系成立。
- 今日/本月费用：对官方 cost 接口按当前设备时区和日期聚合。
- 输入 Token：`PROMPT_CACHE_HIT_TOKEN + PROMPT_CACHE_MISS_TOKEN`。
- 输出 Token：`RESPONSE_TOKEN`。
- 总 Token：输入 + 输出；Cache Hit 是输入的一部分，不重复相加。
- 模型统计：按官方返回的 `model` 动态聚合，并按主币种费用、Token 用量排序。
- 趋势：当前月份每天一个数据点；输入拆分为缓存未命中和 Cache Hit，输出单独显示。
- 消费金额趋势：使用官方 cost 接口的 API Key、模型维度和每日费用，支持按 API Key、模型/调用类型、以及 API Key × 模型组合堆叠查看。

## 多语言

设置页可在以下语言之间即时切换并持久化：

- 中文
- English
- Русский
- 日本語
- 한국어
- Deutsch
- Italiano
- Español

界面、登录页、设置、模型页、错误提示和系统通知均使用当前语言。

## 安全

- Session 使用 Android Keystore AES-GCM 加密。
- 密文存储在 DataStore；不保存明文 Token。
- 不输出 Token 或请求头到 Logcat。
- Android 13+ 在启用通知时请求 `POST_NOTIFICATIONS`。
- 今日阈值按自然日和阈值去重；低余额采用进入/离开状态去重，持续低余额不会重复提醒。
