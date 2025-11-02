package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 엔티티 직접 반환을 막기 위한 신규 ResponseDto 생성
public class ProductResponseDto {
    private Long id;
    private String category;
    private String name;

    public ProductResponseDto(Product dto) {
        this.id = dto.getId();
        this.category = dto.getCategory();
        this.name = dto.getName();
    }
}
