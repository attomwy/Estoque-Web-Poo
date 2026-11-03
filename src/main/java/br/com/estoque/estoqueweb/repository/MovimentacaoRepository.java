package br.com.estoque.estoqueweb.repository;
import br.com.estoque.estoqueweb.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {}
