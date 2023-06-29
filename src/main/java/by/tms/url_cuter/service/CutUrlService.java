package by.tms.url_cuter.service;

import by.tms.url_cuter.entity.ConvertRecord;

import java.net.URISyntaxException;

public interface CutUrlService {
    ConvertRecord getShortNameFromUrl(String url) throws URISyntaxException;
    ConvertRecord getUrlFromShortName(String shortName);
    int getTotalNumberOfUrls();
}
