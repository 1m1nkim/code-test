package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*  문제1 : 엔티티 클래스에서 Setter 사용 시
    원인 : 객체의 불변성 특성이 사라짐
    개선안 : Setter 사용하지 않고 Builder 패턴을 통해 더 유연한 Entity 구조 생성

    문제 2 : Getter 어노테이션 존재하는데 get 메소드 추가적으로 존재
    원인 : get 메소드의 중복
    개선안 : 중복 get 메소드를 제거하여 Getter 어노테이션을 사용

    문제 3: 생성자가 여러개라 읽기 불편하고 가독성이 낮아짐
    개선안 : NoArgsConstructor로 디폴트 생성자 사용하여 코드 가독성 높힘
*/
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;
    // 카테고리는 id 또는 코드 값을 참조 받아 다른 entity로 만드는게 관리 측면에서는 좋아보임

    @Column(name = "name")
    private String name;

    @Builder
    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public void update(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
