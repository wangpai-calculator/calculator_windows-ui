package org.wangpai.calculator.controller;

import org.junit.jupiter.api.Test;
import org.wangpai.mathlab.exception.SyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @since 2021-8-1
 */
public class Url_Test {
    String urlString = "/first/second/third";

    String lowerUrlString = "/second/third";

    Url url = new Url(this.urlString);

    Url lowerUrl = new Url(this.lowerUrlString);

    String[] expectedDirectories = new String[]{"", "first", "second", "third"};

    /**
     * 因为字段初始化需要抛出异常，所以此构造器不能省略
     */
    public Url_Test() throws SyntaxException {
        super();
    }

    @Test
    public void test_toString() {
        assertEquals(this.urlString, this.url.toString());
    }

    @Test
    public void test_getAllLevelsDirectories_void() {
        var directories = this.url.getAllLevelsDirectories();

        /**
         * 下面的相等比较其实是指针比较，不过也足够了
         */
        assertNotEquals(directories, this.url.getAllLevelsDirectories());
    }

    @Test
    public void test_getFirstLevelDirectory() {
        assertEquals(this.expectedDirectories[1], this.url.getFirstLevelDirectory());
    }

    @Test
    public void test_getSecondLevelDirectory() {
        assertEquals(this.expectedDirectories[2], this.url.getSecondLevelDirectory());
    }

    @Test
    public void test_generateLowerUrl() throws SyntaxException {
        assertEquals(this.lowerUrl, this.url.generateLowerUrl());
    }


    @Test
    public void test_setAllLevelsDirectories() {
        // 此测试的正确性与 getAllLevelsDirectories() 等价，于是不再给出
    }

    @Test
    public void test_getAllLevelsDirectories_static() throws SyntaxException {
        var expected = this.expectedDirectories;
        var actual = Url.getAllLevelsDirectories(this.url);
        var expecedLength = expected.length;
        var actualLength = actual.length;
        assertEquals(expecedLength, actualLength);
        for (int order = 0; order < expecedLength; ++order) {
            assertEquals(expected[order], actual[order]);
        }
    }
}
