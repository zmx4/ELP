# 一、需求分析
## 一、选题目的
开发一款面向中国大学生及英语学习者的智能化、个性化英语学习应用，聚焦于大学英语四六级（CET-4/6）,中高考,雅思托福等主流考试需求，通过词典查询、单词默写、听力听写、选择题闯关等互动小游戏，提升用户词汇量、听力理解与应试能力，解决传统背单词枯燥、效率低、缺乏语境等问题。
________________________________________
## 二、背景分析
1. 市场需求旺盛
•	中国每年有超 900万大学生 参加 CET-4/6 考试，是刚需型学习场景。
•	英语能力仍是升学、求职、出国的重要门槛。
•	用户对“碎片化+游戏化”学习方式接受度高（如百词斩、扇贝、墨墨背单词等已验证市场）。
2. 现有产品存在痛点
•	内容同质化严重：多数App仅提供单词列表+例句，缺乏针对CET真题语境的深度整合。
•	互动形式单一：默写、听写等功能薄弱或需付费解锁。
•	个性化不足：无法根据用户错题、记忆曲线动态调整复习计划。
•	缺乏“输入+输出”闭环：重记忆轻应用，用户难以真正掌握词汇用法。
________________________________________
## 三、存在的问题及其危害
问题	危害
学习方式枯燥，缺乏激励机制	用户坚持率低，学习效果差，易半途而废
脱离真实考试语境	背了单词仍不会做题，应试能力提升有限
缺乏听说读写综合训练	导致“哑巴英语”，无法实际运用
数据隐私保护不足（部分App过度收集信息）	用户信任度下降，存在法律合规风险
________________________________________
## 四、解决这些问题的价值
1. 用户价值
•	提升学习兴趣与效率，通过游戏化机制增强粘性；
•	精准匹配CET考纲，提高通过率；
•	构建“查-学-练-测”一体化学习闭环。
2. 社会价值
•	助力教育公平：为资源有限地区学生提供优质学习工具；
•	推动语言能力提升，增强国际竞争力。

# 二、系统设计
## 项目主要构建的是一个基于 Java 的桌面应用程序，采用了现代化的 JavaFX 技术栈。关键技术包括：
•	编程语言: Java (JDK 21)，项目代码完全由 Java 编写。
•	构建工具: Maven 
•	GUI 框架: JavaFX 21 (javafx-controls, javafx-fxml)，用于构建桌面用户界面。
•	UI 组件库: 为了提升界面美观度和交互体验，引入了多个第三方库：
o	ControlsFX: 提供额外的 JavaFX 高级控件。
o	BootstrapFX: 允许在 JavaFX 中使用 Bootstrap 风格的 CSS 样式。
o	JFoenix: 实现了 Google Material Design 风格的 JavaFX 组件。
•	数据库/持久层:
o	SQLite: 嵌入式数据库 (sqlite-jdbc)，用于存储本地数据。
o	ORMLite: 轻量级的对象关系映射库 (ormlite-jdbc)，用于简化数据库操作。
•	测试: JUnit 5 (junit-jupiter) 和 Mockito (mockito-core)。
•	日志: SLF4J (slf4j-simple)。
项目采用了经典的分层架构，结构清晰，主要遵循 MVC（Model-View-Controller）模式的变体：
•	View (视图层): org.tick.elp.View
o	负责界面的展示。由于使用了 JavaFX，这里可能包含 FXML 文件加载逻辑和主应用程序入口 (DictionaryApplication)。
•	Controller (控制层): org.tick.elp.Controller
o	负责处理用户交互，连接 View 和 Service。它接收界面事件，调用后台服务，并更新视图。
•	Service (服务层): org.tick.elp.Service
o	核心业务逻辑层。定义了一系列接口（如 IDataBaseService, IWordQueryService）和具体实现（如 UserDataStorage, QueryService）。
o	负责处理数据查询、用户偏好设置、错题记录等逻辑。
•	Entity (实体层): org.tick.elp.Entity
o	定义数据模型，直接映射到数据库表。例如 Word 类对应字典表。
•	Helper (辅助层): org.tick.elp.Helper
o	包含通用工具类。
接口设计: Service 层通过接口暴露功能，例如 IUserDataStorage 定义了用户数据存储的操作规范，而 UserDataStorage 类去实现它。这降低了耦合度，方便测试和替换实现。
Entity 层
类名: Word (映射表: dictionary)
•	属性:
o	word: String (主键, 单词本身)
o	translation: String (翻译)
o	cet4, cet6: int (四六级标记)
o	tf (托福), ys (雅思), hs (高考), ps (中考): int (各级考试标记)
•	方法: 标准的 Getter 和 Setter 方法，以及构造函数。
类名: TestRecord(映射表: TestRecordTable)
				属性
	Id:int(主键)
	Score:double(正确率)
	testDate:Date(日期)_
	方法: 标准的 Getter 和 Setter 方法，以及构造函数。
