package org.wangpai.calculator.view.control;

import java.awt.*;

/**
 * @since 2021-8-1
 */
public class Gbc extends GridBagConstraints {
    public Gbc() {
        super();
    }

    public Gbc(int gridx, int gridy) {
        super();
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public Gbc(int gridx, int gridy, int gridwidth, int gridheight) {
        super();

        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public Gbc setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public Gbc setFill(int fill) {
        this.fill = fill;
        return this;
    }

    public Gbc setWeight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    public Gbc setInsets(int distance) {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    public Gbc setInsets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public Gbc setIpad(int ipadx, int ipady) {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}
