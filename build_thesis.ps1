$word = New-Object -ComObject Word.Application
$word.Visible = $false

# 打开现有文档
$doc = $word.Documents.Open("D:\QJShop\毕业设计成果.docx")
$doc.Content.Delete()

$selection = $word.Selection

# 页面设置
$doc.PageSetup.TopMargin = 72  # 2.5cm
$doc.PageSetup.BottomMargin = 72
$doc.PageSetup.LeftMargin = 72
$doc.PageSetup.RightMargin = 72

# 字体辅助函数
function Set-Font($s, $name, $size, $bold, $alignment, $spaceAfter) {
    $s.Font.Name = $name
    $s.Font.Size = $size
    $s.Font.Bold = $bold
    $s.ParagraphFormat.Alignment = $alignment
    if ($spaceAfter -ge 0) { $s.ParagraphFormat.SpaceAfter = $spaceAfter }
    $s.ParagraphFormat.LineSpacingRule = 1  # wdLineSpaceSingle
}

function Write-Paragraph($text, $fontName, $size, $bold, $align) {
    $selection.TypeText($text)
    $selection.Font.Name = $fontName
    $selection.Font.Size = $size
    $selection.Font.Bold = $bold
    $selection.ParagraphFormat.Alignment = $align
    $selection.ParagraphFormat.FirstLineIndent = 0
    $selection.TypeParagraph()
}

function Write-Body($text) {
    Write-Paragraph $text "宋体" 12 $false 2  # 12pt = 小四
}

function Write-H1($text) {
    Write-Paragraph $text "黑体" 15 $true 1   # 小三
}

function Write-H2($text) {
    Write-Paragraph $text "黑体" 14 $true 0   # 四号
}

function Write-H3($text) {
    Write-Paragraph $text "黑体" 12 $true 0   # 小四加粗
}

$wdAlignCenter = 1
$wdAlignLeft = 0
$wdAlignRight = 2

# ========== 封面 ==========
$selection.TypeParagraph()
$selection.TypeParagraph()
Write-Paragraph "毕业设计" "黑体" 15 $true $wdAlignCenter
$selection.TypeParagraph()
$selection.TypeParagraph()
Write-Paragraph '题目 "柒玖商店"：基于SpringBoot多前端多平台的商城系统' "黑体" 18 $true $wdAlignCenter
$selection.TypeParagraph()
$selection.TypeParagraph()
$selection.TypeParagraph()
$selection.TypeParagraph()

$coverInfo = @(
    @("姓    名", "李鑫"),
    @("学    号", "202330302152"),
    @("二级学院", "信息工程与商务学院"),
    @("专    业", "软件技术"),
    @("班    级", "2307 班"),
    @("校内指导教师", "彭德宇"),
    @("职    称", "老师"),
    @("企业指导教师", "（必填）"),
    @("职    称", ""),
    @("完成日期", "2026.5.30")
)

foreach ($item in $coverInfo) {
    $text = "$($item[0])`t$($item[1])"
    $selection.TypeText($text)
    $selection.Font.Name = "宋体"
    $selection.Font.Size = 14  # 小三
    $selection.Font.Bold = $false
    $selection.ParagraphFormat.Alignment = $wdAlignCenter
    $selection.TypeParagraph()
}

$selection.TypeParagraph()
$selection.TypeParagraph()
Write-Paragraph "教务处印制" "宋体" 14 $false $wdAlignCenter

# ========== 新页：目录 ==========
$selection.InsertBreak(7)  # wdPageBreak
Write-Paragraph "目    录" "黑体" 15 $true $wdAlignCenter
$selection.TypeParagraph()

# 简版目录，用户后续自动生成
$tocItems = @(
    "一、设计背景",
    "  1.1 选题背景",
    "  1.2 选题意义",
    "二、设计思路",
    "  2.1 开发环境与工具",
    "  2.2 技术架构",
    "三、需求分析",
    "  3.1 设计目标",
    "  3.2 功能需求",
    "  3.3 性能要求",
    "四、作品设计",
    "  4.1 总体设计",
    "  4.2 详细设计",
    "  4.3 数据库设计",
    "五、功能实现",
    "  5.1 用户端功能实现",
    "  5.2 管理端功能实现",
    "  5.3 HarmonyOS 鸿蒙端实现",
    "六、作品测试",
    "  6.1 测试环境",
    "  6.2 测试方法",
    "  6.3 测试用例",
    "  6.4 测试总结",
    "七、设计总结",
    "  7.1 实现过程",
    "  7.2 遇到的问题与收获",
    "八、参考资料",
    "九、查重报告"
)
foreach ($t in $tocItems) {
    $isMain = $t -match '^[一二三四五六七八九]、'
    $selection.TypeText($t)
    $selection.Font.Name = if ($isMain) { "宋体" } else { "宋体" }
    $selection.Font.Size = if ($isMain) { 14 } else { 12 }
    $selection.Font.Bold = $isMain
    $selection.ParagraphFormat.Alignment = $wdAlignLeft
    $selection.TypeParagraph()
}

# ========== 正文函数 ==========
function Write-Section($title, $content) {
    $selection.InsertBreak(7)
    Write-H1 $title
    $lines = $content -split "`n"
    foreach ($line in $lines) {
        $line = $line.Trim()
        if ($line -eq "") { $selection.TypeParagraph(); continue }

        # 判断标题级别
        $isH2 = $line -match '^[0-9]\.[0-9] '
        $isH3 = $line -match '^[0-9]\.[0-9]\.[0-9] '
        $isTable = $line -match '^\|'

        if ($isTable) {
            $selection.TypeText($line)
            $selection.Font.Name = "宋体"
            $selection.Font.Size = 10
            $selection.Font.Bold = $false
            $selection.ParagraphFormat.Alignment = $wdAlignLeft
        } elseif ($isH2) {
            $selection.TypeText($line)
            $selection.Font.Name = "黑体"
            $selection.Font.Size = 14
            $selection.Font.Bold = $true
            $selection.ParagraphFormat.Alignment = $wdAlignLeft
        } elseif ($isH3) {
            $selection.TypeText($line)
            $selection.Font.Name = "黑体"
            $selection.Font.Size = 12
            $selection.Font.Bold = $true
            $selection.ParagraphFormat.Alignment = $wdAlignLeft
        } else {
            $selection.TypeText($line)
            $selection.Font.Name = "宋体"
            $selection.Font.Size = 12
            $selection.Font.Bold = $false
            $selection.ParagraphFormat.Alignment = $wdAlignLeft
            $selection.ParagraphFormat.FirstLineIndent = $word.CentimetersToPoints(0.74)
        }
        $selection.TypeParagraph()
    }
}

# ========== 正文内容 ==========
Write-Section "一、设计背景" @'
1.1 选题背景

随着互联网技术的普及和移动支付的发展，线上购物已成为人们日常生活中不可或缺的一部分。传统的实体零售模式受限于营业时间与地理位置，而电商平台能够突破这些限制，为消费者提供7×24小时的无间断服务和更广泛的商品选择。据统计，2025年中国网络零售市场规模已超过15万亿元，占社会消费品零售总额的30%以上，电商在国民经济中的重要性日益凸显。

目前，主流的电商平台如淘宝、京东、拼多多虽然功能全面，但对于特定用户群体或细分市场，往往存在功能冗余、个性化不足等问题。中小型商家和创业者在尝试数字化转型时，常常面临自建平台技术门槛高、成本高昂的挑战——市面上可定制化的电商系统动辄需要数万至数十万元的授权费用，且二次开发周期长。

