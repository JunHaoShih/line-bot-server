package io.github.junhaoshih.linebotserver.service.cpc;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import io.github.junhaoshih.linebotserver.data.cpc.CPCOilPriceHistory;
import io.github.junhaoshih.linebotserver.enums.CPCOilType;
import io.github.junhaoshih.linebotserver.repository.CPCOilRepository;
import io.github.junhaoshih.linebotserver.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CPCOilPriceService {

    @Autowired
    private CPCOilRepository cpcOilRepository;

    @Autowired
    private LineRepository lineRepository;

    /**
     * 取得油價的LINE FlexMessage
     * @param args 油價參數，其中args[1]為油類id
     * @param event 訊息事件
     * @return 油價的LINE FlexMessage
     */
    public Message getLatestOilPriceHistoryMessage(String[] args, MessageEvent<TextMessageContent> event) {
        if (args.length <= 1)
            return new TextMessage("找不到對應油類: ");

        CPCOilType oilType = CPCOilType.getByName(args[1]);
        if (oilType.equals(CPCOilType.Undefined))
            return new TextMessage("找不到對應油類: " + args[1]);

        List<CPCOilPriceHistory> histories = cpcOilRepository.getLatestOilHistories(oilType);
        String userName = "Not Found";
        try {
            userName = lineRepository.getUserProfile(event).getDisplayName();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return toMessage(histories, userName, oilType);
    }

    /**
     * 將歷史資訊轉換成FlexMessage
     * @param histories 油價歷史
     * @param userName 呼叫者
     * @param oilType 油類
     * @return 油價FlexMessage
     */
    private Message toMessage(List<CPCOilPriceHistory> histories, String userName, CPCOilType oilType) {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder()
                                .body(getMainBodyBox(histories, userName, oilType))
                                .build()
                )
                .build();
        return  flexMessage;
    }

    /**
     * 取得油價歷史的main body資訊
     * @param histories 油價歷史
     * @param userName 呼叫者
     * @param oilType 油類
     * @return 油價歷史的main body資訊
     */
    private Box getMainBodyBox(List<CPCOilPriceHistory> histories, String userName, CPCOilType oilType) {
        Box mainBodyBox = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("中國石油")
                                .size("sm")
                                .color("#1DB446")
                                .weight(Text.TextWeight.BOLD)
                                .build());
                        add(Text.builder()
                                .text("近五周油價歷史")
                                .size("xxl")
                                .margin("md")
                                .weight(Text.TextWeight.BOLD)
                                .build());
                        add(Text.builder()
                                .text(oilType.getName())
                                .size("xs")
                                .color("#aaaaaa")
                                .build());
                        // 投擲者面板
                        add(Box.builder()
                                .layout(FlexLayout.HORIZONTAL)
                                .contents(getCallerComponents(userName))
                                .build());
                        add(Separator.builder()
                                .margin("xxl")
                                .build());
                        // 投擲點數
                        add(Box.builder()
                                .layout(FlexLayout.VERTICAL)
                                .contents(getPriceHistoryComponents(histories))
                                .build());
                    }
                })
                .build();
        return mainBodyBox;
    }

    /**
     * 取得main body中油價歷史清單頁面
     * @param histories 油價歷史
     * @return main body中油價歷史清單頁面
     */
    private List<FlexComponent> getPriceHistoryComponents(List<CPCOilPriceHistory> histories) {
        List<FlexComponent> flexComponents = new ArrayList<>();
        // 顯示所有投擲結果
        for (CPCOilPriceHistory history : histories) {
            flexComponents.add(Box.builder()
                    .layout(FlexLayout.HORIZONTAL)
                    .contents(getPriceHistory(history))
                    .build());
        }
        return flexComponents;
    }

    /**
     * 取得油價歷史欄位
     * @param history 油價歷史
     * @return 油價歷史欄位
     */
    private List<FlexComponent> getPriceHistory(CPCOilPriceHistory history) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        String effectiveDateStr = history.getEffectiveDate().format(formatter);
        List<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text(effectiveDateStr)
                .flex(0)
                .size("sm")
                .color("#555555")
                .build());
        flexComponents.add(Text.builder()
                .text(history.getReferencePrice().toString())
                .color("#111111")
                .size("sm")
                .align(FlexAlign.END)
                .build());
        return flexComponents;
    }

    /**
     * 取得呼叫者面板的所有元件
     * @param callerName 呼叫者名稱
     * @return 投擲者面板的所有元件
     */
    private List<FlexComponent> getCallerComponents(String callerName) {
        List<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text("呼叫者")
                .size("xs")
                .color("#aaaaaa")
                .build());
        flexComponents.add(Text.builder()
                .text(callerName)
                .size("xs")
                .color("#aaaaaa")
                .align(FlexAlign.END)
                .build());
        return flexComponents;
    }
}
