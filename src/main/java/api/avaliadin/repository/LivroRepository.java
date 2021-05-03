package api.avaliadin.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import api.avaliadin.model.Livro;

public interface LivroRepository extends CrudRepository<Livro, Integer>{
	Livro findByTitulo(String titulo);
	Livro findById(int id);
}