Service 层
类名: UserDataStorage (实现 IUserDataStorage，单例模式)
•	内部类:
o	UserMistake (错题记录): id, word, mistakeDate
o	UserCollection (收藏记录): id, word, collectionDate
•	主要方法:
o	getInstance(): 获取单例实例。
o	initializeUserDataBase(): 初始化数据库连接和建表。
o	insertUserTestData(List<String> falseAnswers): 批量插入错题。
o	addCollection(String word): 添加单词到收藏夹。
o	removeCollection(String word): 从收藏夹移除。
o	getCollections(): 获取所有收藏单词列表。
o	isCollected(String word): 判断单词是否已收藏。
o	clearUserTestData(): 清除用户数据。
## 应用界面
JFoenix:界面会有 Material Design 风格
BootstrapFX: 意味着按钮、文本框等带有 Bootstrap 网页风格的样式
ControlsFX: 提供了通知弹窗、进度条、特殊的列表视图等原生 JavaFX 不具备的高级控件。
## 算法
•	随机算法: SQLWordRandomGet 类和 IWordRandomGet 接口表明系统有“随机获取单词”的功能（可能用于背单词或测验）。这通常通过 SQL 的 ORDER BY RANDOM() 或 Java 的 Random 类在特定范围内抽取数据实现。
•	查询逻辑: QueryService 负责单词查找，可能涉及模糊匹配或前缀匹配算法（依赖 SQL 的 LIKE 语句）。
存储机制: 采用 SQLite 文件数据库 (UserDataBase.db)，这是一个轻量级的本地文件数据库，非常适合单机桌面应用，无需用户额外安装数据库软件。
存储的信息:
1.	静态字典数据 (Word 实体):
o	单词拼写 (word)
o	中文释义 (translation)
o	难度/分类标签 (cet4, cet6, tf 等，用于区分四六级、托福、雅思等词汇)。
2.	用户动态数据 (UserDataStorage 管理):
o	错题本 (UserMistake): 记录用户在测验中答错的单词及时间。
o	生词/收藏本 (UserCollection): 记录用户主动收藏的单词及收藏时间。
3.	用户偏好设置 (FilePreferenceStorage):
o	虽然具体字段未完全读取，但通常用于存储用户的设置（如上次登录状态、界面主题等），可能以文件形式（Properties 或 JSON）存储。
使用json存储用户设置sqlite存储用户运行产生的数据



