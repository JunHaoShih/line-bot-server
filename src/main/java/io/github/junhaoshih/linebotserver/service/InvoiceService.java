package io.github.junhaoshih.linebotserver.service;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import io.github.junhaoshih.linebotserver.data.FirstPrizeNumber;
import io.github.junhaoshih.linebotserver.data.InvoicePrizeResult;
import io.github.junhaoshih.linebotserver.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 發票service，目前主要功能為取得發票兌獎資訊
 */
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * 取得發票對獎清單
     * @return 發票對獎清單的FlexMessage，若出問題則回傳TextMessage
     */
    public Message getInvoiceMessage() {
        InvoicePrizeResult invoicePrizeResult = invoiceRepository.getLatestInvoicePrizeResult();
        if (invoicePrizeResult == null)
            return new TextMessage("取得發票兌獎資訊發生問題");

        return toMessage(invoicePrizeResult);
    }

    /**
     * 將InvoicePrizeResult轉換成LINE的FlexMessage
     * @param invoicePrizeResult 發票中獎資訊
     * @return LINE的FlexMessage
     */
    private Message toMessage(InvoicePrizeResult invoicePrizeResult) {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder().body(
                                Box.builder().contents(new ArrayList<>(){
                                    {
                                        add(Text.builder()
                                                .text("統一發票中獎號碼")
                                                .size("sm")
                                                .color("#1DB446")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Text.builder()
                                                .text(invoicePrizeResult.getTitle())
                                                .size("xxl")
                                                .margin("md")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Separator.builder()
                                                .margin("xxl")
                                                .build());
                                        // 發票資訊主頁面
                                        add(Box.builder()
                                                .layout(FlexLayout.VERTICAL)
                                                .spacing("sm")
                                                .margin("xxl")
                                                .contents(getInvoiceNumbers(invoicePrizeResult))
                                                .build());
                                        add(Separator.builder()
                                                .margin("xxl")
                                                .build());
                                        // 資料來源
                                        add(Box.builder()
                                                .layout(FlexLayout.VERTICAL)
                                                .spacing("sm")
                                                .margin("md")
                                                .contents(GetInvoiceSource())
                                                .build());
                                    }
                                }).layout(FlexLayout.VERTICAL).build()
                        ).build()
                ).build();
        return flexMessage;
    }

    /**
     * 取得所有中獎號碼的LINE頁面元件
     * @param invoicePrizeResult 發票中獎資訊
     * @return 所有中獎號碼的LINE頁面元件
     */
    private List<FlexComponent> getInvoiceNumbers(InvoicePrizeResult invoicePrizeResult) {
        List<FlexComponent> flexLayouts = new ArrayList<>();
        // 特別獎
        flexLayouts.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(getOneNumberPrizeComponents("特別獎", invoicePrizeResult.getSpecialPrizeNumber()))
                .build());
        // 特獎
        flexLayouts.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(getOneNumberPrizeComponents("特獎", invoicePrizeResult.getSuperPrizeNumber()))
                .build());
        // 頭獎
        flexLayouts.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(getMultiNumberPrizeComponents("頭獎", invoicePrizeResult.getFirstPrizeNumbers()))
                .build());
        flexLayouts.add(Separator.builder()
                .margin("xxl")
                .build());

        // 增開六獎
        flexLayouts.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(getExtraSixthPrizeComponents("增開六獎", invoicePrizeResult.getExtraSixthPrizeNumbers()))
                .build());
        return  flexLayouts;
    }

    /**
     * 取得前往財政部稅務入口網的按鈕
     * @return 往財政部稅務入口網的按鈕
     */
    private List<FlexComponent> GetInvoiceSource(){
        List<FlexComponent> flexLayouts = new ArrayList<>();
        flexLayouts.add(Text.builder()
                .text("資料來源")
                .flex(0)
                .size("xs")
                .color("#aaaaaa").build());

        URI uri = URI.create("https://invoice.etax.nat.gov.tw/");
        //URIAction.AltUri altUri = new URIAction.AltUri(URI.create(""));
        flexLayouts.add(Button.builder()
                .action(new URIAction("財政部稅務入口網", uri, null))
                .build());
        return flexLayouts;
    }

    /**
     * 取得單一發票號碼獎項的元件清單
     * @param prizeName 獎項名稱
     * @param prizeNumber 中獎編號
     * @return 單一發票號碼獎項的元件清單
     */
    private ArrayList<FlexComponent> getOneNumberPrizeComponents(String prizeName, String prizeNumber) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text(prizeName)
                .flex(0)
                .size("lg")
                .color("#555555")
                .weight(Text.TextWeight.BOLD)
                .build());
        flexComponents.add(Text.builder()
                .text(prizeNumber)
                .size("lg")
                .color("#dc3648")
                .weight(Text.TextWeight.BOLD)
                .align(FlexAlign.END)
                .build());
        return  flexComponents;
    }

    /**
     * 取得多中獎號碼清單的body部分(也就是發票號碼部分)
     * @param prizeNumbers 中獎號碼清單
     * @return 多中獎號碼清單的body components
     */
    private List<FlexComponent> getMultiNumberPrizeBody(List<FirstPrizeNumber> prizeNumbers) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        for (FirstPrizeNumber prizeNumber : prizeNumbers) {
            flexComponents.add(Box.builder()
                    .layout(FlexLayout.HORIZONTAL)
                    .contents(new ArrayList<>() {
                        {
                            add(Text.builder()
                                    .text(prizeNumber.getFirstPrizePrefix())
                                    .size("lg")
                                    .weight(Text.TextWeight.BOLD)
                                    .align(FlexAlign.END)
                                    .build());
                            add(Text.builder()
                                    .text(prizeNumber.getFirstPrizeSuffix())
                                    .flex(0)
                                    .size("lg")
                                    .color("#dc3648")
                                    .weight(Text.TextWeight.BOLD)
                                    .align(FlexAlign.END)
                                    .build());
                        }
                    })
                    .build());
        }
        return flexComponents;
    }

    /**
     * 取得多中獎號碼清單的所有元件，包含標題和發票號碼(getMultiNumberPrizeBody)
     * @param prizeName 標題名稱
     * @param prizeNumbers 中獎號碼清單
     * @return 多中獎號碼清單的main components
     */
    private List<FlexComponent> getMultiNumberPrizeComponents(String prizeName, List<FirstPrizeNumber> prizeNumbers) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text(prizeName)
                .flex(0)
                .size("lg")
                .color("#555555")
                .weight(Text.TextWeight.BOLD)
                .build());
        flexComponents.add(Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(getMultiNumberPrizeBody(prizeNumbers))
                .build());
        return flexComponents;
    }

    /**
     * 取得增開六獎清單的body部分(也就是發票號碼部分)
     * @param prizeNumbers 中獎號碼清單
     * @return 增開六獎清單的body components
     */
    private List<FlexComponent> getExtraSixthPrizeBody(List<String> prizeNumbers) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        for (String prizeNumber : prizeNumbers) {
            flexComponents.add(Text.builder()
                    .text(prizeNumber)
                    .size("lg")
                    .color("#dc3648")
                    .weight(Text.TextWeight.BOLD)
                    .align(FlexAlign.END)
                    .build());
        }
        return flexComponents;
    }

    /**
     * 取得增開六獎的所有元件，包含標題和發票號碼(getExtraSixthPrizeBody)
     * @param title 標題名稱
     * @param prizeNumbers 中獎號碼清單
     * @return 增開六獎清單的main components
     */
    private List<FlexComponent> getExtraSixthPrizeComponents(String title, List<String> prizeNumbers) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text(title)
                .flex(0)
                .size("lg")
                .color("#555555")
                .weight(Text.TextWeight.BOLD)
                .build());
        flexComponents.add(Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(getExtraSixthPrizeBody(prizeNumbers))
                .build());
        return flexComponents;
    }
}
