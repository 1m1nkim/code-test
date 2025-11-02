package com.wjc.codetest.product.model.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
    문제 1: 생성자가 여러개라 읽기 불편하고 가독성이 낮아짐
    개선안 : NoArgsConstructor로 디폴트 생성자 사용하여 코드 가독성 높힘
 */
public class GetProductListRequest {
    private String category;
    private int page;
    private int size;
}