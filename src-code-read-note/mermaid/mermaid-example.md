# 1 方向

```mermaid
graph TD
    Start --> Stop

```
```
graph TD
    Start --> Stop
```
这里的TD指定方向，可选值与其含义有
+ TB / TD：从上到下
+ BT：从下到上
+ RL：从右到左
+ LR：从左到右

# 2 形状
## 2.1 基础形状
普通节点形状，如下
```mermaid
graph LR
		id
```


定义一个节点变量：变量名[填充的文字]，如下
```mermaid
graph LR
	id[This node is a param] --- id2 --- id3 --- id
```
## 2.2 其他形状
```mermaid
graph LR
	shape --- shape2(圆角) --- shape3([这是更圆的圆角]) --- shape4[[外面有两个方框]]
	--- shape5[(类似数据一样的东西)] --- shape6((一个圆)) --- shape7>类似书签一样的内容]

	shape8{菱形} --- shape9{{类似标签一样的东西}} --- shape10[/斜过来的正方形/] --- shape11[\反方向斜的\] --- shape12[/拱型\] --- shape13[\碗一样/]
```
## 2.3 样式
```mermaid
graph LR
    id1(Start)-->id2(Stop)
    style id1 fill:#f9f,stroke:#333,stroke-width:4px
    style id2 fill:#bbf,stroke:#f66,stroke-width:2px,color:#fff,stroke-dasharray: 5 5
```
其中的参数解释：
+ fill：填充颜色
+ stroke：边框颜色
+ stroke-width：边宽
+ stroke-dasharray：交替的长度

或者也可以使用下面的形式来使用样式：
```mermaid
graph LR
		A:::class1 ---> B:::class2
		classDef class1 fill:#f9f,stroke:#333,stroke-width:4px
    classDef class2 fill:#bbf,stroke:#f66,stroke-width:2px,color:#fff,stroke-dasharray: 5 5
```
```mermaid
graph LR
		A:::class1 ---> B:::class2
		classDef class1 fill:#f9f,stroke:#333,stroke-width:4px
    classDef class2 fill:#bbf,stroke:#f66,stroke-width:2px,color:#fff,stroke-dasharray: 5 5
```
# 3 链接
## 3.1 链接种类
```mermaid
graph LR
	link(带箭头的实线) --> link2(没箭头的实线) --- link3(线中有文字)-- 左边两条线右边三条线 ---link4(或者换一种写法)---|这里一定要有内容|结束
```
```mermaid
graph LR
	link(带箭头)-->|这里是内容|link2(或者换一种写法)--这里是内容-->link3(虚线箭头)-.->link4(虚线箭头中加文字)-.这是内容.->link5(粗箭头)==>link6
```
## 3.2 箭头种类
```mermaid
flowchart LR
	A --o B --x C o--o D <--> E x--x F
```
注意，这里不再是graph，而是flowchart。

## 3.3 分支
```mermaid
graph TD
	start --- left & right --- stop
```
```mermaid
graph TD
	Father & Mather --- son & daugher --- 继承权
```
## 3.4 新的单独分支
```mermaid
graph TD
	GrandFather & GrandMother ---> Father;
	Father & Mother ---> you

```
```mermaid
graph TD
	GrandFather & GrandMother ---> Father;
	Father & Mother ---> you
```
## 3.5 新的单独分支
```mermaid
graph TD
	subgraph one
	GrandFather & GrandMother ---> Father;
	end
	subgraph two
	Father & Mother
	end
	subgraph three
	Father & Mother ---> you
	end
```
```mermaid
graph TD
	subgraph one
	end
	subgraph two
	Father & Mother
	end
	subgraph three
	Father & Mother ---> you
	end
```

```mermaid
flowchart TD 
	subgraph Tomcat
    	direction BT
		subgraph Spring容器
			Services & Repositories
		end
    	subgraph Spring子容器
			Controllers & ViewResolver 
		end
		Spring子容器 ---> Spring容器
	end
```
