package ru.totsystems.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.totsystems.dto.HistoryDto;
import ru.totsystems.dto.SecurityDto;
import ru.totsystems.exception.HistoryNotFoundException;
import ru.totsystems.model.History;
import ru.totsystems.model.Security;
import ru.totsystems.repository.HistoryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final SecurityService securityService;

    public HistoryService(HistoryRepository historyRepository, SecurityService securityService) {
        this.historyRepository = historyRepository;
        this.securityService = securityService;
    }

    public void create(HistoryDto historyDto) {
        securityService.get(historyDto.getSecId());
        historyRepository.save(convertDtoToEntity(historyDto));
    }

    public void importAll(List<HistoryDto> historyDto) {
        List<History> historyList = historyDto.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
        List<History> uniqueHistory = historyList.stream()
                .filter(history -> historyRepository.findAllByBoardIdAndSecIdAndTradeDate(
                        history.getBoardId(), history.getSecId(), history.getTradeDate())
                        .isEmpty())
                .collect(Collectors.toList());
        historyRepository.saveAll(uniqueHistory);
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
        SecurityDto securityDto = securityService.get(secId);
        return historyRepository.findAllBySecId(securityService.convertDtoToEntity(securityDto)).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    public List<HistoryDto> getAllByTradeDate(LocalDate tradeDate) {
        return historyRepository.findAllByTradeDate(tradeDate).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<HistoryDto> getAllBySecIdAndTradeDate(String secId, LocalDate tradeDate) {
        SecurityDto securityDto = securityService.get(secId);
        return historyRepository.findAllBySecIdAndTradeDate(securityService.convertDtoToEntity(securityDto), tradeDate).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
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

    public void update(Long id, HistoryDto newHistory) {
        History history = convertDtoToEntity(get(id));

        history.setBoardId(newHistory.getBoardId());
        history.setSecId(securityService.convertDtoToEntity(securityService.get(newHistory.getSecId())));
        history.setShortName(newHistory.getShortName());
        history.setTradeDate(newHistory.getTradeDate());
        history.setNumTrades(newHistory.getNumTrades());
        history.setValue(newHistory.getValue());
        history.setVolume(newHistory.getVolume());
        history.setOpen(newHistory.getOpen());
        history.setClose(newHistory.getClose());
        history.setLow(newHistory.getLow());
        history.setHigh(newHistory.getHigh());

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
        History history = new History();
        Security security = securityService.convertDtoToEntity(securityService.get(historyDto.getSecId()));
        history.setBoardId(historyDto.getBoardId());
        history.setSecId(security);
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
    }
}
