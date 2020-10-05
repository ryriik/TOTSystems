package ru.totsystems.service;

import org.springframework.stereotype.Service;
import ru.totsystems.dto.SecurityDto;
import ru.totsystems.exception.NameNotValidException;
import ru.totsystems.exception.SecurityNotFoundException;
import ru.totsystems.model.Security;
import ru.totsystems.repository.SecurityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    private final String namePattern = "[а-яА-Я0-9 ]+";

    private final SecurityRepository securityRepository;

    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void createSecurity(SecurityDto securityDto) {
        if (securityDto.getName().matches(namePattern)) {
            securityRepository.save(convertDtoToEntity(securityDto));
        } else throw new NameNotValidException();
    }

    public SecurityDto get(String secId) {
        Security security = securityRepository.findBySecId(secId).orElseThrow(SecurityNotFoundException::new);
        return convertEntityToDto(security);
    }

    public List<SecurityDto> getAll() {
        return securityRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public void update(String secId, SecurityDto securityDto) {
        if (securityDto.getName().matches(namePattern)) {
            Security security = convertDtoToEntity(get(secId));

            security.setSecId(securityDto.getSecId());
            security.setName(securityDto.getName());
            security.setRegNumber(securityDto.getRegNumber());
            security.setIsin(securityDto.getIsin());
            security.setEmitentTitle(securityDto.getEmitentTitle());

            securityRepository.save(security);
        } else throw new NameNotValidException();
    }

    public void delete(String secId) {
        Security security = convertDtoToEntity(get(secId));
        securityRepository.delete(security);
    }

    private SecurityDto convertEntityToDto(Security security) {
        SecurityDto securityDto = new SecurityDto();
        securityDto.setSecId(security.getSecId());
        securityDto.setRegNumber(security.getRegNumber());
        securityDto.setName(security.getName());
        securityDto.setIsin(security.getIsin());
        securityDto.setEmitentTitle(security.getEmitentTitle());
        return securityDto;
    }

    private Security convertDtoToEntity(SecurityDto securityDto) {
        Security security = new Security();
        security.setSecId(securityDto.getSecId());
        security.setRegNumber(securityDto.getRegNumber());
        security.setName(securityDto.getName());
        security.setIsin(securityDto.getIsin());
        security.setEmitentTitle(securityDto.getEmitentTitle());
        return security;
    }
}
