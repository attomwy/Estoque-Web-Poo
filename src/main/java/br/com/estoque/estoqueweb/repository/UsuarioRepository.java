package br.com.estoque.estoqueweb.repository;
import br.com.estoque.estoqueweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {}
