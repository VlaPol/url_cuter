package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.ConvertRecord;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CutUrlRepositoryImpl implements CutUrlRepository {
    private final NamedParameterJdbcOperations jdbcTemplate;

    public CutUrlRepositoryImpl(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addNewRecord(ConvertRecord inputRecord) {
        String sql = """ 
                INSERT INTO url_synonims (short_name, url)
                VALUES (:sName, :url)
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", inputRecord.getShortName())
                .addValue("url", inputRecord.getUrl());
        jdbcTemplate.update(sql, parametr);
    }

    @Override
    public Optional<ConvertRecord> getTranslateRecord(String shortName) {

        String sql = """ 
                SELECT us.url, us.short_name
                FROM url_synonims us
                WHERE us.short_name = :sName
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", shortName);

        return jdbcTemplate.query(sql, parametr, (rs, rowNum) -> {
                    ConvertRecord tr = new ConvertRecord();
                    tr.setShortName(rs.getString("SHORT_NAME"));
                    tr.setUrl(rs.getString("URL"));
                    return tr;
                }).stream()
                .findFirst();
    }

    @Override
    public boolean isShortNameExists(String shortName) {

        String sql = """ 
                SELECT us.short_name
                FROM url_synonims us
                WHERE us.short_name = :sName
                """;

        SqlParameterSource parametr = new MapSqlParameterSource("sName", shortName);

        List<String> tmpList = jdbcTemplate.query(sql, parametr, (rs, rowNum) -> rs.getString("SHORT_NAME"));
        if (!tmpList.isEmpty()) {
            return tmpList.get(0).equals(shortName);
        }
        return false;
    }

    @Override
    public Optional<Integer> getTotal() {
        String sql = """ 
                SELECT count(us.short_name) as total
                FROM url_synonims us
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("TOTAL")).stream().findFirst();

    }
}
