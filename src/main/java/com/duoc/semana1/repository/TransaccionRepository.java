package com.duoc.semana1.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.duoc.semana1.model.Transaccion;

import java.util.List;

//consulta en transacciones diarias
@Repository
public class TransaccionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransaccionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaccion> findAll() {

        String sql = """
                SELECT transaccion_id,
                       fecha,
                       monto,
                       tipo,
                       anomalia,
                       motivo_anomalia
                FROM transacciones_diarias
                ORDER BY fecha DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Transaccion transaccion = new Transaccion();

                    transaccion.setTransaccionId(
                            rs.getLong("transaccion_id")
                    );
                    transaccion.setFecha(
                            rs.getDate("fecha").toLocalDate()
                    );
                    transaccion.setMonto(
                            rs.getBigDecimal("monto")
                    );
                    transaccion.setTipo(
                            rs.getString("tipo")
                    );
                    transaccion.setAnomalia(
                            rs.getInt("anomalia") == 1
                    );
                    transaccion.setMotivoAnomalia(
                            rs.getString("motivo_anomalia")
                    );

                    return transaccion;
                }
        );
    }
}
