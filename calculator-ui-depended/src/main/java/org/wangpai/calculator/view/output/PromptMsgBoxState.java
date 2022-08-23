package org.wangpai.calculator.view.output;

public enum PromptMsgBoxState {
    INIT("init"),
    NORMAL("normal"),
    ERROR("error");

    private final String state;

    PromptMsgBoxState(String state) {
        this.state = state;
    }
}
