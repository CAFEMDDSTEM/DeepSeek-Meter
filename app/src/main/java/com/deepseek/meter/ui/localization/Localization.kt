package com.deepseek.meter.ui.localization

import androidx.compose.runtime.staticCompositionLocalOf

data class LanguageOption(val code: String, val nativeName: String)

val supportedLanguages = listOf(
    LanguageOption("zh", "中文"),
    LanguageOption("en", "English"),
    LanguageOption("ru", "Русский"),
    LanguageOption("ja", "日本語"),
    LanguageOption("ko", "한국어"),
    LanguageOption("de", "Deutsch"),
    LanguageOption("it", "Italiano"),
    LanguageOption("es", "Español"),
)

object S {
    const val appName = "appName"; const val login = "login"; const val refresh = "refresh"
    const val settings = "settings"; const val overview = "overview"; const val models = "models"
    const val close = "close"; const val back = "back"; const val retry = "retry"
    const val officialRecharge = "officialRecharge"; const val sameSession = "sameSession"
    const val notLoggedIn = "notLoggedIn"; const val loginDesc = "loginDesc"; const val loginDeepSeek = "loginDeepSeek"
    const val loadingData = "loadingData"; const val errorSessionRead = "errorSessionRead"
    const val errorLoginValidation = "errorLoginValidation"; const val errorLoginNetwork = "errorLoginNetwork"
    const val errorSessionExpired = "errorSessionExpired"; const val errorRefresh = "errorRefresh"
    const val errorRefreshNetwork = "errorRefreshNetwork"; const val todayStats = "todayStats"
    const val monthStats = "monthStats"; const val cost = "cost"; const val requests = "requests"
    const val input = "input"; const val output = "output"; const val cacheHit = "cacheHit"
    const val totalTokens = "totalTokens"; const val tokenTrend = "tokenTrend"; const val tokenTrendDesc = "tokenTrendDesc"
    const val costTrend = "costTrend"; const val costTrendDesc = "costTrendDesc"; const val byApiKey = "byApiKey"
    const val byModel = "byModel"; const val noCostData = "noCostData"; const val chartTotal = "chartTotal"
    const val lastUpdated = "lastUpdated"; const val accountBalance = "accountBalance"; const val quotaFormula = "quotaFormula"
    const val currentTotalRemaining = "currentTotalRemaining"; const val remaining = "remaining"
    const val rechargeBalance = "rechargeBalance"; const val bonusBalance = "bonusBalance"; const val used = "used"
    const val todayUsage = "todayUsage"; const val todayIncluded = "todayIncluded"
    const val totalRemainingFormula = "totalRemainingFormula"; const val otherCurrencySummary = "otherCurrencySummary"
    const val refreshing = "refreshing"; const val autoRefresh = "autoRefresh"; const val autoRefreshDesc = "autoRefreshDesc"
    const val intervalOff = "intervalOff"; const val secondsShort = "secondsShort"; const val minutesShort = "minutesShort"
    const val todayAlert = "todayAlert"; const val enableTodayAlert = "enableTodayAlert"; const val todayThreshold = "todayThreshold"
    const val notificationPermission = "notificationPermission"; const val oncePerDay = "oncePerDay"
    const val lowBalanceAlert = "lowBalanceAlert"; const val enableLowBalance = "enableLowBalance"
    const val lowThreshold = "lowThreshold"; const val lowBalanceDesc = "lowBalanceDesc"; const val ringColors = "ringColors"
    const val ringColorsDesc = "ringColorsDesc"; const val resetColors = "resetColors"; const val security = "security"
    const val sessionLoggedIn = "sessionLoggedIn"; const val sessionLoggedOut = "sessionLoggedOut"
    const val logout = "logout"; const val thresholdNote = "thresholdNote"; const val language = "language"
    const val languageDesc = "languageDesc"; const val noModelData = "noModelData"; const val loginForModels = "loginForModels"
    const val modelsLoginDesc = "modelsLoginDesc"; const val modelCost = "modelCost"; const val modelsDesc = "modelsDesc"
    const val noModelRecords = "noModelRecords"; const val loginTitle = "loginTitle"; const val loginInstruction = "loginInstruction"
    const val validating = "validating"; const val preparing = "preparing"; const val notificationChannel = "notificationChannel"
    const val notificationChannelDesc = "notificationChannelDesc"; const val todayAlertTitle = "todayAlertTitle"
    const val todayAlertText = "todayAlertText"; const val lowBalanceTitle = "lowBalanceTitle"
    const val lowBalanceText = "lowBalanceText"; const val unnamedKey = "unnamedKey"
    const val statusPage = "statusPage"; const val statusDescription = "statusDescription"
    const val allOperational = "allOperational"; const val degraded = "degraded"
    const val partialOutage = "partialOutage"; const val fullOutage = "fullOutage"
    const val maintenance = "maintenance"; const val statusUnknown = "statusUnknown"
    const val components = "components"; const val uptime = "uptime"
    const val activeIncidents = "activeIncidents"; const val noActiveIncidents = "noActiveIncidents"
    const val recentIncidents = "recentIncidents"; const val noRecentIncidents = "noRecentIncidents"
    const val lastChecked = "lastChecked"; const val statusUnavailable = "statusUnavailable"
    const val statusBannerTitle = "statusBannerTitle"; const val statusBannerText = "statusBannerText"
    const val viewStatus = "viewStatus"; const val dismiss = "dismiss"
    const val checkUpdates = "checkUpdates"; const val checkingUpdate = "checkingUpdate"
    const val upToDate = "upToDate"; const val updateAvailableTitle = "updateAvailableTitle"
    const val updateAvailableText = "updateAvailableText"; const val updateNow = "updateNow"
    const val later = "later"; const val versionLabel = "versionLabel"
    const val updateCheckFailed = "updateCheckFailed"
}

