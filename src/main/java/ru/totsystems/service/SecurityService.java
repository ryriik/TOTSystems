package ru.totsystems.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.totsystems.dto.SecurityDto;
import ru.totsystems.exception.NameNotValidException;
import ru.totsystems.exception.SecurityNotFoundException;
import ru.totsystems.model.Security;
import ru.totsystems.repository.SecurityRepository;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.totsystems.service.XmlParser.parseSecurities;

@Service
public class SecurityService {

    @Value("${moex.url}")
    private String moexUrl;

    private final String namePattern = "[а-яА-Я0-9 ]+";

    private final SecurityRepository securityRepository;

    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void create(SecurityDto securityDto) {
        if (securityDto.getName().matches(namePattern)) {
            securityRepository.save(convertDtoToEntity(securityDto));
        } else throw new NameNotValidException();
    }

    public void importAll(List<SecurityDto> securitiesDto) {
        List<Security> securities = securitiesDto.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
        securityRepository.saveAll(securities);
    }

    public SecurityDto get(String secId) {
        Security security = securityRepository.findBySecId(secId)
                .orElse(loadSecurityFromMoex(secId));
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

    private Security loadSecurityFromMoex(String secId) {
        URL url = null;
        try {
            url = new URL(moexUrl + secId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream is = new BufferedInputStream(urlConnection.getInputStream())) {
            List<SecurityDto> securitiesDto = parseSecurities(is);
            for (SecurityDto securityDto : securitiesDto) {
                if (securityDto.getSecId().equals(secId)) {
                    return securityRepository.save(convertDtoToEntity(securityDto));
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        throw new SecurityNotFoundException();
    }

    public SecurityDto convertEntityToDto(Security security) {
        SecurityDto securityDto = new SecurityDto();
        securityDto.setSecId(security.getSecId());
        securityDto.setRegNumber(security.getRegNumber());
        securityDto.setName(security.getName());
        securityDto.setIsin(security.getIsin());
        securityDto.setEmitentTitle(security.getEmitentTitle());
        return securityDto;
    }

    public Security convertDtoToEntity(SecurityDto securityDto) {
        Security security = new Security();
        security.setSecId(securityDto.getSecId());
        security.setRegNumber(securityDto.getRegNumber());
        security.setName(securityDto.getName());
        security.setIsin(securityDto.getIsin());
        security.setEmitentTitle(securityDto.getEmitentTitle());
        return security;
    }
}
