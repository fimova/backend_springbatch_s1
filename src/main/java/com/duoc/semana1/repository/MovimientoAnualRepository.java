package com.duoc.semana1.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.duoc.semana1.model.MovimientoAnual;

import java.util.List;

//consultar a tabla de movimiento anual
@Repository
public class MovimientoAnualRepository {

    private final JdbcTemplate jdbcTemplate;

    public MovimientoAnualRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MovimientoAnual> findByCuentaId(Long cuentaId) {

        String sql = """
                SELECT cuenta_id,
                       fecha,
                       transaccion,
                       monto,
                       descripcion
                FROM movimientos_anuales
                WHERE cuenta_id = ?
                ORDER BY fecha DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    MovimientoAnual movimiento = new MovimientoAnual();

                    movimiento.setCuentaId(rs.getLong("cuenta_id"));
                    movimiento.setFecha(
                            rs.getDate("fecha").toLocalDate()
                    );
                    movimiento.setTransaccion(
                            rs.getString("transaccion")
                    );
                    movimiento.setMonto(
                            rs.getBigDecimal("monto")
                    );
                    movimiento.setDescripcion(
                            rs.getString("descripcion")
                    );

                    return movimiento;
                },
                cuentaId
        );
    }
}
