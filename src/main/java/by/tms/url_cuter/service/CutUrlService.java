package by.tms.url_cuter.service;

import by.tms.url_cuter.entity.ConvertRecord;

public interface CutUrlService {
    ConvertRecord getShortNameFromUrl(String url);
    ConvertRecord getUrlFromShortName(String shortName);
    int getTotalNumberOfUrls();
}
