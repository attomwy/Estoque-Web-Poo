package br.com.estoque.estoqueweb.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity public class Categoria {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@NotBlank private String nome;
private String descricao;
public Long getId(){return id;}
public void setId(Long id){this.id=id;}
public String getNome(){return nome;}
public void setNome(String value){this.nome=value;}
public String getDescricao(){return descricao;}
public void setDescricao(String value){this.descricao=value;}
}
