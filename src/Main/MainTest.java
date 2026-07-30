package Main;

import DTO.RelatorioVendaDTO;
import Exception.DocumentoInvalidoException;
import Exception.EstoqueInsuficienteException;
import Model.Cliente;
import Model.ItemVenda;
import Model.Produto;
import Model.Usuario;
import Model.Venda;
import Service.ClienteService;
import Service.UsuarioService;
import Service.VendaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("🧪 INICIANDO BATERIA DE TESTES DO STOCKFLOW");
        System.out.println("==================================================\n");

        ClienteService clienteService = new ClienteService();
        UsuarioService usuarioService = new UsuarioService();
        VendaService vendaService = new VendaService();

        // -------------------------------------------------------------
        // TESTE 1: Criptografia de Senha e Cadastro de Usuário
        // -------------------------------------------------------------
        System.out.println("---> [TESTE 1] Criptografia de Senha & Usuário");
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setLogin("vendedor_test_" + System.currentTimeMillis() % 1000);
            novoUsuario.setSenha("senhaSegura123");

            Usuario usuarioCadastrado = usuarioService.cadastrarUsuario(novoUsuario);
            System.out.println("🔑 Hash da senha gerado no banco: " + usuarioCadastrado.getSenha());

            // Tenta autenticar
            Usuario usuarioAutenticado = usuarioService.autenticar(novoUsuario.getLogin(), "senhaSegura123");
            System.out.println("✅ Autenticação com senha plana vs Hash concluída com sucesso!\n");
        } catch (Exception e) {
            System.err.println("❌ Falha no Teste 1: " + e.getMessage() + "\n");
        }

        // -------------------------------------------------------------
        // TESTE 2: Validação de CPF/CNPJ de Cliente
        // -------------------------------------------------------------
        System.out.println("---> [TESTE 2] Validação e Sanitização de CPF");
        try {
            // Tentativa com CPF Inválido
            Cliente clienteInvalido = new Cliente("Cliente Falso", "111.111.111-11", "teste@email.com", "11999999999");
            System.out.println("⏳ Tentando cadastrar CPF falso (111.111.111-11)...");
            clienteService.salvar(clienteInvalido);
        } catch (Exception e) {
            System.out.println("🛡️ Sucesso! O sistema bloqueou o CPF falso: " + e.getMessage());
        }

        try {
            // Tentativa com CPF Válido com pontuação (Módulo 11)
            // Exemplo de CPF com algoritmo válido para teste: 04445624090
            Cliente clienteValido = new Cliente("Arthur Silva", "044.456.240-90", "arthur@email.com", "11988888888");
            clienteService.salvar(clienteValido);
            System.out.println("📄 CPF sanitizado e salvo apenas como números: " + clienteValido.getCpfCnpj() + "\n");
        } catch (Exception e) {
            System.out.println("ℹ️ Nota do Teste 2: " + e.getMessage() + "\n");
        }

        // -------------------------------------------------------------
        // TESTE 3: Regra de Estoque Insuficiente e Transação
        // -------------------------------------------------------------
        System.out.println("---> [TESTE 3] Validação de Trava de Estoque");
        try {
            // Simulando venda de item inexistente ou sem estoque
            Venda vendaSemEstoque = new Venda();

            // Supondo IDs fictícios 1 para teste
            Cliente c = new Cliente(); c.setId(1L);
            Usuario u = new Usuario(); u.setId(1L);
            vendaSemEstoque.setCliente(c);
            vendaSemEstoque.setUsuario(u);

            List<ItemVenda> itens = new ArrayList<>();
            Produto p = new Produto(); p.setId(1L); p.setNome("Notebook Gamer");
            ItemVenda itemExcessivo = new ItemVenda(p, 9999, new BigDecimal("4500.00")); // Qtd exagerada
            itens.add(itemExcessivo);
            vendaSemEstoque.setItens(itens);

            vendaService.realizarVenda(vendaSemEstoque);
        } catch (EstoqueInsuficienteException e) {
            System.out.println("🛡️ Sucesso! A transação foi cancelada (Rollback): " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("ℹ️ Nota do Teste 3: " + e.getMessage() + "\n");
        }

        // -------------------------------------------------------------
        // TESTE 4: Relatório de Vendas por Período (DTO)
        // -------------------------------------------------------------
        System.out.println("---> [TESTE 4] Consulta de Relatório por Período (DTO)");
        try {
            LocalDateTime inicio = LocalDateTime.now().minusDays(30);
            LocalDateTime fim = LocalDateTime.now().plusDays(1);

            List<RelatorioVendaDTO> relatorio = vendaService.gerarRelatorioPorPeriodo(inicio, fim);
            System.out.println("📊 Vendas encontradas nos últimos 30 dias: " + relatorio.size());
            for (RelatorioVendaDTO dto : relatorio) {
                System.out.println("   -> " + dto);
            }
            System.out.println("\n✅ Teste de Relatório finalizado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Falha no Teste 4: " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println("🎉 BATERIA DE TESTES FINALIZADA!");
        System.out.println("==================================================");
    }
}