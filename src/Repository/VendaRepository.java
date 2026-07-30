package Repository;

import DTO.RelatorioVendaDTO;
import Model.Venda;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository {
    List<Venda> listarTodas() throws SQLException;
    Venda buscarPorId(Long id) throws SQLException;
    List<RelatorioVendaDTO> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException;
}
