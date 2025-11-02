package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.model.response.ProductResponseDto;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
/*  문제1 : RestController 사용 하지만 공통 url 사용하지 않음
    원인 :
    1. 공통으로 사용되고 있는 /product url을 파일 단위 공통 url 처리
    2. GetMapping 에 굳이 get/by라는 표현이 들어가지 않아도 됨 > 이미 요청 시 get 요청을 보내는데 url에 불필요한 정보,
    개선안 : 가독성 및 restapi 특성 상 한눈에 보기 쉬워야함, >> Get, Post, Put, Delete 분리, rest에 맞지 않는 추가적인 url 삭제

    문제2 : Entity 직접반환
    원인 :
    1. Entity 내부의 모든 데이터가 반환 됨
    2. 추후 카테고리가 엔티티로 분리 된다면 순환 참조 발생 가능성
    해결점 : ProductResponseDto 를 신규 생성하여 필요 데이터만 반환

 */
@RequestMapping(value ="/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(new ProductResponseDto(product));
    }

    @PostMapping
    // Post /product의 의미는 이미 create 이기 떄문에 굳이 추가적인 url 필요 x
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateProductRequest dto){
        ProductResponseDto productResponseDto = productService.create(dto);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping(value = "/{productId}")
    // DeleteMapping 이 존재하는데 Post를 사용할 이유 x >> rest가 아님
    // 만약 논리적 삭제를 위함 이였다면 엔티티에서 delete 여부를 확인할 수 있는 칼럼이나 Service에서 데이터 set 로직이 들어가야함
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(Map.of("message", "204 No Content, 상품이 삭제되었습니다."));
    }

    @PutMapping(value = "/{productId}")
    // 데이터의 id를 url 과 함께 입력, 변경 데이터는 json으로 받음
    // Post와 Put은 rest 적으로 Post가 create, Put이 수정의 의미가 더 큼
    // 분리하면 url 구성도 간단해짐
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest dto){
        ProductResponseDto productResponseDto = productService.update(productId, dto);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping(value = "/list")
    // 조회 로직인데 Post > Get api로 변경
    // dto로 받지 않고 json으로 파라미터 받음
    // 메소드 오버라이딩 되었어도 다른 역할인 메소드의 이름이 같은건 x
    public ResponseEntity<ProductListResponse> getProductList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductListResponse response = productService.getProductList(category, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    //메소드 이름 및 url 변경
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getUniqueCategories();
        return ResponseEntity.ok(categories);
    }
}