package api.avaliadin.controller;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.DisplayChart;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import api.avaliadin.details.MyUserDetails;
import api.avaliadin.model.Amizade;
import api.avaliadin.model.Avaliacao;
import api.avaliadin.model.Item;
import api.avaliadin.model.Recomendacao;
import api.avaliadin.model.User;
import api.avaliadin.recomendation.GraficoDeBarra;
import api.avaliadin.recomendation.Ufc;
import api.avaliadin.repository.AmizadeRepository;
import api.avaliadin.repository.AvaliacaoRepository;
import api.avaliadin.repository.ComentarioRepository;
import api.avaliadin.repository.ItemRepository;
import api.avaliadin.repository.JoinhaRepository;
import api.avaliadin.repository.RecomendacaoRepository;
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
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private RecomendacaoRepository rp;
	@Autowired
	private ComentarioRepository comentarioRepository;
	@Autowired
	private JoinhaRepository joinhaRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping("/cadastro")
	public String showCadastro(User user) throws IOException {
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
		    u.setNumJoinha(0);
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
	public String alterarCadastro(Model model,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		model.addAttribute("user", t);
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
		return "redirect:/indexmembro";
		
	}
	
	@GetMapping(path="/perfilmembro/{username}")
	public String pagMembro(@PathVariable String username, Model model,Authentication authentication) {
		String username_ = authentication.getName();
		model.addAttribute("log", username_);
		User u = userRepository.findByUsername(username);
		if(u!=null) {
			model.addAttribute("user", u);
			Iterable<Avaliacao> listaAvaliacao = avaliacaoRepository.findAllByIdUser(u.getId());
			model.addAttribute("listaAvaliacao", listaAvaliacao);
			model.addAttribute("l1", count(listaAvaliacao));
			Iterable<Amizade> listaAmizade = amizadeRepository.findAllByIdUser(u.getId());
			model.addAttribute("listaAmizade", listaAmizade);
			model.addAttribute("l2", count(listaAmizade));
		}else {
			return "redirect:/indexmembro?notfound";
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
		return "perfilMembro";
	}
	@GetMapping(path="/indexmembro")
	public String pagRecomendacao(Model model,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User u = m.getUser();
		model.addAttribute("user", u);
		Iterable<Amizade> listaSoliAmizade = amizadeRepository.findSolicitacaoById(u.getId());
		model.addAttribute("listaAmizade", listaSoliAmizade);
		model.addAttribute("l3", count(listaSoliAmizade));
		List<User> listaAmigo = new ArrayList<User>();
		List<Item> listaItem = new ArrayList<Item>();
		Recomendacao r = rp.recomendadoById(u.getId());
		
		if(r!=null) {
			Iterable<User> users = userRepository.findAll();
			Iterator<User> itu = users.iterator();
			List<Integer> l1 = new ArrayList<Integer>();
			l1.add(r.getIduser1());
			l1.add(r.getIduser2());
			l1.add(r.getIduser3());
			int count = 0;
			while(itu.hasNext()) {
				User idt = itu.next();
				for(int j = 0; j<3;j++) {
					int o = l1.get(j);
					if(idt.getId()==o) {
						listaAmigo.add(idt);
						count++;
					}
				}
				if(count>=3) {
					break;
				}
			}
		
			
			Iterable<Item> items = itemRepository.findAll();
			Iterator<Item> iti = items.iterator();
			List<Integer> l2 = new ArrayList<Integer>();
			l2.add(r.getIdItem1());
			l2.add(r.getIdItem2());
			l2.add(r.getIdItem3());
			count = 0;
			while(iti.hasNext()) {
				Item idt = iti.next();
				for(int j = 0; j<3;j++) {
					int o = l2.get(j);
					if(idt.getId()==o) {
						listaItem.add(idt);
						count++;
					}
				}
				if(count>=3) {
					break;
				}
			}

		}
		model.addAttribute("listaAmigo", listaAmigo);
		model.addAttribute("listaItem", listaItem);
		model.addAttribute("l1", count(listaAmigo));
		model.addAttribute("l2", count(listaItem));
		return "indexMembro";
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
		a.setNome1(u.getNome());
		a.setNome2(t.getNome());
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
		return "indexadmin";
	}
	@GetMapping(path="/indexgerente")
	public String indexgerente(Model model ,Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws IOException{
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
		
		//gerar dados grafico
		List<Ufc> lista = new ArrayList<Ufc>();
		List<String> teste = userRepository.getCountUf();
		System.out.println(teste.size());
		for(int i = 0; i<teste.size();i++){
			String msg = teste.get(i);
			int j = msg.indexOf(",");
			int valor = Integer.parseInt(msg.substring(j+1,msg.length()));
			lista.add(new Ufc(msg.substring(0,j),valor));
		}
		
		GraficoDeBarra grafico = new GraficoDeBarra();
		CategoryDataset dataset = grafico.createDataSet(lista);
		JFreeChart graficoBarra = grafico.createBarChart(dataset,lista.size());
        String filename = ServletUtilities.saveChartAsPNG(graficoBarra, 500, 500, null,request.getSession());
        String chartURL = request.getContextPath() + "/chart?filename="+filename;
		model.addAttribute("makeline", chartURL);
		return "indexgerente";
	}
	@Bean
	public ServletRegistrationBean<DisplayChart> MyServlet() {
		return new ServletRegistrationBean<>(new DisplayChart(),"/chart");
	}
	
	@Transactional
	@PostMapping("/deletarconta")
	public String deleteUser(Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		amizadeRepository.deleteByIdUser1(t.getId());
		amizadeRepository.deleteByIdUser2(t.getId());
		userRepository.delete(t);
		comentarioRepository.deleteByIdUsuario(t.getId());
		joinhaRepository.deleteByIdUsuario(t.getId());
		avaliacaoRepository.deleteByIdUsuario(t.getId());
		return "redirect:/login?logout";
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