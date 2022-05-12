package io.github.junhaoshih.linebotserver.repository.web;

import io.github.junhaoshih.linebotserver.data.FirstPrizeNumber;
import io.github.junhaoshih.linebotserver.data.InvoicePrizeResult;
import io.github.junhaoshih.linebotserver.repository.InvoiceRepository;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * 從內政部稅務入口網提供的xml取得發票中獎資訊
 */
@Repository
public class WebInvoiceRepository implements InvoiceRepository {

    @Value("${invoice.prize.xml}")
    private String invoiceUrl;

    /**
     * 從內政部提供的xml取得發票兌獎資訊
     * @return 發票兌獎資訊
     */
    @Override
    public InvoicePrizeResult getLatestInvoicePrizeResult() {
        InvoicePrizeResult invoicePrizeResult = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Parse the content to Document object
            Document document = builder.parse(invoiceUrl);
            NodeList itemNodeList = document.getElementsByTagName("item");
            Element itemElement = (Element) itemNodeList.item(0);

            Element titleElement = (Element) itemElement.getElementsByTagName("title").item(0);
            String title = titleElement.getTextContent();
            System.out.println(title);
            Element descriptionElement = (Element) itemElement.getElementsByTagName("description").item(0);
            String description = descriptionElement.getTextContent();
            System.out.println(description);

            invoicePrizeResult = parseHtml2Result(description, title);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return invoicePrizeResult;
    }

    /**
     * 將html格式轉換成InvoicePrizeResult
     * @param html html格式資料
     * @param title 標題
     * @return 發票兌獎資訊
     */
    private InvoicePrizeResult parseHtml2Result(String html, String title) {
        InvoicePrizeResult invoicePrizeResult = new InvoicePrizeResult();
        invoicePrizeResult.setTitle(title);
        org.jsoup.nodes.Document htmlDocument = Jsoup.parse(html);
        Elements pElements = htmlDocument.getElementsByTag("p");
        for (org.jsoup.nodes.Element pElement : pElements) {
            //System.out.println(pElement.text());
            String[] elementValues = pElement.text().split("：");
            switch (elementValues[0]) {
                case "特別獎":
                    invoicePrizeResult.setSpecialPrizeNumber(elementValues[1]);
                    break;
                case "特獎":
                    invoicePrizeResult.setSuperPrizeNumber(elementValues[1]);
                    break;
                case "頭獎":
                    String[] firstPrizeNumberStrArr = elementValues[1].split("、");
                    for (String firstNumberStr : firstPrizeNumberStrArr) {
                        invoicePrizeResult.getFirstPrizeNumbers().add(new FirstPrizeNumber(firstNumberStr));
                    }
                    break;
                case "增開六獎":
                    String[] extraPrizeStrArr = elementValues[1].split("、");
                    for (String extraPrize : extraPrizeStrArr) {
                        invoicePrizeResult.getExtraSixthPrizeNumbers().add(extraPrize);
                    }
                    break;
            }
        }
        //System.out.println(pElements.size());
        return invoicePrizeResult;
    }
}
