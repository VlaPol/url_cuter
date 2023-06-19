package by.tms.url_cuter.repository;

import by.tms.url_cuter.entity.TranslateRecord;

public interface CutUrlRepository {

    void addNewRecord(TranslateRecord inputRecord);

    TranslateRecord getTranslateRecord(String shortName);

    boolean isShortNameExists(String shortName);


}
