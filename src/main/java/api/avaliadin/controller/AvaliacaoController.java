package api.avaliadin.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String addAvaliacao(@RequestParam String descricao,@RequestParam Integer nota, @RequestParam String username, @RequestParam int id ) {
		User t = userRepository.findByUsername(username);
		if(t!=null) {
			Avaliacao a = new Avaliacao();
			a.setNota(nota);
			a.setDescricao(descricao);
			a.setIdUsuario(t.getId());
			a.setIdItem(id);
			Date b = new Date();
			a.setDtCad(b);
			a.setNumJoinha(0);
			avaliacaoRepository.save(a);
		}
		return "redirect:/allAval";
	}
	
	@GetMapping(path="/allAval")
	public @ResponseBody Iterable<Avaliacao> getAllAvalicaos() {
		return avaliacaoRepository.findAll();
	}
}