private val EN = mapOf(
    S.appName to "DeepSeek Meter", S.login to "Log in", S.refresh to "Refresh", S.settings to "Settings",
    S.overview to "Overview", S.models to "Models", S.close to "Close", S.back to "Back", S.retry to "Retry",
    S.officialRecharge to "Official top-up", S.sameSession to "This page uses the same login session",
    S.notLoggedIn to "DeepSeek is not connected", S.loginDesc to "Log in to view balance, daily/monthly cost, model spending and token trends.",
    S.loginDeepSeek to "Log in with DeepSeek", S.loadingData to "Loading DeepSeek usage data…",
    S.errorSessionRead to "Could not read the DeepSeek session. Please log in again.",
    S.errorLoginValidation to "Login validation failed: {0}", S.errorLoginNetwork to "Login validation failed. Check your network and retry.",
    S.errorSessionExpired to "Session expired. Please log in again.", S.errorRefresh to "Refresh failed: {0}",
    S.errorRefreshNetwork to "Refresh failed. Check your network connection.",
    S.todayStats to "Today", S.monthStats to "This month", S.cost to "Cost", S.requests to "Requests",
    S.input to "Input", S.output to "Output", S.cacheHit to "Cache hit", S.totalTokens to "Total tokens",
    S.tokenTrend to "Token usage trend", S.tokenTrendDesc to "Daily tokens for the current month. Input is split into cache miss/hit to avoid double counting.",
    S.costTrend to "Spending trend", S.costTrendDesc to "Official cost data grouped by API key or model/call type. Toggle the dimension below.",
    S.byApiKey to "API key", S.byModel to "Model / call type", S.noCostData to "No cost data for this month.",
    S.chartTotal to "Total", S.lastUpdated to "Last updated: {0}", S.accountBalance to "Account balance",
    S.quotaFormula to "Total quota = recharge + bonus + used; today is included in used.",
    S.currentTotalRemaining to "Current remaining", S.remaining to "Remaining", S.rechargeBalance to "Recharge balance",
    S.bonusBalance to "Bonus balance", S.used to "Used", S.todayUsage to "Today", S.todayIncluded to "Included in used",
    S.totalRemainingFormula to "Recharge balance + bonus balance",
    S.otherCurrencySummary to "{currency}: remaining {remaining} (recharge {recharge} / bonus {bonus}), used {used}",
    S.refreshing to "Updating…", S.autoRefresh to "Auto refresh", S.autoRefreshDesc to "Refreshes while the app is open. Android background checks run at a minimum interval of 15 minutes.",
    S.intervalOff to "Off", S.secondsShort to "{0} sec", S.minutesShort to "{0} min",
    S.todayAlert to "Today spending alert", S.enableTodayAlert to "Enable today spending alert",
    S.todayThreshold to "Today threshold ({0})", S.notificationPermission to "Android 13+ requires notification permission. It will be requested when enabling an alert.",
    S.oncePerDay to "Notifies once per calendar day and threshold.",
    S.lowBalanceAlert to "Low balance alert", S.enableLowBalance to "Enable low balance alert",
    S.lowThreshold to "Low balance threshold ({0})", S.lowBalanceDesc to "Notifies once when the balance stays below the threshold; it resets after the balance recovers.",
    S.ringColors to "Ring chart colors", S.ringColorsDesc to "Outer ring: recharge, bonus and used. Inner ring: today. Enter a hex color or choose a preset.",
    S.resetColors to "Restore default colors", S.security to "Login & security",
    S.sessionLoggedIn to "The session is encrypted with Android Keystore AES-GCM and stored in DataStore. No plaintext token or request headers are logged.",
    S.sessionLoggedOut to "Not logged in. After login, the session is encrypted with Android Keystore AES-GCM; no plaintext token is stored.",
    S.logout to "Log out and clear session", S.thresholdNote to "Thresholds use the primary currency: {0}. Other currencies are shown on the overview.",
    S.language to "Language", S.languageDesc to "Choose the display language for the app and notifications.",
    S.noModelData to "No model data. Please refresh.", S.loginForModels to "Log in to view model spending",
    S.modelsLoginDesc to "Model names, requests, input/output tokens and cache hits are generated from official data.",
    S.modelCost to "Model spending", S.modelsDesc to "Models come from the API and are sorted by {0} spending.",
    S.noModelRecords to "No model usage this month.", S.loginTitle to "Log in to DeepSeek",
    S.loginInstruction to "Complete login on the official DeepSeek page. The session is read automatically and encrypted in Android Keystore.",
    S.validating to "Validating login…", S.preparing to "Preparing the official login page…",
    S.notificationChannel to "Spending alerts", S.notificationChannelDesc to "Today spending and low balance alerts",
    S.todayAlertTitle to "Today spending reached the alert threshold",
    S.todayAlertText to "Today: {0}; threshold: {1}", S.lowBalanceTitle to "DeepSeek balance is low",
    S.lowBalanceText to "Current balance: {0}; threshold: {1}", S.unnamedKey to "Unnamed key"
)

private val ZH = mapOf(
    S.appName to "DeepSeek Meter", S.login to "登录", S.refresh to "刷新", S.settings to "设置",
    S.overview to "概览", S.models to "模型", S.close to "关闭", S.back to "返回", S.retry to "重试",
    S.officialRecharge to "前往官方充值", S.sameSession to "本页使用同一登录会话",
    S.notLoggedIn to "尚未登录 DeepSeek", S.loginDesc to "登录后即可查看账户余额、今日/本月费用、模型消费和 Token 趋势。",
    S.loginDeepSeek to "登录 DeepSeek", S.loadingData to "正在获取 DeepSeek 用量数据…",
    S.errorSessionRead to "未能读取 DeepSeek Session，请重新登录", S.errorLoginValidation to "登录校验失败：{0}",
    S.errorLoginNetwork to "登录校验失败，请检查网络后重试", S.errorSessionExpired to "登录已失效，请重新登录",
    S.errorRefresh to "刷新失败：{0}", S.errorRefreshNetwork to "刷新失败，请检查网络连接",
    S.todayStats to "今日统计", S.monthStats to "本月统计", S.cost to "费用", S.requests to "请求",
    S.input to "输入", S.output to "输出", S.cacheHit to "缓存命中", S.totalTokens to "总 Token",
    S.tokenTrend to "Token 用量趋势", S.tokenTrendDesc to "横轴为当前月份日期，纵轴为 Token 数量；输入按缓存命中/未命中拆分，避免重复统计。",
    S.costTrend to "消费金额", S.costTrendDesc to "根据官方费用数据，按 API Key 或模型（调用类型）堆叠展示；可切换统计维度。",
    S.byApiKey to "API Key", S.byModel to "模型 / 调用类型", S.noCostData to "本月暂无消费数据。",
    S.chartTotal to "合计", S.lastUpdated to "最后更新：{0}", S.accountBalance to "账户余额",
    S.quotaFormula to "总额度 = 充值余额 + 赠送余额 + 已使用；今日使用包含在已使用中",
    S.currentTotalRemaining to "当前总剩余", S.remaining to "剩余", S.rechargeBalance to "充值余额",
    S.bonusBalance to "赠送余额", S.used to "已使用", S.todayUsage to "今日使用", S.todayIncluded to "今日包含在已使用中",
    S.totalRemainingFormula to "充值余额 + 赠送余额",
    S.otherCurrencySummary to "{currency}：剩余 {remaining}（充值 {recharge} / 赠送 {bonus}），已使用 {used}",
    S.refreshing to "正在更新…", S.autoRefresh to "自动刷新", S.autoRefreshDesc to "前台运行时按所选间隔自动刷新；后台由 Android 系统按最低 15 分钟周期检查。",
    S.intervalOff to "关闭", S.secondsShort to "{0} 秒", S.minutesShort to "{0} 分钟",
    S.todayAlert to "今日消费预警", S.enableTodayAlert to "开启今日消费预警", S.todayThreshold to "今日消费阈值（{0}）",
    S.notificationPermission to "Android 13+ 需要通知权限；开启预警时会请求授权。", S.oncePerDay to "同一自然日、同一阈值只提醒一次。",
    S.lowBalanceAlert to "低余额提醒", S.enableLowBalance to "开启低余额提醒", S.lowThreshold to "低余额阈值（{0}）",
    S.lowBalanceDesc to "余额持续低于阈值时只提醒一次；恢复至阈值以上后再次跌破才会再次提醒。",
    S.ringColors to "环形图颜色", S.ringColorsDesc to "外环显示充值余额、赠送余额和已使用；内环显示今日使用。可输入十六进制颜色或选择预设色。",
    S.resetColors to "恢复默认颜色", S.security to "登录与安全",
    S.sessionLoggedIn to "Session 使用 Android Keystore AES-GCM 加密后保存在本机 DataStore；App 不保存明文 Token，也不输出请求头到日志。",
    S.sessionLoggedOut to "当前未登录。登录后 Session 会使用 Android Keystore AES-GCM 加密保存，不会保存明文 Token。",
    S.logout to "退出登录并清除 Session", S.thresholdNote to "阈值按当前主币种解释：{0}。若账户存在多币种，可在概览页查看各币种余额。",
    S.language to "语言", S.languageDesc to "选择应用界面和通知的显示语言。", S.noModelData to "暂无模型数据，请先刷新",
    S.loginForModels to "登录后查看模型消费", S.modelsLoginDesc to "模型名称、请求次数、输入/输出 Token 和 Cache Hit 将按官方数据动态生成。",
    S.modelCost to "模型消费", S.modelsDesc to "模型名称来自 API 返回值，按 {0} 消费金额优先排序。",
    S.noModelRecords to "本月还没有模型用量记录", S.loginTitle to "登录 DeepSeek",
    S.loginInstruction to "请在下方 DeepSeek 官方页面完成登录。登录成功后将自动读取 Session，并加密保存在本机 Keystore 中。",
    S.validating to "正在验证登录状态…", S.preparing to "正在准备官方登录页…",
    S.notificationChannel to "消费提醒", S.notificationChannelDesc to "今日消费预警和低余额提醒",
    S.todayAlertTitle to "今日消费已达到预警阈值", S.todayAlertText to "今日 {0}，阈值 {1}",
    S.lowBalanceTitle to "DeepSeek 账户余额偏低", S.lowBalanceText to "当前余额 {0}，阈值 {1}", S.unnamedKey to "未命名 Key"
)

