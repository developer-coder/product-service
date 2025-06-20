package com.product_service.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product_service.product_service.model.Product;
import com.product_service.product_service.repository.ProductRepository;
import com.product_service.product_service.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3002" ,"http://localhost:3005"}, allowCredentials = "true")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;

	@PostMapping("/add")
	public ResponseEntity<String> addProduct(@RequestBody Product product) {
		System.out.println("product details "+product);
		productService.createProduct(product);
		return ResponseEntity.ok("Product added successfully");
	}

	@GetMapping("/list")
	public ResponseEntity<List<Product>> getProducts() {
		System.out.println("get all product list");
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@PutMapping("updateProduct/{id}")
	public String updateProduct(@PathVariable("id") int id, @RequestBody Product product) {
		System.out.println("product to update " + product);
		product.setId(id);
		productService.updateProduct(product);
		return "Product updated successfully";
	}

	@DeleteMapping("deleteProduct/{id}")
	public String deleteProduct(@PathVariable("id") int id) {
		productService.deleteProduct(id);
		return "Product deleted successfully";
	}
	

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productRepository.searchProducts(keyword);
    }

    @GetMapping("/filter")
    public List<Product> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        if (category != null) {
            return productRepository.findByCategory(category);
        } else if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            return productRepository.findAll();
        }
    }
    
    @PostMapping("/update-stock")
    public ResponseEntity<Product> updateStock(@RequestBody Product request) {
    	System.out.println("request type "+request);
        Product productOpt = productRepository.findById(request.getId());

        if (productOpt==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        if (productOpt.getStock() < request.getStock()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        productOpt.setStock(productOpt.getStock() - request.getStock());
      
        productRepository.updateProduct(productOpt);

        return ResponseEntity.ok(productOpt);
    }
    
    
    @PostMapping("/update-stock-using-productId")
    public ResponseEntity<String> reduceStock(
            @RequestParam("productId") Integer productId,
            @RequestParam ("quantity") Integer quantity) {
    	System.out.println("in updatestock "+productId +quantity);

        boolean success = productRepository.reduceStock(productId, quantity);
        if (success) {
            return ResponseEntity.ok("Stock updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock update failed");
        }
    }


}
