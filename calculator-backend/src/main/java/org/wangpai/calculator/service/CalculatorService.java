package org.wangpai.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import java.util.List;
import java.util.regex.Pattern;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.data.CalculatorData;
import org.wangpai.calculator.model.data.OutputStream;
import org.wangpai.calculator.model.data.SymbolOutputStream;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operator.Operator;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ZERO;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ADD;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DIVIDE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.EQUAL;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.LEFT_BRACKET;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.RIGHT_BRACKET;
import static org.wangpai.calculator.view.output.PromptMsgBoxState.ERROR_TEXT;
import static org.wangpai.calculator.view.output.PromptMsgBoxState.NORMAL_TEXT;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Service("calculatorService")
@Slf4j
public final class CalculatorService {
    @Qualifier("computingCenter")
    @Autowired
    private TerminalController controller;

    private static int calculationTimes = 0;

    protected CalculatorService() {
        super();
    }

    /**
     * 此构造器供不使用 Spring 进行依赖注入时使用本类
     *
     * @param controller 上层控制器对象
     */
    public CalculatorService(TerminalController controller) {
        super();
        this.controller = controller;
    }

    /**
     * @since 2021-8-1
     * @lastModified 2021-10-12
     */
    private void sendPromptMsg(String msg) {
        try {
            this.controller.send(new Url("/view/promptMsgBox/setText"),
                    System.lineSeparator() + "恭喜你，未检测到语法错误" + msg);
            this.controller.send(new Url("/view/promptMsgBox/setState"), NORMAL_TEXT);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2021-10-12
     */
    private void sendExceptionMsg(CalculatorException exception) {
        try {
            this.controller.send(new Url("/view/promptMsgBox/setText"), exception);
            this.controller.send(new Url("/view/promptMsgBox/setState"), ERROR_TEXT);
        } catch (Exception sendException) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2021-10-12
     */
    private void clearPromptMsg() {
        try {
            this.controller.send(new Url("/view/promptMsgBox/setText"),
                    System.lineSeparator() + "恭喜你，未检测到语法错误");
            this.controller.send(new Url("/view/promptMsgBox/setState"), NORMAL_TEXT);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2021-10-12
     */
    private void sendCalculationResult(String output) {
        try {
            this.controller.send(new Url("/view/resultBox/append"), output);
            var promptMsgBoxState = this.controller.send(new Url("/view/promptMsgBox/getState"), null);
            if (promptMsgBoxState.equals(ERROR_TEXT)) {
                this.clearPromptMsg();
            }
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-4
     */
    public void readExpression(final String expression) {
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
             * 那么就向提示框发送默认信息，本算法结束
             */
            try {
                this.expressionCheck(calData.clearAllCalData(), expression + "=");
            } catch (CalculatorException exception) { // 如果发生了异常，说明本次的等号补充是不合理的
                this.clearPromptMsg();
                return;
            }

            // 自动计算并发送计算结果
            this.sendAutoCalculationResult(calData);
            return;
        }

        if (expression.contains("=")) {
            if (calData.opndsIsEmpty()) {
                /**
                 * 上面的预计算完成时，操作数栈应该存放的是预计算的运算结果。
                 * 如果此栈为空，说明用户就只输入了等号，此时应该什么也不做
                 */
                return;
            } else {
                // 生成计算过程，并发送
                this.sendProcessResult(expression);
            }
        } else { // 如果用户输入的是一个不完整但没有语法错误的一个表达式
            this.clearPromptMsg();
        }
    }

    /**
     * 等号补齐之后的自动计算
     * <p>
     * 此方法调用之前，必须已经计算完表达式
     *
     * @since 2021-8-5
     */
    private void sendAutoCalculationResult(CalculatorData calData) {
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
                .append(this.generateExpressionString(calData.getExp()))
                .append(" ")
                .append(resultStr)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("（输入等号可显示计算过程）");
        this.sendPromptMsg(prompMsg.toString());
    }

    /**
     * 生成计算过程，并发送
     *
     * @since 2021-8-5
     */
    private void sendProcessResult(final String expression) {
        StringBuilder prompMsg = new StringBuilder();
        prompMsg.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("----------------------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("【" + (++CalculatorService.calculationTimes) + "】下面是计算过程：")
                .append(System.lineSeparator());

        this.sendCalculationResult(prompMsg + this.generateProcess(expression));
    }

    /**
     * 表达式检查
     * <p>
     * 当这个方法正常结束时，操作数栈将储存表达式的运算结果，运算符栈将储存等号，已读取字符栈将储存整个表达式
     */
    private void expressionCheck(CalculatorData calData, String expression)
            throws SyntaxException, UndefinedException {
        var outputStream = new SymbolOutputStream().init(expression);
        Symbol input;
        calData.clearAllCalData(); // 读取一个表达式之前先重置数据

        /**
         * 此循环的后两个条件指的是，当等号之前的表达式时计算完时，运算符栈一定只剩下等号
         *
         * 注意：不要将 “calData.optrsIsEmpty() ||” 改为 “!calData.optrsIsEmpty() &&”
         */
        while (outputStream.hasNext() &&
                (calData.optrsIsEmpty() || calData.peekFromOptrs().getSymbol() != EQUAL)) {
            input = outputStream.next();
            this.syntaxCheck(calData, input);
            this.readSymbol(outputStream, calData, input);
        }
    }

    /**
     * 单个字符的语法检查。此方法必须与方法 readSymbol 交替配合使用。
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
            throw new SyntaxException(ERROR_INFO,
                    generateExpressionString(calData.getExp()));
        }
        // 如果前面没有第一个操作数或右括号，但是后面输入了非数字（例外有：左括号、一元运算符）
        if (!(input == LEFT_BRACKET || input.isDigit())
                && (calData.expIsEmpty() || (!(calData.peekFromExp().isDigit()
                || calData.peekFromExp() == RIGHT_BRACKET)))) {
            /**
             * 此判断条件的含义是，如果输入的字符不是【左括号或数字】，但上一个字符：（下面的条件满足一个即可）
             * > 1. 为空（本次的输入就是第一个字符）
             * > 2. 不为【数字或右括号】
             * 解释：括号的问题将在后面的条件来单独判断，此处只需要关心二元运算符的语法错误
             */
            var ERROR_INFO = "\n注意：在输入" + "【" + input + "】" + "前，你必须先输入一个合法的数。"
                    + "已为你自动删除" + "【" + input + "】";

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionString(calData.getExp()));
        }
        // 连续输入两个非数字符，例外是【第二个为左括号，第一个为其它】、【第一个为右括号，第二个为其它】
        if (!(input.isDigit() || input == LEFT_BRACKET) && !calData.expIsEmpty()
                && !(calData.peekFromExp().isDigit() || calData.peekFromExp() == RIGHT_BRACKET)) {
            var ERROR_INFO = "\n你不能在" + "【" + calData.peekFromExp() + "】" + "的后面输入"
                    + "【" + input + "】" + "，已为你自动删除"
                    + "【" + input + "】" + "：";

            throw new SyntaxException(ERROR_INFO,
                    generateExpressionString(calData.getExp()));
        }
        // 如果输入的是数字
        if (input.isDigit()) {
            // 如果输入的数字不属于一个小数的小数部分，但是其最高位又为 0
            if (!calData.opndBuffIsEmpty() && calData.peekFromBuff() == ZERO
                    && calData.opndBuffSize() == 1) {
                // 此 if 中的前三个判断用于判断 opndBuff 中是否只有 0
                var ERROR_INFO = "\n你不能一开始就在数中输入【0】，已为你自动删除【0】";
                calData.popFromExp(); // 删除一开始输入的 0
                throw new SyntaxException(ERROR_INFO,
                        generateExpressionString(calData.getExp()));
            }
            // 数字前面有右括号
            if (!calData.expIsEmpty() && calData.peekFromExp() == RIGHT_BRACKET) {
                var ERROR_INFO = "\n你不能在右括号后输入数字" + "【" + input + "】" +
                        "。已为你自动删除" + "【" + input + "】";
                throw new SyntaxException(ERROR_INFO,
                        generateExpressionString(calData.getExp()));
            }
        }
        // 如果左括号前面有数字、小数点，报错
        if (input == LEFT_BRACKET && !calData.expIsEmpty()
                && (calData.peekFromExp().isDigit()
                || calData.peekFromExp() == DOT)) {
            var ERROR_INFO = "\n左括号前面不能有字符" + "【" + calData.peekFromExp() + "】" +
                    "（左括号前面不能有数字、小数点），已为你自动删除【(】";
            throw new SyntaxException(ERROR_INFO,
                    generateExpressionString(calData.getExp()));
        }
        // 如果输入的是右括号
        if (input == RIGHT_BRACKET) {
            // 如果右括号前面直接为左括号
            if (!calData.expIsEmpty() &&
                    calData.peekFromExp() == LEFT_BRACKET) {
                var TIP = "\n这一对括号里什么也没有，已为你自动删除这一对括号：";
                calData.popFromExp();
                throw new SyntaxException(TIP,
                        generateExpressionString(calData.getExp()));
            }

            // 如果右括号前面有非左括号的运算符
            if (!calData.expIsEmpty() && (!calData.peekFromExp().isDigit())) {
                /**
                 * 注意，右括号前面的左括号在前面已进行了判断。所以第二个判断条件可以改为非数字
                 */
                var ERROR_INFO = "\n右括号前面不能有运算符" + "【" + input + "】"
                        + "，已为你自动删除" + "【" + input + "】";
                throw new SyntaxException(ERROR_INFO,
                        generateExpressionString(calData.getExp()));
            }

            // 等下就要检查括号匹配的问题，而括号匹配必须右括号先入栈后才能检查
            var tempCalData = (CalculatorData) calData.clone();
            tempCalData.pushToOptrs(new Operator(input));
            /**
             * 括号匹配检查
             *
             * 此处，只有右括号多于左括号时，才需要进行错误处理
             */
            if (tempCalData.bracketMatch() == 2) {
                var ERROR_INFO = "\n右括号不匹配，已为你自动删除【)】";
                throw new SyntaxException(ERROR_INFO,
                        generateExpressionString(calData.getExp()));
            }
        }
        // 最后准备结束输入时，如果发现括号不匹配
        if (input == EQUAL && calData.bracketMatch() != 0) {
            // 此处实际上只可能左括号多于右括号
            var ERROR_INFO = "\n左括号不能多于右括号，已为你自动删除【=】";
            throw new SyntaxException(ERROR_INFO,
                    generateExpressionString(calData.getExp()));
        }
    }

    private void judgeZeroDivisor(CalculatorData calData, OutputStream outputStream) throws SyntaxException {
        /**
         * 如果最近一个运算符是除号，最近一个操作数是 0
         */
        if (calData.peekFromOptrs().equals(new Operator(DIVIDE))
                && !calData.opndsIsEmpty() && calData.peekFromOpnds().isZero()) {
            if (!calData.expIsEmpty() && calData.peekFromExp() == ZERO) {
                /**
                 * 如果此 0 是刚刚用户的输入产生的
                 *
                 * 这里没有将此判断放置到前面的语法错误中来判断，
                 * 因为操作数是可以有多位的，所以那里不方便判断用户是否真正想输入 0
                 */
                var ERROR_INFO = "\n抱歉，0 不能作除数，已为你自动删除：";

                calData.popFromExp();
                throw new SyntaxException(ERROR_INFO, calData.getExp());
            } else {
                /**
                 * 如果此 0 属于运算的中间结果。当此情况发生时，只能放弃整个表达式
                 */
                var ERROR_INFO = "\n中间有算式让除数为 0，计算失败";
                throw new SyntaxException(ERROR_INFO, outputStream.toString());
            } // 内层 else
        } // 外层 if
    } // 本方法的右括号

    /**
     * 单个字符的输入处理。此方法调用前必须输入字符语法上的正确性
     */
    public void readSymbol(OutputStream outputStream, CalculatorData calData, Symbol input)
            throws SyntaxException, UndefinedException {
        if (input == EQUAL && calData.optrsIsEmpty()) {
            calData.loadOpnd();
            calData.pushSymbol(input); // 此处让等号入栈的目的是为了下次能结束循环
            outputStream.rollback(); // 让下次读取到的依然是等号
            return;
        } else if (input.isDigit() || input == DOT) {
            // 如果输入字符为数字或小数点，将其加入操作数缓存栈
            calData.pushSymbol(input);
            return;
        } else {
            calData.loadOpnd();

            if (calData.optrsIsEmpty()) {
                // 如果运算符栈里什么也没有，该运算符就直接入栈
                calData.pushSymbol(input);
                return;
            }

            switch (this.precede(calData.peekFromOptrs(), new Operator(input))) {
                case "<": // 当前读取的运算符优先级大于最近的运算符。那当前读取的运算符就直接入栈
                case "=":
                    /**
                     * 运行到此 case "="，说明最近的两个运算符是一对括号，
                     * 不过，方法 pushSymbol 已拥有处理此情况的能力，
                     * 因此只需要调用此方法
                     */
                    calData.pushSymbol(input);
                    return;
                case ">":
                    this.judgeZeroDivisor(calData, outputStream);
                    calData.oneTimeCalculation();
                    outputStream.rollback();
                    return;
            } // switch
        } // else
    }

    /**
     * 由于计算表达式与在计算表达式的过程中显示过程是两个不同的行为，
     * 因此建议不要合并
     *
     * @since before 2021-8-5
     */
    public String generateProcess(String expression) {
        if (expression == null || expression.equals("")) {
            return "";
        }

        // 如果 hasCalculation 为 true，说明本函数至少执行了一次计算
        boolean hasCalculation = false;
        StringBuilder result = new StringBuilder();

        SymbolOutputStream outputStream = null;
        try {
            /**
             * 先将等号前面的原表达式输出
             */
            result.append(this.generateExpressionString(
                    new SymbolOutputStream().init(
                                    expression.substring(0, expression.indexOf("=")))
                            .toList()));
            outputStream = new SymbolOutputStream().init(expression);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }

        var calData = new CalculatorData();
        Symbol input;

        // 此循环的后两个条件指的是，当等号之前的表达式时计算完时，运算符栈一定只剩下等号
        while (outputStream.hasNext() &&
                (calData.optrsIsEmpty() || calData.peekFromOptrs().getSymbol() != EQUAL)) {
            input = outputStream.next();

            if (input == EQUAL && calData.optrsIsEmpty()) {
                calData.loadOpnd();
                calData.pushSymbol(input); // 此处让等号入栈的目的是为了下次能结束循环
                outputStream.rollback(); // 让下次读取到的依然是等号
            } else if (input.isDigit() || input == DOT) {
                // 如果是数字或小数点的话，入操作数临时栈
                calData.pushSymbol(input);
            } else {
                calData.loadOpnd();

                if (calData.optrsIsEmpty()) {
                    // 如果运算符栈里什么也没有，该运算符就直接入栈
                    calData.pushSymbol(input);
                } else {
                    String priority = null;
                    try {
                        priority = this.precede(calData.peekFromOptrs(), new Operator(input));
                    } catch (Exception exception) {
                        log.error("异常：", exception);
                    }
                    switch (priority) {
                        case "<": // 当前读取的运算符优先级大于最近的运算符。那当前读取的运算符就直接入栈
                            calData.pushSymbol(input);
                            break;
                        case ">":
                            boolean isPaired = false; // 一个操作数外出现成对括号
                            try {
                                calData.oneTimeCalculation();

                                /**
                                 * 如果 input 为右括号，检查是否可以进行如下优化：
                                 * 运算之后，如果这对括号之间就只有一个操作数，那就直接从中去掉这对括号
                                 */
                                if (input == RIGHT_BRACKET) {
                                    isPaired = this.precede(calData.peekFromOptrs(), new Operator(input)).equals("=");
                                }
                            } catch (Exception exception) {
                                log.error("异常：", exception);
                            }
                            if (isPaired) {
                                /**
                                 * 运行到此处，说明最近的两个运算符是一对括号，那就直接从中去掉这对括号。
                                 * 不过，方法 pushSymbol 已拥有处理此情况的能力，因此只需要调用此方法
                                 */
                                calData.pushSymbol(input);
                            } else {
                                // 本次输入的运算符优先级过低，并没有被使用，所以要将本次的运算符退回输入流
                                outputStream.rollback();
                            }

                            result.append(this.generateMiddleExpression(
                                    calData.calculatedExpToString()
                                            + outputStream.getRest().toString()));

                            hasCalculation = true; // 这是为了告诉程序，本函数至少执行了一次计算
                            break;
                        case "=": // 已经在 case ">" 优化过，此 case 不会发生
                            break;
                        default: // 此 case 不会发生
                            break;
                    } // switch
                }
            }
        } // while

        if (hasCalculation) {
            // 将最终结果转化为小数
            result.append(generateLastExpressionData(calData.getOpnds().peek(), EQUAL));
        } else {
            // 如果表达式过于简单，就将原表达式输出
            result.append(generateMiddleExpression(expression));
        }

        return result.toString();
    }

    /**
     * 展示整个表达式。对于 expression，要求表达式中越靠近等于号的部分序员越大
     *
     * @lastModified 2021-8-9
     * @since 2021-8-1
     */
    private String generateExpressionString(List<Symbol> expression) {
        if (expression == null || expression.size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            return sb.append(System.lineSeparator())
                    .append("  ")
                    .append(this.beautifyExpression(expression, EQUAL))
                    .toString();
        }
    }

    /**
     * 生成中间表达式字符串。该方法会将表达式中的等号前置
     * <p>
     * 对于 expression，要求表达式中靠近等于号的部分位于字符串后面
     *
     * @since 2021-8-9
     */
    private String generateMiddleExpression(String expression) {
        if (expression == null || expression.equals("")) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator())
                .append(System.lineSeparator());
        try {
            if (expression.contains("=")) {
                /**
                 * 如果表达式中含等号，将其前置
                 */
                sb.append("=")
                        .append(this.beautifyExpression(
                                new SymbolOutputStream().init(
                                                expression.substring(0, expression.indexOf("=")))
                                        .toList(), EQUAL));
            } else {
                sb.append(this.beautifyExpression(
                        new SymbolOutputStream()
                                .init(expression)
                                .toList(), EQUAL));
            }
        } catch (Exception exception) {
            log.error("异常：", exception);
        }

        return sb.toString();
    }

    /**
     * 美化输出
     *
     * @since 2021-8-9
     */
    private String beautifyExpression(List<Symbol> expression, Symbol lastSymbol) {
        StringBuilder sb = new StringBuilder();
        for (var symbol : expression) {
            /**
             * 美化输出。将操作数与运算符之间加入空格。
             * 例外是，左括号后面，右括号左边、小数点前后
             */
            if (lastSymbol == LEFT_BRACKET || symbol == RIGHT_BRACKET) {
                // 左括号后面不加空格，右括号左边不加空格
            } else if (lastSymbol == DOT || symbol == DOT) {
                // 小数点前后不加空格
            } else if (lastSymbol == Symbol.getEnum("[") || symbol == Symbol.getEnum("]")) {
                // 左括号后面不加空格，右括号左边不加空格
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

    private String generateLastExpressionData(Operand result, Symbol delimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator())
                .append(System.lineSeparator());

        sb.append(delimiter.toString()); // 将 delimiter 前置

        if (result instanceof RationalNumber) {
            return sb.append(" ")
                    .append(((RationalNumber) result).toDouble())
                    .toString();
        } else {
            return "";
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
        if (formerOptr.equals(LEFT_BRACKET)
                && currentOptr.equals(RIGHT_BRACKET)) {
            return "=";
        } else if (currentOptr.equals(DOT)) {
            return "<";
        } else if (formerOptr.equals(LEFT_BRACKET)
                || currentOptr.equals(LEFT_BRACKET)) {
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
