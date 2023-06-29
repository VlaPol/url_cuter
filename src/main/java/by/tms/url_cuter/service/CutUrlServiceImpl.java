package by.tms.url_cuter.service;

import by.tms.url_cuter.configure.BlackListConfig;
import by.tms.url_cuter.entity.ConvertRecord;
import by.tms.url_cuter.repository.CutUrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CutUrlServiceImpl implements CutUrlService {

    CutUrlRepository cutUrlRepository;
    BlackListConfig conf;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ConvertRecord getShortNameFromUrl(String url) {


        try {
            if (!isCorrectUrl(url)) {
                throw new RuntimeException("введен некоректный адрес или адрес находится в блокировке");
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


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
    }

    @Override
    @Transactional
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
    @Transactional
    public int getTotalNumberOfUrls() {
        Optional<Integer> total = cutUrlRepository.getTotal();
        if (total.isPresent()) {
            return total.get();
        } else {
            return 0;
        }

    }

    private boolean isCorrectUrl(String inputUrl) throws URISyntaxException {

        URI url = new URI(inputUrl);

        try {
            if (url.getScheme().equals("http") || url.getScheme().equals("https")) {
                for (String itm : conf.getBlackList()) {
                    if (url.getHost().equals(itm)) {
                        return false;
                    }
                }
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
}
