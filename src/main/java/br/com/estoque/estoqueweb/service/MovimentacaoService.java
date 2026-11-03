package br.com.estoque.estoqueweb.service;

import br.com.estoque.estoqueweb.model.*;
import br.com.estoque.estoqueweb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {
    private final MovimentacaoRepository movimentacoes;
    private final ProdutoRepository produtos;

    public MovimentacaoService(MovimentacaoRepository movimentacoes, ProdutoRepository produtos) {
        this.movimentacoes = movimentacoes;
        this.produtos = produtos;
    }

    public List<Movimentacao> listar() { return movimentacoes.findAll(); }
    public Movimentacao buscar(Long id) {
        return movimentacoes.findById(id).orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada"));
    }

    private int efeito(Movimentacao m) {
        return m.getTipo() == TipoMovimentacao.ENTRADA ? m.getQuantidade() : -m.getQuantidade();
    }

    @Transactional
    public void salvar(Movimentacao dados) {
        if (dados.getQuantidade() == null || dados.getQuantidade() <= 0 || dados.getTipo() == null ||
                dados.getProduto() == null || dados.getUsuario() == null) {
            throw new IllegalArgumentException("Preencha todos os campos corretamente.");
        }

        // Ao editar, desfazemos primeiro o efeito da movimentação antiga.
        Movimentacao antiga = dados.getId() == null ? null : buscar(dados.getId());
        Produto novoProduto = produtos.findById(dados.getProduto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        int quantidadeAtual = novoProduto.getQuantidade();
        if (antiga != null && antiga.getProduto().getId().equals(novoProduto.getId())) {
            quantidadeAtual -= efeito(antiga);
        }
        int novaQuantidade = quantidadeAtual + efeito(dados);
        if (novaQuantidade < 0) throw new IllegalArgumentException("Estoque insuficiente para esta operação.");

        if (antiga != null && !antiga.getProduto().getId().equals(novoProduto.getId())) {
            Produto produtoAnterior = produtos.findById(antiga.getProduto().getId()).orElseThrow();
            int estoqueAnterior = produtoAnterior.getQuantidade() - efeito(antiga);
            if (estoqueAnterior < 0) throw new IllegalArgumentException("Não é possível desfazer a movimentação anterior.");
            produtoAnterior.setQuantidade(estoqueAnterior);
            produtos.save(produtoAnterior);
        }
        novoProduto.setQuantidade(novaQuantidade);
        produtos.save(novoProduto);
        dados.setData(antiga == null ? LocalDateTime.now() : antiga.getData());
        movimentacoes.save(dados);
    }

    @Transactional
    public void excluir(Long id) {
        Movimentacao m = buscar(id);
        Produto produto = produtos.findById(m.getProduto().getId()).orElseThrow();
        int quantidade = produto.getQuantidade() - efeito(m);
        if (quantidade < 0) throw new IllegalArgumentException("Não é possível desfazer: estoque insuficiente.");
        produto.setQuantidade(quantidade);
        produtos.save(produto);
        movimentacoes.delete(m);
    }
}
