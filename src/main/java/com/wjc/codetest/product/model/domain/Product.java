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
    /* 
        문제 1: 카테고리는 id 또는 코드 값을 참조 받아 다른 entity로 만드는게 설계 측면에서 좋음
        원인 : 같은 데이터가 너무 많이 중복됨, 하나의 key 데이터가 변경되면 다른 데이터 변경 필요, 유지보수 용이 하지 않음
        개선안 : category entity를 새로 생성하여 외래키 관계로 연결
        개선 후 생길 수 있는 문제 :
        1. 다른 entity로 생성 시 n+1 문제 발생 위험 >> 반환 dto도 추가 생성 하여 반환값을 확실하게 생성해줌
    */
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
