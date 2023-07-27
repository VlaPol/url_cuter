package by.tms.url_cuter.controller;

import by.tms.url_cuter.entity.ConvertRecord;
import by.tms.url_cuter.service.CutUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Controller
public class CutUrlController {

    CutUrlService cutUrlService;

    private static final String path = "http://localhost:8080/";

    public CutUrlController(CutUrlService cutUrlService) {
        this.cutUrlService = cutUrlService;
    }

    @GetMapping("/")
    String getMainPage(Model model) {
        model.addAttribute("counter", cutUrlService.getTotalNumberOfUrls());
        model.addAttribute("convertedRecord", new ConvertRecord());
        return "main";
    }


    @PostMapping("/shorturl")
    String getShortUrl(@ModelAttribute("convertedRecord") ConvertRecord convertedRecord,
                       Model model) {

        try {
            convertedRecord = cutUrlService.getShortNameFromUrl(convertedRecord.getUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String convertUrl = path + convertedRecord.getShortName();

        model.addAttribute("sName", convertUrl);
        model.addAttribute("counter", cutUrlService.getTotalNumberOfUrls());
        return "main";
    }

    @PostMapping("/realurl")
    String getRealUrlByShort(@ModelAttribute("convertedRecord") ConvertRecord convertedRecord,
                             Model model) {

        String url = convertedRecord.getUrl();

        convertedRecord = cutUrlService.getUrlFromShortName(url);
        model.addAttribute("sName", convertedRecord.getUrl());
        model.addAttribute("counter", cutUrlService.getTotalNumberOfUrls());
        return "main";
    }
}
