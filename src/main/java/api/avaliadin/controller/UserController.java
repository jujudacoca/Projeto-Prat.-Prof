package api.avaliadin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import api.avaliadin.repository.UserRepository;




@Controller
@RequestMapping
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private AmizadeRepository amizadeRepository;
	
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
			u.setRole("ROLE USER");
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
		String username_ = authentication.getName();
		User t = userRepository.findByUsername(username_);
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
		System.out.println(username);
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
		String username_ = authentication.getName();
		User u = userRepository.findByUsername(username_);
		Iterable<Amizade> listaSoliAmizade = amizadeRepository.findSolicitacaoById(u.getId());
		model.addAttribute("listaAmizade", listaSoliAmizade);
		return "/indexMembro";
	}
	
	
	@PostMapping("/pediramizade")
	public String pediramizade(@RequestParam int id,Authentication authentication) {
		String username = authentication.getName();
		
		User u = userRepository.findByUsername(username);
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
		a.setEstado("a");
		Date b = new Date();
		a.setDtCad(b);
		amizadeRepository.save(a);
		return "redirect:/indexmembro";
	}
	
	@PostMapping("/deletarconta")
	public String deleteUser(Authentication authentication) {
		String username_ = authentication.getName();
		User t = userRepository.findByUsername(username_);
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
}	