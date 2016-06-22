package cn.chenlichao.web.ssm.service;

import org.junit.Test;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.assertArrayEquals;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/16 下午6:00
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class PageResultsTest {

    private static final PageParams pp = new PageParams();
    static {
        pp.setPageSize(10);
        pp.setPageIndex(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getPageList() throws Exception {
        PageResults pr = new PageResults(77, EMPTY_LIST, pp);
        assertArrayEquals("10页以内", new int[]{1,2,3,4,5,6,7,8}, pr.getPageList());

        pr = new PageResults(88, EMPTY_LIST, pp);
        assertArrayEquals("1/9", new int[]{1,2,3,4,5,6,7,8,9}, pr.getPageList());

        pr = new PageResults(98, EMPTY_LIST, pp);
        assertArrayEquals("1/10", new int[]{1,2,3,4,5,6,7,8,9}, pr.getPageList());

        pp.setPageIndex(2);
        pr = new PageResults(98, EMPTY_LIST, pp);
        assertArrayEquals("2/10", new int[]{1,2,3,4,5,6,7,8,9}, pr.getPageList());

        pp.setPageIndex(5);
        pr = new PageResults(98, EMPTY_LIST, pp);
        assertArrayEquals("5/10", new int[]{1,2,3,4,5,6,7,8,9}, pr.getPageList());

        pp.setPageIndex(6);
        pr = new PageResults(98, EMPTY_LIST, pp);
        assertArrayEquals("6/10", new int[]{2,3,4,5,6,7,8,9,10}, pr.getPageList());

        pp.setPageIndex(7);
        pr = new PageResults(98, EMPTY_LIST, pp);
        assertArrayEquals("7/10", new int[]{2,3,4,5,6,7,8,9,10}, pr.getPageList());

        pp.setPageIndex(7);
        pr = new PageResults(198, EMPTY_LIST, pp);
        assertArrayEquals("7/20", new int[]{3,4,5,6,7,8,9,10,11}, pr.getPageList());

        pp.setPageIndex(10);
        pr = new PageResults(198, EMPTY_LIST, pp);
        assertArrayEquals("10/20", new int[]{6,7,8,9,10,11,12,13,14}, pr.getPageList());

        pp.setPageIndex(17);
        pr = new PageResults(198, EMPTY_LIST, pp);
        assertArrayEquals("17/20", new int[]{12,13,14,15,16,17,18,19,20}, pr.getPageList());

        pp.setPageIndex(19);
        pr = new PageResults(198, EMPTY_LIST, pp);
        assertArrayEquals("19/20", new int[]{12,13,14,15,16,17,18,19,20}, pr.getPageList());
    }

}