package api.avaliadin.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import api.avaliadin.details.MyUserDetails;
import api.avaliadin.model.*;
import api.avaliadin.repository.*;

@Controller
@RequestMapping
public class AvaliacaoController {
	
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private ComentarioRepository comentarioRepository;
	@Autowired
	private JoinhaRepository joinhaRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/criaAvaliacao")
	public String showCriaAvaliacao (Model model, @RequestParam int id) {
		Item i = itemRepository.findById(id);
		model.addAttribute("Item", i);
		return "criaAvaliacao";
	}
	
	@PostMapping("/criaAvaliacao")
	public String addAvaliacao(@RequestParam( required = false) String descricao,@RequestParam( required = false) Integer nota, @RequestParam int id, Authentication authentication ) {
		String username = authentication.getName();
		User t = userRepository.findByUsername(username);
		Item p = itemRepository.findById(id);
		Avaliacao b =  avaliacaoRepository.findByIds(t.getId(), p.getId());
		if(b!=null) {
			return "redirect:/paginaitem/"+p.getId();
		}
		if(t!=null && p!=null) {
			Avaliacao a = new Avaliacao();
			if(nota==null) {
				a.setNota(0);
			}
			a.setNota(nota);
			a.setDescricao(descricao);
			a.setIdUsuario(t.getId());
			a.setIdItem(id);
			Date D = new Date();
			a.setDtCad(D);
			a.setNumJoinha(0);
			a.setUsername(username);
			a.setTitulo(p.getTitulo());
			a.setNome(t.getNome());
			avaliacaoRepository.save(a);
		}
		return "redirect:/paginaavaliacao/"+b.getId();
	}
	@PostMapping("/updateAvaliacao/{id}")
	public String updateAvaliacao(@RequestParam( required = false) String descricao ,@RequestParam( required = false) Integer nota,@PathVariable int id , Authentication authentication ) {
		String username = authentication.getName();
		Avaliacao b =  avaliacaoRepository.findById(id);
		if(b!=null) {
			if(nota!=null) {
				b.setNota(nota);
			}else {
				b.setNota(0);
			}
			if(descricao!="" && descricao!=null  ) {
				b.setDescricao(descricao);
			}
			avaliacaoRepository.save(b);
		}
		return "redirect:/paginaavaliacao/"+b.getId();
	}
	
	@PostMapping("/deletaravaliacao/{id}")
	public String deletarAvaliacao(@PathVariable int id) {
		avaliacaoRepository.deleteById(id);
		return "redirect:/indexmembro";
	}
	
	@GetMapping(path="/allAval")
	public @ResponseBody Iterable<Avaliacao> getAllAvalicaos() {
		return avaliacaoRepository.findAll();
	}
	
	@PostMapping("/darJoinha")
	public String addJoinhapgitem(@RequestParam int id,@RequestParam String ind,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		Avaliacao a = avaliacaoRepository.findById(id);
		Joinha j = joinhaRepository.existeJoinha(t.getId(), a.getId());
		if(j!=null) {
			joinhaRepository.delete(j);
			int y = a.getNumJoinha()-1;
			a.setNumJoinha(y);
			avaliacaoRepository.save(a);
			if(ind.equals("pgitem")) {
				return "redirect:/paginaitem/"+a.getIdItem()+"?curtido";
			}else if(ind.equals("pgmembro")) {
				return "redirect:/perfilmembro/"+a.getUsername()+"?curtido";
			}
		}else {
			Joinha o = new Joinha();
			o.setIdAvaliacao(a.getId());
			o.setIdUsuario(t.getId());
			Date b = new Date();
			o.setDtCad(b);
			joinhaRepository.save(o);
			int x = a.getNumJoinha()+1;
			a.setNumJoinha(x);
			
		}
		avaliacaoRepository.save(a);
		if(ind.equals("pgitem")) {
			return "redirect:/paginaitem/"+a.getIdItem();
		}else {
			return "redirect:/perfilmembro/"+a.getUsername();
		}	
		
	}
	
	@PostMapping("/comentar")
	public String addComentario(@RequestParam String comentario,@RequestParam int id,@RequestParam String ind,Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		Avaliacao a = avaliacaoRepository.findById(id);
		Comentario c = new Comentario();
		c.setDescricao(comentario);
		//c.setIdAvaliacao(a.getId());
		c.setAvaliacao(a);
		c.setIdUsuario(t.getId());
		c.setUsername(t.getUsername());
		comentarioRepository.save(c);
		if(ind.equals("pgitem")) {
			return "redirect:/paginaitem/"+a.getIdItem();
		}else {
			return "redirect:/perfilmembro/"+a.getUsername();
		}	
	}
	
	@PostMapping("/deletarcomentario/{id}")
	public String deleteComentario(@PathVariable int id) {
		comentarioRepository.deleteById(id);
		Comentario c = comentarioRepository.findById(id);
		return "redirect:/paginaavaliacao/"+ c.getAvaliacao();
	}
	
	
	@GetMapping(path="/paginaavaliacao/{id}")
	public String pagAvaliacao(@PathVariable int id, Model model, Authentication authentication) {
		MyUserDetails m = (MyUserDetails) authentication.getPrincipal();
		User t = m.getUser();
		Avaliacao a = avaliacaoRepository.findById(id);
		
		System.out.println("Inicio");
		
		if(a!=null) {
			Item i = itemRepository.findById(a.getIdItem());
			model.addAttribute("Item", i);
			model.addAttribute("avaliacao", a);
			model.addAttribute("l5",a.getComentarios().size());
			model.addAttribute("user", t);
			if(t.getId()==a.getIdUsuario()){
				
				model.addAttribute("membro", "eu");
				
			}else {
					model.addAttribute("membro", "outro");
			}
			System.out.println("final");
			return "paginaavaliacao";
			
		}
		
		return "redirect:/indexmembro?itemnotfound";//implementar essa excessão ainda 
	}
	
	
	@PostMapping("/deletarconta/{id}")
	public String deleteAval(@PathVariable int id) {
		Avaliacao a = avaliacaoRepository.findById(id);
		avaliacaoRepository.delete(a);
		return "redirect:/perfilmembro/"+a.getUsername();
		

	}
}