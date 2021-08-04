package org.wangpai.calculator.controller.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.SyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @since 2021-8-1
 */
class Url_Test {
    String urlString = "/first/second/third";

    String lowerUrlString = "/second/third";

    Url url = new Url(urlString);

    Url lowerUrl = new Url(lowerUrlString);

    String[] expectedDirectories = new String[]{"", "first", "second", "third"};

    Url_Test() throws SyntaxException {
    }

    @Test
    void toString_test() {
        assertEquals(this.urlString, url.toString());
    }

    @Test
    void getAllLevelsDirectories_void() {
        var directories = this.url.getAllLevelsDirectories();

        /**
         * 下面的相等比较其实是指针比较，不过也足够了
         */
        assertNotEquals(directories, this.url.getAllLevelsDirectories());
    }


    @Test
    void getFirstLevelDirectory() {
        assertEquals(this.expectedDirectories[1], this.url.getFirstLevelDirectory());
    }

    @Test
    void getSecondLevelDirectory() {
        assertEquals(this.expectedDirectories[2], this.url.getSecondLevelDirectory());
    }

    @Test
    void generateLowerUrl() throws SyntaxException {
        assertEquals(lowerUrl, url.generateLowerUrl());
    }


    @Test
    void setAllLevelsDirectories() {
        // 此测试的正确性与 getAllLevelsDirectories() 等价，于是不再给出
    }

    @Test
    void getAllLevelsDirectories_static() throws SyntaxException {
        var expected = this.expectedDirectories;
        var actual = Url.getAllLevelsDirectories(url);
        var expecedLength = expected.length;
        var actualLength = actual.length;
        assertEquals(expecedLength, actualLength);
        for (int order = 0; order < expecedLength; ++order) {
            assertEquals(expected[order], actual[order]);
        }
    }
}