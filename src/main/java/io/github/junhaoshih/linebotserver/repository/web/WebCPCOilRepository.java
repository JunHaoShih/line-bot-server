package io.github.junhaoshih.linebotserver.repository.web;

import io.github.junhaoshih.linebotserver.data.cpc.CPCOilPriceHistory;
import io.github.junhaoshih.linebotserver.enums.CPCOilType;
import io.github.junhaoshih.linebotserver.repository.CPCOilRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 從中國石油webservice取得油價資訊
 */
@Repository
public class WebCPCOilRepository implements CPCOilRepository {
    @Value("${cpc.oil-price-history.web-service}")
    private String webServiceUrl;

    @Value("${cpc.oil-price-history.count}")
    private int topCount;

    @Value("${cpc.oil-price-history.effective-date-tag}")
    private String effectiveDateTag;

    @Value("${cpc.oil-price-history.product-name-tag}")
    private String productNameTag;

    @Value("${cpc.oil-price-history.reference-price-tag}")
    private String referencePriceTag;

    @Value("${cpc.oil-price-history.charge-unit-tag}")
    private String chargeUnitTag;

    @Override
    public List<CPCOilPriceHistory> getLatestOilHistories(CPCOilType oilType) {
        List<CPCOilPriceHistory> histories = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Parse the content to Document object
            String targetUrl = String.format(webServiceUrl, oilType.getValue());
            Document document = builder.parse(targetUrl);
            NodeList tbTables = document.getElementsByTagName("tbTable");
            for (int i = tbTables.getLength() - 1; i >= tbTables.getLength() - topCount; i--) {
                Element currentElement = (Element)tbTables.item(i);
                CPCOilPriceHistory history = parseElement2PriceHistory(currentElement, oilType);
                histories.add(history);
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return histories;
    }

    /**
     * 將element轉換成CPCOilPriceHistory
     * @param currentElement 要轉換的element
     * @param oilType 油類
     * @return 油價歷史
     */
    private CPCOilPriceHistory parseElement2PriceHistory(Element currentElement, CPCOilType oilType) {
        // 生效日期
        Element effectiveDateElement = (Element) currentElement.getElementsByTagName(effectiveDateTag).item(0);
        String effectiveDateStr = effectiveDateElement.getTextContent();
        ZonedDateTime effectiveDate = ZonedDateTime.parse(effectiveDateStr);
        // 參考牌價
        Element referencePriceElement = (Element) currentElement.getElementsByTagName(referencePriceTag).item(0);
        String referencePriceStr = referencePriceElement.getTextContent();
        BigDecimal referencePrice = new BigDecimal(referencePriceStr);
        // 計價單位
        Element chargeUnitElement = (Element) currentElement.getElementsByTagName(chargeUnitTag).item(0);
        String chargeUnitStr = chargeUnitElement.getTextContent();

        return new CPCOilPriceHistory(effectiveDate, oilType.getName(), referencePrice, chargeUnitStr);
    }
}