private val RU = mapOf(
    S.login to "Войти", S.refresh to "Обновить", S.settings to "Настройки", S.overview to "Обзор", S.models to "Модели",
    S.close to "Закрыть", S.back to "Назад", S.retry to "Повторить", S.officialRecharge to "пополнение",
    S.sameSession to "Страница использует текущую сессию", S.notLoggedIn to "DeepSeek не подключён",
    S.loginDesc to "Войдите, чтобы видеть баланс, расходы за день и месяц, модели и динамику токенов.",
    S.loginDeepSeek to "Войти в DeepSeek", S.loadingData to "Загрузка данных DeepSeek…",
    S.errorSessionRead to "Не удалось прочитать сессию DeepSeek. Войдите снова.", S.errorLoginValidation to "Ошибка проверки входа: {0}",
    S.errorLoginNetwork to "Ошибка проверки входа. Проверьте сеть и повторите.", S.errorSessionExpired to "Сессия истекла. Войдите снова.",
    S.errorRefresh to "Ошибка обновления: {0}", S.errorRefreshNetwork to "Ошибка обновления. Проверьте подключение.",
    S.todayStats to "Сегодня", S.monthStats to "Этот месяц", S.cost to "Расход", S.requests to "Запросы", S.input to "Ввод",
    S.output to "Вывод", S.cacheHit to "Кэш", S.totalTokens to "Всего токенов", S.tokenTrend to "Динамика токенов",
    S.tokenTrendDesc to "Токены по дням текущего месяца. Ввод разделён на кэш и промахи кэша.", S.costTrend to "Динамика расходов",
    S.costTrendDesc to "Официальные расходы по API-ключам или моделям (типам вызовов). Переключите измерение.", S.byApiKey to "API-ключ",
    S.byModel to "Модель / тип вызова", S.noCostData to "За этот месяц расходов нет.", S.chartTotal to "Итого",
    S.lastUpdated to "Обновлено: {0}", S.accountBalance to "Баланс аккаунта",
    S.quotaFormula to "Общий лимит = пополнение + бонус + использовано; сегодня входит в использованное.",
    S.currentTotalRemaining to "Текущий остаток", S.remaining to "Остаток", S.rechargeBalance to "Пополнение",
    S.bonusBalance to "Бонусный баланс", S.used to "Использовано", S.todayUsage to "Сегодня", S.todayIncluded to "Входит в использованное",
    S.totalRemainingFormula to "Пополнение + бонус", S.otherCurrencySummary to "{currency}: остаток {remaining} (пополнение {recharge} / бонус {bonus}), использовано {used}",
    S.refreshing to "Обновление…", S.autoRefresh to "Автообновление", S.autoRefreshDesc to "Обновляется при открытом приложении; фоновая проверка Android — не чаще 15 минут.",
    S.intervalOff to "Выкл.", S.secondsShort to "{0} сек", S.minutesShort to "{0} мин", S.todayAlert to "Предупреждение о расходах",
    S.enableTodayAlert to "Включить предупреждение", S.todayThreshold to "Порог за сегодня ({0})",
    S.notificationPermission to "Android 13+ требует разрешение на уведомления.", S.oncePerDay to "Уведомление один раз в день для каждого порога.",
    S.lowBalanceAlert to "Низкий баланс", S.enableLowBalance to "Включить предупреждение", S.lowThreshold to "Порог баланса ({0})",
    S.lowBalanceDesc to "Уведомление отправляется один раз; сбрасывается после восстановления баланса.", S.ringColors to "Цвета кольцевой диаграммы",
    S.ringColorsDesc to "Внешнее кольцо: пополнение, бонус и расход. Внутреннее: сегодня. Введите HEX или выберите цвет.",
    S.resetColors to "Сбросить цвета", S.security to "Вход и безопасность",
    S.sessionLoggedIn to "Сессия шифруется Android Keystore AES-GCM и хранится в DataStore. Токен не пишется в логи.",
    S.sessionLoggedOut to "Вы не вошли. После входа сессия будет зашифрована Android Keystore AES-GCM.", S.logout to "Выйти и очистить сессию",
    S.thresholdNote to "Пороги применяются к основной валюте: {0}.", S.language to "Язык", S.languageDesc to "Выберите язык интерфейса и уведомлений.",
    S.noModelData to "Нет данных моделей. Обновите.", S.loginForModels to "Войдите для просмотра расходов моделей",
    S.modelsLoginDesc to "Названия моделей и токены создаются по официальным данным.", S.modelCost to "Расходы моделей",
    S.modelsDesc to "Модели берутся из API и сортируются по расходам в {0}.", S.noModelRecords to "В этом месяце расхода моделей нет.",
    S.loginTitle to "Вход в DeepSeek", S.loginInstruction to "Завершите вход на официальной странице DeepSeek. Сессия шифруется в Keystore.",
    S.validating to "Проверка входа…", S.preparing to "Подготовка официальной страницы входа…",
    S.notificationChannel to "Оповещения о расходах", S.notificationChannelDesc to "Расходы за сегодня и низкий баланс",
    S.todayAlertTitle to "Достигнут порог расходов за сегодня", S.todayAlertText to "Сегодня: {0}; порог: {1}",
    S.lowBalanceTitle to "Низкий баланс DeepSeek", S.lowBalanceText to "Баланс: {0}; порог: {1}", S.unnamedKey to "Ключ без имени"
)

