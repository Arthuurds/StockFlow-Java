package Service;

import Model.Cliente;
import Repository.ClienteRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteRepositoryImpl clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepositoryImpl();
    }

    public Cliente salvar(Cliente cliente) throws IllegalArgumentException, SQLException {
        validarCliente(cliente);

        Cliente existente = clienteRepository.buscarPorCpfCnpj(cliente.getCpfCnpj());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF/CNPJ.");
        }
        return clienteRepository.salvar(cliente);
    }

    public List<Cliente> listarTodos() throws SQLException {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de cliente.");
        }
        Cliente cliente = clienteRepository.buscarPorId(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return cliente;
    }

    public void atualizar(Cliente cliente) throws IllegalArgumentException, SQLException {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização.");
        }
        buscarPorId(cliente.getId());
        validarCliente(cliente);
        clienteRepository.atualizar(cliente);
    }

    public void deletar(Long id) throws IllegalArgumentException, SQLException {
        buscarPorId(id);
        clienteRepository.deletar(id);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (cliente.getCpfCnpj() == null || cliente.getCpfCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF/CNPJ do cliente é obrigatório.");
        }

        // --- ADICIONE AQUI: Sanitização e Validação ---
        String docLimpo = cliente.getCpfCnpj().replaceAll("\\D", "");

        if (docLimpo.length() == 11) {
            if (!Util.ValidarDocumento.isCpfValido(docLimpo)) {
                throw new IllegalArgumentException("O CPF informado é inválido.");
            }
        } else if (docLimpo.length() != 14) {
            throw new IllegalArgumentException("O documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ).");
        }

        // Salva o documento formatado apenas com números
        cliente.setCpfCnpj(docLimpo);
    }
}
