package api.avaliadin.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String addAvaliacao(@RequestParam String descricao,@RequestParam Integer nota, @RequestParam int id, Authentication authentication ) {
		String username = authentication.getName();
		User t = userRepository.findByUsername(username);
		Item p = itemRepository.findById(id);
		Avaliacao b =  avaliacaoRepository.findByIds(t.getId(), p.getId());
		if(b!=null) {
			return "redirect:/paginaitem/"+p.getId()+"?avaliado";
		}
		if(t!=null && p!=null) {
			Avaliacao a = new Avaliacao();
			a.setNota(nota);
			a.setDescricao(descricao);
			a.setIdUsuario(t.getId());
			a.setIdItem(id);
			Date D = new Date();
			a.setDtCad(D);
			a.setNumJoinha(0);
			a.setUsername(username);
			a.setTitulo(p.getTitulo());
			avaliacaoRepository.save(a);
		}
		return "redirect:/paginaitem/"+p.getId();
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
	
}
