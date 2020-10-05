package ru.totsystems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.totsystems.model.History;
import ru.totsystems.model.Security;
import ru.totsystems.repository.HistoryRepository;
import ru.totsystems.repository.SecurityRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class XmlParser {

    @Value("${moex.url}")
    private String moexUrl;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private HistoryRepository historyRepository;

    public void parseXml(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        NodeList root = document.getDocumentElement().getChildNodes();
        NodeList data = root.item(1).getChildNodes();
        NodeList rows = data.item(3).getChildNodes();
        String documentType = ((Element) root.item(1)).getAttribute("id");

        switch (documentType) {
            case "securities":
                parseSecurities(rows);
                break;
            case "history":
                parseHistory(rows);
                break;
        }
    }

    private void parseSecurities(NodeList rows) {
        List<Security> securities = new ArrayList<>(rows.getLength());
        for (int i = 1; i < rows.getLength(); i += 2) {
            String secId = ((Element) rows.item(i)).getAttribute("secid");
            String regNumber = ((Element) rows.item(i)).getAttribute("regnumber");
            String name = ((Element) rows.item(i)).getAttribute("name");
            String isin = ((Element) rows.item(i)).getAttribute("isin");
            String emitentTitle = ((Element) rows.item(i)).getAttribute("emitent_title");

            securities.add(new Security(secId, regNumber, name, isin, emitentTitle));
        }
        try {
            securityRepository.saveAll(securities);
        } catch (Exception ignored) {
        }
    }

    private void parseHistory(NodeList rows) throws IOException, ParserConfigurationException, SAXException {
        List<History> history = new ArrayList<>(rows.getLength());
        for (int i = 1; i < rows.getLength(); i += 2) {
            String secId = ((Element) rows.item(i)).getAttribute("SECID");
            Optional<Security> security = securityRepository.findBySecId(secId);
            if (security.isEmpty()) {
                security = loadSecurityFromMoex(secId);
                if (security.isEmpty()) {
                    continue;
                }
            }
            String boardId = ((Element) rows.item(i)).getAttribute("BOARDID");
            String shortName = ((Element) rows.item(i)).getAttribute("SHORTNAME");
            LocalDate tradeDate = LocalDate.parse(((Element) rows.item(i)).getAttribute("TRADEDATE"));

            String stringNumTrades = ((Element) rows.item(i)).getAttribute("NUMTRADES");
            Double numTrades = Double.valueOf(stringNumTrades.isEmpty() ? "0" : stringNumTrades);
            String stringValue = ((Element) rows.item(i)).getAttribute("VALUE");
            Double value = Double.valueOf(stringValue.isEmpty() ? "0" : stringValue);
            String stringVolume = ((Element) rows.item(i)).getAttribute("VOLUME");
            Double volume = Double.valueOf(stringVolume.isEmpty() ? "0" : stringVolume);
            String stringOpen = ((Element) rows.item(i)).getAttribute("OPEN");
            Double open = Double.valueOf(stringOpen.isEmpty() ? "0" : stringOpen);
            String stringClose = ((Element) rows.item(i)).getAttribute("CLOSE");
            Double close = Double.valueOf(stringClose.isEmpty() ? "0" : stringClose);
            String stringLow = ((Element) rows.item(i)).getAttribute("LOW");
            Double low = Double.valueOf(stringLow.isEmpty() ? "0" : stringLow);
            String stringHigh = ((Element) rows.item(i)).getAttribute("HIGH");
            Double high = Double.valueOf(stringHigh.isEmpty() ? "0" : stringHigh);

            try {
                historyRepository.save(new History(boardId, security.get(), shortName, tradeDate, numTrades, value, volume, open, close, low, high));
            } catch (Exception ignored) {
            }
        }
    }

    public Optional<Security> loadSecurityFromMoex(String secId) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(moexUrl + secId);
        URLConnection urlConnection = url.openConnection();
        try (InputStream is = new BufferedInputStream(urlConnection.getInputStream())){
            parseXml(is);
        }
        return securityRepository.findBySecId(secId);
    }
}
