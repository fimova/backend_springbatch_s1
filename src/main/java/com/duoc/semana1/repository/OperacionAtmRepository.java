package com.duoc.semana1.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.duoc.semana1.model.OperacionAtm;

@Repository
public class OperacionAtmRepository {

    private final JdbcTemplate jdbcTemplate;

    public OperacionAtmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(OperacionAtm operacion) {

        String sql = """
                INSERT INTO operaciones_atm (
                    cuenta_id,
                    tipo,
                    monto,
                    fecha
                )
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                operacion.getCuentaId(),
                operacion.getTipo(),
                operacion.getMonto(),
                operacion.getFecha()
        );
    }

    public List<OperacionAtm> findByCuentaId(Long cuentaId) {

        String sql = """
                SELECT
                    operacion_id,
                    cuenta_id,
                    tipo,
                    monto,
                    fecha
                FROM operaciones_atm
                WHERE cuenta_id = ?
                ORDER BY fecha DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    OperacionAtm operacion =
                            new OperacionAtm();

                    operacion.setOperacionId(
                            rs.getLong("operacion_id")
                    );

                    operacion.setCuentaId(
                            rs.getLong("cuenta_id")
                    );

                    operacion.setTipo(
                            rs.getString("tipo")
                    );

                    operacion.setMonto(
                            rs.getBigDecimal("monto")
                    );

                    operacion.setFecha(
                            rs.getTimestamp("fecha")
                                    .toLocalDateTime()
                    );

                    return operacion;
                },
                cuentaId
        );
    }
}