# 三、系统实现
本项目采用分层架构，主要分为 View（视图层）、Controller（控制层） 和 Service（服务层）。模块间的交互主要通过 Service 层定义的接口进行解耦，Controller 层调用 Service 接口，Service 层负责具体的数据处理和持久化。
### 1.1 Service 层接口定义与实现
Service 层封装了核心业务逻辑，对外提供接口，对内屏蔽数据库和文件操作细节。
•	数据库连接服务 (IDataBaseService)
o	接口定义: 定义了初始化连接 initializeConnect() 和获取 DAO 对象 getWordDao() 的方法。
o	具体实现 (DataBaseService):
	采用 Singleton（单例模式） 保证全局只有一个数据库连接实例。
	使用 ORMLite 框架管理 SQLite 数据库 (stardict.db)。
	initializeConnect() 方法中配置 JDBC 连接字符串 (jdbc:sqlite:...)，并创建 Word 实体的 DAO 对象。
•	单词查询服务 (IWordQueryService)
o	接口定义: 提供 queryTranslation(String word)（查释义）、queryWordByTranslation(String translation)（查单词）和 queryWord(String word)（查详情）接口。
o	具体实现 (QueryService):
	持有 IDataBaseService 的引用。
	通过 wordDao.queryForId(word) 实现主键精确查询。
	通过 wordDao.queryBuilder().where().like(...) 实现模糊查询（反向查词）。
•	用户数据存储服务 (IUserDataStorage)
o	接口定义: 定义了用户生词本（Collection）和错题集（Mistake）的增删改查接口，如 addCollection、getCollections、insertUserTestData。
o	具体实现 (UserDataStorage):
	管理独立的数据库 UserDataBase.db，与字典数据分离，便于用户数据迁移。
	内部维护 UserMistake 和 UserCollection 两个表的 DAO。
	实现了 initializeUserDataBase() 自动建表逻辑，确保首次运行时环境就绪。
•	随机单词获取服务 (IWordRandomGet)
o	接口定义: getRandomWordArray(int number, String table)，用于从指定词库（如 CET4, CET6）中随机抽取指定数量的单词。
o	具体实现 (SQLWordRandomGet):
	利用 SQLite 的 Recursive CTE (公用表表达式) 和 random() 函数高效生成随机 ID。
	通过 SQL 语句直接在数据库层面完成随机抽取，避免将全量数据加载到内存中，提高了性能。
