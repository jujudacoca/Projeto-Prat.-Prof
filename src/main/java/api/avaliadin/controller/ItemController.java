package api.avaliadin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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
public class ItemController {

	@Autowired
	private FilmeRepository filmeRepository;
	@Autowired
	private SerieRepository serieRepository;
	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping("/cadastrolivro")
	public String showCadastro(Livro livro) {
	    return "cadastroLivro";
	}
	@PostMapping("/cadastroLivro")
	public String addLivro(@RequestParam String titulo, @RequestParam String autor,@RequestParam String editora,@RequestParam String ano, @RequestParam String pais) throws ParseException {
		
		Livro t = livroRepository.findByTitulo(titulo.toLowerCase());
		if(t!=null){
			return "redirect:/cadastrolivro?error";
		}else {
			Livro u = new Livro();
		    u.setTitulo(titulo.toLowerCase());
		    u.setAutor(autor.toLowerCase());
		    u.setEditora(editora.toLowerCase());
		    u.setAno(ano.toLowerCase());
		    u.setPais(pais.toLowerCase());
		    u.setEstado(false);
		    Date b = new Date();
			u.setDtCad(b);
		    livroRepository.save(u);
		    return "redirect:/login";
		}
		}
	@GetMapping(path="/allLivro")
	public @ResponseBody Iterable<Livro> getAllLivros() {
		return livroRepository.findAll();
	}
	
	@GetMapping("/cadastrofilme")
	public String showCadastro(Filme filme) {
	    return "cadastroFilme";
	}
	@PostMapping("/cadastroFilme")
	public String addFilme(@RequestParam String titulo, @RequestParam String diretor,@RequestParam String elenco,@RequestParam String ano, @RequestParam String pais) throws ParseException {
		Filme t = filmeRepository.findByTitulo(titulo.toLowerCase());
		if(t!=null){
			return "redirect:/cadastrofilme?error";
		}else {
			Filme u = new Filme();
			u.setTitulo(titulo.toLowerCase());
			u.setDiretor(diretor.toLowerCase());
			u.setElenco(elenco.toLowerCase());
		    u.setAno(ano.toLowerCase());
		    u.setPais(pais.toLowerCase());
		    u.setEstado(false);
		    Date b = new Date();
			u.setDtCad(b);
		    filmeRepository.save(u);
		    return "redirect:/login";
		}
	}
	@GetMapping(path="/allFilme")
	public @ResponseBody Iterable<Filme> getAllFilmes() {
		return filmeRepository.findAll();
	}
	@GetMapping("/cadastroserie")
	public String showCadastro(Serie serie) {
	    return "cadastroSerie";
	}
	@PostMapping("/cadastroSerie")
	public String addSerie(@RequestParam String titulo, @RequestParam String diretor,@RequestParam String elenco,@RequestParam String ano, @RequestParam String pais, @RequestParam Integer numTemp) throws ParseException {
		Serie t = serieRepository.findByTitulo(titulo.toLowerCase());
		if(t!=null){
			return "redirect:/cadastroserie?error";
		}else {
			Serie u = new Serie();
			u.setTitulo(titulo.toLowerCase());
			u.setDiretor(diretor.toLowerCase());
			u.setElenco(elenco.toLowerCase());
		    u.setAno(ano.toLowerCase());
		    u.setPais(pais.toLowerCase());
		    u.setNumTemp(numTemp);
		    u.setEstado(false);
		    Date b = new Date();
			u.setDtCad(b);
		    serieRepository.save(u);
		    return "redirect:/login";
		
		}
		}
	@GetMapping(path="/allSerie")
	public @ResponseBody Iterable<Serie> getAllSerie() {
		return serieRepository.findAll();
	}
	
	@GetMapping(path="/paginaitem/{id}")
	public String pagItem(@PathVariable int id, Model model) {
		String dtype = itemRepository.findDtypeById(id);
		if(dtype.equals("F")) {
			Filme u = filmeRepository.findById(id);
			model.addAttribute("Item",u);
		}else if(dtype.equals("S")) {
			Serie u = serieRepository.findById(id);
			model.addAttribute("Item",u);
		}else if (dtype.equals("L")) {
			Livro u = livroRepository.findById(id);
			model.addAttribute("Item",u);
		}
		return "/paginaitem";
	}
	
}
