package io.github.junhaoshih.linebotserver.repository.web;

import io.github.junhaoshih.linebotserver.data.cpc.CPCOilPriceHistory;
import io.github.junhaoshih.linebotserver.data.cpc.CpcOilPrice;
import io.github.junhaoshih.linebotserver.data.cpc.CpcOilPriceGroup;
import io.github.junhaoshih.linebotserver.enums.CPCOilType;
import io.github.junhaoshih.linebotserver.repository.CPCOilRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<CpcOilPriceGroup> priceGroups = getOilPriceHistories();
        for (CpcOilPriceGroup priceGroup: priceGroups) {
        	for (CpcOilPrice price: priceGroup.getData()) {
        		if (price.getName().equals(oilType.getName())) {
        			histories.add(new CPCOilPriceHistory(priceGroup.getName(), oilType.getName(), price.getY(), ""));
        			break;
        		}
        	}
        }
        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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
        }*/
        return histories;
    }
    
	private List<CpcOilPriceGroup> getOilPriceHistories() {
		List<CpcOilPriceGroup> priceGroups = new ArrayList<CpcOilPriceGroup>();
		
		HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(10000))
                .build();
		
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.cpc.com.tw/historyprice.aspx?n=2890"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(10000))
                .GET()
                .build();
		
		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			Pattern p = Pattern.compile("var pieSeries = (.*);");
			Matcher matcher = p.matcher(response.body());
			if (matcher.find() && matcher.groupCount() > 0) {
				String data = matcher.group(1);
				ObjectMapper mapper = new ObjectMapper();

		        priceGroups = Arrays.asList(mapper.readValue(data, CpcOilPriceGroup[].class));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return priceGroups;
	}
}
