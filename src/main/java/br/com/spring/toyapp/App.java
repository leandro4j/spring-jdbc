package br.com.spring.toyapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.com.spring.toyapp.data.ArticleData;

@SpringBootApplication
public class App implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Executing query...");
        
        querySigleLine();
        querySigleLineWrapper();
        querySigleLineAndColumn();

        queryMultipleLines();
    }
    
    private void querySigleLine() {
    	String query = "select id, title, url_external_repository as url from article where id = ?";
        ArticleData article = jdbcTemplate.queryForObject(query, new Object[] { 18489 }, new BeanPropertyRowMapper<ArticleData>(ArticleData.class));
        
        log.info("querySigleLine() article = {}", article);
    }
    
    private void querySigleLineAndColumn() {
    	String query = "select title from article where id = ?";
        String articleTitle = jdbcTemplate.queryForObject(query, new Object[] { 18489 }, String.class);
        
        log.info("querySigleLineAndColumn() article = {}", articleTitle);
    }
    
    private void querySigleLineWrapper() {
    	 String query = "select id, title, url_external_repository as url from article where id = ?";
         ArticleData article = jdbcTemplate.queryForObject(
         		query, new Object[] { 18489 }, new RowMapper<ArticleData>() {

 					@Override
 					public ArticleData mapRow(ResultSet rs, int rowNum) throws SQLException {
 						ArticleData article = new ArticleData();
 						article.setId(rs.getInt("id"));
 						article.setTitle(rs.getString("title"));
 						article.setUrl(rs.getString("url"));
 						return article;
 					}
         		});
         
         log.info("querySigleLineWrapper() article = {}", article);
    }
    
    private void queryMultipleLines() {
    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
        		"select id, title, url_external_repository as url from article where title like ?", 
        		new Object[] { "%Venda%" }
        	);
        
        for(Map<String, Object> row : rows) {
        	log.info("queryMultipleLines() id = {}, title = {}, url = {}", row.get("id"), row.get("title"), row.get("url"));
        }
    }
}