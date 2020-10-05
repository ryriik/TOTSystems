package ru.totsystems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.totsystems.dto.HistoryDto;
import ru.totsystems.exception.HistoryNotFoundException;
import ru.totsystems.exception.SecurityNotFoundException;
import ru.totsystems.model.History;
import ru.totsystems.model.Security;
import ru.totsystems.repository.HistoryRepository;
import ru.totsystems.repository.SecurityRepository;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private XmlParser xmlParser;

    private final HistoryRepository historyRepository;
    private final SecurityRepository securityRepository;

    public HistoryService(HistoryRepository historyRepository, SecurityRepository securityRepository) {
        this.historyRepository = historyRepository;
        this.securityRepository = securityRepository;
    }

    public void create(HistoryDto historyDto) throws IOException, ParserConfigurationException, SAXException {
        Optional<Security> security = securityRepository.findBySecId(historyDto.getSecId());
        if (security.isPresent()) {
            historyRepository.save(convertDtoToEntity(historyDto));
        } else {
            security = xmlParser.loadSecurityFromMoex(historyDto.getSecId());
            if (security.isPresent()) {
                historyRepository.save(convertDtoToEntity(historyDto));
            }
        }
    }

    public HistoryDto get(Long id) {
        History history = historyRepository.findById(id).orElseThrow(HistoryNotFoundException::new);
        return convertEntityToDto(history);
    }

    public List<HistoryDto> getAll() {
        return historyRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<HistoryDto> getAllBySecId(String secId) {
        Optional<Security> security = securityRepository.findBySecId(secId);
        if (security.isPresent()) {
            return historyRepository.findAllBySecId(security.get()).stream()
                    .map(this::convertEntityToDto)
                    .collect(Collectors.toList());
        } else throw new SecurityNotFoundException();
    }

    public List<HistoryDto> getAllByTradeDate(LocalDate tradeDate) {
        return historyRepository.findAllByTradeDate(tradeDate).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<HistoryDto> getAllBySecIdAndTradeDate(String secId, LocalDate tradeDate) {
        Optional<Security> security = securityRepository.findBySecId(secId);
        if (security.isPresent()) {
            return historyRepository.findAllBySecIdAndTradeDate(security.get(), tradeDate).stream()
                    .map(this::convertEntityToDto)
                    .collect(Collectors.toList());
        } else throw new SecurityNotFoundException();
    }

    public List<?> getAllDop(String sortBy) {
        return historyRepository.findAllDop(Sort.by(sortBy));
    }

    public List<?> getAllDopByEmitentTitle(String emitentTitle, String sortBy) {
        return historyRepository.findAllDopByEmitentTitle(emitentTitle, Sort.by(sortBy));
    }

    public List<?> getAllDopByTradeDate(LocalDate tradeDate, String sortBy) {
        return historyRepository.findAllDopByTradeDate(tradeDate, Sort.by(sortBy));
    }

    public List<?> getAllDopByEmitentTitleAndTradeDate(String emitentTitle, LocalDate tradeDate, String sortBy) {
        return historyRepository.findAllDopByEmitentTitleAndTradeDate(emitentTitle, tradeDate, Sort.by(sortBy));
    }

    public void update(Long id, HistoryDto historyDto) {
        History history = convertDtoToEntity(get(id));

        history.setBoardId(historyDto.getBoardId());
        history.setShortName(historyDto.getShortName());
        history.setTradeDate(historyDto.getTradeDate());
        history.setNumTrades(historyDto.getNumTrades());
        history.setValue(historyDto.getValue());
        history.setVolume(historyDto.getVolume());
        history.setOpen(historyDto.getOpen());
        history.setClose(historyDto.getClose());
        history.setLow(historyDto.getLow());
        history.setHigh(historyDto.getHigh());

        historyRepository.save(history);
    }

    public void delete(Long id) {
        History history = convertDtoToEntity(get(id));
        historyRepository.delete(history);
    }

    private HistoryDto convertEntityToDto(History history) {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setBoardId(history.getBoardId());
        historyDto.setSecId(history.getSecId().getSecId());
        historyDto.setShortName(history.getShortName());
        historyDto.setTradeDate(history.getTradeDate());
        historyDto.setNumTrades(history.getNumTrades());
        historyDto.setValue(history.getValue());
        historyDto.setVolume(history.getVolume());
        historyDto.setOpen(history.getOpen());
        historyDto.setClose(history.getClose());
        historyDto.setLow(history.getLow());
        historyDto.setHigh(history.getHigh());
        return historyDto;
    }

    private History convertDtoToEntity(HistoryDto historyDto) {
        Optional<Security> security = securityRepository.findBySecId(historyDto.getSecId());
        if (security.isPresent()) {
            History history = new History();
            history.setBoardId(historyDto.getBoardId());
            history.setSecId(security.get());
            history.setShortName(historyDto.getShortName());
            history.setTradeDate(historyDto.getTradeDate());
            history.setNumTrades(historyDto.getNumTrades());
            history.setValue(historyDto.getValue());
            history.setVolume(historyDto.getVolume());
            history.setOpen(historyDto.getOpen());
            history.setClose(historyDto.getClose());
            history.setLow(historyDto.getLow());
            history.setHigh(historyDto.getHigh());
            return history;
        } else {
            throw new SecurityNotFoundException();
        }
    }
}
