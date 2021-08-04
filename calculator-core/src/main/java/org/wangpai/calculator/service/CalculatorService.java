package org.wangpai.calculator.service;

import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.data.CalculatorData;
import org.wangpai.calculator.model.data.SymbolOutputStream;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operator.Operator;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.*;

import lombok.SneakyThrows;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @since 2021-8-1
 */
public final class CalculatorService {
    private SymbolOutputStream outputStream;
    private TerminalController controller;

    private static int calculationTimes = 0;

    protected CalculatorService() {
        super();
    }

    public CalculatorService(TerminalController controller) {
        super();
        this.controller = controller;
    }

    @SneakyThrows
    private void sendPromptMsg(String msg) {
        this.controller.send(new Url("/view/promptmsgbox/settext"),
                System.lineSeparator() + "恭喜你，未检测到语法错误" + msg);
    }

    @SneakyThrows
    private void sendExceptionMsg(CalculatorException exception) {
        this.controller.send(new Url("/view/promptmsgbox/settext"), exception);
    }

    @SneakyThrows
    private void clearPromptMsg() {
        this.controller.send(new Url("/view/promptmsgbox/settext"),
                System.lineSeparator() + "恭喜你，未检测到语法错误");
    }

    @SneakyThrows
    private void sendCalculationResult(String output) {
        this.controller.send(new Url("/view/resultbox/append"), output);
    }

    /**
     * @since 2021-8-4
     */
    public void readExpression(final String expression) throws CalculatorException {
        CalculatorData calData = new CalculatorData();

        // 对原表达式进行静态检查和动态检查。如果检查不通过，向提示框发送异常信息，本算法结束
        try {
            this.expressionCheck(calData.clearAllCalData(), expression);
        } catch (CalculatorException exception) {
            this.sendExceptionMsg(exception);
            return;
        }

        // 判断是否以数字结尾
        boolean isEndWithDigit = Pattern.compile("\\d+$").matcher(expression).find();
        // 判断是否以右括号结尾
        boolean isEndWithRightBracket = expression.endsWith(RIGHT_BRACKET.toString());
        // 初步判断是否可以添加等号
        boolean needCompletion = !expression.contains("=")
                && (isEndWithDigit || isEndWithRightBracket);
        if (needCompletion) {
            /**
             * 对补充等号的表达式进行静态检查和动态检查。
             * 说明原表达式不完整但没有语法错误，且自行添加等号是不合理的，
             * 那么就向提示框发送默认信息，本算法结束。
             */
            try {
                this.expressionCheck(calData.clearAllCalData(), expression + "=");
            } catch (CalculatorException exception) { // 如果发生了异常，说明本次的等号补充是不合理的
                this.clearPromptMsg();
                return;
            }

            var result = calData.getOpnds().peek();
            String resultStr;
            if (result instanceof RationalNumber) {
                resultStr = Double.toString(((RationalNumber) result).toDouble());
            } else {
                resultStr = result.toString();
            }
            StringBuilder prompMsg = new StringBuilder();
            prompMsg.append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append("自动计算的结果为：")
                    .append(System.lineSeparator())
                    .append(this.generateExpressionData(calData.getExp()))
                    .append(" ")
                    .append(resultStr)
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append("（输入等号可显示计算过程）");
            this.sendPromptMsg(prompMsg.toString());
            return;
        }

        if (expression.contains("=")) {
            if (calData.opndsIsEmpty()) {
                return;
            } else {
                StringBuilder prompMsg = new StringBuilder();
                prompMsg.append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append("----------------------")
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append("【" + (++CalculatorService.calculationTimes) + "】下面是计算过程：")
                        .append(System.lineSeparator());

                this.sendCalculationResult(prompMsg + this.generateProcess(calData));
            }
        } else { // 如果用户输入的是一个不完整但没有语法错误的一个表达式
            this.clearPromptMsg();
        }
    }

