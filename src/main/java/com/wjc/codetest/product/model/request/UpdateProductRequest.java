package com.wjc.codetest.product.model.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
    문제 1 : 생성자가 여러개라 읽기 불편하고 가독성이 낮아짐
    개선안 : NoArgsConstructor로 디폴트 생성자 사용하여 코드 가독성 높힘

    문제 2 : Id를 url에 함께 받아 관리
    개선안 : PathVariable로 변경하여 product를 id로 받아 업데이트 하는게 아니라 프론트나 이외 실행 시 명시되어 받을 수 있음
 */
public class UpdateProductRequest {
    private String category;
    private String name;
}

