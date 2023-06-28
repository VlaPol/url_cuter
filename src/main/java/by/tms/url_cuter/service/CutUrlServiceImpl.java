package by.tms.url_cuter.service;

import by.tms.url_cuter.entity.ConvertRecord;
import by.tms.url_cuter.repository.CutUrlRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CutUrlServiceImpl implements CutUrlService {

    CutUrlRepository cutUrlRepository;

    public CutUrlServiceImpl(CutUrlRepository cutUrlRepository) {
        this.cutUrlRepository = cutUrlRepository;
    }

    @Override
    public ConvertRecord getShortNameFromUrl(String url) {

        String shortName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        while (cutUrlRepository.isShortNameExists(shortName)) {
            shortName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        }

        ConvertRecord tr = ConvertRecord.builder()
                .shortName(shortName)
                .url(url)
                .build();

        try {
            cutUrlRepository.addNewRecord(tr);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        Optional<ConvertRecord> translateRecord = cutUrlRepository.getTranslateRecord(shortName);
        if (translateRecord.isPresent()) {
            return translateRecord.get();
        } else {
            throw new RuntimeException("Что-то пошло не так");
        }
        //return cutUrlRepository.getTranslateRecord(shortName);
    }

    @Override
    public ConvertRecord getUrlFromShortName(String shortName) {

        String sName = shortName.substring(shortName.lastIndexOf('/') + 1);

        if (!cutUrlRepository.isShortNameExists(sName)) {
            throw new RuntimeException("Нет такого адреса в БД");
        }

        Optional<ConvertRecord> translateRecord = cutUrlRepository.getTranslateRecord(shortName);
        if (translateRecord.isPresent()) {
            return translateRecord.get();
        } else {
            throw new RuntimeException("Что-то пошло не так");
        }
    }

    @Override
    public int getTotalNumberOfUrls() {
        Optional<Integer> total = cutUrlRepository.getTotal();
        if(total.isPresent()){
            return total.get();
        }else{
            return 0;
        }

    }
}
