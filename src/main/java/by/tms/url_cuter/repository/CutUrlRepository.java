package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.ConvertRecord;

public interface CutUrlRepository {

    void addNewRecord(ConvertRecord inputRecord);

    ConvertRecord getTranslateRecord(String shortName);

    boolean isShortNameExists(String shortName);

    int getTotal();
}
