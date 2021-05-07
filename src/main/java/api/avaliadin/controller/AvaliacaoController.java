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
		System.out.println(username);
		User t = userRepository.findByUsername(username);
		Item p = itemRepository.findById(id);
		if(t!=null && p!=null) {
			Avaliacao a = new Avaliacao();
			a.setNota(nota);
			a.setDescricao(descricao);
			a.setIdUsuario(t.getId());
			a.setIdItem(id);
			Date b = new Date();
			a.setDtCad(b);
			a.setNumJoinha(0);
			a.setUsername(username);
			a.setTitulo(p.getTitulo());
			avaliacaoRepository.save(a);
		}
		return "redirect:/allAval";
	}
	
	@GetMapping(path="/allAval")
	public @ResponseBody Iterable<Avaliacao> getAllAvalicaos() {
		return avaliacaoRepository.findAll();
	}
	
	@PostMapping("/darJoinha")
	public String addJoinha(@RequestParam int id,Authentication authentication) {
		String username = authentication.getName();
		Avaliacao a = avaliacaoRepository.findById(id);
		User t = userRepository.findByUsername(username);
		Joinha j = joinhaRepository.existeJoinha(t.getId(), a.getId());
		if(j==null) {
			//implementar erro caso ja foi curtido
		}else {
			Joinha o = new Joinha();
			o.setIdAvaliacao(a.getId());
			o.setIdUsuario(t.getId());
			Date b = new Date();
			o.setDtCad(b);
			joinhaRepository.save(o);
			int x = a.getNumJoinha()+1;
			System.out.println(x);
			a.setNumJoinha(x);
			avaliacaoRepository.save(a);
		}
		
		
		
	return "redirect:/paginaitem/"+a.getIdItem();	
	}
	
}
