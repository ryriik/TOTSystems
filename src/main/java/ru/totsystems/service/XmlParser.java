package ru.totsystems.service;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.totsystems.dto.HistoryDto;
import ru.totsystems.dto.SecurityDto;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser {

    public static List<SecurityDto> parseSecurities(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        List<SecurityDto> securities = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(file, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("row")) {
                    String secId = attributes.getValue("secid");
                    String regNumber = attributes.getValue("regnumber");
                    String name = attributes.getValue("name");
                    String isin = attributes.getValue("isin");
                    String emitentTitle = attributes.getValue("emitent_title");

                    securities.add(new SecurityDto(secId, regNumber, name, isin, emitentTitle));
                }
            }
        });
        return securities;
    }

    public static List<HistoryDto> parseHistory(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        List<HistoryDto> historyDtoList = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(file, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("row") && attributes.getLength() > 3) {
                    String boardId = attributes.getValue("BOARDID");
                    String secId = attributes.getValue("SECID");
                    String shortName = attributes.getValue("SHORTNAME");
                    String tradeDate = attributes.getValue("TRADEDATE");
                    String numTrades = attributes.getValue("NUMTRADES");
                    String value = attributes.getValue("VALUE");
                    String volume = attributes.getValue("VOLUME");
                    String open = attributes.getValue("OPEN");
                    String close = attributes.getValue("CLOSE");
                    String low = attributes.getValue("LOW");
                    String high = attributes.getValue("HIGH");

                    historyDtoList.add(new HistoryDto(
                            boardId, secId, shortName, LocalDate.parse(tradeDate),
                            numTrades.isEmpty() ? 0 : Double.parseDouble(numTrades),
                            value.isEmpty() ? 0 : Double.parseDouble(value),
                            volume.isEmpty() ? 0 : Double.parseDouble(volume),
                            open.isEmpty() ? 0 : Double.parseDouble(open),
                            close.isEmpty() ? 0 : Double.parseDouble(close),
                            low.isEmpty() ? 0 : Double.parseDouble(low),
                            high.isEmpty() ? 0 : Double.parseDouble(high)
                    ));
                }
            }
        });
        return historyDtoList;
    }
}
