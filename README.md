# 多功能计算器

* 此代码是作者出于兴趣爱好自己编写的。转载请注明。

* 作者的邮箱：contactwangpai@163.com

---

分支 M # 版本 ` M.3.1 ` 支持的功能：（以用户的角度）

1. 操作数支持多位运算。一个操作数可以是十位数或者更高位数的数

2. 操作数支持小数点、负数，运算结果支持显示小数、分数。

3. 表达式可以含多个操作数、多个运算符，还可以带括号

4. 对输入表达式提供实时自动语法检查与错误定位，并支持检查后的修改

5. 对输入无误的表达式进行实时自动无损计算，无运算累计误差，并可选显示详细的计算过程

6. 使用 GUI 界面来显示上面的表达式输入、报错显示、运算过程

7. 提供界面按钮以供鼠标点击输入。按钮功能包含击键变色、文本全选、光标左移与右移，选中文本的删除与替换、撤消与重做

8. 提供右键菜单以供撤消、重做、剪切、复制、粘贴、删除、全选

9. 支持键盘输入

10. 提供 Windows 下免安装 JDK 直接运行的 EXE 文件。此 EXE 程序拥有程序名、自制程序图标


---

# 本次更新介绍

<p align="right">——2022年4月24日</p>

本次更新介绍：

&emsp;&emsp;本工程是在本分支的上一版本的基础之上进行的升级。

&emsp;&emsp;相对于版本 `M.3.3`，本版本 `M.3.4` 的改进主要有：

* 使用 log4j2 作为日志，去掉了之前的 log4j 日志。
* 缓解了 JavaFX 中滑条首次出现时不能置底的 Bug。
* 将 Spring 的 XML 配置改为 Java 配置类配置。
* 去除 pom.xml 多余依赖。
* 优化、纠正一些代码、文本等。

---

&emsp;&emsp;本版本的已知不足与预想的改进方案：

* 本版暂无

---

&emsp;&emsp;未来版本可能会实现的功能：

* 提供文本的剪切、粘贴按钮（已提供剪切、粘贴右键菜单与快捷键）
* 提供更高级的运算符运算及语法解析
* 提供安卓环境与 Windows 环境的连接，可通过手机局域网来控制 Windows 应用


---

* 本项目使用的开发环境：
  - JDK 17.0.1 2021-10-19
  - Maven 3.8.3
  - IntelliJ IDEA 2021.3 (Ultimate Edition)

---

* 核心 Java 语言代码文件：

* 程序启动入口 API：

  >  模块 `calculator-entrance` 中的
  >
  >  > 包 `org.wangpai.calculator` 中的
  >  >
  >  > > 类  `CalculatorApplication` 中的
  >  > >
  >  > > > 静态方法 `main`

---

* 总计：8870
* Java 代码：7719
  - 测试代码：1857

* 文本语言代码：1151

  - FXML：201
  - CSS：33
  - XML：917

---

* 程序屏幕输入示例（供复制、自行测试）：`2334.623*6345-234/1234+234*(254-45.242)=`

---

* 一些程序截图片段：（如果图片不能显示，请尝试刷新本页面）

---

![在这里插入图片描述](https://img-blog.csdnimg.cn/ce7d3dfc473842659eadf6f27cddb5c7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA56eY5aKD5aWH5omN,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

---

![在这里插入图片描述](https://img-blog.csdnimg.cn/0f21327293af4e1b8366022c2f627bd8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA56eY5aKD5aWH5omN,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

---

![在这里插入图片描述](https://img-blog.csdnimg.cn/2053b745a9b2444b8c881f0738e21a41.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA56eY5aKD5aWH5omN,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

---
