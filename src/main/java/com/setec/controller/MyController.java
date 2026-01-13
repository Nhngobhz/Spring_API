package com.setec.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.setec.entities.PostProductDAO;
import com.setec.entities.Product;
import com.setec.entities.PutProductDAO;
import com.setec.repos.ProductRepo;

@RestController
@RequestMapping("/api/product")
public class MyController {
	
	@Autowired
	private ProductRepo productRepo;
	
	@GetMapping
	public Object getAll() {
		var products = productRepo.findAll();
		if (products.isEmpty()) {
			return "No products found.";
		}
		
		return productRepo.findAll();
	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addProduct(@ModelAttribute PostProductDAO postProductDAO) 
			throws Exception {
		String uploadDir = new File("myApps/Static").getAbsolutePath();
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		var file = postProductDAO.getFile();
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		String filePath = Paths.get(uploadDir,uniqueFileName).toString();
		
		file.transferTo(new File(filePath));
		
		var pro = new Product();
		pro.setName(postProductDAO.getName());
		pro.setPrice(postProductDAO.getPrice());
		pro.setQty(postProductDAO.getQty());
		pro.setImageUrl("/Static/" + uniqueFileName);
		
		productRepo.save(pro);
		
		
		return ResponseEntity.status(201).body(pro);
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateProduct(@ModelAttribute PutProductDAO putProductDAO) 
			throws Exception {
		
		var p = productRepo.findById(putProductDAO.getId());
		
		if(p.isPresent()) {
			var prod = p.get();
			prod.setName(putProductDAO.getName());
			prod.setPrice(putProductDAO.getPrice());
			prod.setQty(putProductDAO.getQty());
			
			
			if (putProductDAO.getFile() != null) {
				String uploadDir = new File("myApps/Static").getAbsolutePath();
				File dir = new File(uploadDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				var file = putProductDAO.getFile();
				String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
				String filePath = Paths.get(uploadDir,uniqueFileName).toString();
				
				new File("myApps" + prod.getImageUrl()).delete();
				
				file.transferTo(new File(filePath));
				
				prod.setImageUrl("/Static/" + uniqueFileName);
			}
			
			productRepo.save(prod);
			return ResponseEntity.ok(prod);
//			return ResponseEntity.status(HttpStatus.ACCEPTED).body(prod);
		}
		
		// Implementation for updating a product goes here
		return ResponseEntity.status(404).body(Map.of("message", "Product id = "+putProductDAO.getId()+" not found."));
	}
	
	@GetMapping({"/{id}", "/id/{id}"})
	public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
		var p = productRepo.findById(id);
		if (p.isPresent()) {
			return ResponseEntity.ok(p.get());
		}
		return ResponseEntity.status(404).body(Map.of("message", "Product id = "+id+" not found."));
	}
	
	@DeleteMapping({"/{id}", "/id/{id}"})
	public ResponseEntity<?> deleteProductById(@PathVariable("id") Integer id) {
		var p = productRepo.findById(id);
		if (p.isPresent()) {
			var prod = p.get();
			new File("myApps" + prod.getImageUrl()).delete();
			productRepo.deleteById(id);
			return ResponseEntity.ok(Map.of("message", "Product id = "+id+" deleted successfully."));
		}
		return ResponseEntity.status(404).body(Map.of("message", "Product id = "+id+" not found."));
	}
	
}