private val JA = mapOf(
    S.login to "ログイン", S.refresh to "更新", S.settings to "設定", S.overview to "概要", S.models to "モデル",
    S.close to "閉じる", S.back to "戻る", S.retry to "再試行", S.officialRecharge to "公式チャージ",
    S.sameSession to "このページは同じログインセッションを使用します", S.notLoggedIn to "DeepSeek に未ログイン",
    S.loginDesc to "ログインすると、残高、今日/今月の費用、モデル別消費、Token 推移を確認できます。",
    S.loginDeepSeek to "DeepSeek にログイン", S.loadingData to "DeepSeek の利用データを取得中…",
    S.errorSessionRead to "DeepSeek セッションを読み取れません。再ログインしてください。", S.errorLoginValidation to "ログイン検証に失敗：{0}",
    S.errorLoginNetwork to "ログイン検証に失敗しました。ネットワークを確認してください。", S.errorSessionExpired to "セッションが切れました。再ログインしてください。",
    S.errorRefresh to "更新に失敗：{0}", S.errorRefreshNetwork to "更新に失敗しました。接続を確認してください。",
    S.todayStats to "今日", S.monthStats to "今月", S.cost to "費用", S.requests to "リクエスト", S.input to "入力",
    S.output to "出力", S.cacheHit to "キャッシュ", S.totalTokens to "総 Token", S.tokenTrend to "Token 使用推移",
    S.tokenTrendDesc to "今月の日別 Token。入力はキャッシュヒット/ミスに分けて表示します。", S.costTrend to "消費金額",
    S.costTrendDesc to "公式費用データを API Key またはモデル（呼び出し種別）別に積み上げ表示します。", S.byApiKey to "API Key",
    S.byModel to "モデル / 呼び出し種別", S.noCostData to "今月の費用データはありません。", S.chartTotal to "合計",
    S.lastUpdated to "最終更新：{0}", S.accountBalance to "アカウント残高",
    S.quotaFormula to "総枠 = チャージ残高 + ボーナス残高 + 使用済み。今日の使用は使用済みに含まれます。",
    S.currentTotalRemaining to "現在の総残高", S.remaining to "残り", S.rechargeBalance to "チャージ残高",
    S.bonusBalance to "ボーナス残高", S.used to "使用済み", S.todayUsage to "今日", S.todayIncluded to "使用済みに含まれます",
    S.totalRemainingFormula to "チャージ残高 + ボーナス残高", S.otherCurrencySummary to "{currency}：残り {remaining}（チャージ {recharge} / ボーナス {bonus}）、使用済み {used}",
    S.refreshing to "更新中…", S.autoRefresh to "自動更新", S.autoRefreshDesc to "アプリ表示中に更新します。Android のバックグラウンド確認は最短 15 分です。",
    S.intervalOff to "オフ", S.secondsShort to "{0} 秒", S.minutesShort to "{0} 分", S.todayAlert to "今日の消費アラート",
    S.enableTodayAlert to "今日の消費アラートを有効化", S.todayThreshold to "今日のしきい値（{0}）",
    S.notificationPermission to "Android 13+ では通知権限が必要です。", S.oncePerDay to "同じ日・同じしきい値で 1 回だけ通知します。",
    S.lowBalanceAlert to "残高不足アラート", S.enableLowBalance to "残高不足アラートを有効化", S.lowThreshold to "残高しきい値（{0}）",
    S.lowBalanceDesc to "しきい値を下回った時に 1 回通知し、回復後にリセットされます。", S.ringColors to "リンググラフの色",
    S.ringColorsDesc to "外環：チャージ、ボーナス、使用済み。内環：今日。HEX 入力またはプリセットを選択できます。",
    S.resetColors to "既定色に戻す", S.security to "ログインとセキュリティ",
    S.sessionLoggedIn to "セッションは Android Keystore AES-GCM で暗号化し DataStore に保存します。Token はログに出しません。",
    S.sessionLoggedOut to "未ログインです。ログイン後、セッションは Android Keystore AES-GCM で暗号化されます。", S.logout to "ログアウトしてセッションを削除",
    S.thresholdNote to "しきい値は主通貨 {0} で解釈されます。", S.language to "言語", S.languageDesc to "アプリと通知の表示言語を選択します。",
    S.noModelData to "モデルデータがありません。更新してください。", S.loginForModels to "ログインしてモデル消費を表示",
    S.modelsLoginDesc to "モデル名、リクエスト、入出力 Token、キャッシュは公式データから生成されます。", S.modelCost to "モデル別消費",
    S.modelsDesc to "モデルは API の値を使用し、{0} の消費額順に並びます。", S.noModelRecords to "今月のモデル利用はありません。",
    S.loginTitle to "DeepSeek にログイン", S.loginInstruction to "公式 DeepSeek ページでログインしてください。セッションは Keystore に暗号化保存されます。",
    S.validating to "ログインを確認中…", S.preparing to "公式ログインページを準備中…",
    S.notificationChannel to "消費アラート", S.notificationChannelDesc to "今日の消費と残高不足の通知",
    S.todayAlertTitle to "今日の消費がしきい値に達しました", S.todayAlertText to "今日：{0}、しきい値：{1}",
    S.lowBalanceTitle to "DeepSeek 残高が少なくなっています", S.lowBalanceText to "現在残高：{0}、しきい値：{1}", S.unnamedKey to "名前なし Key"
)

private val KO = mapOf(
    S.login to "로그인", S.refresh to "새로고침", S.settings to "설정", S.overview to "개요", S.models to "모델",
    S.close to "닫기", S.back to "뒤로", S.retry to "다시 시도", S.officialRecharge to "공식 충전",
    S.sameSession to "이 페이지는 같은 로그인 세션을 사용합니다", S.notLoggedIn to "DeepSeek에 로그인되지 않음",
    S.loginDesc to "로그인하면 잔액, 오늘/이번 달 비용, 모델 소비, Token 추세를 볼 수 있습니다.",
    S.loginDeepSeek to "DeepSeek 로그인", S.loadingData to "DeepSeek 사용 데이터 불러오는 중…",
    S.errorSessionRead to "DeepSeek 세션을 읽을 수 없습니다. 다시 로그인하세요.", S.errorLoginValidation to "로그인 확인 실패: {0}",
    S.errorLoginNetwork to "로그인 확인에 실패했습니다. 네트워크를 확인하세요.", S.errorSessionExpired to "세션이 만료되었습니다. 다시 로그인하세요.",
    S.errorRefresh to "새로고침 실패: {0}", S.errorRefreshNetwork to "새로고침 실패. 네트워크 연결을 확인하세요.",
    S.todayStats to "오늘", S.monthStats to "이번 달", S.cost to "비용", S.requests to "요청", S.input to "입력",
    S.output to "출력", S.cacheHit to "캐시", S.totalTokens to "총 Token", S.tokenTrend to "Token 사용 추세",
    S.tokenTrendDesc to "이번 달 일별 Token입니다. 입력은 캐시 적중/미스로 구분합니다.", S.costTrend to "소비 금액",
    S.costTrendDesc to "공식 비용 데이터를 API Key 또는 모델(호출 유형)별로 누적 표시합니다.", S.byApiKey to "API Key",
    S.byModel to "모델 / 호출 유형", S.noCostData to "이번 달 비용 데이터가 없습니다.", S.chartTotal to "합계",
    S.lastUpdated to "마지막 업데이트: {0}", S.accountBalance to "계정 잔액",
    S.quotaFormula to "총 한도 = 충전 + 보너스 + 사용액. 오늘 사용량은 사용액에 포함됩니다.",
    S.currentTotalRemaining to "현재 총 잔액", S.remaining to "남은 금액", S.rechargeBalance to "충전 잔액",
    S.bonusBalance to "보너스 잔액", S.used to "사용액", S.todayUsage to "오늘", S.todayIncluded to "사용액에 포함",
    S.totalRemainingFormula to "충전 잔액 + 보너스 잔액", S.otherCurrencySummary to "{currency}: 남은 금액 {remaining}(충전 {recharge} / 보너스 {bonus}), 사용액 {used}",
    S.refreshing to "업데이트 중…", S.autoRefresh to "자동 새로고침", S.autoRefreshDesc to "앱이 열려 있을 때 갱신하며 Android 백그라운드 확인은 최소 15분입니다.",
    S.intervalOff to "끔", S.secondsShort to "{0}초", S.minutesShort to "{0}분", S.todayAlert to "오늘 소비 경고",
    S.enableTodayAlert to "오늘 소비 경고 사용", S.todayThreshold to "오늘 기준값({0})",
    S.notificationPermission to "Android 13+에서는 알림 권한이 필요합니다.", S.oncePerDay to "같은 날짜와 기준값당 한 번 알립니다.",
    S.lowBalanceAlert to "낮은 잔액 알림", S.enableLowBalance to "낮은 잔액 알림 사용", S.lowThreshold to "잔액 기준값({0})",
    S.lowBalanceDesc to "기준값 아래로 내려갈 때 한 번 알리며 잔액이 회복되면 초기화됩니다.", S.ringColors to "링 차트 색상",
    S.ringColorsDesc to "외부 링: 충전, 보너스, 사용액. 내부 링: 오늘. HEX 입력 또는 프리셋을 선택하세요.",
    S.resetColors to "기본 색상 복원", S.security to "로그인 및 보안",
    S.sessionLoggedIn to "세션은 Android Keystore AES-GCM으로 암호화되어 DataStore에 저장됩니다. Token은 로그에 남지 않습니다.",
    S.sessionLoggedOut to "로그인되지 않았습니다. 로그인 후 세션은 Android Keystore AES-GCM으로 암호화됩니다.", S.logout to "로그아웃 및 세션 삭제",
    S.thresholdNote to "기준값은 기본 통화 {0} 기준입니다.", S.language to "언어", S.languageDesc to "앱과 알림의 표시 언어를 선택하세요.",
    S.noModelData to "모델 데이터가 없습니다. 새로고침하세요.", S.loginForModels to "로그인하여 모델 소비 보기",
    S.modelsLoginDesc to "모델 이름, 요청, 입출력 Token 및 캐시는 공식 데이터로 생성됩니다.", S.modelCost to "모델 소비",
    S.modelsDesc to "모델은 API 값을 사용하며 {0} 소비액 순으로 정렬합니다.", S.noModelRecords to "이번 달 모델 사용 기록이 없습니다.",
    S.loginTitle to "DeepSeek 로그인", S.loginInstruction to "공식 DeepSeek 페이지에서 로그인하세요. 세션은 Keystore에 암호화됩니다.",
    S.validating to "로그인 확인 중…", S.preparing to "공식 로그인 페이지 준비 중…",
    S.notificationChannel to "소비 알림", S.notificationChannelDesc to "오늘 소비 및 낮은 잔액 알림",
    S.todayAlertTitle to "오늘 소비가 기준값에 도달했습니다", S.todayAlertText to "오늘: {0}; 기준값: {1}",
    S.lowBalanceTitle to "DeepSeek 잔액이 부족합니다", S.lowBalanceText to "현재 잔액: {0}; 기준값: {1}", S.unnamedKey to "이름 없는 Key"
)

