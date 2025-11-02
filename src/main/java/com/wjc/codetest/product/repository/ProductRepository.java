package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategory(String name, Pageable pageable);

    // 문제 : JPQL사용하여 유지보수 힘듬
    // 개선안 : 추후 Mybatis, 또는 QueryDsl 적용
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
