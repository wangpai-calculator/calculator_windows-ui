package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

import static java.lang.Math.pow;

public class Calculator {
    // 对于Stack定义的栈，其栈底的序号为0，入栈、出栈操作均是在栈顶进行的。
    private Stack<Double> opnds = new Stack<>();// opnds：operand 操作数
    private Stack<Character> opndBuff = new Stack<>();// opndBuff：operand buffer 缓存的操作数的每一位的值
    private Stack<Character> optrs = new Stack<>();// optrs：operator 运算符
    private Stack<Character> inte = new Stack<>();// inte：integration 当前整个表达式的状态
    private Stack<Character> buttons = new Stack<>();// 储存得到定义的按键

    private Queue<Character> aLineInput = new LinkedList<>();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    boolean isRedundantChar(char ch) {
        if (ch == '\n') {
            return true;
        } else {
            return false;
        }
    }

    char getNextChar() throws IOException {
        if (this.aLineInput.size() == 0) {
            char[] aLine = this.input.readLine().toCharArray();
            for (var ch : aLine) {
                aLineInput.add(ch);
            }
        }

        while (isRedundantChar(this.aLineInput.peek())) {
            aLineInput.remove();
        }

        return aLineInput.remove();
    }

    // resetInput：reset input 重置输入
    private void resetInput() {
        // 清空键盘输入流缓存。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
        aLineInput = new LinkedList<Character>();
    }

    // FIXME 以前的工程（C++）中残留的方法，原方法是为了设置cout显示的浮点数的位数
    private int coutset(int n)// 将cout小数点后的输出，输出至n位，并且不使用科学计数法
    {
        /*
        using namespace std;
        cout.precision(n);
        auto oldset = cout.setf(ios_base::fixed, ios_base::floatfield);
        */
        return 0;
    }

    public Calculator() {
        initButton();// 初始化按键
    }

    private void initButton() {
        // 设置数字键
        for (char num = '0'; num <= '9'; ++num) {
            this.buttons.push(num);
        }

        this.buttons.push('=');// 设置等于号

        this.buttons.push('+');// 设置加号
        this.buttons.push('-');// 设置减号
        this.buttons.push('*');// 设置乘号
        this.buttons.push('/');// 设置除号

        this.buttons.push('(');// 设置左小括号
        this.buttons.push(')');// 设置右小括号

        this.buttons.push('.');// 设置小数点符号
    }