### 1.2 Controller 与 Service 的交互
Controller 层不直接操作数据库，而是通过组合（Composition）的方式持有 Service 接口的引用。
•	依赖注入: 在 Controller 的构造函数或初始化块中实例化具体的 Service 类（如 new QueryService() 或 UserDataStorage.getInstance()）。
•	调用方式: 例如 DictionaryController 在用户点击查询时，直接调用 queryService.queryTranslation(input)，获取返回的 String 或 List 数据更新 UI。
________________________________________
## 2. 主要的业务逻辑和流程的具体实现
### 2.1 单词查询流程
1.	用户输入: 用户在 DictionaryController 的 wordField 输入内容。
2.	模式判断: 系统根据单选框 (RadioButton) 判断是 "查单词" 还是 "查释义"。
3.	服务调用:
o	查单词: 调用 QueryService.queryTranslation()。如果找到，返回释义并显示；如果未找到，提示用户。
o	查释义: 调用 QueryService.queryWordByTranslation()，返回包含该释义的单词列表 (List<String>)，并在 ListView 中展示。
4.	详情查看: 用户双击结果列表中的单词，触发 MainController.showDetail()，跳转到详情页展示完整信息（包括各级考试频率等）。
### 2.2 生词本（收藏）管理流程
1.	状态同步: 在查询单词详情时，DictionaryController 会调用 UserDataStorage.isCollected() 检查该词是否已收藏，并同步 ToggleButton 的选中状态。
2.	添加/移除: 用户点击收藏按钮，触发 addCollection 或 removeCollection。操作直接写入 UserDataBase.db。
3.	列表展示: CollectionController 初始化时调用 getCollections() 获取所有收藏单词，并填充到 ObservableList 中，通过 JavaFX 的数据绑定机制自动更新 ListView。
### 2.3 单词测试流程
1.	参数设置: 用户在 TestController 选择词库（如 CET4）和题目数量。
2.	试题生成:
o	调用 SQLWordRandomGet 获取随机单词 Map（单词 -> 释义）。
o	将 Map 转换为 List 以便通过索引遍历。
3.	题目展示:
o	随机题型: 系统随机决定当前题目是 "选择题" 还是 "拼写题"。
o	选择题: 生成 3 个干扰项（从剩余单词中随机选取）和 1 个正确答案，打乱顺序后绑定到按钮上。
o	拼写题: 隐藏选项，显示输入框，根据单词长度生成提示（如首字母）。
4.	判题与反馈: 用户提交答案后，系统比对输入与正确答案，更新界面显示 "正确" 或 "错误"，并记录错题（通过 UserDataStorage）。
________________________________________
## 3. 界面的具体实现
界面层基于 JavaFX 框架实现，采用 FXML 进行布局与代码分离。
3.1 视图布局 (FXML)
•	主容器 (main-view.fxml): 使用 BorderPane 或 StackPane 作为根布局。左侧/顶部通常为导航栏，中间区域 (contentArea) 用于动态加载子视图。
•	功能视图:
o	hello-view.fxml (字典查询): 包含 TextField (输入)、TextArea (显示释义)、ListView (显示列表结果) 和 ToggleButton (收藏)。
o	collection-view.fxml: 主要包含一个 ListView 用于展示收藏列表。
o	test-view.fxml: 复杂的动态布局，包含 VBox 用于垂直排列题目、选项区域 (optionsBox) 和填空区域 (fillBox)，通过 setVisible() 动态切换题型显示。
3.2 视图导航与控制
•	MainController (路由中心):
o	维护一个 StackPane contentArea。
o	提供 loadView(String fxmlPath) 方法，使用 FXMLLoader 加载 FXML 文件并替换 contentArea 的子节点，实现页面切换（如从字典页切换到测试页）。
o	提供 showDetail(String word) 方法，支持传递参数（单词内容）到下一个视图的 Controller (DetailController.initData)。
3.3 数据绑定与事件处理
•	FXML 注解: Controller 使用 @FXML 注解注入 UI 组件（如 @FXML private TextField wordField;）。
•	事件监听:
o	按钮点击: 在 FXML 中定义 onAction="#onQueryButtonClick"，或在 initialize() 中通过 button.setOnAction(...) 绑定。
o	列表交互: ListView 设置了鼠标点击监听器 (setOnMouseClicked)，检测双击事件 (event.getClickCount() == 2) 以跳转详情。
o	属性监听: 使用 JavaFX 的 Property 监听机制，例如监听
RadioButton 的切换来动态改变输入框的提示文本 (PromptText)。

