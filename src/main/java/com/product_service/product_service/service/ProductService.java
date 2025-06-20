package com.product_service.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product_service.product_service.model.Product;
import com.product_service.product_service.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public int createProduct(Product product) {
    	System.out.println("product details "+product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
       List<Product> productList=productRepository.findAll();
       System.out.println("productList "+productList);
       return productList;
    }




	public String updateProduct(Product product) {
		// TODO Auto-generated method stub
		int result= productRepository.updateProduct(product);
		if(result>0)
		{
			return "Product updated Successfully";
		}
		else
			return "Not updated";
	}

	public String deleteProduct(int id) {
		int result= productRepository.deleteProduct(id);
		if(result>0)
		{
			return "Product Deleted Successfully";
		}
		else
			return "Not Deleted";
		}
		
}
