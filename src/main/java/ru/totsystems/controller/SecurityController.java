package ru.totsystems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import ru.totsystems.dto.SecurityDto;
import ru.totsystems.service.SecurityService;
import ru.totsystems.service.XmlParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/securities")
public class SecurityController {

    @Value("${xml.path}")
    private String xmlPath;

    @Autowired
    private XmlParser xmlParser;

    @Autowired
    private SecurityService securityService;

    @EventListener(ApplicationReadyEvent.class)
    public void autoImport() {
        File folder = new File(xmlPath);
        File[] files = Objects.requireNonNull(folder.listFiles());
        Arrays.stream(files).filter(file -> file.getName().contains("securities")).forEach(file -> {
            try {
                xmlParser.parseXml(new FileInputStream(file));
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        });
        Arrays.stream(files).filter(file -> file.getName().contains("history")).forEach(file -> {
            try {
                xmlParser.parseXml(new FileInputStream(file));
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/import")
    public void importFiles(@RequestParam MultipartFile[] files) throws IOException, ParserConfigurationException, SAXException {
        for (MultipartFile file : files) {
            xmlParser.parseXml(file.getInputStream());
        }
    }

    @PostMapping
    public void create(@RequestBody SecurityDto securityDto) {
        securityService.createSecurity(securityDto);
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