# 四、系统测试
## 1. 测试用例 (Test Cases)
我们将针对三个核心模块：单词查询 (Dictionary)、单词测试 (Quiz/Test) 和 生词本 (Collection) 设计测试用例。
1.1 模块：单词查询与详情展示 (Dictionary & Detail)
测试环境：应用启动，位于主界面或查询界面。
ID	功能点	测试数据 (输入)	预期结果
TC-01	精确查询存在的单词	输入: apple  点击查询	1. 界面跳转到详情页。2. 显示单词 apple。3. 显示翻译（如"苹果"）。4. 显示对应的考级标签（如 "CET4", "中考"）。
TC-02	查询不存在的单词	输入: xyz123  点击查询	1. 界面显示详情页或提示框。2. 单词显示 xyz123。3. 翻译区域显示 "未找到详细信息" 或类似提示。
TC-03	查询空字符串	输入: (空)  点击查询	1. 系统无反应，或者提示"请输入单词"。2. 程序不应崩溃。
TC-04	详情页返回	在 apple 详情页点击 "返回"	1. 返回到进入前的界面（主界面或生词本界面）。
## 1.2 模块：单词测试 (Test/Quiz)
测试环境：点击 "Test" 进入测试界面。
ID	功能点	测试数据 (输入)	预期结果
TC-05	开始测试 (数据获取)	选择词库: CET4  点击 "Start"	1. 按钮变为不可用。2. 从数据库加载20个单词。3. 显示第1题界面。4. 题目区域可见，选项/输入框可见。
TC-06	题型1：选择题 (正确)	题目: apple (对应翻译"苹果")操作: 点击选项 "苹果"	1. 提示 "回答正确" (绿色反馈)。2. "Next" 按钮变亮（可用）。3. 错误次数不增加。
TC-07	题型1：选择题 (错误)	题目: apple操作: 点击选项 "香蕉"	1. 提示 "回答错误" (红色反馈)。2. 显示正确答案。3. 单词被自动加入错题集/生词本（如果实现了该逻辑）。
TC-08	题型2：拼写题 (正确)	提示: "苹果", 首字母 a...输入: apple点击确认	1. 提示 "回答正确"。2. 进入下一题状态。
TC-09	题型2：拼写题 (错误)	提示: "苹果"输入: pear	1. 提示 "回答错误"。2. 显示正确拼写 apple。
TC-10	测试结束	完成第20题后点击 "Next"	1. 题目区域隐藏。2. 显示 "测试结束" 字样。3. 显示本次测试的正确率或总结。
## 1.3 模块：生词本 (Collection)
测试环境：点击 "Collection" 进入生词本界面。
ID	功能点	测试数据 (输入)	预期结果
TC-11	加载生词列表	进入界面	1. ListView 中列出所有已收藏的单词。2. 如果为空，列表显示空白。
TC-12	查看生词详情	双击列表中的 apple	1. 跳转到详情页 (DetailView)。2. 详情页正确显示 apple 的信息。3. 点击返回时，应确切回到生词本界面，而不是主界面。
TC-13	删除生词	选中 apple点击 "Delete"	1. apple 从列表中消失。2. 数据库中该条收藏记录被删除。3. 刷新后不再出现。


# 六、参考文献
https://github.com/sshahine/JFoenix 项目使用框架
https://github.com/controlsfx/controlsfx 控件扩展
https://github.com/JabRef/jabref 结构参考,思想来源
https://github.com/zmx4/Team 先前编写项目,提供部分思路https://www.baeldung-cn.com/ormlite ORMLite学习
https://gitee.com/zhangyin-gitee/daily-poetry-a 借鉴其MVVM,MOCK思想
https://github.com/zmx4/WordErrorCorrectionSolution  csvToSqlote脚本
https://code.google.com/p/stardict-3  项目主词典来源
https://github.com/skywind3000/ECDICT 项目辅助词典来源
https://steamcommunity.com/sharedfiles/filedetails/?id=3488050738 四六级词典
https://github.com/huzheng001/stardict-3 部分转换工具
https://code.visualstudio.com/docs/copilot/overview 项目规范化工具
https://prettier.io/ 代码规范化工具
https://git-scm.cn/install/ 代码版本管理工具
https://github.com/ 代码托管平台
https://code.visualstudio.com/ 项目主要IDE之一
https://www.jetbrains.com/zh-cn/idea/ 项目主要IDE之一
https://www.jetbrains.com/zh-cn/datagrip/ 数据库管理软件
https://marketplace.visualstudio.com/items?itemName=mhutchie.git-graph git工具
https://github.com/zmx4/ELP/blob/main/.github/workflows/maven-publish.yml 自动化工具
https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-upgrade  代码规范化工具
https://graalvm.java.net.cn/ 项目使用JDK
https://graalvm.java.net.cn/latest/getting-started/ 参考教程
https://www.bilibili.com/list/37737161/?sid=2800550&oid=113510197630837&bvid=BV1ZgScYfE3r maven教程
https://www.bilibili.com/list/37737161/?sid=2800550&oid=647924810&bvid=BV1ce4y1W7YB git教程
https://stackoverflow.com/questions/201323/

# 七、致谢
