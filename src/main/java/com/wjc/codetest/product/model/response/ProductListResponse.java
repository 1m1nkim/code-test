package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
@Getter
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponseDto> products;
    private int totalPages;
    private long totalElements;
    private int page;

}
