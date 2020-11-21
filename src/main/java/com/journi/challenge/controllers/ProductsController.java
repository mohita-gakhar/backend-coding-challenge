package com.journi.challenge.controllers;

import com.journi.challenge.models.Product;
import com.journi.challenge.repositories.ProductsRepository;
import com.journi.challenge.service.ProductService;
import com.journi.challenge.vo.ProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class ProductsController {

    @Inject
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductVO> list(@RequestParam(name = "countryCode", defaultValue = "AT") String countryCode) {
        return productService.getAllProducts(countryCode);
    }
}
