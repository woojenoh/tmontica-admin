package com.internship.tmontica_admin.paging;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaginationTest {

    @Test
    public void 페이징_테스트(){
        Pagination pagination = new Pagination();
        pagination.pageInfo(2, 10, 18);
        System.out.println(pagination.toString());
    }

}