package br.com.estoque.estoqueweb.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity public class Produto {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@NotBlank private String nome;
@NotNull @DecimalMin("0.0") private BigDecimal preco;
@NotNull @Min(0) private Integer quantidade;
@NotNull @ManyToOne private Categoria categoria;
@NotNull @ManyToOne private Fornecedor fornecedor;
public Long getId(){return id;}
public void setId(Long id){this.id=id;}
public String getNome(){return nome;}
public void setNome(String value){this.nome=value;}
public BigDecimal getPreco(){return preco;}
public void setPreco(BigDecimal value){this.preco=value;}
public Integer getQuantidade(){return quantidade;}
public void setQuantidade(Integer value){this.quantidade=value;}
public Categoria getCategoria(){return categoria;}
public void setCategoria(Categoria value){this.categoria=value;}
public Fornecedor getFornecedor(){return fornecedor;}
public void setFornecedor(Fornecedor value){this.fornecedor=value;}
}