private val DE = mapOf(
    S.login to "Anmelden", S.refresh to "Aktualisieren", S.settings to "Einstellungen", S.overview to "Übersicht", S.models to "Modelle",
    S.close to "Schließen", S.back to "Zurück", S.retry to "Erneut versuchen", S.officialRecharge to "Offiziell aufladen",
    S.sameSession to "Diese Seite nutzt dieselbe Anmeldesitzung", S.notLoggedIn to "DeepSeek ist nicht verbunden",
    S.loginDesc to "Melde dich an, um Guthaben, Tages-/Monatskosten, Modellkosten und Token-Verläufe zu sehen.",
    S.loginDeepSeek to "Bei DeepSeek anmelden", S.loadingData to "DeepSeek-Nutzungsdaten werden geladen…",
    S.errorSessionRead to "DeepSeek-Sitzung konnte nicht gelesen werden. Bitte erneut anmelden.", S.errorLoginValidation to "Anmeldevalidierung fehlgeschlagen: {0}",
    S.errorLoginNetwork to "Anmeldevalidierung fehlgeschlagen. Netzwerk prüfen und erneut versuchen.", S.errorSessionExpired to "Sitzung abgelaufen. Bitte erneut anmelden.",
    S.errorRefresh to "Aktualisierung fehlgeschlagen: {0}", S.errorRefreshNetwork to "Aktualisierung fehlgeschlagen. Netzwerk prüfen.",
    S.todayStats to "Heute", S.monthStats to "Dieser Monat", S.cost to "Kosten", S.requests to "Anfragen", S.input to "Eingabe",
    S.output to "Ausgabe", S.cacheHit to "Cache-Treffer", S.totalTokens to "Token gesamt", S.tokenTrend to "Token-Nutzung",
    S.tokenTrendDesc to "Tägliche Token im aktuellen Monat. Eingabe ist in Cache-Miss/Hit getrennt.", S.costTrend to "Ausgabenverlauf",
    S.costTrendDesc to "Offizielle Kostendaten nach API-Key oder Modell/Aufruftyp gestapelt. Dimension unten wechseln.", S.byApiKey to "API-Key",
    S.byModel to "Modell / Aufruftyp", S.noCostData to "Keine Kostendaten für diesen Monat.", S.chartTotal to "Gesamt",
    S.lastUpdated to "Zuletzt aktualisiert: {0}", S.accountBalance to "Kontoguthaben",
    S.quotaFormula to "Gesamtlimit = Aufladung + Bonus + Verbraucht; heute ist in Verbraucht enthalten.",
    S.currentTotalRemaining to "Aktuelles Restguthaben", S.remaining to "Verbleibend", S.rechargeBalance to "Aufgeladenes Guthaben",
    S.bonusBalance to "Bonusguthaben", S.used to "Verbraucht", S.todayUsage to "Heute", S.todayIncluded to "In Verbraucht enthalten",
    S.totalRemainingFormula to "Aufgeladenes Guthaben + Bonusguthaben", S.otherCurrencySummary to "{currency}: verbleibend {remaining} (Aufladung {recharge} / Bonus {bonus}), verbraucht {used}",
    S.refreshing to "Aktualisierung…", S.autoRefresh to "Automatisch aktualisieren", S.autoRefreshDesc to "Aktualisiert bei geöffneter App; Android-Hintergrundprüfung mindestens alle 15 Minuten.",
    S.intervalOff to "Aus", S.secondsShort to "{0} Sek.", S.minutesShort to "{0} Min.", S.todayAlert to "Heutige Ausgabenwarnung",
    S.enableTodayAlert to "Warnung für heute aktivieren", S.todayThreshold to "Heutiger Schwellenwert ({0})",
    S.notificationPermission to "Android 13+ benötigt Benachrichtigungsberechtigung.", S.oncePerDay to "Einmal pro Kalendertag und Schwellenwert.",
    S.lowBalanceAlert to "Geringes Guthaben", S.enableLowBalance to "Warnung bei geringem Guthaben", S.lowThreshold to "Guthaben-Schwellenwert ({0})",
    S.lowBalanceDesc to "Meldet einmal beim Unterschreiten und setzt sich nach Erholung zurück.", S.ringColors to "Farben des Ringdiagramms",
    S.ringColorsDesc to "Außenring: Aufladung, Bonus, Verbrauch. Innenring: heute. HEX eingeben oder Vorgabe wählen.",
    S.resetColors to "Standardfarben wiederherstellen", S.security to "Anmeldung & Sicherheit",
    S.sessionLoggedIn to "Die Sitzung wird mit Android Keystore AES-GCM verschlüsselt und in DataStore gespeichert. Kein Token im Log.",
    S.sessionLoggedOut to "Nicht angemeldet. Nach der Anmeldung wird die Sitzung mit Android Keystore AES-GCM verschlüsselt.", S.logout to "Abmelden und Sitzung löschen",
    S.thresholdNote to "Schwellenwerte gelten für die Hauptwährung: {0}.", S.language to "Sprache", S.languageDesc to "Sprache für App und Benachrichtigungen wählen.",
    S.noModelData to "Keine Modelldaten. Bitte aktualisieren.", S.loginForModels to "Anmelden, um Modellkosten zu sehen",
    S.modelsLoginDesc to "Modellnamen, Anfragen, Token und Cache-Werte stammen aus offiziellen Daten.", S.modelCost to "Modellkosten",
    S.modelsDesc to "Modelle stammen aus der API und sind nach Kosten in {0} sortiert.", S.noModelRecords to "Diesen Monat keine Modellnutzung.",
    S.loginTitle to "Bei DeepSeek anmelden", S.loginInstruction to "Auf der offiziellen DeepSeek-Seite anmelden. Die Sitzung wird im Keystore verschlüsselt.",
    S.validating to "Anmeldung wird geprüft…", S.preparing to "Offizielle Anmeldeseite wird vorbereitet…",
    S.notificationChannel to "Ausgabenwarnungen", S.notificationChannelDesc to "Heutige Ausgaben und geringes Guthaben",
    S.todayAlertTitle to "Heutiges Ausgabenlimit erreicht", S.todayAlertText to "Heute: {0}; Schwellenwert: {1}",
    S.lowBalanceTitle to "DeepSeek-Guthaben ist niedrig", S.lowBalanceText to "Guthaben: {0}; Schwellenwert: {1}", S.unnamedKey to "Unbenannter Key"
)

