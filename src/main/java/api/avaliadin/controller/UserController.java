package api.avaliadin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import api.avaliadin.model.*;
import api.avaliadin.repository.AmizadeRepository;
import api.avaliadin.repository.AvaliacaoRepository;
import api.avaliadin.repository.ItemRepository;
import api.avaliadin.repository.UserRepository;
import details.MyUserDetails;

@Controller
@RequestMapping
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private AmizadeRepository amizadeRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping("/cadastro")
	public String showCadastro(User user) {
	    return "cadastro";
	}
	@PostMapping("/cadastro")
	public String addUser(@RequestParam String username, @RequestParam String senha,@RequestParam String nome,@RequestParam String cidade, @RequestParam String uf, @RequestParam String dtNasc) throws ParseException {
		User t = userRepository.findByUsername(username);
		if(t!=null){
			return "redirect:/cadastro?error";
		}else {
			User u = new User();
			String teste = username.substring(0, 5);
			if(teste.equals("admin")){
				username = username.replaceAll("admin", "");
				u.setRole("ROLE ADMIN");
			}else if(teste.equals("geren")){
				username = username.replaceAll("geren", "");
				u.setRole("ROLE GM");
			}else {
				u.setRole("ROLE USER");
			}
		    u.setUsername(username);
		    u.setSenha(criptografar(senha));
		    u.setNome(nome);
		    u.setCidade(cidade);
		    u.setUf(uf);
		    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		    if(dtNasc!="") {
		    	Date d = formatter.parse(dtNasc);
		    	u.setDtNasc(d);
		    }
		    Date b = new Date();
			u.setDtCad(b);
			
			u.setEnabled(true);
		    userRepository.save(u);
		    return "redirect:/login";
		}
	}
	@GetMapping("/alterarCadastro")
	public String alterarCadastro(User user) {
	    return "alterarCadastro";
	}
	@PostMapping("/alterarcadastro")
	public String updateUser(Authentication authentication,@RequestParam String username, @RequestParam String senha,@RequestParam String nome,@RequestParam String cidade, @RequestParam String uf, @RequestParam String dtNasc) throws ParseException {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		if (!username.isBlank()){
			t.setUsername(username);
		}
		if (!senha.isBlank()){
			t.setSenha(criptografar(senha));
		}
		if (!nome.isBlank()){
			t.setNome(nome);
		}
		if (!cidade.isBlank()){
			t.setCidade(cidade);
		}
		if (!uf.isBlank()){
			t.setUf(uf);
		}
		if (!dtNasc.isBlank()){
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		    if(dtNasc!="") {
		    	Date d = formatter.parse(dtNasc);
		    	t.setDtNasc(d);
		    }
		}
	
		userRepository.save(t);
		return "redirect:/listaUser";
		
	}
	
	@GetMapping(path="/perfilmembro/{username}")
	public String pagItem(@PathVariable String username, Model model,Authentication authentication) {
		String username_ = authentication.getName();
		User u = userRepository.findByUsername(username);
		if(u!=null) {
			model.addAttribute("user", u);
			Iterable<Avaliacao> listaAvaliacao = avaliacaoRepository.findAllByIdUser(u.getId());
			model.addAttribute("listaAvaliacao", listaAvaliacao);
			Iterable<Amizade> listaAmizade = amizadeRepository.findAllByIdUser(u.getId());
			model.addAttribute("listaAmizade", listaAmizade);
		}else {
			return "redirect:/indexMembro?notfound";//implementar essa excessão ainda 
		}
		if(username.equals(username_)) {
				model.addAttribute("membro", "eu");
		}else {
			User t = userRepository.findByUsername(username_);
			Amizade a = amizadeRepository.findByTwoId(u.getId(), t.getId());
			if(a!=null) {
				if(a.getEstado().equals("a")) {
					model.addAttribute("membro", "amigo");
				}else {
					model.addAttribute("membro", "pedido");
				}
			}else {
				model.addAttribute("membro", "membro");
			}
			
		}
		return "/perfilMembro";
	}
	@GetMapping(path="/indexmembro")
	public String pagRecomendacao(Model model,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		Iterable<Amizade> listaSoliAmizade = amizadeRepository.findSolicitacaoById(u.getId());
		model.addAttribute("listaAmizade", listaSoliAmizade);
		return "/indexMembro";
	}
	
	
	@PostMapping("/pediramizade")
	public String pediramizade(@RequestParam int id,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		User t = userRepository.findById(id);
		Amizade a = new Amizade();
		a.setIdUser1(u.getId());
		a.setIdUser2(t.getId());
		a.setUsername1(u.getUsername());
		a.setUsername2(t.getUsername());
		a.setEstado("s");
		Date b = new Date();
		a.setDtCad(b);
		amizadeRepository.save(a);
		return "redirect:/perfilmembro/"+t.getUsername();
	}
	@PostMapping("/checaramizade")
	public String checaramizade(@RequestParam int id,@RequestParam String check) {
		Amizade a = amizadeRepository.findById(id);
		User u1 = userRepository.findById(a.getIdUser1());
		User u2 = userRepository.findById(a.getIdUser2());
		if(check.equals("s")) {
			a.setEstado("a");
			Date b = new Date();
			a.setDtCad(b);
			u1.setNumAmigos(u1.getNumAmigos()+1);
			u2.setNumAmigos(u2.getNumAmigos()+1);
			amizadeRepository.save(a);
		}else {
			amizadeRepository.delete(a);
		}
		return "redirect:/indexmembro";
	}
	
	@GetMapping(path="/indexadmin")
	public String indexAdmin(Model model ,Authentication authentication){
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		model.addAttribute("user", u);
		Iterable<Item> listaItem = itemRepository.findAllStateFalse();
		model.addAttribute("listaItem", listaItem);
		model.addAttribute("l2", count(listaItem));
		return "/indexadmin";
	}
	@GetMapping(path="/indexgerente")
	public String indexmembro(Model model ,Authentication authentication){
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		model.addAttribute("user", u);
		float numUsers = userRepository.findSomTotalUser(); 
		float numAmigos = userRepository.findSomTotalAmigos(); 
		float numMedio = numAmigos/numUsers;
		model.addAttribute("numMedio", numMedio);
		Pageable top10 = PageRequest.of(0, 10);
		List<User> listatop10 = userRepository.findByOrderByNumAmigosDesc(top10);
		model.addAttribute("listatop10", listatop10);
		return "/indexgerente";
	}
	
	
	@PostMapping("/deletarconta")
	public String deleteUser(Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		userRepository.delete(t);
		return "redirect:/login?logout";
	}
	
	@RequestMapping(value = "/listaUser", method = RequestMethod.GET)
    public String listaUsers(Model model) {
		Iterable<User> listaUser = userRepository.findAll();
        if (listaUser != null) {
        	model.addAttribute("users", listaUser);
        }
        return "listaUser";
    }
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
    public static String criptografar(String rawPassword) {
   	 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
   }
    public int count(Iterable a) {
		int counter = 0;
		for (Object i : a) {
		    counter++;
		}
		return counter;
	}
}	