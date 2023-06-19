package by.tms.url_cuter.service;

import by.tms.url_cuter.entity.TranslateRecord;

public interface CutUrlService {
    TranslateRecord getShortNameFromUrl(String url);
    TranslateRecord getUrlFromShortName(String shortName);
}