    private boolean isDefinedButton(char button) {
        if (this.buttons.search(button) != -1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @return 左右括号相等时，返回1；左括号多于右括号，返回2；左括号小于右括号，返回0
     * @apiNote pareMatch：parentheses match 括号匹配
     * @since 2021年3月7日以前
     */
    private int pareMatch() {
        var antiOptrs = (Stack<Character>) this.optrs.clone();// antiOptrs：anti optr optr的反转
        Collections.reverse(antiOptrs);

        Stack<Character> pares = new Stack<>();

        while (!antiOptrs.empty()) {
            var tempOptr = antiOptrs.pop();

            switch (tempOptr) {
                case '(':
                    pares.push(tempOptr);
                    break;
                case ')':
                    if (!pares.empty() && '(' == pares.peek())
                        pares.pop();
                    else
                        return 0;
                default:
                    break;
            }
        }

        // 如果最后pares为空，说明左右括号相等
        if (pares.empty()) {
            return 1;
        } else {// 如果最后pares不为空，说明左括号数量多于右括号
            return 2;
        }
    }

    /**
     * @apiNote optrLeft：operator left 左边的运算符
     * optrRight：operator right 右边的运算符
     * @since 2021年3月7日以前
     */
    private char precede(char optrLeft, char optrRight) {
        // 注意：==的优先级比&&高，&&的优先级比||高
        // 此区域 if 语句内都只有一条语句，故都不使用大括号
        if (optrLeft == '(' && optrRight == ')')
            return '=';
        else if (optrRight == '.')
            return '<';
        else if (optrLeft == '(' || optrRight == '(')
            return '<';
        else if ((optrLeft == '+' || optrLeft == '-')
                && (optrRight == '*' || optrRight == '/'))
            return '<';
        else
            return '>';
    }

    // 展示整个表达式。对于expression，要求表达式中靠近等于号的部分靠近栈顶
    private void show(final Stack<Character> expression) {
        if (true == expression.empty()) {
            return;
        } else {
            coutset(10);
            System.out.print("\n ");

            // expLen：expression length 表达式的长度
            int expLen = expression.size();
            for (int order = 0; order < expLen; ++order) {
                System.out.print(expression.elementAt(order));
            }
        }
    }

    /**
     * @param delimiter  这里delimiter有默认参数，默认参数为#，当其为其他符号时，意味着可能将输出最后的定界符前置
     * @param expression 对于expression、medResults，要求表达式中靠近等于号的部分靠近栈顶
     * @param medResults 见{@code expression}
     * @apiNote medResults：intermediate result 中间结果
     * @since 2021年3月7日以前
     */
    private void show(final Stack<Character> expression,
                      final Stack<Double> medResults, char delimiter) {
        if (true == expression.empty()) {
            return;
        } else {
            coutset(10);
            System.out.println("");

            int expEnd = expression.size();
            if ('#' != delimiter && delimiter == expression.peek()) {
                System.out.print(delimiter);// expression末尾如果有该定界符，将其前置
                --expEnd;
            }

            int reOrderExp = 0;// reOrderExp：reverse order expression expression的倒数序号
            int reOrderMed = 0;// reOrderMed：reverse order medResults medResults的倒数序号

            // #为转换符
            for (; reOrderExp < expEnd; ++reOrderExp) {
                // 如果遇到转换符，到medResults里找操作数
                if ('#' == expression.elementAt(reOrderExp)) {
                    System.out.print(medResults.elementAt(reOrderMed));
                    ++reOrderMed;
                } else {
                    System.out.print(expression.elementAt(reOrderExp));
                }
            }
        }
    }

    /**
     * 此算法希望尽量不使用链表的方法而使用链栈的方法
     * <p>
     * 将this.opndBuff内的缓存数字转换成操作数压入操作数栈
     *
     * @return 如果实际装载了操作数，返回true
     * @apiNote loadOpnd：load operand 装载操作数
     * @since 2021年3月7日以前
     */
    private boolean loadOpnd() {
        if (true == this.opndBuff.empty()) {
            return false;
        }

        double sum = 0;

        // 得到小数点的位置，这个位置是把栈顶视为起点来计算的，且视栈顶序号为1
        int pointLoc = this.opndBuff.search('.');// pointLoc：point loction

        if (-1 != pointLoc) {
            int fractionLen = pointLoc - 1;
            var fraction = new Stack<Character>();

            // 如果有小数部分，先要反转才能弹出
            for (int digitOrder = 0; digitOrder < fractionLen; ++digitOrder) {
                var topTemp = this.opndBuff.pop();
                fraction.push(topTemp);
            }
            for (int digitBit = 1; !fraction.empty(); ++digitBit) {
                var topTemp = fraction.pop();
                sum = sum + (topTemp - '0') / pow(10, digitBit);
            }
            this.opndBuff.pop();// 小数部分已读取完毕，应去掉缓存里的小数点
        }

        // 整数部分可以不用反转直接弹出
        for (int digit = 1; !this.opndBuff.empty(); ++digit) {
            var topTemp = this.opndBuff.pop();
            sum = sum + (topTemp - '0') * pow(10, digit - 1);
        }

        this.opnds.push(sum);
        return true;
    }


    // 计算两数运算结果，如果aOptr是下面未给出的运算符，那程序运行以后将报弹窗错误
    private double calculate(double opndLeft, char aOptr, double opndRight) {
        switch (aOptr) {
            case '+':
                return opndLeft + opndRight;
            case '-':
                return opndLeft - opndRight;
            case '*':
                return opndLeft * opndRight;
            case '/':
                return opndLeft / opndRight;
        }
        return 0;
    }

    /**
     * 此函数与evalExp()函数中显示表达式的过程还是有区别的，所以不能将它们之中显示的部分提取出来制成一个单独的函数为它们调用。
     * 它们的区别在于：
     * 1.刚开始用户的输入一定全是字符，而后续计算时会产生浮点数。因此各自使用的显示函数将不同
     * 2.刚开始的显示(evalExp)涉及计算过程中，对中间结果异常（如0除的问题）的处理
     * 3.显示过程(showCalProce)需要涉及栈的合并、以及美观上的处理
     * <p>
     * 此函数默认inte里面没有语法错误，而且inte没有被清空
     * 此函数会首先清空opnd、this.opndBuff、optr的内容
     *
     * @apiNote showCalProce：show the calculation process 展示计算过程
     * @since 2021年3月7日以前
     */
    private void showCalProce() {
        if (this.inte.empty()) {
            var ERROR_INFO = "\n表达式不存在，无法展示细节";
            System.out.println(ERROR_INFO);
            return;
        }

        // calAtLeastOnce：cal calculate 计算
        boolean calAtLeastOnce = false;// 如果calAtLeastOnce为true，说明本函数至少执行了一次计算

        var inteTemp = (Stack<Character>) this.inte.clone();
        if ('=' == inteTemp.peek()) {
            inteTemp.pop();
        }
        show(inteTemp);

        var antinte = (Stack<Character>) this.inte.clone();// antinte：anti inte inte的反转。代表待读取的输入表达式
        Collections.reverse(antinte);
        var inteOfShow = new Stack<Character>();

        this.opnds = new Stack<>();// 清空opnd原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
        this.opndBuff = new Stack<>();// 清空this.opndBuff原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
        this.optrs = new Stack<>();// 清空optr原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐

        // chBeingRead：characters being read 正在读取的字符
        var chBeingRead = antinte.pop();

        while (!antinte.empty() || inteOfShow.empty() || '=' != inteOfShow.peek()) {
            boolean needContinue = false;

            // 只循环一次，为了避免使用goto
            for (int order = 1; order <= 1; ++order) {
                // 如果循环对=前面的操作已处理完毕
                if ('=' == chBeingRead && this.optrs.empty()) {
                    if (loadOpnd())
                        inteOfShow.push('#');// '#'为转换符，以后如在inteOfShow遇到此符，说明此处是操作数，需要去别处寻找

                    this.optrs.push(chBeingRead);
                    inteOfShow.push(chBeingRead);
                    // 这是为了跳过下面的input输入，因为能执行这个代码块，
                    // 说明表达式已到尽头，不需要新的输入而直接将“=”入栈能马上跳出该循环
                    needContinue = true;
                    break;
                } else if (Character.isDigit(chBeingRead) || '.' == chBeingRead) {
                    // 如果是数字或小数点的话，入操作数临时栈
                    this.opndBuff.push(chBeingRead);
                } else {
                    if (loadOpnd()) {
                        inteOfShow.push('#');// '#'为转换符，以后如在inteOfShow遇到此符，说明此处是操作数，需要去别处寻找
                    }

                    if (this.optrs.empty()) {
                        this.optrs.push(chBeingRead);
                        inteOfShow.push(chBeingRead);
                        break;// 这是为了跳到最下面（只循环一次的for循环的下面）
                    }

                    var priority = precede(this.optrs.peek(), chBeingRead);
                    if (priority == '<') {
                        this.optrs.push(chBeingRead);
                        inteOfShow.push(chBeingRead);
                        break;
                    } else if (priority == '>') {
                        var aOptr = this.optrs.pop();
                        var opndRight = this.opnds.pop();
                        var opndLeft = this.opnds.pop();

                        for (int i = 1; i <= 3; ++i) {
                            inteOfShow.pop();// 弹出两个操作数、一个运算符，共三个字符
                        }
                        // medResult：intermediate result 中间结果
                        double medResult = calculate(opndLeft, aOptr, opndRight);
                        this.opnds.push(medResult);
                        inteOfShow.push('#');// '#'为转换符，遇到此符，说明此处是操作数，需要去别处寻找

                        // 下面将显示表达式本步运算的相关内容。这需要同时显示前面已经计算的操作inteOfShow、
                        // 当前的运算符chBeingRead、以及待读取的输入表达式antinte

                        // 因为show的传参要求的顺序与inte是一致的，所以这里要对antinte进行倒序
                        var restExp = (Stack<Character>) antinte.clone();// 待读取的输入表达式antinte中剩余的部分
                        restExp.push(chBeingRead);
                        var antiRest = (Stack<Character>) restExp.clone();
                        Collections.reverse(antiRest);

                        var fulExp = (Stack<Character>) inteOfShow.clone();// fulExp：full expression 完整表达式
                        fulExp.addAll((Stack<Character>) antiRest.clone());

                        System.out.println("");
                        show(fulExp, this.opnds, '=');
                        calAtLeastOnce = true;// 这是为了告诉程序，本函数至少执行了一次计算

                        // 这是为了跳过下面的input输入，因为能执行这个代码块，
                        // 说明chBeingRead的优先级比较低，因此尚未入栈（未经过处理），所以不需要新的输入
                        needContinue = true;

                        break;
                    } else if (priority == '=') {// 如果两个括号之间没有其他运算符，就只有一个操作数
                        this.optrs.pop();// 弹出运算符栈的左括号

                        var opndTemp = inteOfShow.peek();

                        // 现在弹出的不是左括号，因为inteOfShow中的左括号离栈顶还有一个操作数。
                        // 而两个括号之间的操作数不是我们想要弹出的，因此要先保存该操作数
                        inteOfShow.pop();

                        inteOfShow.pop();// 弹出左括号
                        inteOfShow.push(opndTemp);// 将前面被迫先暂时弹出的操作数再加入栈中
                        break;
                    } else { // 此代码块不需要做任何事情，只是因为代码编写规范的需要而添加
                    }

                }// else的右括号
            }// 只循环一次的for循环的右括号

            if (needContinue) {
                continue;
            }

            chBeingRead = antinte.pop();
        }// while的右括号

        // calAtLeastOnce不为真，说明很可能用户输入的是一个不需要计算的表达式
        if (!calAtLeastOnce) {
            inteTemp.push('=');// inteTemp是本函数开始出保存inte的一个对象，它的“=”之前被弹出，因此这里把它弹回去
            show(inteTemp, this.opnds, '=');
        }
    }

    // evalExp：evaluate expression 对表达式求值
    public void evalExp() throws IOException {
        var FRONT_MSG = "请输入一个表达式（你可以选择一个一个地输入，也可以选择一次性输完）：\n" +
                "(请注意除号是“/”，而不是“\\”)";
        System.out.println(FRONT_MSG);

        show(this.inte);
        char charEntered = getNextChar();

        // 以=为结束标志
        while (this.optrs.empty() || '=' != this.optrs.peek()) {
            boolean needContinue = false;
            for (int order = 1; order <= 1; ++order) {// 只循环一次，为了避免使用goto
                // 先尽可能处理所有能单独判断的语法错误

                // 此部分代表进行了导致输入流被破坏的输入
                if (false) {// FIXME
                    var ERROR_INFO = "\n你进行了意外的输入，已为你重置";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }

                if (!isDefinedButton(charEntered)) {
                    var ERROR_INFO = "\n你输入了未定义按键（其转化为十进制的int值编码为"
                            + "【" + (int) charEntered + "】"
                            + "），已为你清除";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }
                // 一个操作数里，输入了两个小数点
                if ('.' == charEntered && -1 != this.opndBuff.search('.')) {
                    var ERROR_INFO = "\n你不能在一个数里输入两个【.】，已为你自动删除【.】";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }
                // 如果输入的数字不是小于1的小数，但是其最高位又为0
                if (!this.opndBuff.empty() && '0' == this.opndBuff.peek() && 1 == this.opndBuff.size()
                        && '.' != charEntered) {
                    var ERROR_INFO = "\n你不能一开始就在数中输入【0】，已为你自动删除【0】";
                    System.out.println(ERROR_INFO);

                    this.opndBuff.pop();// 删除一开始输入的0
                    this.inte.pop();
                    resetInput();
                    show(this.inte);
                    break;
                }

                // 如果前面没有第一个操作数，但是后面输入了一个非数字符，报错
                if (('+' == charEntered || '-' == charEntered
                        || '*' == charEntered || '/' == charEntered
                        || '.' == charEntered)
                        && (this.inte.empty() || (!Character.isDigit(this.inte.peek()) && this.inte.peek() != ')'))) {
                    var ERROR_INFO = "\n注意：在输入" + "【" + charEntered + "】" + "前，你必须先输入一个合法的数。"
                            + "已为你自动删除" + "【" + charEntered + "】";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }

                // 连续输入两个非数字符，报错
                if (('+' == charEntered || '-' == charEntered
                        || '*' == charEntered || '/' == charEntered
                        || '=' == charEntered || '.' == charEntered)
                        && !this.inte.empty() && ('+' == this.inte.peek() || '-' == this.inte.peek() || '*' == this.inte.peek()
                        || '/' == this.inte.peek() || '(' == this.inte.peek() || '.' == this.inte.peek())) {
                    var ERROR_INFO = "\n你不能在" + "【" + this.inte.peek() + "】" + "的后面输入"
                            + "【" + charEntered + "】" + "，已为你自动删除"
                            + "【" + charEntered + "】" + "：";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }

                // 如果左括号前面有数字、小数点，报错
                if ('(' == charEntered && !this.inte.empty() && Character.isDigit(this.inte.peek())
                        && '.' == charEntered) {
                    var ERROR_INFO = "\n左括号前面不能有数字，已为你自动删除【(】";
                    System.out.println(ERROR_INFO);

                    resetInput();
                    show(this.inte);
                    break;
                }

                if (')' == charEntered) {
                    // 如果右括号前面有四则运算符，报错
                    if (!this.inte.empty() && (('+' == this.inte.peek() || '-' == this.inte.peek()
                            || '*' == this.inte.peek() || '/' == this.inte.peek()))) {
                        var ERROR_INFO = "\n右括号前面不能有四则运算符，已为你自动删除【)】";
                        System.out.println(ERROR_INFO);

                        resetInput();
                        show(this.inte);
                        break;
                    }
                    if (!this.inte.empty() && '(' == this.inte.peek()) {
                        var TIP = "\n这一对括号里什么也没有，已为你自动删除这一对括号：";
                        System.out.println(TIP);

                        resetInput();
                        this.optrs.pop();
                        this.inte.pop();
                        show(this.inte);
                        break;
                    }

                    this.optrs.push(charEntered);// 等下就要检查括号匹配的问题，而括号匹配必须先入栈才能检查
                    int matchresult = pareMatch();// 括号匹配检查
                    this.optrs.pop();// 刚刚只是为了检查括号匹配而入栈，这里来消除这种检查的副作用

                    // 如果右括号不匹配，报错
                    if (0 == matchresult) {
                        var ERROR_INFO = "\n右括号不匹配，已为你自动删除【)】";
                        System.out.println(ERROR_INFO);

                        resetInput();
                        show(this.inte);
                        break;
                    }
                }

                if ('=' == charEntered)
                    // 最后准备结束输入时，如果发现左括号多于右括号，报错
                    if (2 == pareMatch()) {
                        var ERROR_INFO = "\n左括号不能多于右括号，已为你自动删除【)】";
                        System.out.println(ERROR_INFO);

                        resetInput();
                        show(this.inte);
                        break;
                    }

                // 现在的正常情况下的处理
                // 如果循环对=前面的操作已处理完毕，那么这里将做收尾工作用以结束循环
                if ('=' == charEntered && this.optrs.empty()) {
                    loadOpnd();
                    this.optrs.push(charEntered);// 此处让“=”入栈的目的是为了下次能结束循环
                    this.inte.push(charEntered);
                    show(this.inte);
                    needContinue = true;// 跳过最后的输入部分。因为现在不需要再输入
                    break;
                } else if (Character.isDigit(charEntered) || '.' == charEntered) {// 如果是数字或小数点的话，入操作数临时栈
                    this.opndBuff.push(charEntered);
                    this.inte.push(charEntered);
                    show(this.inte);
                } else {
                    loadOpnd();
                    if (this.optrs.empty()) {// 如果运算符栈里什么也没有，该运算符就直接入栈
                        this.optrs.push(charEntered);
                        this.inte.push(charEntered);
                        show(this.inte);
                        break;
                    }

                    var priority = precede(this.optrs.peek(), charEntered);
                    if (priority == '<') {
                        this.optrs.push(charEntered);
                        this.inte.push(charEntered);
                        show(this.inte);
                        break;
                    } else if (priority == '>') {
                        //如果把0作为除数
                        if ('(' != charEntered && '/' == this.optrs.peek() && !this.opnds.empty() && 0 == this.opnds.peek()) {
                            if (!this.inte.empty() && '0' == this.inte.peek()) {
                                var ERROR_INFO = "\n抱歉，0不能作除数，已为你自动删除：";
                                System.out.println(ERROR_INFO);

                                resetInput();
                                this.opnds.pop();
                                this.inte.pop();
                                show(this.inte);
                                break;
                            } else {
                                var ERROR_INFO = "\n中间有的算式让除数为0，本次计算失败，正在为你定位到新输入：";
                                System.out.println(ERROR_INFO);

                                resetInput();
                                this.opnds = new Stack<>();// 清空opnd原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐。
                                this.opndBuff = new Stack<>();// 清空this.opndBuff原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐。
                                this.optrs = new Stack<>();//  清空optr原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐。
                                this.inte = new Stack<>();// 清空inte原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐。
                                break;
                            }
                        }
                        var aOptr = this.optrs.pop();
                        var opndRight = this.opnds.pop();
                        var opndRest = this.opnds.pop();

                        this.opnds.push(calculate(opndRest, aOptr, opndRight));
                        needContinue = true;
                        break;// 跳过最后的输入部分。因为这里通过计算产生了新的操作数，所以不需要输入
                    } else if (priority == '=') {
                        this.optrs.pop();
                        this.inte.push(charEntered);
                        show(this.inte);
                        break;
                    } else {// 此代码块不需要做任何事情，只是因为代码编写规范的需要而添加
                    }
                }// else的右括号
            }// 只循环一次的for循环的右括号

            if (needContinue) {
                continue;
            }

            charEntered = getNextChar();
        }// while的右括号

        if (this.opnds.empty()) {
            var OUTPUT_INFO = "\n此表达式为空，计算失败";
            System.out.println(OUTPUT_INFO);
            resetInput();

            this.opnds = new Stack<>();// 清空opnd原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.opndBuff = new Stack<>();// 清空this.opndBuff原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.optrs = new Stack<>();// 清空optr原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.inte = new Stack<>();// 清空inte原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
        } else {
            var OUTPUT_END_INFO = "\n\n输入结束，下面是计算结果：";
            System.out.println(OUTPUT_END_INFO);

            show(this.inte);// 输出表达式（包括最后的“=”）
            System.out.println(this.opnds.peek());// 在“=”后输出计算结果

            var SHOW_PROCESS_INFO = "\n下面是计算过程：";
            System.out.println(SHOW_PROCESS_INFO);
            showCalProce();
            resetInput();

            this.opnds = new Stack<>();// 清空opnd原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.opndBuff = new Stack<>();// 清空this.opndBuff原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.optrs = new Stack<>();// 清空optr原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
            this.inte = new Stack<>();// 清空inte原来的Stack。不过这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
        }
    }// evalExp函数的右括号
}
