下载了spring源码，以gradle项目构建后，正常会显示build sucess。 
但是spring-aspects模块还会报Aspectj切面相关的错误,问题原因是使用了aspectJ ，而java不识别aspectJ,因此还需要如下步骤操作（已导入idea为例）：
1、下载Aspectj
https://www.eclipse.org/aspectj/downloads.php
2、安装AspectJ
   (1)切换工作目录到 下载的AspectJ 所在目录
   (2)执行 java -jar aspectj-1.9.0.jar
3、为spring-aspect 工程添加Facets属性
   (1)File -> Project Structure -> Facets -> 点击 + 按钮 -> AspectJ -> 选择 spring-aop_main -> 点击OK
   (2)给 spring-aspects_main 以同样的操作也添加Facets属性
4、更改编译器
    IntelliJ IDEA  -> Preferences -> Java Compiler
    1) Use Compiler ： 选Ajc
    2) Path to Ajc Compiler：步骤2.(2)中最终安装的目录
    3) 勾选 Delegate to Javac
具体可参考:https://blog.csdn.net/m0_67394360/article/details/126597374
