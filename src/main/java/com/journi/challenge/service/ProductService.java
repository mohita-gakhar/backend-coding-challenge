package com.journi.challenge.service;

import com.journi.challenge.util.currency.CountryCurrencyUtil;
import com.journi.challenge.models.Product;
import com.journi.challenge.repositories.ProductsRepository;
import com.journi.challenge.util.currency.CurrencyConverterUtil;
import com.journi.challenge.vo.ProductVO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Service
public class ProductService {

    @Inject
    private ProductsRepository productsRepository;

    @Inject
    private CountryCurrencyUtil countryCurrencyUtil;

    @Inject
    private CurrencyConverterUtil currencyConverterUtil;

    public List<ProductVO> getAllProducts(String countryCode) {
        String currencyCode = countryCurrencyUtil.getCurrencyForCountryCode(countryCode);
        List<Product> products = productsRepository.list();
        return products.stream().map(p -> new ProductVO(p, currencyCode,
                currencyConverterUtil.convertCurrencyToEuro(currencyCode, p.getPrice()))).collect(Collectors.toList());
    }
}
