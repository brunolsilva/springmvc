package br.com.casadocodigo.loja.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.conf.ProductValidator;
import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;

@Controller
@Transactional
@RequestMapping("/produtos")
public class ProductsController {
	
	private ProductDAO productDAO;
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new ProductValidator());
//	}
	
	@Autowired
	public ProductsController(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}
	
	@RequestMapping("/form")
	public String form(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("types", BookType.values());
		return "products/form";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("products", productDAO.list());
		return "products/list";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Product product, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("Cadastrando o produto");
		
		if(bindingResult.hasErrors()) {
			System.out.println(bindingResult.getErrorCount());
			model.addAttribute("product", product);
			model.addAttribute("types", BookType.values());
			return "products/form";
		}
			
		productDAO.save(product);
		redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso");
		return "redirect:produtos";
	}
}
