package cn.chenlichao.web.ssm.utils;

import org.junit.Test;

import java.util.Queue;

import static org.junit.Assert.*;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/22 下午5:40
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class LinkedReadOnceQueueTest {

    @Test
    public void test() throws Exception {
        Queue<String> queue = new LinkedReadOnceQueue<>();

        for(int i=0; i<10; i++) {
            queue.add("" + i);
        }

        assertEquals("循环前", 10, queue.size());
        for (String s : queue) {
            //System.out.println(s);
        }
        assertEquals("循环后", 0, queue.size());
    }

}