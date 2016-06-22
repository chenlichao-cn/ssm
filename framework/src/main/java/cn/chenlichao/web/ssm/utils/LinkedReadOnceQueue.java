package cn.chenlichao.web.ssm.utils;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/22 下午5:35
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class LinkedReadOnceQueue<E> extends LinkedBlockingQueue<E> {

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        @Override
        public boolean hasNext() {
            return size() > 0;
        }

        @Override
        public E next() {
            try {
                return take();
            } catch (InterruptedException e) {
                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
