package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.ConvertRecord;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CutUrlRepositoryImpl implements CutUrlRepository {

    DataSource dataSource;
    NamedParameterJdbcTemplate jdbcTemplate;

    public CutUrlRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addNewRecord(ConvertRecord inputRecord) {
        String sql = """ 
                INSERT INTO url_synonims (short_name, url)
                VALUES (:sName, :url)
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", inputRecord.getShortName()).addValue("url", inputRecord.getUrl());
        jdbcTemplate.update(sql, parametr);
    }

    @Override
    public ConvertRecord getTranslateRecord(String shortName) {

        String sql = """ 
                SELECT us.url, us.short_name
                FROM url_synonims us
                WHERE us.short_name = :sName
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", shortName);

        List<ConvertRecord> records = jdbcTemplate.query(sql, parametr, (rs, rowNum) -> {
            ConvertRecord tr = new ConvertRecord();
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
        String result = mresult.replace("[", "").replace("]", "");

        return result.equals(shortName);
    }

    @Override
    public int getTotal() {
        String sql = """ 
                SELECT count(us.short_name) as total
                FROM url_synonims us
                """;

        return Integer.parseInt(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("TOTAL")).toString()
                .replace("[", "").replace("]", ""));
    }
}
