# BubbleGame
## 运行环境

- 开发环境：Eclipse + OracleJDK 1.8
- 运行环境：OracleJRE 1.8
- 机器环境：Intel i7-8750H @ 2.20GHz，内存16G

运行前需要确保bubblegame.jar文件、pic文件夹和sound文件夹在同一目录下。
注意，游戏分辨率固定为$ 1000\times800 $，运行前需确保窗口分辨率足够大。

## 基本功能实现

- 游戏基本功能：开始游戏、暂停/继续游戏，结束游戏。
- 游戏生成：游戏地图生成，游戏角色初始位置随机。
- 游戏规则：满足泡泡爆炸连锁规则，道具拾取与使用规则，人物移动与死亡规则(为方便AI实现，仅支持整数格子行走)。
- 难度选择：开始界面可以选择三种难度进行游戏。
- 美观度：使用PS设计UI。
- 用户交互：允许使用键盘和鼠标进行交互。
- 系统流畅性：通过双缓冲技术和多线程编程，在实机测试下无明显停顿，流畅运行。
- 系统稳定性：游戏稳定运行，不发生崩溃事件。
- 实验报告：无。

## 附加功能实现

- AI对战：本游戏每一局支持1个玩家与3个电脑玩家同时对战。电脑AI具有自动躲避炸弹，自动放炸弹，自动拾取道具的功能。使得游戏不再枯燥。
- 背景音乐：游戏增添了背景音乐。
- 简略动画：为了游戏体验的丰富性，游戏中的人物与道具都有最基本的动画变化。

其中，AI的实现思想如下：
通过在每一个时刻，用BFS方法找寻人物可达的格点，并通过给予每个格点打分，来决定下一步的策略。目前的策略优先度是从大到小是：

1. 若在炸弹范围内，逃命
2. 拾取道具，道具优先级分别为：生命药水>炸弹>威力药水
3. 炸箱子
4. 炸人
5. 无所事事

## 使用流程

双击Jar包运行游戏。在游戏界面选择难度。进入游戏过程后，通过WASD或者方向键来控制人物行走，使用空格键来放置炸弹。

若要使用道具，需将鼠标以至屏幕下方的道具处单击(双击)使用。单机左下角暂停键可暂停游戏，并选择返回标题或继续游戏。

当游戏胜利或失败时，切换至相应界面，并可返回标题界面重新开始。