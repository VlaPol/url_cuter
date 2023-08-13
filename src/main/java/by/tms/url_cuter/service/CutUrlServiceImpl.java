package by.tms.url_cuter.service;

import by.tms.url_cuter.configure.BlackListConfig;
import by.tms.url_cuter.entity.ConvertRecord;
import by.tms.url_cuter.exceptions.AppException;
import by.tms.url_cuter.repository.CutUrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CutUrlServiceImpl implements CutUrlService {

    private final CutUrlRepository cutUrlRepository;
    private final BlackListConfig conf;

    @Override
    @Transactional
    public ConvertRecord getShortNameFromUrl(String url) {


        try {
            if (!isCorrectUrl(url)) {
                throw new AppException("Введен некоректный адрес или адрес находится в блокировке");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String shortName;
        do {
            shortName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        } while (cutUrlRepository.isShortNameExists(shortName));

        ConvertRecord tr = ConvertRecord.builder()
                .shortName(shortName)
                .url(url)
                .build();

        cutUrlRepository.addNewRecord(tr);

        return cutUrlRepository.getTranslateRecord(shortName)
                .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));

    }

    @Override
    @Transactional
    public ConvertRecord getUrlFromShortName(String shortName) {

        return cutUrlRepository.getTranslateRecord(shortName)
                .orElseThrow(() -> new AppException("Нет такого адреса в БД"));
    }

    @Override
    @Transactional
    public int getTotalNumberOfUrls() {
        return cutUrlRepository.getTotal().orElse(0);
    }

    private boolean isCorrectUrl(String inputUrl) {

        URI url = URI.create(inputUrl);

        if (url.getScheme().equals("http") || url.getScheme().equals("https")) {
            return !conf.getBlackList().contains(url.getHost());

        }

        return false;
    }
}