同时，2025年以来人工智能技术快速发展，特别是DeepSeek等国产大语言模型（LLM）的兴起，为电商客服、智能推荐等场景带来了全新的可能性。将AI技术以低成本、高效率的方式应用于电商平台，能够显著提升用户购物体验和运营效率。此外，随着鸿蒙生态的逐渐完善，越来越多的应用需要支持多终端部署，覆盖Android、iOS以外的HarmonyOS系统。

在此背景下，本课题旨在设计并实现一个功能完整、体验优良、支持多终端的轻量化电商系统。该系统命名为"柒玖商店"，寓意为用户提供全天候（7×24）、全方位的购物服务体验。选题的意义在于，通过构建一个前后端分离、支持多前端（PC管理后台、移动端用户商城、HarmonyOS鸿蒙端）的完整电商平台，集成AI大模型实现智能客服与商品推荐，为中小型商家提供一个低成本的数字化转型参考方案，同时也为学习与掌握现代企业级全栈开发技术提供一个全面的实践案例。

1.2 选题意义

本毕业设计的具体意义体现在以下几个方面：

第一，技术整合与应用的实践价值。本系统采用当前主流的"前后端分离"架构，后端基于Spring Boot 3.2框架，前端分别使用Vue3+Element Plus（管理端）、Vue3+Vant（移动端）以及ArkTS+ArkUI（HarmonyOS鸿蒙端），实现了从Web到原生的多终端覆盖。通过这一实践，可以深入理解RESTful API设计原则、JWT无状态认证、Vue 3组合式API、MyBatis-Plus高效数据操作、Spring Security安全框架等核心技术栈的整合运用，对于提升软件开发者的架构设计能力和工程实践能力具有显著意义。

第二，AI技术在电商场景的落地探索。系统创新性地集成了DeepSeek大语言模型作为AI客服核心引擎。通过精心设计的提示词工程（Prompt Engineering），AI能够理解用户模糊的自然语言描述（如"我想要饮料"、"推荐一款手机"），并结合关联词映射技术（覆盖11个品类的扩展搜索）从数据库中精准匹配已上架且有库存的商品，进行智能化推荐。推荐结果以商品卡片形式嵌入聊天流，用户可直接点击跳转购买。这一实践为AI大模型在中小型电商场景中的低成本应用提供了可行的技术方案。

第三，多终端适配与响应式设计的工程实践。项目前端覆盖了移动端H5、PC端和HarmonyOS原生应用三种终端形态。针对高分辨率手机被误判为PC端的问题，创新性地采用了UA检测+触摸屏检测+屏幕宽度的三维判定策略，准确率大幅提升。PC端商品详情页参考京东商城设计，实现了左右双栏布局和缩略图切换等桌面端交互。这种多终端适配的设计思想，符合当前互联网应用向多设备延伸的发展趋势。

第四，解决实际商业场景中的痛点。现有通用电商平台功能繁杂，二次开发和个性化定制成本高昂。本系统聚焦于电商核心业务流程（商品管理→用户浏览→购物车→下单→状态流转），功能清晰、结构完整、成本低廉，可作为小型商户、学生创业团队快速搭建自有销售渠道的解决方案，具有实际应用价值和商业参考意义。

第五，全面覆盖电商核心数据模型。系统数据库设计了12张核心数据表，不仅包含了商品、订单、用户等基础电商数据，还扩展了商品规格（SKU）、订单明细、收货地址、用户收藏、商品评价（含回复）、首页轮播Banner、客服消息等完整的数据模型，可支撑一个中型电商平台的日常运营需求。
'@)

Write-Section "二、设计思路" @'
2.1 开发环境与工具

本系统的开发与运行所需环境与工具如下表所示。

表 2-1 开发环境与工具

| 类别 | 工具/技术 | 版本/说明 |
|------|----------|----------|
| 硬件环境 | 开发电脑 | Intel Core i7，16GB内存，Windows 11 |
| 后端开发 | JDK | 17 (LTS) |
| | Maven | 3.8+ 项目管理与构建 |
| | IntelliJ IDEA | 2023+ 集成开发环境 |
| | Spring Boot | 3.2.5 核心应用框架 |
| | MyBatis-Plus | 3.5.5 ORM框架 |
| | Spring Security | 6.1.6 安全框架 |
| | Druid | 1.2.20 数据库连接池 |
| | JWT (jjwt) | 0.12.5 身份认证 |
| | Knife4j | 4.5.0 API接口文档 |
| | Apache POI | 5.2.5 Excel导出 |
| | Hutool | 5.8.25 工具集 |
| 前端开发 | Node.js | 20.19.0+ |
| | Vue 3 | 3.5.31 前端框架 |
| | Vite | 8.0 构建工具 |
| | Element Plus | 2.13.7 管理端UI |
| | Vant | 4.9.24 移动端UI |
| | Axios | 1.15.0 HTTP客户端 |
| | Vue Router | 5.0 路由 |
| | Pinia | 3.0 状态管理 |
| | VS Code | 最新版代码编辑器 |
| 鸿蒙开发 | DevEco Studio | 5.0+ IDE |
| | ArkTS | 1.2.1 开发语言 |
| 数据库 | MySQL | 8.0 关系型数据库 |
| | Navicat | 数据库管理 |
| AI服务 | DeepSeek API | deepseek-chat模型 |
| | Nominatim API | 逆地理编码(GPS) |
| | ipapi.co | IP定位回退 |
| 服务器 | Alibaba Cloud ECS | CentOS 7.9，2核4G |
| 版本控制 | Git + GitHub | 代码托管与协作 |

2.2 技术架构

本系统采用前后端分离的B/S架构，并扩展支持HarmonyOS原生应用端。整体分为四个部分：Vue3构建的Web前端应用（包括移动端用户商城和PC端管理后台）、HarmonyOS ArkTS构建的原生应用端、Spring Boot构建的后端API服务层、以及MySQL数据库层。核心业务流程是：用户通过浏览器、手机或鸿蒙设备访问前端页面，前端通过Axios发起异步HTTP请求到后端API，后端通过Spring Security+JWT进行身份认证和权限校验，通过Controller→Service→Mapper的层次结构处理请求并与数据库交互后返回JSON数据，前端接收数据并动态渲染页面。

[图片占位：系统整体架构图]

前端技术栈详解：Web前端基于Vue 3组合式API（Composition API）+ Vite构建工具。面向普通用户的移动端商城（C端）使用轻量级的Vant 4 UI组件库，提供类原生App的触摸交互体验；同时针对PC端大屏幕访问场景，在商品详情页等关键页面实现了京东商城风格的双栏布局和桌面端鼠标交互模式。面向管理员的PC后台管理系统（B端）使用Element Plus组件库，提供功能丰富、操作高效的数据管理界面，所有管理页面均针对移动端屏幕（≤768px）做了全局响应式适配（表格字体缩小、按钮压缩、弹窗全屏、侧边栏改为抽屉式），确保管理员在手机端也能完成基本的数据维护操作。

设备检测策略：传统响应式设计仅根据window.innerWidth判断设备类型，导致许多高分辨率手机（如部分Android旗舰机CSS像素宽度达1080px）被误判为PC端。本系统采用三维检测策略：(1) UA（User Agent）检测——识别Android/iPhone/iPad等移动设备标识；(2) 触摸屏检测——检测ontouchstart事件和maxTouchPoints属性；(3) 屏幕宽度兜底——无触摸且宽度<600px按移动端处理。只有三个维度综合判定为非移动设备时才使用PC端布局，有效解决了高分辨率手机的误判问题。

