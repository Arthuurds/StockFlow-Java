package Util;

import Model.*;
import Service.*;

import java.math.BigDecimal;
import java.util.List;

public class Menu {

    private final ProdutoService produtoService = new ProdutoService();
    private final ClienteService clienteService = new ClienteService();
    private final FornecedorService fornecedorService = new FornecedorService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final VendaService vendaService = new VendaService();

    public void iniciar() {
        int opcao;
        do {
            Console.exibirCabecalho("Menu Principal");
            System.out.println("1 - Módulo de Produtos");
            System.out.println("2 - Módulo de Clientes");
            System.out.println("3 - Módulo de Fornecedores");
            System.out.println("4 - Módulo de Vendas");
            System.out.println("5 - Módulo de Usuários");
            System.out.println("0 - Sair");

            opcao = Imput.lerInt("\nOpção");

            try {
                switch (opcao) {
                    case 1 -> menuProdutos();
                    case 2 -> menuClientes();
                    case 3 -> menuFornecedores();
                    case 4 -> menuVendas();
                    case 5 -> menuUsuarios();
                    case 0 -> System.out.println("\nEncerrando o StockFlow. Até logo!");
                    default -> Console.exibirErro("Opção inválida!");
                }
            } catch (Exception e) {
                Console.exibirErro(e.getMessage());
            }
        } while (opcao != 0);
    }

    // --- SUBMENU PRODUTOS ---
    private void menuProdutos() throws Exception {
        Console.exibirCabecalho("Gerenciamento de Produtos");
        System.out.println("1 - Cadastrar Produto");
        System.out.println("2 - Listar Produtos");
        System.out.println("3 - Buscar por ID");
        System.out.println("4 - Registrar Compra / Entrada de Estoque");
        System.out.println("5 - Deletar Produto");
        System.out.println("0 - Voltar");

        int op = Imput.lerInt("\nEscolha");
        switch (op) {
            case 1 -> {
                String nome = Imput.lerString("Nome do produto");
                String desc = Imput.lerString("Descrição");
                String cat = Imput.lerString("Categoria");
                BigDecimal preco = Imput.lerBigDecimal("Preço");
                int qtd = Imput.lerInt("Estoque Inicial");
                long fornId = Imput.lerLong("ID do Fornecedor");

                Fornecedor f = fornecedorService.buscarPorId(fornId);
                Produto p = new Produto(nome, desc, cat, preco, qtd, f);
                produtoService.salvar(p);
                Console.exibirSucesso("Produto cadastrado!");
            }
            case 2 -> {
                List<Produto> lista = produtoService.listarTodos();
                lista.forEach(System.out::println);
            }
            case 3 -> {
                long id = Imput.lerLong("ID do produto");
                System.out.println(produtoService.buscarPorId(id));
            }
            case 4 -> {
                long id = Imput.lerLong("ID do produto");
                int qtd = Imput.lerInt("Quantidade adquirida");
                produtoService.darEntradaEstoque(id, qtd);
                Console.exibirSucesso("Estoque atualizado com sucesso!");
            }
            case 5 -> {
                long id = Imput.lerLong("ID a remover");
                produtoService.deletar(id);
                Console.exibirSucesso("Produto removido!");
            }
        }
    }

    // --- SUBMENU CLIENTES ---
    private void menuClientes() throws Exception {
        Console.exibirCabecalho("Gerenciamento de Clientes");
        System.out.println("1 - Cadastrar Cliente");
        System.out.println("2 - Listar Clientes");
        System.out.println("0 - Voltar");

        int op = Imput.lerInt("\nEscolha");
        if (op == 1) {
            String nome = Imput.lerString("Nome");
            String cpf = Imput.lerString("CPF/CNPJ");
            String email = Imput.lerString("Email");
            String tel = Imput.lerString("Telefone");

            clienteService.salvar(new Cliente(nome, cpf, email, tel));
            Console.exibirSucesso("Cliente cadastrado!");
        } else if (op == 2) {
            clienteService.listarTodos().forEach(System.out::println);
        }
    }

    // --- SUBMENU FORNECEDORES ---
    private void menuFornecedores() throws Exception {
        Console.exibirCabecalho("Gerenciamento de Fornecedores");
        System.out.println("1 - Cadastrar Fornecedor");
        System.out.println("2 - Listar Fornecedores");
        System.out.println("0 - Voltar");

        int op = Imput.lerInt("\nEscolha");
        if (op == 1) {
            String nome = Imput.lerString("Nome/Razão Social");
            String cnpj = Imput.lerString("CNPJ");
            String email = Imput.lerString("Email");
            String tel = Imput.lerString("Telefone");

            fornecedorService.salvar(new Fornecedor(nome, cnpj, email, tel));
            Console.exibirSucesso("Fornecedor cadastrado!");
        } else if (op == 2) {
            fornecedorService.listarTodos().forEach(System.out::println);
        }
    }

    // --- SUBMENU VENDAS ---
    private void menuVendas() throws Exception {
        Console.exibirCabecalho("Gerenciamento de Vendas");
        System.out.println("1 - Registrar Nova Venda");
        System.out.println("2 - Listar Vendas");
        System.out.println("3 - Detalhar Venda por ID");
        System.out.println("0 - Voltar");

        int op = Imput.lerInt("\nEscolha");
        switch (op) {
            case 1 -> {
                long idCliente = Imput.lerLong("ID do Cliente");
                long idUsuario = Imput.lerLong("ID do Usuário Operador");

                Cliente cliente = clienteService.buscarPorId(idCliente);
                Usuario usuario = usuarioService.buscarPorId(idUsuario);

                Venda venda = new Venda(cliente, usuario);

                boolean adicionarMais = true;
                while (adicionarMais) {
                    long idProd = Imput.lerLong("ID do Produto");
                    int qtd = Imput.lerInt("Quantidade");

                    Produto prod = produtoService.buscarPorId(idProd);
                    ItemVenda item = new ItemVenda(prod, qtd, prod.getPreco());
                    venda.adicionarItem(item);

                    String res = Imput.lerString("Adicionar mais produtos? (S/N)");
                    adicionarMais = res.equalsIgnoreCase("S");
                }

                vendaService.realizarVenda(venda);
                Console.exibirSucesso("Venda registrada com sucesso! Total: R$ " + venda.getValorTotal());
            }
            case 2 -> vendaService.listarTodas().forEach(System.out::println);
            case 3 -> {
                long id = Imput.lerLong("ID da Venda");
                Venda v = vendaService.buscarPorId(id);
                System.out.println(v);
                System.out.println("--- Itens ---");
                v.getItens().forEach(System.out::println);
            }
        }
    }

    // --- SUBMENU USUÁRIOS ---
    private void menuUsuarios() throws Exception {
        Console.exibirCabecalho("Gerenciamento de Usuários");
        System.out.println("1 - Cadastrar Usuário");
        System.out.println("2 - Listar Usuários");
        System.out.println("0 - Voltar");

        int op = Imput.lerInt("\nEscolha");
        if (op == 1) {
            String nome = Imput.lerString("Nome completo");
            String login = Imput.lerString("Login");
            String senha = Imput.lerString("Senha");
            String perfil = Imput.lerString("Perfil (ADMIN/OPERADOR)");

            usuarioService.cadastrarUsuario(new Usuario(nome, login, senha, perfil));
            Console.exibirSucesso("Usuário cadastrado com sucesso!");
        } else if (op == 2) {
            usuarioService.listarTodos().forEach(System.out::println);
        }
    }
}
