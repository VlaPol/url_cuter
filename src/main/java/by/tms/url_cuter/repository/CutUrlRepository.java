package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.ConvertRecord;

import java.util.Optional;

public interface CutUrlRepository {

    void addNewRecord(ConvertRecord inputRecord);

    Optional<ConvertRecord> getTranslateRecord(String shortName);

    boolean isShortNameExists(String shortName);

    Optional<Integer> getTotal();
}