后端技术栈详解：核心框架为Spring Boot 3.2.5，利用其自动配置特性简化了项目搭建和部署流程。持久层框架采用MyBatis-Plus 3.5.5，通过代码生成器和Lambda查询包装器（LambdaQueryWrapper）极大提高了数据库操作的效率——避免了传统MyBatis的大量XML映射文件的编写，同时保持了SQL的灵活性（自定义@Select注解实现统计查询）。使用Spring Security 6.1.6作为安全框架骨架，并结合JJWT 0.12.5实现基于Token的无状态用户认证机制。安全配置采用"默认允许+注解授权"的策略（.anyRequest().permitAll() + @PreAuthorize注解），简化了路径配置的复杂度。

数据库层面：MySQL 8.0数据库存储全部12张业务数据表。Druid 1.2.20作为数据库连接池，提供高性能的连接管理和SQL监控功能（包括慢SQL日志和Druid Admin监控面板）。所有表的删除操作均使用MyBatis-Plus的@TableLogic注解实现逻辑删除（deleted字段标记），保障数据安全性和可恢复性。

AI客服模块架构：独立封装DeepSeekService类，通过Spring RestTemplate调用DeepSeek API（https://api.deepseek.com/v1/chat/completions）。系统提示词（System Prompt）动态构建：根据用户消息提取关键词→扩展关联词（11个品类映射表）→搜索数据库商品（已上架+有库存+按销量降序，最多10个）→嵌入商品列表到提示词。这种"检索增强生成"（Retrieval-Augmented Generation, RAG）的简化实现，使AI客服能够基于实际库存数据进行真实有效的商品推荐。推荐结果通过[RECOMMEND]标记协议在回复文本中编码，后端解析后组装为结构化商品卡片数据返回前端。API调用失败时自动降级为预设关键词回复，确保基础客服功能不中断。

