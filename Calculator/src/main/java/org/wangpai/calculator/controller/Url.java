package org.wangpai.calculator.controller;

import org.wangpai.calculator.exception.SyntaxException;

public class Url {
    private String underlyingUrl;
    private String[] directories;

    protected Url() {
        super();
    }

    public Url(Url url) throws SyntaxException {
        this(url.underlyingUrl);
    }

    public Url(String url) throws SyntaxException {
        UrlCheck(url);
        this.underlyingUrl = url;
        this.setAllLevelsDirectories();
    }

    @Override
    public String toString() {
        return this.underlyingUrl;
    }

    /**
     * 当 URL 为空时返回 false，语法错误时抛出异常，其它情况返回 true
     */
    protected static boolean UrlCheck(String url) throws SyntaxException {
        if (url == null) {
            return false;
        }
        if (url.toCharArray()[0] != '/') {
            throw new SyntaxException("异常：URL 有误");
        }

        return true;
    }

    /**
     * 当 URL 为空时返回 false，语法错误时抛出异常，其它情况返回 true
     */
    protected static boolean UrlCheck(Url url) throws SyntaxException {
        return Url.UrlCheck(url.underlyingUrl);
    }

    protected Url setAllLevelsDirectories() {
        try {
            this.directories = getAllLevelsDirectories(this);
        } catch (SyntaxException exception) {
            // 上面的 try 块实际上不可能有异常，因此此处为空
        }

        return this;
    }

    public String[] getAllLevelsDirectories() {
        return this.directories.clone();
    }

    public static String[] getAllLevelsDirectories(Url url) throws SyntaxException {
        UrlCheck(url);

        return url.underlyingUrl.split("/");
    }

    public String getFirstLevelDirectory() {
        return this.directories[1];
    }

    public String getSecondLevelDirectory() {
        return this.directories[2];
    }

    public Url generateLowerUrl() {
        var result = new Url();
        result.directories = new String[this.directories.length - 1];
        result.directories[0] = "";
        System.arraycopy(this.directories, 2,
                result.directories, 1, this.directories.length - 2);
        result.underlyingUrl = String.join("/", result.directories);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Url)) {
            return false;
        }

        return this.underlyingUrl.equals(((Url) other).underlyingUrl);
    }
}