    /**
     * 表达式检查
     * <p>
     * 当这个方法正常结束时，操作数栈将储存表达式的运算结果，运算符栈将储存等号，已读取字符栈将储存整个表达式
     */
    private void expressionCheck(CalculatorData calData, String expression) throws CalculatorException {
        this.outputStream = new SymbolOutputStream().init(expression);
        Symbol symbolEntered;
        calData.clearAllCalData(); // 读取一个表达式之前先重置数据

        // 此循环的后两个条件指的是，当等号之前的表达式时计算完时，运算符栈一定只剩下等号
        while (this.outputStream.hasNext() &&
                (calData.optrsIsEmpty() || calData.peekFromOptrs().getSymbol() != EQUAL)) {
            symbolEntered = this.outputStream.next();
            this.syntaxCheck(calData, symbolEntered);
            this.readSymbol(calData, symbolEntered);
        }
    }

    /**
     * 单个字符的语法检查。此方法必须与方法 readSymbol 接替配合使用。
     * 一般先调用一次本方法，然后调用一次方法 readSymbol，接着再调用本方法，以此类推
     */
    private void syntaxCheck(CalculatorData calData, Symbol input)
            throws SyntaxException {
        /**
         * 先尽可能处理所有简单、能单独判断的语法错误
         *
         * 只需要考虑当前输入的符号以及当前输入的符号与其之前的符号之间有无错误，
         * 不需要考虑当前输入的符号与其之后的符号之间有无错误
         */

        // 一个操作数里，输入了两个小数点
        if (input == DOT && calData.searchFromBuff(DOT) != -1) {
            var ERROR_INFO = "\n你不能在一个数里输入两个【.】，已为你自动删除【.】";
            System.out.println(ERROR_INFO);
            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }
        // 如果输入的数字不属于一个小数的小数部分，但是其最高位又为 0
        if (!calData.opndBuffIsEmpty() && calData.peekFromBuff() == ZERO
                && calData.opndBuffSize() == 1 && input.isDigit()) {
            /**
             * 此 if 中的前三个判断用于判断 opndBuff 中是否只有 0
             *
             * 为了提高效率，这里将 input.isDigit() 作为最后一个判断条件
             */

            var ERROR_INFO = "\n你不能一开始就在数中输入【0】，已为你自动删除【0】";
            System.out.println(ERROR_INFO);
            calData.popFromExp(); // 删除一开始输入的 0
            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }
        // 如果前面没有第一个操作数或右括号，但是后面输入了非数字（例外有：左括号、一元运算符）
        if ((input != LEFT_BRACKET && !input.isDigit())
                && !calData.inteIsEmpty() &&
                !(calData.peekFromExp().isDigit()
                        || calData.peekFromExp() == RIGHT_BRACKET)) {
            var ERROR_INFO = "\n注意：在输入" + "【" + input + "】" + "前，你必须先输入一个合法的数。"
                    + "已为你自动删除" + "【" + input + "】";
            System.out.println(ERROR_INFO);

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }
        // 连续输入两个非数字符，例外是【第二个为左括号，第一个为其它】、【第一个为右括号，第二个为其它】
        if (!(input.isDigit() || input == LEFT_BRACKET) && !calData.inteIsEmpty()
                && !(calData.peekFromExp().isDigit() || calData.peekFromExp() == RIGHT_BRACKET)) {
            var ERROR_INFO = "\n你不能在" + "【" + calData.peekFromExp() + "】" + "的后面输入"
                    + "【" + input + "】" + "，已为你自动删除"
                    + "【" + input + "】" + "：";
            System.out.println(ERROR_INFO);

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }
        // 如果左括号前面有数字、小数点，报错
        if (input == LEFT_BRACKET && !calData.inteIsEmpty()
                && (calData.peekFromExp().isDigit()
                || calData.peekFromExp() == DOT)) {
            var ERROR_INFO = "\n左括号前面不能有" + "【" + calData.peekFromExp() + "】" +
                    "数字、小数点，已为你自动删除【(】";
            System.out.println(ERROR_INFO);

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }

        if (input == RIGHT_BRACKET) {
            // 如果右括号前面直接为左括号
            if (!calData.inteIsEmpty() &&
                    calData.peekFromExp() == LEFT_BRACKET) {
                var TIP = "\n这一对括号里什么也没有，已为你自动删除这一对括号：";
                calData.popFromExp();
                System.out.println(TIP);

                throw new SyntaxException(TIP,
                        generateExpressionData(calData.getExp()));
            }

            // 如果右括号前面有非左括号的运算符
            if (!calData.inteIsEmpty() && (!calData.peekFromExp().isDigit())) {
                /**
                 * 注意，右括号前面的左括号在前面已进行了判断。所以第二个判断条件可以改为非数字
                 */

                var ERROR_INFO = "\n右括号前面不能有运算符" + "【" + input + "】"
                        + "，已为你自动删除" + "【" + input + "】";
                System.out.println(ERROR_INFO);

                throw new SyntaxException(ERROR_INFO,
                        generateExpressionData(calData.getExp()));
            }

            // 等下就要检查括号匹配的问题，而括号匹配必须先入栈才能检查
            calData.pushToOptrs(new Operator(input));
            /**
             * 括号匹配检查
             *
             * 此处，只有右括号多于左括号时，才需要进行错误处理
             */
            if (calData.pareMatch() == 2) {
                var ERROR_INFO = "\n右括号不匹配，已为你自动删除【)】";
                System.out.println(ERROR_INFO);

                throw new SyntaxException(ERROR_INFO,
                        generateExpressionData(calData.getExp()));
            }
            calData.popFromOptrs(); // 刚刚只是为了检查括号匹配而入栈，这里来消除这种检查的副作用
        }
        // 最后准备结束输入时，如果发现左括号多于右括号
        if (input == EQUAL && calData.pareMatch() == 1) {
            var ERROR_INFO = "\n左括号不能多于右括号，已为你自动删除【=】";
            System.out.println(ERROR_INFO);

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionData(calData.getExp()));
        }
    }

    /**
     * 单个字符的输入处理。此方法调用前必须输入字符语法上的正确性
     */
    public void readSymbol(CalculatorData calData, Symbol symbolEntered)
            throws CalculatorException {
        if (symbolEntered == Symbol.EQUAL && calData.optrsIsEmpty()) {
            calData.loadOpnd();
            calData.pushSymbol(symbolEntered); // 此处让 “=” 入栈的目的是为了下次能结束循环
            this.outputStream.rollback(); // 让下次读取到的依然是 “=”
            return;
        } else if (symbolEntered.isDigit() || symbolEntered == DOT) {
            // 如果是数字或小数点的话，入操作数临时栈
            calData.pushSymbol(symbolEntered);
            return;
        } else {
            calData.loadOpnd();

            if (calData.optrsIsEmpty()) {
                // 如果运算符栈里什么也没有，该运算符就直接入栈
                calData.pushSymbol(symbolEntered);
                return;
            }

            switch (this.precede(calData.peekFromOptrs(), new Operator(symbolEntered))) {
                case "<":
                    calData.pushSymbol(symbolEntered);
                    return;
                case ">":
                    this.judgeZeroDivisor(calData);

                    calData.oneTimeCalculation();
                    this.outputStream.rollback();
                    return;
                case "=":
                    /**
                     * 运行到此 case，说明最近的两个运算符是一对括号，
                     * 此时应该将这一对括号从运算符栈弹出，然后将右括号加入 inte 栈
                     */
                    calData.popFromOptrs();
                    calData.pushToExp(symbolEntered);
                    return;
            } // switch
        } // else
    }

    private void judgeZeroDivisor(CalculatorData calData) throws SyntaxException {
        /**
         * 如果最近一个运算符是除号，最近一个操作数是 0
         */
        if (calData.peekFromOptrs().equals(new Operator(DIVIDE))
                && !calData.opndsIsEmpty() && calData.peekFromOpnds().isZero()) {
            if (!calData.inteIsEmpty() && calData.peekFromExp() == ZERO) {
                /**
                 * 如果此 0 是刚刚用户的输入产生的
                 *
                 * 这里没有将此判断放置到前面的语法错误中来判断，
                 * 因为操作数是可以有多位的，所以那里不方便判断用户是否真正想输入 0
                 */
                var ERROR_INFO = "\n抱歉，0 不能作除数，已为你自动删除：";

                calData.popFromExp();
                System.out.println(ERROR_INFO);

                throw new SyntaxException(ERROR_INFO, calData.getExp());
            } else {
                /**
                 * 如果此 0 属于运算的中间结果。当此情况发生时，只能放弃整个表达式
                 */
                var ERROR_INFO = "\n中间有的算式让除数为 0，计算失败";
                System.out.println(ERROR_INFO);

                throw new SyntaxException(ERROR_INFO, outputStream.getOriginInput());
            } // 内层 else
        } // 外层 if
    } // 本方法的右括号

    public String generateProcess(CalculatorData calculatorData) throws CalculatorException {
        if (calculatorData.inteIsEmpty()) {
            return "";
        }

        // 如果 hasCalculation 为 true，说明本函数至少执行了一次计算
        boolean hasCalculation = false;
        StringBuilder result = new StringBuilder();

        var inteTemp = (Stack<Symbol>) calculatorData.getExp().clone();
        if (EQUAL == inteTemp.peek()) {
            inteTemp.pop();
            result.append(generateExpressionData(inteTemp));
            inteTemp.push(EQUAL);
        } else {
            result.append(generateExpressionData(inteTemp));
        }

        // antiInte：anti inte inte的反转。代表待读取的输入表达式
        var antiInte = (Stack<Symbol>) calculatorData.getExp().clone();
        Collections.reverse(antiInte);
        var inteOfShow = new Stack<Symbol>();

        var calData = new CalculatorData();

//        calData.clearAllCalData();

        var input = antiInte.pop();

        while (!antiInte.empty() || inteOfShow.empty() || EQUAL != inteOfShow.peek()) {
            boolean needInput = true;

            // 如果循环对等号前面的操作已处理完毕
            if (EQUAL == input && calData.optrsIsEmpty()) {
                if (calData.loadOpnd()) {
                    inteOfShow.push(F1);// '#'为转换符，以后如在inteOfShow遇到此符，说明此处是操作数，需要去别处寻找
                }

                /**
                 * 这是为了跳过下面的input输入，因为能执行这个代码块，
                 * 说明表达式已到尽头，不需要新的输入而直接将“=”入栈能马上跳出该循环
                 */
                calData.pushToOptrs(new Operator(input));

                inteOfShow.push(input);
                needInput = false;
            } else if (input.isDigit() || DOT == input) {
                // 如果是数字或小数点的话，入操作数临时栈
                calData.pushToBuff(input);
            } else {
                if (calData.loadOpnd()) {
                    inteOfShow.push(F1);// F1 为转换符，以后如在 inteOfShow 遇到此符，说明此处是操作数，需要去别处寻找
                }

                if (calData.optrsIsEmpty()) {
                    calData.pushToOptrs(new Operator(input));
                    inteOfShow.push(input);
                } else {
                    switch (precede(calData.peekFromOptrs(), new Operator(input))) {
                        case "<":
                            calData.pushToOptrs(new Operator(input));
                            inteOfShow.push(input);
                            break;
                        case ">":
                            var aOptr = calData.popFromOptrs();
                            var opndRight = calData.popFromOpnds();
                            var opndLeft = calData.popFromOpnds();

                            for (int i = 1; i <= 3; ++i) {
                                inteOfShow.pop();// 弹出两个操作数、一个运算符，共三个字符
                            }
                            // medResult：intermediate result 中间结果
                            Operand medResult = CalculatorData.oneTimeCalculation(opndLeft, aOptr, opndRight);
                            calData.pushToOpnds(medResult);
                            inteOfShow.push(F1);// F1 为转换符，遇到此符，说明此处是操作数，需要去别处寻找

                            /**
                             * 下面将显示表达式本步运算的相关内容。这需要同时显示前面已经计算的操作 inteOfShow、
                             * 当前的运算符 input、以及待读取的输入表达式 antiInte
                             */

                            // 因为show的传参要求的顺序与inte是一致的，所以这里要对antinte进行倒序
                            var restExp = (Stack<Symbol>) antiInte.clone(); // 待读取的输入表达式antinte中剩余的部分
                            restExp.push(input);
                            var antiRest = (Stack<Symbol>) restExp.clone();
                            Collections.reverse(antiRest);

                            var fulExp = (Stack<Symbol>) inteOfShow.clone(); // fulExp：full expression 完整表达式
                            fulExp.addAll((Stack<Symbol>) antiRest.clone());

                            System.out.println("");
                            result.append(generateExpressionData(fulExp, calData.getOpnds(), EQUAL));
                            hasCalculation = true; // 这是为了告诉程序，本函数至少执行了一次计算

                            /**
                             * 这是为了跳过下面的输入读取，因为能执行这个代码块，
                             * 说明 input 的优先级比较低，因此尚未入栈（未经过处理），所以不需要新的输入
                             */
                            needInput = false;

                            break;
                        case "=": // 如果两个括号之间没有其他运算符，就只有一个操作数
                            calData.popFromOptrs(); // 弹出运算符栈的左括号
                            var opndTemp = inteOfShow.peek();
                            /**
                             * 现在弹出的不是左括号，因为 inteOfShow 中的左括号离栈顶还有一个操作数。
                             * 而两个括号之间的操作数不是我们想要弹出的，因此要先保存该操作数
                             */
                            inteOfShow.pop();
                            inteOfShow.pop(); // 弹出左括号
                            inteOfShow.push(opndTemp); // 将前面被迫先暂时弹出的操作数再加入栈中
                            break;
                    }
                }
            } // else的右括号

            if (needInput) {
                input = antiInte.pop();
            }
        }

        if (hasCalculation) {
            // 将最终结果转化为小数
            result.append(generateLastExpressionData(inteTemp, calData.getOpnds(), EQUAL));
        } else {
            // 如果表达式过于简单，就将原表达式输出。inteTemp 是之前对 inte 的备份
            result.append(generateExpressionData(inteTemp, calData.getOpnds(), EQUAL));
        }

        return result.toString();
    }

    // 展示整个表达式。对于 expression，要求表达式中靠近等于号的部分靠近栈顶
    @Deprecated
    private String generateExpressionData(Stack<Symbol> expression) {
        if (expression.empty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(System.lineSeparator())
                    .append("  ");

            Symbol lastSymbol = EQUAL;

            for (var symbol : expression) {
                /**
                 * 美化输出。将操作数与运算符之间加入空格。
                 * 例外是，左括号后面，右括号左边、小数点前后
                 */
                if (lastSymbol == LEFT_BRACKET || symbol == RIGHT_BRACKET) {
                    // 左括号后面不加空格，右括号左边不加空格
                } else if (lastSymbol == DOT || symbol == DOT) {
                    // 小数点前后不加空格
                } else if (!symbol.isDigit()) {
                    sb.append(" ");
                } else if (symbol.isDigit() && !lastSymbol.isDigit()) {
                    sb.append(" ");
                }

                sb.append(symbol.toString());
                lastSymbol = symbol;
            }

            return sb.toString();
        }
    }

    /**
     * @param delimiter  这里 delimiter 有默认参数，默认参数为#，当其为其他符号时，意味着可能将输出最后的定界符前置
     * @param expression 对于expression、medResults，要求表达式中靠近等于号的部分靠近栈顶
     * @param medResults 见{@code expression}
     * @apiNote medResults：intermediate result 中间结果
     * @since 2021-7-30
     */
    @Deprecated
    private String generateExpressionData(
            Stack<Symbol> expression, Stack<Operand> medResults, Symbol delimiter) {
        if (expression.empty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(System.lineSeparator())
                    .append(System.lineSeparator());

            int expEnd = expression.size();
            if (delimiter == expression.peek()) {
                sb.append(delimiter.toString()); // expression 末尾如果有该定界符，将其前置
                --expEnd;
            }

            int reOrderExp = 0;// reOrderExp：reverse order expression expression 的倒数序号
            int reOrderMed = 0;// reOrderMed：reverse order medResults medResults 的倒数序号

            Symbol lastSymbol = EQUAL;

            // F1 为转换符
            for (; reOrderExp < expEnd; ++reOrderExp) {
                Symbol currentSymbol = expression.elementAt(reOrderExp);

                /**
                 * 美化输出。将操作数与运算符之间加入空格。
                 * 例外是，左括号后面，右括号左边、小数点前后
                 */
                if (lastSymbol == LEFT_BRACKET || currentSymbol == RIGHT_BRACKET) {
                    // 左括号后面不加空格，右括号左边不加空格
                } else if (lastSymbol == DOT || currentSymbol == DOT) {
                    // 小数点前后不加空格
                } else if (!currentSymbol.isDigit()) {
                    sb.append(" ");
                } else if (currentSymbol.isDigit() && !lastSymbol.isDigit()) {
                    sb.append(" ");
                }
                lastSymbol = currentSymbol;

                // 如果遇到转换符，到 medResults 里找操作数
                if (F1 == currentSymbol) {
                    sb.append(medResults.elementAt(reOrderMed).toString());
                    lastSymbol = ZERO; // 0 表示本次读取到的字符为数字
                    ++reOrderMed;
                } else {
                    sb.append(currentSymbol.toString());
                }
            }
            return sb.toString();
        }
    }

    @Deprecated
    private String generateLastExpressionData(
            Stack<Symbol> expression, Stack<Operand> medResults, Symbol delimiter) {
        if (expression.empty() || medResults.empty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(System.lineSeparator())
                    .append(System.lineSeparator());

            if (delimiter == expression.peek()) {
                sb.append(delimiter.toString()); // expression 末尾如果有该定界符，将其前置
            }

            var result = medResults.peek();
            if (result instanceof RationalNumber) {
                return sb.append(" ")
                        .append(((RationalNumber) result).toDouble())
                        .toString();
            } else {
                return "";
            }
        }
    }

    /**
     * 此方法的下一个版本将改为：
     * 通过对运算符设置一个优先级权重值
     * 然后比较两个运算符的优先级权重值，
     * 当权重值小的运算符视为优先级高
     */
    @Deprecated
    public String precede(Operator formerOptr, Operator currentOptr) {
        /**
         * 注意：== 的优先级比 && 高，&& 的优先级比 || 高
         */
        if (formerOptr.equals(Symbol.LEFT_BRACKET)
                && currentOptr.equals(Symbol.RIGHT_BRACKET)) {
            return "=";
        } else if (currentOptr.equals(DOT)) {
            return "<";
        } else if (formerOptr.equals(Symbol.LEFT_BRACKET)
                || currentOptr.equals(Symbol.LEFT_BRACKET)) {
            return "<";
        } else if ((formerOptr.equals(ADD)
                || formerOptr.equals(Symbol.SUBTRACT))
                && ((currentOptr.equals(Symbol.MULTIPLY)
                || currentOptr.equals(DIVIDE)))) {
            return "<";
        } else {
            return ">";
        }
    }
}




