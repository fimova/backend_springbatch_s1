package com.duoc.semana1.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.duoc.semana1.model.CuentaInteres;

import java.util.List;

//consulta en cuenta intereses
@Repository
public class CuentaInteresRepository {

    private final JdbcTemplate jdbcTemplate;

    public CuentaInteresRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CuentaInteres> findByCuentaId(Long cuentaId) {

        String sql = """
                SELECT cuenta_id,
                       nombre,
                       saldo_inicial,
                       edad,
                       tipo,
                       interes_aplicado,
                       saldo_final,
                       periodo
                FROM cuentas_intereses
                WHERE cuenta_id = ?
                ORDER BY periodo DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapRow(rs),
                cuentaId
        );
    }

    private CuentaInteres mapRow(
            java.sql.ResultSet rs
    ) throws java.sql.SQLException {

        CuentaInteres cuenta = new CuentaInteres();

        cuenta.setCuentaId(
                rs.getLong("cuenta_id")
        );
        cuenta.setNombre(
                rs.getString("nombre")
        );
        cuenta.setSaldo(
                rs.getBigDecimal("saldo_inicial")
        );
        cuenta.setEdad(
                rs.getInt("edad")
        );
        cuenta.setTipo(
                rs.getString("tipo")
        );
        cuenta.setInteresAplicado(
                rs.getBigDecimal("interes_aplicado")
        );
        cuenta.setSaldoFinal(
                rs.getBigDecimal("saldo_final")
        );
        cuenta.setPeriodo(
                rs.getString("periodo")
        );

        return cuenta;
    }
}
