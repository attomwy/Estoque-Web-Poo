package br.com.estoque.estoqueweb.repository;
import br.com.estoque.estoqueweb.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {}
