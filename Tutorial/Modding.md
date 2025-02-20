本文是基于现有教程的一些扩展说明，或许可以称作导读。

阅读之前建议有一定的代码基础，百科上也有关于 java 的一些教程。

C++/python 选手应该也是能上手的。

推荐阅读：

- [dddddsj 的文章](https://www.mcmod.cn/post/3282.html)
- [耿悠博的文章](https://www.mcmod.cn/post/3993.html)
- [一篇旧版本的教程，但很多时候仍然适用](https://neutrino.v2mcdev.com/)
- [一篇旧版本的教程，但很多时候仍然适用](https://teamcovertdragon.github.io/Harbinger/63/)

建议前两篇教程先读一遍，后两篇可以读一点，后面会标注对应部分建议阅读的内容。

# 构建工作区

可以自己下 forge mdk 折腾，不过我们比较懒，所以此处建议是直接安装 IDEA 插件：Minecraft Development。

然后直接在 IDEA 中新建项目，选择 生成器 选项卡中的 Minecraft ：

![](image-1.png)

填写名称，会自动帮你生成其他信息，但也可以自行配置，每个字段的作用可见两位老师的博客。

这里建议把 Mixin 勾选上，之后自己单独添加容易出问题。

然后就可以创建工作区了，此时会自行进行构建，不过建议先停止构建，给 gradle 换个源。

> 换源，就是更换 gradle 相关文件的下载源，因官方文件托管在国外，因此换为国内镜像可以提升构建速度。

找到目录下 `gradle/wrapper/gradle-wrapper.properties`，做以下修改：

```
distributionUrl=https\://services.gradle.org/distributions/gradle-8.8-bin.zip
```

换为

```
distributionUrl=https\://mirrors.aliyun.com/macports/distfiles/gradle/gradle-8.8-bin.zip
```

请注意，此处原为 `gradle-8.8-bin.zip`，若版本号不同，请将 `8.8` 换为对应的版本号。

![](image.png)

现在构建工作区，速度和成功率将会有显著提升。

# 有关 Minecraft & Forge 的一些介绍

事实上笔者也不是很了解，但你需要形成一个大致的认知。

mc 的代码由 java 编写，因而几乎不可能彻底闭源，因为总是能根据反编译得到一份像模像样的东西。

而针对反编译出来的源码，我们需要反混淆使得其更可度，这就是 MCP Mapping，一套猜测反编译出的符号是什么用处的映射表。

而在此基础上有了 Forge 这样的项目，其中 Forge Mod Loader（FML）用于加在 Mod，而 MinecraftForge 则是一个修改了 mc 源代码的项目，它使得 mc 的代码更加合理也更具扩展性。

在 MinecraftForge 的工作中，一个重要的部分是事件系统，原版 mc 中没有事件，因此很多东西写死后，除非修改原类（事实上可以用 Mixin 做到），否则无法完成。

## 事件系统

事件系统的工作模式大概是：

- 一开始，定义一些事件类型，不需要做其它的事。
- 定义一些方法，处理对应的一类事件，将它们注册到监听器的列表里，并指定优先级。
- 某件事情将要发生时，按照优先级调用监听器，然后根据调用后的结果决定是否发生，以及各个相关量的变化。


## 服务端 与 客户端

一个值得注意的部分是 服务端 与 客户端。

自从 mc 加入联机系统以来，所有的游戏都被拆分为 服务端 与 客户端 两部分，就连单人游戏也是如此。

实体的移动、方块破坏 等都是 服务端 控制的，也就是整个游戏世界发生的。

而玩家所看见的，以及一些不影响世界的行为，比如 按键、渲染 这些都是客户端的工作。

之所以介绍这个，是因为我们有时需要在 服务端 和 客户端 间通信，来处理特殊的需求。

比如添加 按下 v 键冲刺，左键空气爆炸 这样的功能，就会涉及到服务端和客户端间的通信。

其中 左键空气爆炸 这个需求甚至需要我们手动完成通信这一步骤，因为原版并没有在左键空气时从客户端向服务端发包（发送信息）。

## Mixin

Mixin 用于往已有的代码中加东西，也就是我们上面提到的修改原类的代码。

如果详细介绍篇幅就过长了，此处推荐阅读 [Fabric 官方给出的 Mixin 教程](https://wiki.fabricmc.net/zh_cn:tutorial:mixin_examples)，这和 forge 是基本通用的。

如果你打不开 fabric wiki，可以读 [这篇知乎专栏](https://zhuanlan.zhihu.com/p/677090570)。

事实上 Mixin 官方也是有教程的，但是 [英文](https://mixin-wiki.readthedocs.io/)。

这里描述一个上述文章内没有提及（wiki 除外）但我遇到的问题的解决方案：

```java
// 在外面定义一个接口
public interface IA {
	void setValue(int value);
	int gettValue();
}

// Mixin 类可以原类没实现的接口。
@Mixin(A.class)
public class Amixin implements IA {
	@Unique
	private int value;
	public void setValue(int value) { this.value = value; }
	public int gettValue() { return value; }
}

// 在其他地方使用 Mixin 类中添加的 value
public class B {
	public void work(A a) {
		return a.value; // 编译错误
		return ((Amixin)a).value; // 编译错误
		return a.getValue(); // 编译错误
		return ((Amixin)a).getValue(); // 编译错误
		return ((IA)a).getValue(); // 正确用法
	}
}
```

# 添加工具

阅读下面的部分前，建议阅读 [Item 和 ItemStack](https://neutrino.v2mcdev.com/item/itemstack.html)。

开发过程中建议去 github 下一份匠魂源码，这样方便学习。

## 匠魂本身的物品

所有种类的匠魂 工具（盔甲、手杖） 都是 ModifiableItem 或其某个子类的实例。

那么我们就需要介绍 ModifiableItem 是什么，跳转定义会发现这是 TieredItem 的一个子类，同时实现了 IModifiableDisplay 接口。

显然 TieredItem 是原版中 Item 的一个子类，大概是某类特殊的物品，而 IModifiableDisplay 接口就意味着这是一件能以匠魂方式修改的工具，或者说实现了 IModifiableDisplay 就可以视作一件匠魂物品。

