package org.wangpai.calculator.view.output;

public enum PromptMsgBoxState {
    INIT_TEXT("init_text"),
    NORMAL_TEXT("normal_text"),
    ERROR_TEXT("error_text");

    private final String state;

    PromptMsgBoxState(String state) {
        this.state = state;
    }
}
