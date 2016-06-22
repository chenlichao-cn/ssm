package cn.chenlichao.web.ssm;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/22 下午5:27
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Queue<String> queue = new LinkedBlockingQueue<>();
        for (int i=0; i<10; i++) {
            queue.add(i + "");
        }

        System.out.println("init size: " + queue.size());

        for(String s : queue) {
            System.out.println(s);
        }

        System.out.println("end size: " + queue.size());
    }
}
