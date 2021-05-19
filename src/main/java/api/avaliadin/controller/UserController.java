package api.avaliadin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

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

import api.avaliadin.details.MyUserDetails;
import api.avaliadin.model.*;
import api.avaliadin.recomendation.Ulikes;
import api.avaliadin.repository.AmizadeRepository;
import api.avaliadin.repository.AvaliacaoRepository;
import api.avaliadin.repository.ItemRepository;
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
		model.addAttribute("user", u);
		Iterable<Amizade> listaSoliAmizade = amizadeRepository.findSolicitacaoById(u.getId());
		model.addAttribute("listaAmizade", listaSoliAmizade);
		List<User> listaAmigo = topMatches(gerarLista(true,u.getId()),u.getId());
		List<Item> listaItem = getRecomendation(gerarLista(false,u.getId()),u.getId());
		model.addAttribute("listaAmigo", listaAmigo);
		model.addAttribute("listaItem", listaItem);
		model.addAttribute("l1", count(listaAmigo));
		model.addAttribute("l2", count(listaItem));
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
	public String indexgerente(Model model ,Authentication authentication){
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
    
    
    //recomendacao
    
    public List<Ulikes> gerarLista(boolean amigo, int id) {
		List<Ulikes> l = new ArrayList<Ulikes>(); 
		Iterable<Avaliacao> la = avaliacaoRepository.findAll();
		Map<Integer,Float> map = new HashMap<Integer,Float>();
		if(amigo) {
			Iterable<User> users = userRepository.findAll();
			Iterable<Amizade> amgs = amizadeRepository.findAll();
			List<Integer> lista = new ArrayList<Integer>();
			boolean temamizade = false;
			for(User u: users) {
				temamizade = false;
				if(u.getId()==id){
					continue;
				}
				for(Amizade a: amgs) {
					if((u.getId()==a.getIdUser1())&&(id==a.getIdUser2())||(u.getId()==a.getIdUser2())&&(id==a.getIdUser1())) {
						temamizade = true;
						break;
					}
				}
				if(!temamizade){
					lista.add(u.getId());
				}
			}
			for(int i: lista) {
				for(Avaliacao a: la) {
					if(a.getIdUsuario()==i) {
						map.put(a.getIdItem(), a.getNota());
					}
				}
				Ulikes o = new Ulikes();
				o.setIduser(i);
				o.setLista(map);
				l.add(o);
				map.clear();
			}
		}else {
			Iterable<User> users = userRepository.findAll();
			for(User u : users) {
				for(Avaliacao a: la) {
					if(a.getIdUsuario()==u.getId()) {
						map.put(a.getIdItem(), a.getNota());
					}
				}
				Ulikes o = new Ulikes();
				o.setIduser(u.getId());
				o.setLista(map);
				l.add(o);
				map.clear();
			}
		}
		return l;
	}
    public double sim_pearson(List<Ulikes> l,int a1, int a2) {
		Map<Integer,Float> l1 = new HashMap<Integer,Float>();
		Map<Integer,Float> l2 = new HashMap<Integer,Float>();
		for(Ulikes u : l) {
			if(a1==u.getIduser()) {
				l1 = u.getLista();
			}else if(a2==u.getIduser()) {
				l2= u.getLista();
			}
			if(!l1.isEmpty() &&  !l2.isEmpty()){
				break;
			}
		}
		if(l1.isEmpty() ||  l2==null){
			return -1;
		}
		Map<Integer,Integer> si = new HashMap<Integer,Integer>();
		for(int i1 : l1.keySet() ) {
			if(l2.containsKey(i1)) {
				si.put(i1, 1);
			}
		}
		if (si.isEmpty()) {
			return 0;
		}
		int n = si.size();
		
		//soma
		float sum1 = 0;
		float sum2 = 0;
		//soma das quadrados
		float sum1sq = 0;
		float sum2sq = 0;
		//soma dos produtos
		float psum = 0;
		for(int i: si.keySet()){
			if(l1.containsKey(i) || l2.containsKey(i)) {
				sum1 += l1.get(i);
				sum2 += l2.get(i);
				sum1sq += Math.pow(l1.get(i), 2);
				sum2sq += Math.pow(l2.get(i),2);
				psum += l1.get(i)*l2.get(i);
			}
		}
		double num = psum - ((sum1*sum2)/2);
		double den = Math.sqrt((((sum1sq-Math.pow(sum1, 2))/n)*((sum2sq-Math.pow(sum2, 2))/n)));
		if(den==0) {
			return 0;
		}
		return num/den;
	}
    public List<User> topMatches(List<Ulikes> l,int a1) {
		Map<Integer,Double> list = new HashMap<Integer,Double>();
		double sim = 0;
		
		for(Ulikes other: l) {
			if(other.getIduser()==a1) {
				continue;
			}
			sim = sim_pearson(l,a1,other.getIduser());
			if(sim<=0) {
				sim = sim + 1;
			}
			list.put(other.getIduser(), sim);
		}
		
		List<Entry<Integer, Double>> rank = new ArrayList<>(list.entrySet());
		rank.sort(Entry.comparingByValue());
		int val = rank.size();
		if(rank.size()>=3) {
			val = 3;
		}
		
		List<Entry<Integer, Double>> rank2 = rank.subList(0, val);
		List<User> rec = new ArrayList<User>();
		User u = new User();
		for(int i=0;i<rank2.size();i++) {
			u = userRepository.getUserById(rank2.get(i).getKey());
			if(u!=null) {
				rec.add(u);	
			}
		}
		return rec;
	}
	public List<Item> getRecomendation(List<Ulikes> l,int a1) {
		Map<Integer,Float> l1 = new HashMap<Integer,Float>();
		Map<Integer,Float> l2 = new HashMap<Integer,Float>();
		Map<Integer,Double> totals = new HashMap<Integer,Double>();
		Map<Integer,Double> simsums = new HashMap<Integer,Double>();
		Map<Integer,Double> rank = new HashMap<Integer,Double>();
		double v = 0;
		for(Ulikes u : l) {
			if(a1==u.getIduser()) {
				l1 = u.getLista();
				break;
			}
		}
		for(Ulikes other: l) {
			if(other.getIduser()==a1) {
				continue;
			}
			double sim = sim_pearson(l,a1,other.getIduser());
			if(sim<=0) {
				continue;
			}
			l2 = other.getLista();
			for(int item: l2.keySet()) {
				if(!l1.containsKey(item)) {
					if(!totals.containsKey(item)) {
						totals.put(item,  1.0);
					}
					v = l2.get(item)*sim;
					totals.replace(item, v);
					if(!simsums.containsKey(item)) {
						simsums.put(item, 0.00);
					}
					v = simsums.get(item) + sim;
					simsums.put(item, v);
					v=(totals.get(item)/simsums.get(item));
				}
			}
		}
		for(int item: totals.keySet()) {
			rank.put(item,totals.get(item)/simsums.get(item));
			
		}
		List<Entry<Integer, Double>> list = new ArrayList<>(rank.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);
		int val = list.size();
		if(list.size()>=3) {
			val = 3;
		}
		List<Entry<Integer, Double>> rank2 = list.subList(0, val);
		List<Item> rec = new ArrayList<Item>();
		Item u = new Item();
		int x = 0;
		for(int i=0;i<rank2.size();i++) {
			v = rank2.get(i).getKey();
			u = itemRepository.findById(x);
			rec.add(u);
		}
		if(rec.isEmpty()) {
			Set<Integer> itemv = new HashSet<Integer>();
			for(Ulikes other: l) {
				if(other.getIduser()==a1) {
					itemv = other.getLista().keySet();
				}
			}
			Iterable<Item> items = itemRepository.findAll();
			Iterator<Item> it = items.iterator();
			List<Item> rec2 = new ArrayList<Item>();
			while (it.hasNext()) {
				rec2.add(it.next());
			}
			val = rec2.size();
			if(rec2.size()>=3) {
				val = 3;
			}
			rec = rec2.subList(0, val);
			
		}
		return rec;
	}
}	