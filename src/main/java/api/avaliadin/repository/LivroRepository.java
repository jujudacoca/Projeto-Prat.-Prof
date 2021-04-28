package api.avaliadin.repository;

import org.springframework.data.repository.CrudRepository;
import api.avaliadin.model.Livro;

public interface LivroRepository extends CrudRepository<Livro, Integer>{
	Livro findByTitulo(String titulo);
}
