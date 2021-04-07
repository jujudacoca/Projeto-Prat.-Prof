package api.avaliadin.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import api.avaliadin.model.User;
import api.avaliadin.repository.UserRepository;




@Controller
@RequestMapping
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/login")
	public String showLogin(User user) {
		return "login";
	}
	@PostMapping("/login")
	public String testeAcesso(@RequestParam String username, @RequestParam String senha) {
		User t = userRepository.findByUsername(username);
		if(t!=null) {
			if(senha.equals(t.getSenha())) {
				return "index";
			}
		}
		return "login";
		
	}
	@GetMapping("/cadastro")
	public String showCadastro(User user) {
	    return "cadastro";
	}
	@PostMapping("/cadastro")
	public String addUser(@RequestParam String username, @RequestParam String senha,@RequestParam String nome,@RequestParam String cidade, @RequestParam String uf) {
		User t = userRepository.findByUsername(username);
		if(t!=null){
			return "cadastro";
		}else {
			User u = new User();
		    u.setUsername(username);
		    u.setSenha(senha);
		    u.setNome(nome);
		    u.setCidade(cidade);
		    u.setUf(uf);
		    Date b = new Date();
			u.setDtCad(b);
		    System.out.println(u.toString());
		    userRepository.save(u);
		    return "index";
		}
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}	