private val IT = mapOf(
    S.login to "Accedi", S.refresh to "Aggiorna", S.settings to "Impostazioni", S.overview to "Panoramica", S.models to "Modelli",
    S.close to "Chiudi", S.back to "Indietro", S.retry to "Riprova", S.officialRecharge to "Ricarica ufficiale",
    S.sameSession to "Questa pagina usa la stessa sessione", S.notLoggedIn to "DeepSeek non è connesso",
    S.loginDesc to "Accedi per vedere saldo, costi di oggi/mese, spesa per modello e andamento Token.",
    S.loginDeepSeek to "Accedi a DeepSeek", S.loadingData to "Caricamento dati DeepSeek…",
    S.errorSessionRead to "Impossibile leggere la sessione DeepSeek. Accedi di nuovo.", S.errorLoginValidation to "Verifica accesso fallita: {0}",
    S.errorLoginNetwork to "Verifica accesso fallita. Controlla la rete e riprova.", S.errorSessionExpired to "Sessione scaduta. Accedi di nuovo.",
    S.errorRefresh to "Aggiornamento fallito: {0}", S.errorRefreshNetwork to "Aggiornamento fallito. Controlla la connessione.",
    S.todayStats to "Oggi", S.monthStats to "Questo mese", S.cost to "Costo", S.requests to "Richieste", S.input to "Input",
    S.output to "Output", S.cacheHit to "Cache hit", S.totalTokens to "Token totali", S.tokenTrend to "Andamento Token",
    S.tokenTrendDesc to "Token giornalieri del mese. L'input è diviso tra cache miss e hit.", S.costTrend to "Andamento spesa",
    S.costTrendDesc to "Costi ufficiali raggruppati per API key o modello/tipo di chiamata. Cambia dimensione sotto.", S.byApiKey to "API key",
    S.byModel to "Modello / tipo chiamata", S.noCostData to "Nessun dato di costo per questo mese.", S.chartTotal to "Totale",
    S.lastUpdated to "Ultimo aggiornamento: {0}", S.accountBalance to "Saldo account",
    S.quotaFormula to "Quota totale = ricarica + bonus + usato; oggi è incluso nell'usato.",
    S.currentTotalRemaining to "Saldo residuo totale", S.remaining to "Residuo", S.rechargeBalance to "Saldo ricaricato",
    S.bonusBalance to "Saldo bonus", S.used to "Usato", S.todayUsage to "Oggi", S.todayIncluded to "Incluso nell'usato",
    S.totalRemainingFormula to "Saldo ricaricato + saldo bonus", S.otherCurrencySummary to "{currency}: residuo {remaining} (ricarica {recharge} / bonus {bonus}), usato {used}",
    S.refreshing to "Aggiornamento…", S.autoRefresh to "Aggiornamento automatico", S.autoRefreshDesc to "Aggiorna con l'app aperta; il controllo Android in background è almeno ogni 15 minuti.",
    S.intervalOff to "Off", S.secondsShort to "{0} sec", S.minutesShort to "{0} min", S.todayAlert to "Avviso spesa odierna",
    S.enableTodayAlert to "Abilita avviso spesa odierna", S.todayThreshold to "Soglia odierna ({0})",
    S.notificationPermission to "Android 13+ richiede il permesso notifiche.", S.oncePerDay to "Notifica una volta per giorno e soglia.",
    S.lowBalanceAlert to "Avviso saldo basso", S.enableLowBalance to "Abilita avviso saldo basso", S.lowThreshold to "Soglia saldo ({0})",
    S.lowBalanceDesc to "Notifica una volta sotto la soglia; si azzera dopo il recupero del saldo.", S.ringColors to "Colori del grafico ad anello",
    S.ringColorsDesc to "Anello esterno: ricarica, bonus e usato. Anello interno: oggi. Inserisci HEX o scegli un preset.",
    S.resetColors to "Ripristina colori predefiniti", S.security to "Accesso e sicurezza",
    S.sessionLoggedIn to "La sessione è cifrata con Android Keystore AES-GCM e salvata in DataStore. Nessun Token nei log.",
    S.sessionLoggedOut to "Non hai effettuato l'accesso. Dopo l'accesso la sessione sarà cifrata con Android Keystore AES-GCM.", S.logout to "Esci e cancella sessione",
    S.thresholdNote to "Le soglie usano la valuta principale: {0}.", S.language to "Lingua", S.languageDesc to "Scegli la lingua dell'app e delle notifiche.",
    S.noModelData to "Nessun dato modello. Aggiorna.", S.loginForModels to "Accedi per vedere la spesa dei modelli",
    S.modelsLoginDesc to "Nomi modello, richieste, Token e cache provengono dai dati ufficiali.", S.modelCost to "Spesa per modello",
    S.modelsDesc to "I modelli provengono dall'API e sono ordinati per spesa in {0}.", S.noModelRecords to "Nessun uso di modelli questo mese.",
    S.loginTitle to "Accedi a DeepSeek", S.loginInstruction to "Completa l'accesso sulla pagina ufficiale DeepSeek. La sessione è cifrata nel Keystore.",
    S.validating to "Verifica accesso…", S.preparing to "Preparazione pagina di accesso ufficiale…",
    S.notificationChannel to "Avvisi di spesa", S.notificationChannelDesc to "Spesa odierna e saldo basso",
    S.todayAlertTitle to "Soglia di spesa odierna raggiunta", S.todayAlertText to "Oggi: {0}; soglia: {1}",
    S.lowBalanceTitle to "Saldo DeepSeek basso", S.lowBalanceText to "Saldo attuale: {0}; soglia: {1}", S.unnamedKey to "Key senza nome"
)

private val ES = mapOf(
    S.login to "Iniciar sesión", S.refresh to "Actualizar", S.settings to "Ajustes", S.overview to "Resumen", S.models to "Modelos",
    S.close to "Cerrar", S.back to "Volver", S.retry to "Reintentar", S.officialRecharge to "Recarga oficial",
    S.sameSession to "Esta página usa la misma sesión", S.notLoggedIn to "DeepSeek no está conectado",
    S.loginDesc to "Inicia sesión para ver el saldo, costes de hoy/mes, gasto por modelo y tendencias de Token.",
    S.loginDeepSeek to "Iniciar sesión en DeepSeek", S.loadingData to "Cargando datos de DeepSeek…",
    S.errorSessionRead to "No se pudo leer la sesión de DeepSeek. Inicia sesión de nuevo.", S.errorLoginValidation to "Falló la validación: {0}",
    S.errorLoginNetwork to "Falló la validación. Comprueba la red e inténtalo de nuevo.", S.errorSessionExpired to "La sesión caducó. Inicia sesión de nuevo.",
    S.errorRefresh to "Error al actualizar: {0}", S.errorRefreshNetwork to "Error al actualizar. Comprueba la conexión.",
    S.todayStats to "Hoy", S.monthStats to "Este mes", S.cost to "Coste", S.requests to "Solicitudes", S.input to "Entrada",
    S.output to "Salida", S.cacheHit to "Acierto caché", S.totalTokens to "Token total", S.tokenTrend to "Tendencia de Token",
    S.tokenTrendDesc to "Token diarios del mes actual. La entrada se divide en fallo/acierto de caché.", S.costTrend to "Tendencia de gasto",
    S.costTrendDesc to "Costes oficiales agrupados por API key o modelo/tipo de llamada. Cambia la dimensión abajo.", S.byApiKey to "API key",
    S.byModel to "Modelo / tipo de llamada", S.noCostData to "No hay datos de coste este mes.", S.chartTotal to "Total",
    S.lastUpdated to "Última actualización: {0}", S.accountBalance to "Saldo de la cuenta",
    S.quotaFormula to "Límite total = recarga + bono + usado; hoy está incluido en usado.",
    S.currentTotalRemaining to "Saldo restante total", S.remaining to "Restante", S.rechargeBalance to "Saldo recargado",
    S.bonusBalance to "Saldo de bono", S.used to "Usado", S.todayUsage to "Hoy", S.todayIncluded to "Incluido en usado",
    S.totalRemainingFormula to "Saldo recargado + saldo de bono", S.otherCurrencySummary to "{currency}: restante {remaining} (recarga {recharge} / bono {bonus}), usado {used}",
    S.refreshing to "Actualizando…", S.autoRefresh to "Actualización automática", S.autoRefreshDesc to "Actualiza con la app abierta; la comprobación Android en segundo plano es cada 15 minutos como mínimo.",
    S.intervalOff to "Desactivado", S.secondsShort to "{0} s", S.minutesShort to "{0} min", S.todayAlert to "Alerta de gasto de hoy",
    S.enableTodayAlert to "Activar alerta de gasto de hoy", S.todayThreshold to "Umbral de hoy ({0})",
    S.notificationPermission to "Android 13+ requiere permiso de notificaciones.", S.oncePerDay to "Notifica una vez por día y umbral.",
    S.lowBalanceAlert to "Alerta de saldo bajo", S.enableLowBalance to "Activar alerta de saldo bajo", S.lowThreshold to "Umbral de saldo ({0})",
    S.lowBalanceDesc to "Notifica una vez al bajar del umbral; se reinicia al recuperar el saldo.", S.ringColors to "Colores del gráfico circular",
    S.ringColorsDesc to "Anillo exterior: recarga, bono y usado. Interior: hoy. Introduce HEX o elige un preajuste.",
    S.resetColors to "Restaurar colores", S.security to "Inicio de sesión y seguridad",
    S.sessionLoggedIn to "La sesión se cifra con Android Keystore AES-GCM y se guarda en DataStore. No se registra el Token.",
    S.sessionLoggedOut to "No has iniciado sesión. Tras iniciarla, se cifrará con Android Keystore AES-GCM.", S.logout to "Cerrar sesión y borrar datos",
    S.thresholdNote to "Los umbrales usan la moneda principal: {0}.", S.language to "Idioma", S.languageDesc to "Elige el idioma de la app y las notificaciones.",
    S.noModelData to "No hay datos de modelos. Actualiza.", S.loginForModels to "Inicia sesión para ver el gasto por modelo",
    S.modelsLoginDesc to "Los modelos, solicitudes, Token y caché se generan desde datos oficiales.", S.modelCost to "Gasto por modelo",
    S.modelsDesc to "Los modelos vienen de la API y se ordenan por gasto en {0}.", S.noModelRecords to "Sin uso de modelos este mes.",
    S.loginTitle to "Iniciar sesión en DeepSeek", S.loginInstruction to "Completa el acceso en la página oficial de DeepSeek. La sesión se cifra en Keystore.",
    S.validating to "Validando acceso…", S.preparing to "Preparando la página oficial de acceso…",
    S.notificationChannel to "Alertas de gasto", S.notificationChannelDesc to "Gasto de hoy y saldo bajo",
    S.todayAlertTitle to "Se alcanzó el umbral de gasto de hoy", S.todayAlertText to "Hoy: {0}; umbral: {1}",
    S.lowBalanceTitle to "El saldo de DeepSeek es bajo", S.lowBalanceText to "Saldo actual: {0}; umbral: {1}", S.unnamedKey to "Key sin nombre"
)

