package com.product_service.product_service.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.product_service.product_service.model.Product;

@Repository
public class ProductRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 public int save(Product product) {
	        String sql = "INSERT INTO products (name, price, description,stock) VALUES (?, ?, ?,?)";
	        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDescription(),product.getStock());
	    }

	    public List<Product> findAll() {
	        String sql = "SELECT * FROM products";
	        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
	    }

	    public int updateProduct(Product product) {
	        String sql = "UPDATE products SET stock=?, name = ?, description = ?, price = ? WHERE id = ?";
	        return jdbcTemplate.update(sql,product.getStock(), product.getName(), product.getDescription(), product.getPrice(), product.getId());
	    }

	    public int deleteProduct(int id) {
	        String sql = "DELETE FROM products WHERE id = ?";
	        return jdbcTemplate.update(sql, id);
	    }

	    public List<Product> searchProducts(String keyword) {
	        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";
	        String pattern = "%" + keyword.toLowerCase() + "%";
	        return jdbcTemplate.query(sql, new Object[]{pattern, pattern}, new ProductRowMapper());
	    }

	    public List<Product> findByCategory(String category) {
	        String sql = "SELECT * FROM products WHERE category = ?";
	        return jdbcTemplate.query(sql, new Object[]{category}, new ProductRowMapper());
	    }

	    public List<Product> findByPriceBetween(double minPrice, double maxPrice) {
	        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";
	        return jdbcTemplate.query(sql, new Object[]{minPrice, maxPrice}, new ProductRowMapper());
	    }
	    
	    private static class ProductRowMapper implements RowMapper<Product> {
	        @Override
	        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
	            Product product = new Product();
	            product.setId(rs.getInt("id"));
	            product.setName(rs.getString("name"));
	            product.setDescription(rs.getString("description"));
	           //ProductRepository. product.setCategory(rs.getString("category"));
	            product.setPrice(rs.getDouble("price"));
	            product.setStock(rs.getInt("stock"));
	            return product;
	        }
	    }

		
		public Product findById(int id) {
		    String sql = "SELECT * FROM products WHERE id = ?";
		    return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProductRowMapper());
		}

		public boolean reduceStock(Integer productId, Integer quantity) {
		    String selectSql = "SELECT stock FROM products WHERE id = ?";
		    Integer currentStock = jdbcTemplate.queryForObject(selectSql, Integer.class, productId);

		    if (currentStock == null || currentStock < quantity) {
		        return false; 
		    }

		    int newStock = currentStock - quantity;

		    // Update the stock
		    String updateSql = "UPDATE products SET stock = ? WHERE id = ?";
		    int rowsAffected = jdbcTemplate.update(updateSql, newStock, productId);

		    return rowsAffected > 0;
		}

}
