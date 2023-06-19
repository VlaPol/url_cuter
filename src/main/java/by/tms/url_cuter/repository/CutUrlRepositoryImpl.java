package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.TranslateRecord;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CutUrlRepositoryImpl implements CutUrlRepository {

    DataSource dataSource;
    NamedParameterJdbcTemplate jdbcTemplate;

    public CutUrlRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addNewRecord(TranslateRecord inputRecord) {
        String sql = """ 
                INSERT INTO url_synonims (short_name, url)
                VALUES (:sName, :url)
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", inputRecord.getShortName())
                .addValue("url", inputRecord.getUrl());
        jdbcTemplate.update(sql, parametr);
    }

    @Override
    public TranslateRecord getTranslateRecord(String shortName) {

        String sql = """ 
                SELECT us.url, us.short_name
                FROM url_synonims us
                WHERE us.short_name = :sName
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", shortName);

        List<TranslateRecord> records = jdbcTemplate.query(sql, parametr, (rs, rowNum) -> {
            TranslateRecord tr = new TranslateRecord();
            tr.setShortName(rs.getString("SHORT_NAME"));
            tr.setUrl(rs.getString("URL"));
            return tr;
        });

        if (!records.isEmpty()) {
            return records.get(0);
        } else {
            throw new RuntimeException("Нет такого урла!");
        }
    }

    @Override
    public boolean isShortNameExists(String shortName) {

        String sql = """ 
                SELECT us.short_name
                FROM url_synonims us
                WHERE us.short_name = :sName
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", shortName);

        String mresult = jdbcTemplate.query(sql, parametr, (rs, rowNum) -> rs.getString("SHORT_NAME")).toString();
        String result = mresult.replace("[","").replace("]","");

        boolean equals = result.equals(shortName);
        return equals;
    }
}
