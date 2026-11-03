package br.com.estoque.estoqueweb.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity public class Fornecedor {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@NotBlank private String nome;
private String cnpj;
public Long getId(){return id;}
public void setId(Long id){this.id=id;}
public String getNome(){return nome;}
public void setNome(String value){this.nome=value;}
public String getCnpj(){return cnpj;}
public void setCnpj(String value){this.cnpj=value;}
}
