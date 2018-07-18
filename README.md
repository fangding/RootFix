## 简介

RootFix是一个在针对Android应用的热更新框架。

### 几个例子

1. 初始化：

```java
    RootFix.initDexPatch(getBaseContext(), "RootFix");
```

以上代码进行了RootFix的初始化过程

2. 热更的加载（重启生效）：

```java
    File fixApkFile = null; //补丁包文件对象
    RootFix.installDexPatch(MainActivity.this, fixApkFile, "RootFix");
```

3. 热更的加载（立即生效生效）：

```java
    String fixApkFilePath = null; //补丁包文件对象
    RootFix.installDexPatchRuntime(MainActivity.this, "RootFix", fixApkFilePath);
```

## 支持情况

目前RootFix 支持 Android 5.0~ 8.1 的 系统；Android O的支持正在计划中。但是，本项目已经在SDK类和App的项目中使用过, 搭配使用我的另外一个开源项目PuddingResReLoader更易于使用,
目前仅仅是个人用途，欢迎给我提 issue :)

PuddingResReLoader：此项目支持res、assets、libs等资源的插件化读取,还在完善和新增功能，尚未开源

Android版本支持情况：

Runtime | Android Version | Support
------  | --------------- | --------
Dalvik  | 2.2             | Yes
Dalvik  | 2.3             | Yes
Dalvik  | 3.0             | Yes
Dalvik  | 4.0-4.4         | Yes
ART     | L (5.0)         | Yes
ART     | L MR1 (5.1)     | Yes
ART     | M (6.0)         | Yes
ART     | N (7.0)         | Yes
ART     | N MR1 (7.1)     | Yes
ART     | O (8.0)         | Yes
ART     | O MR1(8.1)      | Yes

## 已知问题

1. 当前热更粒度级别为class，无法针对method单独更新


## 交流和讨论

欢迎邮件至756224278@qq.com
