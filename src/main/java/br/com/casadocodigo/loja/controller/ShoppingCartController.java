package br.com.casadocodigo.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;
import br.com.casadocodigo.loja.models.ShoppingCart;
import br.com.casadocodigo.loja.models.ShoppingItem;

@Controller
@RequestMapping("/shopping")
public class ShoppingCartController {
	
	private ProductDAO productDAO;
	private ShoppingCart shoppingCart;
	
	@Autowired
	public ShoppingCartController(ProductDAO productDAO, ShoppingCart shoppingCart) {
		this.productDAO = productDAO;
		this.shoppingCart = shoppingCart;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String add(Integer productId, BookType bookType) {
		ShoppingItem item = createItem(productId, bookType);
		shoppingCart.add(item);
		return "redirect:/produtos";
	}
	
	private ShoppingItem createItem(Integer productId, BookType bookType) {
		Product product = productDAO.find(productId);
		ShoppingItem item = new ShoppingItem(product, bookType);
		return item;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String items(){
		return "shoppingCart/items";
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{productId}")
	public String remove(@PathVariable("productId") Integer productId,BookType bookType){
		shoppingCart.remove(createItem(productId, bookType));
		return "redirect:/shopping";
	}
}
