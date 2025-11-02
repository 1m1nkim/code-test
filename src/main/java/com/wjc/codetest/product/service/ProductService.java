package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.model.response.ProductResponseDto;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/*
    문제1 : Entity 직접반환
    원인 :
    1. Entity 내부의 모든 데이터가 반환 됨
    2. 추후 카테고리가 엔티티로 분리 된다면 순환 참조 발생 가능성
    해결점 :
    ProductResponseDto 를 신규 생성하여 필요 데이터만 반환

    문제2 : 트랜잭션 처리 되지 않은 service 계층
    원인 : 데이터 CRUD 시 정확하지 않은 데이터 반환 가능성 생김
    해결점 : 트랜잭션 명시, 추후 exception 같은 롤백 상황 있을 시 트랜잭션 추가 기능 사용 ex)rollbackFor=

    문제3 : Optional 클래스 사용
    원인 : 코드의 가독성 > 너무 긴 코드,
    해결점 : orElseThrow, 람다식을 통해 한번에 exception과 데이터 조회를 가독성 있게 1줄로 return 시킴

    문제4 : update 메소드 구조 변경
    원인 : update 시 id까지 한번에 받아 save 하는 형식사용
    해결점 : save 메소드를 사용하지 않고 jpa 의 특성을 활용하여 직접 데이터 변경 후 jpa의 자동 update 활용
 */
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    // 상품 생성 시 트랜잭션 보장
    public ProductResponseDto create(CreateProductRequest dto) {
        //커스텀 반환 Dto 를 생성하여 반환
        // create 에서는 id를 builder로 초기화 하지 않아 수정 불가능
        // 엔티티 상 product는 builder 패턴으로 교체하였음
        Product product = Product.builder()
                .category(dto.getCategory())
                .name(dto.getName())
                .build();
        Product saveProduct = productRepository.save(product);
        return new ProductResponseDto(saveProduct);
    }

    @Transactional(readOnly = true)
    //단일 데이터 조회 시 데이터 보장
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));
        //가독성을 위해 optional 새로 생성하지 않고 바로 return
    }

    @Transactional
    public ProductResponseDto update(Long productId, UpdateProductRequest dto) {
        //업데이트 시 현재 dto id 값을 확인 후
        Product product = getProductById(productId);
        product.update(dto.getCategory(), dto.getName());
        //save 하지 않는 이유 : jpa가 관리하는 데이터 변경 시 커밋 시점에 자동 update 처리
        return new ProductResponseDto(product);
    }

    @Transactional
    //삭제 시 해당 데이터 조회 후 삭제 > 두개의 트랜잭션 보장 필요
    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductListResponse getProductList(String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category"));

        Page<Product> productPage = productRepository.findAllByCategory(category, pageRequest);

        // for each 문법을 사용하여 list 화
        // Stream 보다는 효율이 좋아 for 문 활용
        List<ProductResponseDto> productList = new ArrayList<>();
        for (Product product : productPage) {
            productList.add(new ProductResponseDto(product));
        }

        return new ProductListResponse(productList,productPage.getTotalPages(),productPage.getTotalElements(),productPage.getNumber());
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}