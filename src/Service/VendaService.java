package Service;

import DTO.RelatorioVendaDTO;
import DataBase.ConnectionFactory;
import Exception.EstoqueInsuficienteException;
import Model.ItemVenda;
import Model.Venda;
import Repository.VendaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository;

    // Construtor do VendaService
    public VendaService() {
        this.vendaRepository = new VendaRepository();
    }

    /**
     * Realiza o processo completo de venda de forma atômica:
     * 1. Valida o estoque do produto.
     * 2. Cria o registro da Venda.
     * 3. Cria os Itens da Venda.
     * 4. Abate a quantidade vendida do estoque do Produto.
     * 5. Efetua o COMMIT (se tudo der certo) ou ROLLBACK (se ocorrer qualquer erro).
     */
    public boolean realizarVenda(Venda venda) throws EstoqueInsuficienteException, SQLException {

        Connection conn = null;

        try {
            // 1. Abre a conexão com o banco
            conn = ConnectionFactory.getConnection();

            // 2. DESATIVA O AUTO-COMMIT (Início da Transação Manual)
            conn.setAutoCommit(false);

            // 3. Validação e Baixa no Estoque para cada item
            for (ItemVenda item : venda.getItens()) {

                // Busca a quantidade atual direto no banco para garantir dado atualizado
                int estoqueAtual = buscarEstoqueAtual(conn, item.getProduto().getId());

                if (item.getQuantidade() > estoqueAtual) {
                    throw new EstoqueInsuficienteException(
                            "Estoque insuficiente para o produto: " + item.getProduto().getNome() +
                                    ". Disponível: " + estoqueAtual + ", Solicitado: " + item.getQuantidade()
                    );
                }

                // Deduz o estoque do produto
                atualizarEstoqueProduto(conn, item.getProduto().getId(), item.getQuantidade());
            }

            // 4. Insere a Venda no Banco de Dados
            long vendaId = salvarVenda(conn, venda);
            venda.setId(vendaId);

            // 5. Insere os Itens da Venda vinculados ao ID da venda criada
            for (ItemVenda item : venda.getItens()) {
                salvarItemVenda(conn, vendaId, item);
            }

            // 6. TUDO DEU CERTO! Confirma as alterações no banco de dados
            conn.commit();
            System.out.println("✅ Venda #" + vendaId + " realizada e estoque atualizado com sucesso!");
            return true;

        } catch (EstoqueInsuficienteException e) {
            // Erro de Regra de Negócio: desfaz alterações e lança a mensagem para o usuário
            if (conn != null) {
                conn.rollback();
            }
            throw e;

        } catch (SQLException e) {
            // Erro de Banco de Dados: garante que nada incompleto fique salvo
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("❌ Erro ao processar venda no banco de dados. Transação cancelada.");
            throw e;

        } finally {
            // Restaura o comportamento padrão da conexão e fecha
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // --- Métodos Auxiliares de Banco (Executados dentro da mesma conexão) ---

    private int buscarEstoqueAtual(Connection conn, long produtoId) throws SQLException {
        String sql = "SELECT quantidade_estoque FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade_estoque");
                } else {
                    throw new SQLException("Produto com ID " + produtoId + " não encontrado.");
                }
            }
        }
    }

    private void atualizarEstoqueProduto(Connection conn, long produtoId, int quantidadeVendida) throws SQLException {
        String sql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidadeVendida);
            stmt.setLong(2, produtoId);
            stmt.executeUpdate();
        }
    }

    private long salvarVenda(Connection conn, Venda venda) throws SQLException {
        String sql = "INSERT INTO vendas (cliente_id, usuario_id, valor_total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, venda.getCliente().getId());
            stmt.setLong(2, venda.getUsuario().getId());
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Falha ao obter o ID da venda gerada.");
                }
            }
        }
    }

    private void salvarItemVenda(Connection conn, long vendaId, ItemVenda item) throws SQLException {
        String sql = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getPrecoUnitario());
            stmt.setBigDecimal(5, item.getSubtotal());
            stmt.executeUpdate();
        }
    }

    public List<RelatorioVendaDTO> gerarRelatorioPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas inicial e final são obrigatórias.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }

        return vendaRepository.buscarVendasPorPeriodo(inicio, fim);
    }

    // --- Consultas delegadas para o Repositório ---

    /**
     * Lista todas as vendas cadastradas no sistema.
     */
    public List<Venda> listarTodas() throws SQLException {
        return vendaRepository.listarTodas();
    }

    /**
     * Busca os detalhes de uma venda pelo seu ID.
     */
    public Venda buscarPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de venda inválido.");
        }
        Venda venda = vendaRepository.buscarPorId(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada com o ID: " + id);
        }
        return venda;
    }
}
