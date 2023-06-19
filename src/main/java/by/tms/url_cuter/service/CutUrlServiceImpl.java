package by.tms.url_cuter.service;

import by.tms.url_cuter.entity.TranslateRecord;
import by.tms.url_cuter.repository.CutUrlRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CutUrlServiceImpl implements CutUrlService {

    CutUrlRepository cutUrlRepository;

    public CutUrlServiceImpl(CutUrlRepository cutUrlRepository) {
        this.cutUrlRepository = cutUrlRepository;
    }

    @Override
    public TranslateRecord getShortNameFromUrl(String url) {

        String shortName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        while (cutUrlRepository.isShortNameExists(shortName)) {
            shortName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        }

        TranslateRecord tr = TranslateRecord.builder()
                .shortName(shortName)
                .url(url)
                .build();

        try {
            cutUrlRepository.addNewRecord(tr);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return cutUrlRepository.getTranslateRecord(shortName);
    }

    @Override
    public TranslateRecord getUrlFromShortName(String shortName) {

        String sName = shortName.substring(shortName.lastIndexOf('/') + 1);

        if (!cutUrlRepository.isShortNameExists(sName)){
            throw new RuntimeException("нет такого адреса в БД");
        }

        TranslateRecord translateRecord = cutUrlRepository.getTranslateRecord(sName);
        return translateRecord;
    }
}
