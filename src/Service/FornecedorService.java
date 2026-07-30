package Service;

import Model.Fornecedor;
import Repository.FornecedorRepository;

import java.sql.SQLException;
import java.util.List;

public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService() {
        this.fornecedorRepository = new FornecedorRepository();
    }

    public Fornecedor salvar(Fornecedor fornecedor) throws IllegalArgumentException, SQLException {
        validarFornecedor(fornecedor);
        return fornecedorRepository.salvar(fornecedor);
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        return fornecedorRepository.listarTodos();
    }

    public Fornecedor buscarPorId(Long id) throws IllegalArgumentException, SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para fornecedor.");
        }
        Fornecedor fornecedor = fornecedorRepository.buscarPorId(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        }
        return fornecedor;
    }

    public void atualizar(Fornecedor fornecedor) throws IllegalArgumentException, SQLException {
        if (fornecedor.getId() == null) {
            throw new IllegalArgumentException("ID é obrigatório para atualizar o fornecedor.");
        }
        buscarPorId(fornecedor.getId());
        validarFornecedor(fornecedor);
        fornecedorRepository.atualizar(fornecedor);
    }

    public void deletar(Long id) throws IllegalArgumentException, SQLException {
        buscarPorId(id);
        fornecedorRepository.deletar(id);
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do fornecedor é obrigatório.");
        }
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ do fornecedor é obrigatório.");
        }
    }
}