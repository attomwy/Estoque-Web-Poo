package br.com.estoque.estoqueweb.repository;
import br.com.estoque.estoqueweb.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProdutoRepository extends JpaRepository<Produto,Long> {}
