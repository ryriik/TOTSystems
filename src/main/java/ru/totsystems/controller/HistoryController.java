package ru.totsystems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import ru.totsystems.dto.HistoryDto;
import ru.totsystems.service.HistoryService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ru.totsystems.service.XmlParser.parseHistory;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/import")
    public void importFiles(@RequestParam MultipartFile[] files) throws IOException, ParserConfigurationException, SAXException {
        for (MultipartFile file : files) {
            List<HistoryDto> historyDto = parseHistory(file.getInputStream());
            historyService.importAll(historyDto);
        }
    }

    @PostMapping
    public void create(@RequestBody HistoryDto historyDto) {
        historyService.create(historyDto);
    }

    @GetMapping(value = "/{id}")
    public HistoryDto get(@PathVariable Long id) {
        return historyService.get(id);
    }

    @GetMapping
    public List<HistoryDto> getAll(
            @RequestParam(required = false) String secid,
            @RequestParam(required = false) String trade_date
    ) {
        if ((secid != null && !secid.isEmpty()) && (trade_date != null && !trade_date.isEmpty())) {
            return historyService.getAllBySecIdAndTradeDate(secid, LocalDate.parse(trade_date));
        }
        if (secid != null && !secid.isEmpty()) {
            return historyService.getAllBySecId(secid);
        }
        if (trade_date != null && !trade_date.isEmpty()) {
            return historyService.getAllByTradeDate(LocalDate.parse(trade_date));
        }
        return historyService.getAll();
    }

    @GetMapping("/merged")
    public List<?> getDopTable(
            @RequestParam(required = false) String sort_by,
            @RequestParam(required = false) String emitent_title,
            @RequestParam(required = false) String trade_date
    ) {
        if (sort_by == null || sort_by.isEmpty()) {
            sort_by = "secId";
        }
        if ((emitent_title != null && !emitent_title.isEmpty()) && (trade_date != null && !trade_date.isEmpty())) {
            return historyService.getAllMergedByEmitentTitleAndTradeDate(emitent_title, LocalDate.parse(trade_date), sort_by);
        }
        if (emitent_title != null && !emitent_title.isEmpty()) {
            return historyService.getAllMergedByEmitentTitle(emitent_title, sort_by);
        }
        if (trade_date != null && !trade_date.isEmpty()) {
            return historyService.getAllMergedByTradeDate(LocalDate.parse(trade_date), sort_by);
        }
        return historyService.getAllMerged(sort_by);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody HistoryDto historyDto) {
        historyService.update(id, historyDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        historyService.delete(id);
    }
}
