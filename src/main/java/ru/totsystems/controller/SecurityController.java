package ru.totsystems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import ru.totsystems.dto.HistoryDto;
import ru.totsystems.dto.SecurityDto;
import ru.totsystems.service.HistoryService;
import ru.totsystems.service.SecurityService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ru.totsystems.service.XmlParser.parseHistory;
import static ru.totsystems.service.XmlParser.parseSecurities;

@RestController
@RequestMapping("/securities")
public class SecurityController {

    @Value("${xml.path}")
    private String xmlPath;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private HistoryService historyService;

    @EventListener(ApplicationReadyEvent.class)
    public void autoImport() throws IOException, ParserConfigurationException, SAXException {
        File folder = new File(xmlPath);
        File[] files = Objects.requireNonNull(folder.listFiles());
        for (File file : files) {
            if (file.getName().contains("securities")) {
                List<SecurityDto> securitiesDto = parseSecurities(new FileInputStream(file));
                securityService.importAll(securitiesDto);
            }
        }
        for (File file : files) {
            if (file.getName().contains("history")) {
                List<HistoryDto> history = parseHistory(new FileInputStream(file));
                historyService.importAll(history);
            }
        }
    }

    @PostMapping("/import")
    public void importFiles(@RequestParam MultipartFile[] files) throws IOException, ParserConfigurationException, SAXException {
        for (MultipartFile file : files) {
            List<SecurityDto> securitiesDto = parseSecurities(file.getInputStream());
            securityService.importAll(securitiesDto);
        }
    }

    @PostMapping
    public void create(@RequestBody SecurityDto securityDto) {
        securityService.create(securityDto);
    }

    @GetMapping(value = "/{secId}")
    public SecurityDto get(@PathVariable String secId) {
        return securityService.get(secId);
    }

    @GetMapping
    public List<SecurityDto> getAll() {
        return securityService.getAll();
    }

    @PutMapping("/{secId}")
    public void update(@PathVariable String secId, @RequestBody SecurityDto securityDto) {
        securityService.update(secId, securityDto);
    }

    @DeleteMapping("/{secId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String secId) {
        securityService.delete(secId);
    }
}
