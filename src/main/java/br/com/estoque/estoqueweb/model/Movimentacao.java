package br.com.estoque.estoqueweb.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity public class Movimentacao {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@NotNull @Enumerated(EnumType.STRING) private TipoMovimentacao tipo;
@NotNull @Min(1) private Integer quantidade;
private LocalDateTime data;
@NotNull @ManyToOne private Produto produto;
@NotNull @ManyToOne private Usuario usuario;
public Long getId(){return id;}
public void setId(Long id){this.id=id;}
public TipoMovimentacao getTipo(){return tipo;}
public void setTipo(TipoMovimentacao value){this.tipo=value;}
public Integer getQuantidade(){return quantidade;}
public void setQuantidade(Integer value){this.quantidade=value;}
public LocalDateTime getData(){return data;}
public void setData(LocalDateTime value){this.data=value;}
public Produto getProduto(){return produto;}
public void setProduto(Produto value){this.produto=value;}
public Usuario getUsuario(){return usuario;}
public void setUsuario(Usuario value){this.usuario=value;}
}