private val EN_EXTRA = mapOf(
    S.statusPage to "DeepSeek Status", S.statusDescription to "Live service and system status provided by the official DeepSeek Status page.",
    S.allOperational to "All systems operational", S.degraded to "Degraded performance", S.partialOutage to "Partial outage",
    S.fullOutage to "Full outage", S.maintenance to "Under maintenance", S.statusUnknown to "Status unavailable",
    S.components to "Components", S.uptime to "90-day uptime", S.activeIncidents to "Active incidents",
    S.noActiveIncidents to "No active incidents", S.recentIncidents to "Recent incidents", S.noRecentIncidents to "No recent incidents",
    S.lastChecked to "Last checked: {0}", S.statusUnavailable to "Unable to load the official status page.",
    S.statusBannerTitle to "DeepSeek service issue", S.statusBannerText to "Current status: {0}. Some services may be affected.",
    S.viewStatus to "View status", S.dismiss to "Dismiss", S.checkUpdates to "Check for updates",
    S.checkingUpdate to "Checking…", S.upToDate to "You are using the latest version.", S.updateAvailableTitle to "Update available",
    S.updateAvailableText to "Version {0} is available. Current version: {1}.", S.updateNow to "Update now", S.later to "Later",
    S.versionLabel to "Version {0}", S.updateCheckFailed to "Unable to check for updates. Please try again later."
)
private val ZH_EXTRA = mapOf(
    S.statusPage to "DeepSeek 服务状态", S.statusDescription to "以下信息来自 DeepSeek 官方 Status 页面。",
    S.allOperational to "所有系统运行正常", S.degraded to "服务性能下降", S.partialOutage to "部分服务中断",
    S.fullOutage to "服务全面中断", S.maintenance to "维护中", S.statusUnknown to "状态不可用",
    S.components to "服务组件", S.uptime to "90 天可用性", S.activeIncidents to "当前事件",
    S.noActiveIncidents to "当前没有活动事件", S.recentIncidents to "最近事件", S.noRecentIncidents to "最近没有事件",
    S.lastChecked to "最后检查：{0}", S.statusUnavailable to "无法加载官方状态页面。",
    S.statusBannerTitle to "DeepSeek 服务状态异常", S.statusBannerText to "当前状态：{0}，部分服务可能受到影响。",
    S.viewStatus to "查看状态", S.dismiss to "关闭", S.checkUpdates to "检查更新",
    S.checkingUpdate to "正在检查…", S.upToDate to "当前已是最新版本。", S.updateAvailableTitle to "发现新版本",
    S.updateAvailableText to "新版本 {0} 可用，当前版本 {1}。", S.updateNow to "立即更新", S.later to "稍后",
    S.versionLabel to "版本 {0}", S.updateCheckFailed to "检查更新失败，请稍后重试。"
)
private val RU_EXTRA = mapOf(
    S.statusPage to "Статус DeepSeek", S.statusDescription to "Актуальный статус сервисов с официальной страницы DeepSeek Status.",
    S.allOperational to "Все системы работают", S.degraded to "Снижение производительности", S.partialOutage to "Частичный сбой",
    S.fullOutage to "Полный сбой", S.maintenance to "Обслуживание", S.statusUnknown to "Статус недоступен",
    S.components to "Компоненты", S.uptime to "Доступность за 90 дней", S.activeIncidents to "Активные инциденты",
    S.noActiveIncidents to "Активных инцидентов нет", S.recentIncidents to "Последние инциденты", S.noRecentIncidents to "Последних инцидентов нет",
    S.lastChecked to "Проверено: {0}", S.statusUnavailable to "Не удалось загрузить официальную страницу статуса.",
    S.statusBannerTitle to "Проблема с сервисом DeepSeek", S.statusBannerText to "Текущий статус: {0}. Некоторые сервисы могут быть недоступны.",
    S.viewStatus to "Открыть статус", S.dismiss to "Закрыть", S.checkUpdates to "Проверить обновления",
    S.checkingUpdate to "Проверка…", S.upToDate to "Установлена последняя версия.", S.updateAvailableTitle to "Доступно обновление",
    S.updateAvailableText to "Доступна версия {0}. Текущая версия: {1}.", S.updateNow to "Обновить", S.later to "Позже",
    S.versionLabel to "Версия {0}", S.updateCheckFailed to "Не удалось проверить обновления."
)
private val JA_EXTRA = mapOf(
    S.statusPage to "DeepSeek ステータス", S.statusDescription to "DeepSeek 公式ステータスページの最新情報です。",
    S.allOperational to "すべて正常稼働中", S.degraded to "性能低下", S.partialOutage to "一部停止",
    S.fullOutage to "全面停止", S.maintenance to "メンテナンス中", S.statusUnknown to "ステータス取得不可",
    S.components to "サービスコンポーネント", S.uptime to "90日間の稼働率", S.activeIncidents to "発生中の障害",
    S.noActiveIncidents to "発生中の障害はありません", S.recentIncidents to "最近の障害", S.noRecentIncidents to "最近の障害はありません",
    S.lastChecked to "最終確認：{0}", S.statusUnavailable to "公式ステータスページを読み込めません。",
    S.statusBannerTitle to "DeepSeek サービス障害", S.statusBannerText to "現在の状態：{0}。一部サービスに影響しています。",
    S.viewStatus to "ステータスを見る", S.dismiss to "閉じる", S.checkUpdates to "アップデートを確認",
    S.checkingUpdate to "確認中…", S.upToDate to "最新バージョンです。", S.updateAvailableTitle to "アップデートがあります",
    S.updateAvailableText to "バージョン {0} が利用可能です。現在：{1}。", S.updateNow to "今すぐ更新", S.later to "後で",
    S.versionLabel to "バージョン {0}", S.updateCheckFailed to "アップデートを確認できませんでした。"
)
private val KO_EXTRA = mapOf(
    S.statusPage to "DeepSeek 상태", S.statusDescription to "DeepSeek 공식 상태 페이지의 실시간 정보입니다.",
    S.allOperational to "모든 시스템 정상", S.degraded to "성능 저하", S.partialOutage to "부분 장애",
    S.fullOutage to "전체 장애", S.maintenance to "점검 중", S.statusUnknown to "상태 확인 불가",
    S.components to "서비스 구성 요소", S.uptime to "90일 가동률", S.activeIncidents to "진행 중인 장애",
    S.noActiveIncidents to "진행 중인 장애가 없습니다", S.recentIncidents to "최근 장애", S.noRecentIncidents to "최근 장애가 없습니다",
    S.lastChecked to "마지막 확인: {0}", S.statusUnavailable to "공식 상태 페이지를 불러올 수 없습니다.",
    S.statusBannerTitle to "DeepSeek 서비스 장애", S.statusBannerText to "현재 상태: {0}. 일부 서비스가 영향을 받을 수 있습니다.",
    S.viewStatus to "상태 보기", S.dismiss to "닫기", S.checkUpdates to "업데이트 확인",
    S.checkingUpdate to "확인 중…", S.upToDate to "최신 버전입니다.", S.updateAvailableTitle to "업데이트 가능",
    S.updateAvailableText to "버전 {0}을 사용할 수 있습니다. 현재 버전: {1}.", S.updateNow to "지금 업데이트", S.later to "나중에",
    S.versionLabel to "버전 {0}", S.updateCheckFailed to "업데이트를 확인할 수 없습니다."
)
private val DE_EXTRA = mapOf(
    S.statusPage to "DeepSeek-Status", S.statusDescription to "Live-Status der offiziellen DeepSeek-Statusseite.",
    S.allOperational to "Alle Systeme betriebsbereit", S.degraded to "Leistung beeinträchtigt", S.partialOutage to "Teilausfall",
    S.fullOutage to "Komplettausfall", S.maintenance to "Wartung", S.statusUnknown to "Status nicht verfügbar",
    S.components to "Komponenten", S.uptime to "Verfügbarkeit (90 Tage)", S.activeIncidents to "Aktive Störungen",
    S.noActiveIncidents to "Keine aktiven Störungen", S.recentIncidents to "Letzte Störungen", S.noRecentIncidents to "Keine aktuellen Störungen",
    S.lastChecked to "Zuletzt geprüft: {0}", S.statusUnavailable to "Offizielle Statusseite konnte nicht geladen werden.",
    S.statusBannerTitle to "DeepSeek-Serviceproblem", S.statusBannerText to "Aktueller Status: {0}. Einige Dienste können betroffen sein.",
    S.viewStatus to "Status anzeigen", S.dismiss to "Schließen", S.checkUpdates to "Nach Updates suchen",
    S.checkingUpdate to "Prüfe…", S.upToDate to "Die neueste Version ist installiert.", S.updateAvailableTitle to "Update verfügbar",
    S.updateAvailableText to "Version {0} ist verfügbar. Aktuell: {1}.", S.updateNow to "Jetzt aktualisieren", S.later to "Später",
    S.versionLabel to "Version {0}", S.updateCheckFailed to "Updates konnten nicht geprüft werden."
)
private val IT_EXTRA = mapOf(
    S.statusPage to "Stato DeepSeek", S.statusDescription to "Stato in tempo reale dalla pagina ufficiale DeepSeek Status.",
    S.allOperational to "Tutti i sistemi operativi", S.degraded to "Prestazioni ridotte", S.partialOutage to "Interruzione parziale",
    S.fullOutage to "Interruzione totale", S.maintenance to "Manutenzione", S.statusUnknown to "Stato non disponibile",
    S.components to "Componenti", S.uptime to "Disponibilità 90 giorni", S.activeIncidents to "Incidenti attivi",
    S.noActiveIncidents to "Nessun incidente attivo", S.recentIncidents to "Incidenti recenti", S.noRecentIncidents to "Nessun incidente recente",
    S.lastChecked to "Ultimo controllo: {0}", S.statusUnavailable to "Impossibile caricare la pagina di stato ufficiale.",
    S.statusBannerTitle to "Problema del servizio DeepSeek", S.statusBannerText to "Stato attuale: {0}. Alcuni servizi potrebbero essere interessati.",
    S.viewStatus to "Vedi stato", S.dismiss to "Chiudi", S.checkUpdates to "Controlla aggiornamenti",
    S.checkingUpdate to "Controllo…", S.upToDate to "Hai già l'ultima versione.", S.updateAvailableTitle to "Aggiornamento disponibile",
    S.updateAvailableText to "La versione {0} è disponibile. Versione attuale: {1}.", S.updateNow to "Aggiorna ora", S.later to "Più tardi",
    S.versionLabel to "Versione {0}", S.updateCheckFailed to "Impossibile controllare gli aggiornamenti."
)
private val ES_EXTRA = mapOf(
    S.statusPage to "Estado de DeepSeek", S.statusDescription to "Estado en directo de la página oficial DeepSeek Status.",
    S.allOperational to "Todos los sistemas operativos", S.degraded to "Rendimiento degradado", S.partialOutage to "Interrupción parcial",
    S.fullOutage to "Interrupción total", S.maintenance to "En mantenimiento", S.statusUnknown to "Estado no disponible",
    S.components to "Componentes", S.uptime to "Disponibilidad de 90 días", S.activeIncidents to "Incidentes activos",
    S.noActiveIncidents to "No hay incidentes activos", S.recentIncidents to "Incidentes recientes", S.noRecentIncidents to "No hay incidentes recientes",
    S.lastChecked to "Última comprobación: {0}", S.statusUnavailable to "No se pudo cargar la página oficial de estado.",
    S.statusBannerTitle to "Problema en el servicio DeepSeek", S.statusBannerText to "Estado actual: {0}. Algunos servicios pueden verse afectados.",
    S.viewStatus to "Ver estado", S.dismiss to "Cerrar", S.checkUpdates to "Buscar actualizaciones",
    S.checkingUpdate to "Comprobando…", S.upToDate to "Ya tienes la última versión.", S.updateAvailableTitle to "Actualización disponible",
    S.updateAvailableText to "La versión {0} está disponible. Versión actual: {1}.", S.updateNow to "Actualizar ahora", S.later to "Más tarde",
    S.versionLabel to "Versión {0}", S.updateCheckFailed to "No se pudieron buscar actualizaciones."
)
class AppStrings(private val values: Map<String, String>) {
    operator fun get(key: String): String = values[key] ?: EN[key] ?: key

    fun format(key: String, vararg args: Pair<String, Any?>): String {
        var result = get(key)
        args.forEach { (name, value) -> result = result.replace("{$name}", value?.toString() ?: "") }
        return result
    }
}

fun stringsFor(code: String): AppStrings = when (code.lowercase()) {
    "zh" -> AppStrings(ZH + ZH_EXTRA)
    "ru" -> AppStrings(RU + RU_EXTRA)
    "ja" -> AppStrings(JA + JA_EXTRA)
    "ko" -> AppStrings(KO + KO_EXTRA)
    "de" -> AppStrings(DE + DE_EXTRA)
    "it" -> AppStrings(IT + IT_EXTRA)
    "es" -> AppStrings(ES + ES_EXTRA)
    else -> AppStrings(EN + EN_EXTRA)
}

val LocalAppStrings = staticCompositionLocalOf { AppStrings(EN) }

fun languageName(code: String): String = supportedLanguages.firstOrNull { it.code == code }?.nativeName ?: "English"