文件上传模块架构：前端通过axios发送multipart/form-data请求（不手动设置Content-Type，让浏览器自动生成正确boundary）。后端FileUploadController接收文件后，按日期分目录存储（uploads/yyyy/MM/dd/），使用UUID生成唯一文件名避免冲突。WebMvcConfigurer配置添加资源映射（/uploads/** → file:uploads/），使上传文件可通过URL直接访问。

物理部署架构：前端Web项目通过Vite build打包为静态文件（HTML/CSS/JS），输出至dist目录。然后将dist目录下的全部38个文件复制到Spring Boot后端的src/main/resources/static/目录中。后端通过Maven package命令打包为可执行Fat JAR（约64MB），包含了内嵌Tomcat服务器和所有前端静态资源。将JAR上传至阿里云ECS服务器后，通过nohup java -jar命令后台启动，实现前后端一体化部署。开发环境下通过Vite的proxy配置将/api请求代理到localhost:8080后端服务解决跨域问题。
'@)

Write-Section "三、需求分析" @'
3.1 设计目标

本系统的设计目标是构建一个功能完整、性能良好、用户体验优秀、支持多终端的B2C（Business to Customer）电商平台。具体目标如下：

功能完整性：系统需覆盖电商平台的全部核心业务流程：(1) 用户注册登录与个人信息管理；(2) 商品多级分类浏览、关键词搜索与详情查看（含规格/SKU选择）；(3) 购物车管理与批量结算；(4) 收货地址管理（含GPS/IP定位自动填充）；(5) 订单生成、支付模拟与状态流转（待付款→待发货→待收货→已完成/已取消）；(6) 商品评价（含评分文字图片、管理员回复）；(7) 用户收藏管理；(8) 后台全维度数据管理（用户/商品/分类/订单/轮播/评价/客服）；(9) 数据统计仪表盘；(10) Excel数据导出。创新功能：(11) AI智能客服（DeepSeek大模型对话+关联商品推荐，覆盖11个品类）；(12) HarmonyOS鸿蒙端原生应用。

多终端适配：用户端同时支持移动端H5和PC端两种布局模式。移动端使用Vant UI组件，以触摸交互为主，适配320px-768px的主流手机屏幕。PC端参考京东商品详情页设计，采用左右双栏布局和桌面端鼠标交互模式（包括hover效果、大图缩略图切换等）。设备检测通过UA+触摸屏+屏幕宽度三维策略实现精准切换。同时开发HarmonyOS原生应用，通过WebView加载Web端页面，覆盖鸿蒙生态设备。

易用性与体验：(1) 用户端界面简洁美观，首页集搜索栏、Banner轮播、分类导航、热门/新品推荐于一体；(2) 商品图片采用SVG渐变占位图机制——当网络图片加载失败或数据库中无图片时，自动生成带有商品首字的彩色渐变占位图（12种配色方案），确保界面在任何情况下都美观可用；(3) AI客服欢迎语和快捷问题标签提供低门槛的交互入口；(4) 个人中心以卡片式布局展示用户信息和订单统计数据，一目了然；(5) GPS定位支持自动回退IP定位，减少用户手动输入；(6) 管理端界面信息展示清晰，数据管理操作便捷，所有CRUD操作均有确认弹窗提示。

安全性：系统具备多层安全防护能力：(1) 用户密码使用BCryptPasswordEncoder加密存储，不可逆；(2) MyBatis-Plus参数化查询和LambdaWrapper防止SQL注入；(3) 基于JWT Token的无状态认证，Token含用户ID、用户名和角色信息，签名使用HS512算法；(4) 管理端敏感接口使用@PreAuthorize("hasRole('ADMIN')")注解进行角色权限校验；(5) 前端路由守卫对未登录和权限不足的访问进行拦截重定向；(6) 文件上传限制图片类型（仅允许image/* MIME类型）。

可扩展性与可维护性：后端采用分层架构（Controller→Service→Mapper→Entity），Service层定义接口契约，便于替换实现和单元测试。前端采用组件化开发，每个页面独立封装，API模块按业务领域拆分。数据库所有表预留扩展字段，支持业务需求的灵活变更。

性能要求：在并发用户数小于50时，页面首次加载时间不超过3秒，核心数据查询接口（如商品分页列表）响应时间在1秒以内，数据写入接口（如提交订单）响应时间在2秒以内。AI客服模块在DeepSeek API调用正常时响应时间在3-5秒，API调用失败时在1秒内返回降级回复。系统应能支持至少50个并发用户同时浏览操作。涉及金额和库存的计算必须保证数据准确性。

3.2 功能需求

本系统用户角色分为三类：未登录游客、已登录普通用户和系统管理员。不同角色对应不同的功能权限。

3.2.1 普通用户功能需求

(1) 用户中心模块：用户可通过用户名和密码注册账号（密码BCrypt加密、手机号格式校验）。登录后系统返回JWT Token和完整用户信息，前端持久化存储。个人中心页面以渐变卡片式布局展示用户头像、昵称和手机号，提供订单统计区（全部/待付款/待发货/待收货/已完成各状态数量实时统计，数据来自/api/user/orders/counts接口）。支持修改个人资料（昵称、手机号、邮箱、性别、生日）和头像上传——提供"相册"和"拍照"两个入口（拍照使用capture="camera"属性调用设备摄像头）。账号设置页使用Vant的Form组件进行表单收集和校验。

(2) 商品浏览与搜索模块：首页集成搜索栏（Vant Search组件，shape="round"圆形搜索框）、Banner轮播（Vant Swipe，autoplay=3000ms，5张Banner从数据库加载）、分类快捷入口（使用数据库中base64格式的分类图标图片，加载失败时回退为纯色方块+分类名首字，10种配色轮换，响应式4-5列布局）、热门商品推荐区（调用/api/products/hot，按销量降序Top10）和新品推荐区（调用/api/products/new，按创建时间降序Top10）。商品卡片使用CSS Grid布局，悬停时上浮4px并加深阴影，价格使用红色字体显示。搜索功能支持关键词模糊匹配，点击搜索后跳转至分类/搜索页，在搜索模式下调用/api/products/search接口（匹配商品名称、副标题和描述字段），隐藏分类侧边栏，导航栏显示"搜索: 关键词"。

(3) 商品分类页：左侧一级分类侧边栏（Vant Sidebar）+ 右侧二级分类Tab + 商品网格。商品网格使用CSS Grid响应式布局：小屏(<500px)3列、中屏(500-900px)4列、大屏(>900px)5列。每个商品卡片为正方形（aspect-ratio:1），展示商品图片（含占位图回退）、名称（最多两行截断）、价格和销量。

(4) 商品详情页（双端布局）：移动端布局包括顶部商品图片Vant Swipe轮播、红色大字价格区（¥价格+原价划线+已售/库存/评分元数据行）、服务承诺条（正品保障/7天退换/极速发货）、规格选择入口（点击弹出底部Popup来选择颜色/尺码等SKU，价格联动切换）、商品详情HTML内容区和用户评价列表区（展示最近5条评价，含评分/Vant Rate星级/回复）。底部固定Vant ActionBar操作栏（客服/收藏/购物车/加入购物车/立即购买）。PC端布局：左侧450px商品图片区（主图+底部60px缩略图列表，点击缩略图切换主图），右侧为商品信息区——商品标题（20px加粗）→副标题（红色促销语）→粉色价格区块（红色大号价格¥+原价划线+红色折扣百分比标签）→销量/库存/评分行→规格标签选择器→数量调节（Vant Stepper, min=1, max=库存）→大按钮操作区（加入购物车160px宽48px高/立即购买160px宽48px高/收藏/客服按钮并排）→服务承诺条。下方为Tab切换区（商品详情/商品评价），默认展示详情HTML富文本内容。

(5) 购物车模块：购物车状态由Pinia cartStore集中管理，数据同步存储到localStorage实现离线可用。购物车页面展示商品列表（图片/名称/价格/数量Stepper/删除），支持全选/取消全选（Vant Checkbox）、单个数量修改、删除商品。底部Vant SubmitBar实时计算已选中商品的总金额（filter+reduce计算），点击"提交订单"跳转结算页（Checkout独立页面）。

(6) 订单模块：结算页展示三部分：(a)收货地址选择区——显示默认地址或已选地址，支持切换已有地址和新增地址（提供GPS定位按钮自动填充省市区）；(b)商品清单——展示每件商品的图片、名称、规格、单价、数量和金额小计；(c)订单备注输入框和合计金额展示。用户确认后点击"提交订单"，调用/api/user/orders接口——后端使用Hutool的雪花算法（IdUtil.getSnowflakeNextIdStr()）生成唯一订单号，遍历购物车商品计算totalAmount，批量保存order_item明细。返回订单号和金额，前端弹出模拟支付弹窗（微信/支付宝单选），确认支付后订单状态更新为"待发货"。订单列表页使用Vant Tabs按状态分类（全部/待付款/待发货/待收货/已完成），切换Tab时调用resetAndLoad清空列表并重新请求对应状态的数据。每个订单卡片展示订单号、金额、时间和状态标签，以及对应操作按钮（查看详情/取消订单/去支付/确认收货）。订单详情页展示订单基本信息（Descriptions组件）和商品明细表格。

(7) 收货地址模块：地址列表使用Vant AddressList组件展示，自带添加和编辑入口。新增/编辑地址时在表单上方提供"快速获取位置"按钮——调用navigator.geolocation.getCurrentPosition()获取GPS坐标（超时8秒、低精度模式），通过OpenStreetMap Nominatim API逆地理编码（含User-Agent请求头）解析地址并自动填充省市区字段。若GPS权限被拒绝（err.code===1）或超时（err.code===3），自动回退到IP定位方案（ipapi.co API，若失败再回退api.ip.sb API），最终全部失败则提示手动输入。支持设置默认地址。

(8) 收藏模块：用户在商品详情页点击收藏按钮（实心/空心五角星切换），调用/api/user/favorites/{productId}添加收藏。收藏列表页展示已收藏商品的图片、名称和价格，点击跳转商品详情，支持取消收藏（即时从列表移除，不刷新页面）。

(9) AI客服模块：本模块为系统创新功能。每次进入客服页面自动清空聊天历史，显示AI欢迎语"您好，工号26793小琉为您服务，请问有什么需要帮助的吗？"以及5个快捷问题标签（"推荐一款手机"、"有什么好用的护肤品"、"最近有什么新品"、"包邮吗"、"如何退货"）。用户发送消息后，后端处理流程：保存用户消息→提取关键词（正则移除标点后分词）→扩展关联词（从11个品类的HashMap映射表中查找并展开关联关键词集合）→使用LambdaQueryWrapper构建动态SQL（eq(status=1) + gt(stock>0) + or.like(name/subtitle/description)多个关键词）→按销量降序取前10条→取最近10条对话历史→构建含商品列表的System Prompt并调用DeepSeek API→解析返回内容中的[RECOMMEND]标记→组装商品卡片数据→保存AI回复→返回给前端。前端收到回复后先清理残留Markdown符号（*#_~等）和RECOMMEND标记文本，在聊天流中渲染文字气泡和商品推荐卡片（展示图片/名称/价格/跳转箭头）。

3.2.2 管理员功能需求

(1) 仪表盘：登录后首页展示四个统计卡片（用户总数/商品总数/订单总数/销售总额，使用Element Plus Icon彩色图标圆角背景），下方左右各一个卡片区域——左侧展示最近10条订单表格，右侧展示热销商品Top10表格。数据从/api/admin/dashboard/stats接口获取。

(2) 用户管理：用户分页列表（支持keyword/role/status筛选搜索），每行显示ID/用户名/昵称/手机号/邮箱/角色标签/状态标签/创建时间。操作按钮：编辑（弹出Dialog表单，包含所有用户字段及头像上传）、删除（确认弹窗提示）。新增用户时密码通过BCrypt加密存储。支持导出Excel——调用/api/admin/users/export接口，后端查询全部匹配用户通过Apache POI SXSSFWorkbook生成xlsx文件流返回。

(3) 分类管理：独立管理页面，一级分类表格带展开功能（el-table type="expand"），展开后显示该分类下二级分类的子表格。每个分类支持名称/排序/状态的编辑和删除（删除前确认：提示将级联删除子分类和商品）。

(4) 商品管理：三级展开式导航（一级分类→查看子分类→二级分类→查看商品→商品列表）。商品列表支持keyword/status筛选搜索，每行显示ID/名称/主图缩略图/价格/库存/销量/状态标签。操作：编辑（Dialog表单含所有商品字段，支持多张详情图片上传和管理）、删除、导出Excel。新增商品时自动关联当前二级分类的categoryId。

(5) 订单管理：订单分页列表（支持orderNo/status筛选），每行显示ID/订单号/金额/支付方式/状态标签/创建时间。操作：查看（调用/api/admin/orders/{id}获取订单信息和order_items明细列表，在Dialog中以Descriptions展示基本信息、以表格展示商品明细）、发货（status=1时显示，调用/api/admin/orders/{id}/ship更新status=2并记录delivery_time）。支持导出Excel。

(6) Banner管理：轮播图CRUD列表。新增/编辑时通过Dialog表单设置标题、上传图片（调用uploadApi.uploadImage接口，后端保存到uploads目录并返回URL）、链接类型（商品/分类/URL）、链接目标、位置（如home）、排序、有效期（开始/结束时间，NULL表示不限）、状态（启用/禁用）。

(7) 评价管理：评价分页列表（支持productId/status筛选），每行显示ID/商品ID/用户ID/Vant Rate评分星级/评价内容/回复内容/状态标签/时间。操作：回复（Dialog弹窗输入回复文本）、隐藏/显示（切换status）、删除。

(8) 客服消息管理：展示未读用户消息列表，点击用户可查看该用户的完整对话记录。管理员可通过文本输入框手动回复（若非AI自动回复场景）。

(9) 系统设置：修改密码功能（输入原密码和新密码，通过/api/user/profile接口更新）、系统关于信息展示（技术栈版本号）。

3.3 性能要求

响应时间：并发用户数小于50时，页面首次加载时间≤3秒，核心数据查询接口（如商品分页列表）响应时间≤1秒，数据写入接口（如提交订单）响应时间≤2秒。AI客服模块在DeepSeek API正常时响应时间3-5秒，API失败时1秒内返回降级回复。并发能力：系统应能支持至少50个并发用户同时浏览搜索操作。数据准确性：涉及金额、库存的计算必须绝对准确。系统可用性：核心服务可用性≥99.5%，AI客服模块在API失败时自动降级，部分模块故障不应导致整个系统崩溃。多终端兼容性：移动端适配Chrome/Safari/微信内置浏览器，PC端适配Chrome/Edge/Firefox（≥1024px分辨率），鸿蒙端支持HarmonyOS 4.0+。
'@)

Write-Section "四、作品设计" @'
4.1 总体设计

根据需求分析，系统采用前后端分离的架构进行总体设计，分为前端展示层（Web端+鸿蒙端）和后端服务层，前后端通过HTTP/JSON协议进行RESTful通信。

前端展示层进一步拆分为三个子端：(1) 移动端用户商城（C端）——Vue3+Vant 4 UI框架，聚焦触摸操作和移动端视觉体验，核心页面包括首页、分类/搜索页、商品详情页（移动端布局）、购物车、结算页、订单页、地址管理、收藏、个人中心、AI客服、登录注册；(2) PC端管理后台（B端）——Vue3+Element Plus，聚焦数据管理和桌面端交互效率，核心页面包括仪表盘、用户管理、分类管理、商品管理（三级导航）、订单管理（含发货和明细查看）、Banner管理（含图片上传）、评价管理（含回复）、客服消息、系统设置；(3) HarmonyOS鸿蒙端——ArkTS+WebView，加载已部署的Web端页面，作为原生外壳提供鸿蒙生态入口。

后端服务层基于Spring Boot 3.2.5构建，分为以下几个核心模块：(1) 认证授权模块——Spring Security+JWT过滤器+@PreAuthorize注解，负责用户登录/注册/Token生成与验证/权限控制；(2) 业务模块——19个Controller覆盖全部功能（5个客户端Controller、9个管理端Controller、5个公共Controller），11个Service+Impl业务逻辑实现；(3) AI服务模块——DeepSeekService负责调用DeepSeek API，含提示词构建、关联词映射、推荐解析和降级策略；(4) 文件上传模块——FileUploadController接收文件、UUID命名、按日期分目录存储、WebMvcConfigurer映射URL路径；(5) 数据导出模块——3个Controller的export方法使用Apache POI生成Excel；(6) 全局配置模块——CORS跨域配置、Druid连接池监控面板、Knife4j API文档、SPA路由404回退处理器（SpaErrorController）。

[图片占位：系统整体架构图]

4.2 详细设计

4.2.1 用户端核心流程设计

用户端核心购物流程：首页浏览→搜索/分类筛选→点击商品进入详情→选择规格和数量→加入购物车→购物车中勾选商品→结算页确认地址和商品→提交订单→模拟支付→订单状态流转→收货评价。

4.2.2 管理端模块设计

管理端采用深色侧边栏+白色顶栏+浅灰内容区的经典后台布局。侧边栏（el-aside）使用深色背景#1e293b，顶部Logo区使用蓝色渐变。顶栏（el-header）展示面包屑导航和用户下拉菜单（头像+昵称+设置/退出）。内容区（el-main）使用浅灰色背景#f0f2f5，所有管理页面使用el-card作为内容容器，统一24px内边距。针对移动端访问场景，在App.vue中通过全局CSS媒体查询实现了全站响应式适配（≤768px：表格字体12px、按钮padding缩小、分页居中、Dialog弹窗宽度92%、Drawer侧边栏宽度220px、表单inline-item间距缩小、ElMain内边距10px），确保管理员在手机端也能查看和管理数据。

4.3 数据库设计

本系统共设计12张核心数据表，全面覆盖电商平台的数据需求。所有表均设置deleted字段（默认0）并使用MyBatis-Plus @TableLogic注解实现逻辑删除（delete操作自动转为UPDATE deleted=1，查询自动过滤deleted=1的数据）。以下列出主要数据表的结构设计。

表 4-1 用户表（user）

| 字段名 | 数据类型 | 长度 | 主键 | 可空 | 说明 |
|--------|---------|------|------|------|------|
| id | bigint | 20 | 是 | 否 | 用户ID，自增 |
| username | varchar | 50 | 否 | 否 | 用户名，唯一索引 |
| password | varchar | 100 | 否 | 否 | BCrypt加密后的密码 |
| nickname | varchar | 50 | 否 | 是 | 用户昵称 |
| phone | varchar | 20 | 否 | 是 | 手机号 |
| email | varchar | 100 | 否 | 是 | 邮箱 |
| avatar | longtext | - | 否 | 是 | 头像(base64/URL) |
| gender | tinyint | 1 | 否 | 是 | 性别(0女1男) |
| birthday | date | - | 否 | 是 | 生日 |
| role | tinyint | 1 | 否 | 否 | 角色(0用户1管理员) |
| status | tinyint | 1 | 否 | 否 | 状态(0禁用1正常) |
| last_login_time | datetime | - | 否 | 是 | 最后登录时间 |
| create_time | datetime | - | 否 | 否 | 创建时间 |
| update_time | datetime | - | 否 | 是 | 更新时间 |
| deleted | tinyint | 1 | 否 | 否 | 逻辑删除 |

表 4-2 商品表（product）

| 字段名 | 数据类型 | 长度 | 主键 | 可空 | 说明 |
|--------|---------|------|------|------|------|
| id | bigint | 20 | 是 | 否 | 商品ID，自增 |
| category_id | bigint | 20 | 否 | 否 | 所属二级分类ID |
| name | varchar | 100 | 否 | 否 | 商品名称 |
| subtitle | varchar | 200 | 否 | 是 | 副标题/卖点 |
| main_image | longtext | - | 否 | 是 | 主图URL |
| detail_images | longtext | - | 否 | 是 | 详情图片(JSON数组) |
| price | decimal | 10,2 | 否 | 否 | 售价 |
| original_price | decimal | 10,2 | 否 | 是 | 原价(划线价) |
| stock | int | 11 | 否 | 否 | 库存数量 |
| sales | int | 11 | 否 | 是 | 累计销量 |
| description | varchar | 500 | 否 | 是 | 商品简要描述 |
| detail_content | text | - | 否 | 是 | 详情内容(HTML) |
| status | tinyint | 1 | 否 | 否 | 0下架 1上架 |
| is_hot | tinyint | 1 | 否 | 是 | 是否热销 |
| is_new | tinyint | 1 | 否 | 是 | 是否新品 |
| sort_order | int | 11 | 否 | 是 | 排序(越大越前) |
| create_time | datetime | - | 否 | 否 | 创建时间 |
| update_time | datetime | - | 否 | 是 | 更新时间 |
| deleted | tinyint | 1 | 否 | 否 | 逻辑删除 |

表 4-3 分类表（category）

| 字段名 | 数据类型 | 长度 | 主键 | 可空 | 说明 |
|--------|---------|------|------|------|------|
| id | bigint | 20 | 是 | 否 | 分类ID，自增 |
| parent_id | bigint | 20 | 否 | 否 | 父分类ID(0=一级) |
| name | varchar | 50 | 否 | 否 | 分类名称 |
| icon | longtext | - | 否 | 是 | 分类图标(base64) |
| sort_order | int | 11 | 否 | 是 | 排序 |
| status | tinyint | 1 | 否 | 否 | 0禁用 1启用 |
| create_time | datetime | - | 否 | 否 | 创建时间 |
| update_time | datetime | - | 否 | 是 | 更新时间 |
| deleted | tinyint | 1 | 否 | 否 | 逻辑删除 |

表 4-4 订单表（order_info）

| 字段名 | 数据类型 | 长度 | 主键 | 可空 | 说明 |
|--------|---------|------|------|------|------|
| id | bigint | 20 | 是 | 否 | 订单ID，自增 |
| order_no | varchar | 50 | 否 | 否 | 订单号(雪花算法) |
| user_id | bigint | 20 | 否 | 否 | 用户ID |
| address_id | bigint | 20 | 否 | 是 | 收货地址ID |
| total_amount | decimal | 10,2 | 否 | 否 | 订单总金额 |
| pay_amount | decimal | 10,2 | 否 | 是 | 实付金额 |
| freight | decimal | 10,2 | 否 | 是 | 运费 |
| pay_type | tinyint | 1 | 否 | 是 | 1微信 2支付宝 |
| pay_time | datetime | - | 否 | 是 | 支付时间 |
| delivery_time | datetime | - | 否 | 是 | 发货时间 |
| receive_time | datetime | - | 否 | 是 | 收货时间 |
| status | tinyint | 1 | 否 | 否 | 0待付款1待发货2待收货3已完成4已取消 |
| remark | varchar | 255 | 否 | 是 | 订单备注 |
| create_time | datetime | - | 否 | 否 | 创建时间 |
| update_time | datetime | - | 否 | 是 | 更新时间 |
| deleted | tinyint | 1 | 否 | 否 | 逻辑删除 |

表 4-5 订单明细表（order_item）

| 字段名 | 数据类型 | 长度 | 主键 | 可空 | 说明 |
|--------|---------|------|------|------|------|
| id | bigint | 20 | 是 | 否 | 明细ID，自增 |
| order_id | bigint | 20 | 否 | 否 | 订单ID |
| product_id | bigint | 20 | 否 | 否 | 商品ID |
| spec_id | bigint | 20 | 否 | 是 | 规格ID |
| product_name | varchar | 100 | 否 | 否 | 商品名称快照 |
| product_image | longtext | - | 否 | 是 | 商品图片快照 |
| spec_info | varchar | 200 | 否 | 是 | 规格信息快照 |
| price | decimal | 10,2 | 否 | 否 | 下单时单价 |
| quantity | int | 11 | 否 | 否 | 数量 |
| total_price | decimal | 10,2 | 否 | 否 | 小计金额 |
| create_time | datetime | - | 否 | 否 | 创建时间 |

表 4-6 其余业务表汇总

| 表名 | 核心字段 | 说明 |
|------|---------|------|
| product_spec | product_id, spec_name, spec_value, price, stock, image | 商品规格SKU(颜色/尺码等) |
| address | user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default | 收货地址 |
| banner | title, image, link_type, link_target, sort_order, status, position, start_time, end_time | 首页轮播图 |
| favorite | user_id, product_id, create_time | 用户收藏 |
| review | product_id, user_id, order_id, rating, content, images, reply, reply_time, is_anonymous, status | 商品评价+回复 |
| cart | user_id, product_id, spec_id, quantity, selected | 购物车(服务端) |
| customer_service | user_id, admin_id, content, image, sender_type, is_read, create_time | 客服消息 |

[图片占位：数据库ER图]
'@)

Write-Section "五、功能实现" @'
5.1 用户端功能实现

5.1.1 用户登录/注册功能实现

用户端登录页和注册页使用Vant的Form组件进行表单收集和前端校验。用户填写表单后点击提交，前端调用/api/auth/login接口（或/api/auth/register注册接口）。请求成功后，后端返回JWT Token和用户基本信息。前端将Token和用户信息存储到Pinia Store（同时持久化到localStorage实现刷新不丢失登录状态）。注册时密码通过Spring Security的BCryptPasswordEncoder加密后存储。

核心代码如下：

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    const res = await request.post('/api/auth/login', form.value)
    userStore.setToken(res.token)
    userStore.setUserInfo(res.user)
    showToast('登录成功')
    const redirect = route.query.redirect || '/client/home'
    router.push(redirect)
  } catch (error) {
    showToast(error.message || '登录失败')
  }
}

5.1.2 首页功能实现

首页集成了搜索栏、Banner轮播、分类导航入口、热门商品推荐和新品推荐。页面加载时使用Promise.all并行请求四个接口（分类列表/api/admin/categories/first、热门商品/api/products/hot、新品/api/products/new、Banner列表/api/banners?position=home），提高页面加载速度。Banner图片通过getImageUrl工具函数处理URL引用，若图片为空则使用SVG渐变占位图（getPlaceholder函数生成，包含商品名的首字和QJ商城文字，12种配色方案轮换）。搜索功能调用/api/products/search接口，支持模糊匹配商品名/副标题/描述字段，按销量降序排列。

[图片占位：首页完整截图]

5.1.3 商品详情双端布局实现

商品详情页是系统中技术复杂度最高的页面之一。通过设备检测策略（useResponsive composable）判断当前设备类型，渲染不同的模板分支：

移动端模板（v-if="isMobile"）：顶部Vant Swipe轮播→红色大字价格信息区→规格选择入口→详情描述→评价列表→底部固定ActionBar操作栏。

PC端模板（v-else）：左侧450px大图+缩略图切换→右侧商品信息区（京东风格红色价签+粉色价格区块+折扣标签+规格标签选择器+数量Stepper+大按钮区）→下方详情/评价Tab切换。

v-for遍历规格列表，点击后通过selectedSpec响应式变量联动更新显示价格。

5.1.4 AI客服与商品推荐实现

AI客服模块是本系统的创新亮点。技术实现分为后端和前端两部分：

后端DeepSeekService：通过Spring RestTemplate调用DeepSeek API。系统提示词(System Prompt)动态构建：提示词要求AI扮演QJ商城客服助手"小Q"，禁止使用Markdown符号，要求口语化回复。当有匹配商品时（关联词扩展搜索数据库返回的商品列表），在提示词中嵌入商品信息（ID/名称/价格/描述），要求AI从列表中挑选1-3个最相关的商品推荐，并在回复末尾标记[RECOMMEND]id1,id2,id3[/RECOMMEND]。关联词映射表(HashMap)覆盖11个品类——饮料/手机/电脑/耳机/零食/衣服/鞋/护肤/家电/酒/手表，共映射60+个DB搜索关键词。

前端CustomerService.vue：onMounted时清空messages数组并设置welcomeMsg。sendMessage方法发送消息后，接收AI回复→清理Markdown符号（regex: /[*#_~>-]/g）→删除[RECOMMEND]标记文本→渲染消息气泡和商品推荐卡片组件。商品卡片展示图片(56x44)、名称、价格和跳转箭头，点击调用goProduct(id)跳转商品详情。

[图片占位：AI客服对话界面截图]

5.1.5 收货地址GPS定位

getLocation()方法先尝试GPS定位，失败后自动回退IP定位：

async function getLocation() {
  if (!navigator.geolocation) { ipFallback(); return }
  navigator.geolocation.getCurrentPosition(
    async (pos) => {
      const addr = await fetchAddr(pos.coords.latitude, pos.coords.longitude)
        || await fetchAddrFallback(pos.coords.latitude, pos.coords.longitude)
      fillAddress(addr)
    },
    (err) => { ipFallback() },  // PERMISSION_DENIED等错误自动回退IP
    { timeout: 8000, enableHighAccuracy: false }
  )
}

5.1.6 设备响应式检测

const ua = navigator.userAgent || ''
const isMobileUA = /Android|iPhone|iPad|iPod|Mobile/i.test(ua)
const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0

function detectMobile() {
  if (isMobileUA) return true
  if (hasTouch && window.innerWidth < 1024) return true
  if (!hasTouch && window.innerWidth < 600) return true
  return false
}

5.2 管理端功能实现

管理端基于Vue3+Element Plus开发，采用深色侧边栏+白色顶栏+浅灰色内容区的经典管理后台布局。全局CSS变量统一配色方案，所有管理页使用el-card组件包裹。移动端全局响应式（≤768px：字体缩小、按钮压缩、弹窗占屏92%、Drawer侧边栏220px、分页居中）。

[图片占位：管理端仪表盘截图]
[图片占位：管理端商品管理截图]
[图片占位：管理端订单管理截图]
[图片占位：管理端Banner管理截图]

5.3 HarmonyOS鸿蒙端实现

基于ArkTS语言和ArkUI框架，使用@kit.ArkWeb中的Web组件加载已部署在阿里云服务器上的Web端页面（http://47.100.214.45:8080/）。配置domStorageAccess(true)和javaScriptAccess(true)以支持完整的Web应用交互体验，配置mixedMode(MixedMode.All)允许加载混合内容。

ArkTS核心代码（Index.ets）：

import { webview } from '@kit.ArkWeb'

@Entry
@Component
struct Index {
  build() {
    Column() {
      Web({ src: 'http://47.100.214.45:8080/',
            controller: new webview.WebviewController() })
        .width('100%')
        .height('100%')
        .domStorageAccess(true)
        .javaScriptAccess(true)
        .mixedMode(MixedMode.All)
    }
    .width('100%')
    .height('100%')
  }
}

[图片占位：DevEco Studio鸿蒙端运行截图]
'@)

Write-Section "六、作品测试" @'
6.1 测试环境

| 项目 | 说明 |
|------|------|
| 服务器 | 阿里云ECS，CentOS 7.9，2核4G |
| 数据库 | MySQL 8.0，数据库qj_shop，40件商品/12条订单/8名用户 |
| 浏览器 | Chrome 120+、Edge 120+、Safari 17+ |
| 移动设备 | iPhone 14 Pro (390x844) / Android Chrome (412x915) |
| PC分辨率 | 1920×1080、1366×768 |
| 鸿蒙设备 | DevEco Studio模拟器 HarmonyOS 4.0 |

6.2 测试方法

采用黑盒测试方法，按照测试用例逐项验证系统的功能完整性和兼容性。测试范围覆盖用户端全部10个功能模块和管理端全部9个功能模块。

6.3 测试用例

表6-1 用户端功能测试用例

| 编号 | 测试模块 | 测试用例 | 预期结果 | 结论 |
|------|---------|---------|---------|------|
| UC01 | 用户注册 | 输入合法用户名/密码/手机号 | 注册成功跳转登录 | 通过 |
| UC02 | 用户注册 | 输入已存在用户名 | 提示用户名已存在 | 通过 |
| UC03 | 用户登录 | 输入正确账号密码 | 登录成功跳转首页 | 通过 |
| UC04 | 用户登录 | 输入错误密码 | 提示用户名或密码错误 | 通过 |
| UC05 | 商品浏览 | 首页加载 | Banner/分类/热门/新品正常 | 通过 |
| UC06 | 商品搜索 | 输入"手机"关键词 | 返回匹配商品列表 | 通过 |
| UC07 | 商品详情 | 移动端打开商品 | 轮播+价格+底部操作栏 | 通过 |
| UC08 | 商品详情 | PC端打开商品 | 京东风格双栏布局 | 通过 |
| UC09 | 商品详情 | 无图商品 | 显示SVG渐变占位图 | 通过 |
| UC10 | 规格选择 | 选择不同颜色 | 价格联动切换 | 通过 |
| UC11 | 购物车 | 加入购物车 | 角标数量增加 | 通过 |
| UC12 | 购物车 | 修改数量/删除/全选 | 金额实时更新 | 通过 |
| UC13 | 收货地址 | GPS定位获取 | 自动填充省市区 | 通过 |
| UC14 | 收货地址 | 编辑已有地址 | 弹出编辑表单，数据正确 | 通过 |
| UC15 | 下单 | 选择商品提交订单 | 生成订单状态待付款 | 通过 |
| UC16 | 支付 | 模拟微信支付 | 订单状态变为待发货 | 通过 |
| UC17 | 订单列表 | 切换Tab（待发货） | 只显示待发货订单 | 通过 |
| UC18 | 订单详情 | 查看订单明细 | 展示商品列表和价格 | 通过 |
| UC19 | 取消订单 | 取消待付款订单 | 状态变为已取消 | 通过 |
| UC20 | 个人中心 | 查看订单统计 | 各状态数量正确 | 通过 |
| UC21 | 头像上传 | 摄像头拍照换头像 | 调用相机成功获取 | 通过 |
| UC22 | AI客服 | 发送"我想要饮料" | AI推荐元气森林等商品 | 通过 |
| UC23 | AI客服 | 发送普通问候 | AI口语化回复无符号 | 通过 |
| UC24 | 收藏 | 收藏/取消收藏 | 状态正确切换 | 通过 |

表6-2 管理端功能测试用例

| 编号 | 测试模块 | 测试用例 | 预期结果 | 结论 |
|------|---------|---------|---------|------|
| AC01 | 管理员登录 | 普通用户登录后台 | 提示无权访问 | 通过 |
| AC02 | 仪表盘 | 查看统计卡片 | 用户/商品/订单/销售额正确 | 通过 |
| AC03 | 用户管理 | 禁用用户 | 用户状态变为禁用 | 通过 |
| AC04 | 用户管理 | 导出Excel | 下载xlsx文件数据完整 | 通过 |
| AC05 | 分类管理 | 新增/编辑分类 | 分类数据正确更新 | 通过 |
| AC06 | 商品管理 | 新增商品 | 商品显示在前台 | 通过 |
| AC07 | 商品管理 | 删除商品 | 确认弹窗后删除 | 通过 |
| AC08 | 订单管理 | 点击发货 | 订单状态变为待收货 | 通过 |
| AC09 | 订单管理 | 查看详情 | 弹窗展示商品明细表 | 通过 |
| AC10 | Banner管理 | 上传图片新增Banner | 首页轮播正常显示 | 通过 |
| AC11 | 评价管理 | 回复评价 | 回复内容正常保存 | 通过 |
| AC12 | 评价管理 | 隐藏评价 | 前台不再显示 | 通过 |
| AC13 | 移动端管理 | 手机访问后台 | 表格/按钮响应式适配 | 通过 |

6.4 测试总结

总计执行37个测试用例，通过率100%。系统各项功能运行稳定，用户端在移动端和PC端均可正常显示和操作，设备检测准确。AI客服模块在DeepSeek API正常时可在3-5秒内返回智能回复和商品推荐，在API异常时可在1秒内降级为预设回复。管理端数据操作准确，导出Excel格式正确。移动端后台响应式适配可在手机上完成基本的管理操作。系统响应时间均在合理范围内。
'@)

Write-Section "七、设计总结" @'
7.1 实现过程

本次毕业设计严格遵循软件工程标准流程：需求分析→系统设计→编码实现→系统测试→部署上线。

第一阶段（需求分析）：对电商平台进行详细需求分析，明确用户端10个功能模块和管理端9个功能模块划分，确定技术选型。

第二阶段（系统设计）：设计12张数据库表结构及ER关系图，绘制系统整体架构图，完成前后端项目初始化和框架搭建，配置Spring Boot、MyBatis-Plus、Spring Security等核心框架。

第三阶段（编码实现）：采用前后端分离模式并行开发——后端按Service→Mapper→Controller层次开发19个Controller和11个Service实现类；前端开发客户端15个Vue页面和管理端12个Vue页面。创新功能包括集成DeepSeek AI对话、GPS/IP定位、SVG占位图生成、关联词映射搜索等。

第四阶段（系统测试）：进行4轮前后端联调测试，发现并修复了Vant 4组件名变更、响应式检测误判、Vite代理路径匹配、MySQL Socket连接、SPA路由404、文件上传boundary等7个主要技术问题。执行37个测试用例，全部通过。

第五阶段（部署上线）：将项目部署到阿里云ECS服务器，安装MySQL 8.0并执行数据库初始化脚本，配置安全组开放8080端口，通过nohup java -jar实现后台运行。同时开发HarmonyOS鸿蒙端ArkTS应用。

7.2 遇到的问题与收获

问题1：Vant 4组件名变更。Vant 4版本将van-goods-action系列组件改名为van-action-bar，导致ProductDetail页面组件解析失败。通过查阅Vant官方文档的CHANGELOG和Migration Guide，找到组件对照关系并逐一替换。

问题2：高分辨率手机响应式误判。部分Android旗舰机的CSS像素宽度达1080px，仅靠window.innerWidth判断会导致显示PC布局。解决方案是增加UA检测（/Android|iPhone|iPad/i）和触摸屏检测（ontouchstart/maxTouchPoints），形成三维判定策略。

问题3：Vite代理rewrite路径问题。Vite开发环境的proxy配置中使用rewrite: (path) => path.replace(/^\/api/, '')去除了/api前缀，后端Controller的@RequestMapping("/api/...")无法匹配。解决方案是移除rewrite规则，同时移除axios baseURL避免双重前缀，所有API路径统一使用完整/api/...格式。

问题4：Banner时间过滤SQL问题。查询条件startTime <= now在startTime为NULL时发生NULL比较（NULL <= value结果为NULL非TRUE），导致全部5条Banner被过滤。解决方案是在LambdaQueryWrapper中添加.isNull(Banner::getStartTime).or()判断。

问题5：Linux MySQL Socket连接失败。服务器上JDBC URL使用localhost解析为Unix Socket，而MySQL Socket文件不在默认路径/tmp/mysql.sock。解决方案是将JDBC URL改为jdbc:mysql://127.0.0.1:3306/强制TCP连接。

问题6：SPA路由服务端404问题。Vue Router History模式下，直接访问/admin等前端路由时Spring找不到对应资源返回404。解决方案是实现ErrorController接口的SpaErrorController，拦截非/api/路径的404请求，返回forward:/index.html让Vue Router接管。

问题7：文件上传Content-Type边界问题。FormData上传时手动设置Content-Type: multipart/form-data缺少boundary参数，服务端无法解析。解决方案是移除手动Content-Type设置，让axios自动生成含正确boundary的请求头。

收获：掌握了Spring Boot+Vue 3全栈开发完整流程，深入理解了JWT认证、MyBatis-Plus、响应式布局、AI大模型集成、SPA路由、文件上传等核心技术，提升了工程化排错能力和独立项目开发经验。

7.3 创新点与不足

创新点：(1) 集成DeepSeek大语言模型实现AI智能客服，支持自然语言对话和数据库商品关联推荐（覆盖11个品类60+关键词），探索了RAG简化实现方案；(2) 双端自适应商品详情布局（移动端Vant+PC端京东风格），通过三维设备检测策略解决高分辨率手机误判问题；(3) GPS/IP双重定位自动填充收货地址；(4) 覆盖HarmonyOS鸿蒙端；(5) SVG渐变占位图机制保证无图商品的美观展示。

不足与改进方向：(1) 支付为模拟流程，未来可接入真实支付SDK；(2) AI推荐目前仅支持品类关联词匹配，未来可引入向量数据库实现语义级别的精准推荐；(3) 图片目前使用base64存储（占用数据库空间较大），未来可全部迁移至对象存储（OSS）；(4) 订单物流信息可接入快递100等第三方API实现真实物流跟踪。
'@)

Write-Section "八、参考资料" @'
[1] 王松. Spring Boot+Vue 全栈开发实战[M]. 北京:清华大学出版社, 2023.
[2] 陈恒, 梁艺多. Vue.js 3 企业级项目开发实战[M]. 北京:机械工业出版社, 2024.
[3] 柳伟卫. Spring Security 原理与实战[M]. 北京:北京大学出版社, 2022.
[4] 黄文毅. MyBatis-Plus 从入门到精通[M]. 北京:电子工业出版社, 2023.
[5] 张峰. MySQL 数据库设计与应用(第2版)[M]. 北京:人民邮电出版社, 2024.
[6] 李刚. 疯狂Java讲义(第6版)[M]. 北京:电子工业出版社, 2023.
[7] 尤雨溪. Vue.js 官方文档[EB/OL]. https://vuejs.org/, 2026-03-10.
[8] VMware. Spring Boot Reference Documentation[EB/OL]. https://docs.spring.io/spring-boot/docs/current/reference/html/, 2026-04-01.
[9] 阿里巴巴. Element Plus 官方文档[EB/OL]. https://element-plus.org/, 2026-02-15.
[10] 有赞. Vant 4 官方文档[EB/OL]. https://vant-ui.github.io/vant/, 2026-01-20.
[11] DeepSeek. DeepSeek API Documentation[EB/OL]. https://api-docs.deepseek.com/, 2026-04-10.
[12] 华为. HarmonyOS ArkTS 开发文档[EB/OL]. https://developer.huawei.com/consumer/cn/arkts/, 2026-05-01.
[13] OpenStreetMap. Nominatim API Documentation[EB/OL]. https://nominatim.org/release-docs/latest/api/Reverse/, 2026-04-20.
'@)

# ========== 保存 ==========
$doc.SaveAs([ref]"D:\QJShop\毕业设计成果.docx", [ref]16)  # wdFormatDocumentDefault
$doc.Close()
$word.Quit()
[System.Runtime.InteropServices.Marshal]::ReleaseComObject($selection) | Out-Null
[System.Runtime.InteropServices.Marshal]::ReleaseComObject($doc) | Out-Null
[System.Runtime.InteropServices.Marshal]::ReleaseComObject($word) | Out-Null
Write-Host "Thesis generated successfully!"
