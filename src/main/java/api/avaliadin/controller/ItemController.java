package api.avaliadin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/cadastrolivro")
	public String showCadastro(Livro livro) {
	    return "cadastroLivro";
	}
	@PostMapping("/cadastroLivro")
	public String addLivro(@RequestParam String titulo, @RequestParam String autor,@RequestParam String editora,@RequestParam String ano, @RequestParam String pais) throws ParseException {
		/*Livro t = livroRepository.findBy(username);
		if(t!=null){
			return "redirect:/cadastro?error";
		}else {*/
			Livro u = new Livro();
		    u.setTitulo(titulo);
		    u.setAutor(autor);
		    u.setEditora(editora);
		    u.setAno(ano);
		    u.setPais(pais);
		    //}
		    Date b = new Date();
			u.setDtCad(b);
		    livroRepository.save(u);
		    return "redirect:/login";
		
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
		Filme u = new Filme();
		u.setTitulo(titulo);
		u.setDiretor(diretor);
		u.setElenco(elenco);
	    u.setAno(ano);
	    u.setPais(pais);
	    Date b = new Date();
		u.setDtCad(b);
	    filmeRepository.save(u);
	    return "redirect:/login";
		
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
		Serie u = new Serie();
		u.setTitulo(titulo);
		u.setDiretor(diretor);
		u.setElenco(elenco);
	    u.setAno(ano);
	    u.setPais(pais);
	    u.setNumTemp(numTemp);
	    Date b = new Date();
		u.setDtCad(b);
	    serieRepository.save(u);
	    return "redirect:/login";
		
		}
	@GetMapping(path="/allSerie")
	public @ResponseBody Iterable<Serie> getAllSerie() {
		return serieRepository.findAll();
	}
	
}
