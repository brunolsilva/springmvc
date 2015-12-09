package br.com.casadocodigo.loja.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.casadocodigo.loja.models.Product;

@Repository
public class ProductDAO {
	
	@PersistenceContext
	private EntityManager manager;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ProductDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void save(Product product) {
		manager.persist(product);
	}
	
	public void queryWithTemplate() {
		System.out.println(this.jdbcTemplate.queryForObject("select count(*) from Product", Integer.class));
		List<Product> products = this.jdbcTemplate.query(
				"select id, description from Product",
				new RowMapper<Product>() {
					public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
						Product product = new Product();
						product.setId(rs.getInt("id"));
						product.setDescription(rs.getString("description"));
						return product;
					}
		});
		for (Product product : products) {
			System.out.println(product.getDescription());
		}
		
		String desc = this.jdbcTemplate.queryForObject("select description from Product where id = ?", new Object[]{1}, String.class);
		System.out.println();
		System.out.println(desc);
	}
	
	public List<Product> list() {
		return manager.createQuery("select distinct(p) from "
				+ "Product p join fetch p.prices", Product.class).getResultList();
	}
	
	public Product find(Integer id) {
		return manager.createQuery("select p from "
				+ "Product p where p.id = :id", Product.class).setParameter("id", id).getSingleResult();
	}
}
