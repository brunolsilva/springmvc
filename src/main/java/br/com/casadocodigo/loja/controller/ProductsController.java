package br.com.casadocodigo.loja.controller;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;
import br.com.casadocodigo.loja.models.User;

@Controller
@Transactional
@RequestMapping("/produtos")
public class ProductsController {
	
	private ProductDAO productDAO;
	private MailSender mailSender;
	private RestTemplate restTemplate;
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new ProductValidator());
//	}
	
	@Autowired
	public ProductsController(ProductDAO productDAO, MailSender mailSender,
			RestTemplate restTemplate) {
		this.productDAO = productDAO;
		this.mailSender = mailSender;
		this.restTemplate = restTemplate;
	}
	
	@RequestMapping("/form")
	public String form(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("types", BookType.values());
		return "products/form";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@Cacheable(value="books")
	public String list(Model model) {
		model.addAttribute("products", productDAO.list());
		return "products/list";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(allEntries = true, value = "books")
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
	
	@RequestMapping("/{id}")
	public String show(@PathVariable Integer id, Model model) {
		Product product = productDAO.find(id);
		model.addAttribute("product", product);
		return "products/show";
	}
	
	@RequestMapping(value="/mail", method = RequestMethod.GET)
	@ResponseBody
	public String mail(@AuthenticationPrincipal User user) {
		sendMail(user);
		return "oi";
	}
	
	@RequestMapping(value = "/rest", method = RequestMethod.GET)
	public String rest() {
		String uriPost = "http://book-payment.herokuapp.com/payment";
		String uriGet = "http://gturnquist-quoters.cfapps.io/api/random";
		
//		String postResponse = restTemplate.postForObject(uriPost, new PaymentData(new BigDecimal(20)), String.class);
//		System.out.println(postResponse);
		
		Quote quote = restTemplate.getForObject(uriGet, Quote.class);
		System.out.println(quote);
		
		productDAO.queryWithTemplate();
		
		return "oi";
	}
	
	private void sendMail(User user) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("");
		mail.setTo(user.getUsername());
		mail.setSubject("Batata");
		mail.setText("Olar");
		mailSender.send(mail);
	}
}


@JsonIgnoreProperties(ignoreUnknown = true)
class Quote {

    private String type;
    private Value value;

    public Quote() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Value {

    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}

class PaymentData {
	private BigDecimal value;
	public PaymentData() { }
	
	public PaymentData(BigDecimal value) {
		this.value = value;
	}
	
	public BigDecimal getValue() {
		return value;
	}
}