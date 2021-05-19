package api.avaliadin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.avaliadin.details.MyUserDetails;
import api.avaliadin.model.*;
import api.avaliadin.repository.ItemRepository;
import api.avaliadin.repository.UserRepository;

@Controller
@RequestMapping
public class PesquisaController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItemRepository itemRepository;

	@GetMapping("/pesquisa")
	public String showPesquisa(Model model, @RequestParam String pesquisa,Authentication authentication) {
		Iterable<User> listaUser= userRepository.pesquisa(pesquisa);
		model.addAttribute("l1", count(listaUser));
		Iterable<Item> listaItem= itemRepository.pesquisa(pesquisa);
		model.addAttribute("l2", count(listaItem));
		model.addAttribute("listaUser", listaUser);
		model.addAttribute("listaItem", listaItem);
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		model.addAttribute("User", u);
		
	    return "pesquisa";
	}
	
	@PostMapping("/pesquisar")
	public String pesquisar(@RequestParam String pesquisa) {
		
		return "redirect:/pesquisa?pesquisa="+pesquisa;
	}
	
	public int count(Iterable a) {
		int counter = 0;
		for (Object i : a) {
		    counter++;
		}
		return counter;
	}
}
