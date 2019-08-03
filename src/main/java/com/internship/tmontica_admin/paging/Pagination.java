package com.internship.tmontica_admin.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pagination {

    private int page;           // 현재 페이지 번호

    private int range;          // 현재 페이지 범위 (1: 1~10, 2:11~20)

    private int totalCnt;        // 전체 게시물의 개수

    private int pageCnt;        // 전체 페이지 총 개수

    private int size;           // 한 페이지당 보여질 리스트의 개수

    private int rangeSize = 10; // 한 페이지 범위에 보여질 페이지의 개수

    private int startPage;      // 현재 페이지 범위 시작 번호

    private int startList;      // 게시판의 시작 번호(db offset)

    private int endPage;        // 현재 페이지 범위 끝 번호

    private boolean prev;       // 이전 페이지 여부

    private boolean next;       // 다음 페이지 여부


    public void pageInfo(int page, int size, int totalCnt) {

        this.page = page;
        this.size = size;
        this.totalCnt = totalCnt;

        //현재 페이지 범위
        this.range = ((page - 1) / rangeSize) + 1;

        //전체 페이지수
        this.pageCnt = (int) Math.ceil((double)totalCnt/size);

        //시작 페이지
        this.startPage = (range - 1) * rangeSize + 1 ;

        //끝 페이지
        this.endPage = range * rangeSize;

        //게시판 시작번호
        this.startList = (page - 1) * size;

        //이전 버튼 상태
        this.prev = range != 1;

        //다음 버튼 상태
        this.next = endPage < pageCnt;

        if (this.endPage > this.pageCnt) {
            this.endPage = this.pageCnt;
            this.next = false;
        }

    }



}
