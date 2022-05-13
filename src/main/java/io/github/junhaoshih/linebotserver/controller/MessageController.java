package io.github.junhaoshih.linebotserver.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import io.github.junhaoshih.linebotserver.service.*;
import io.github.junhaoshih.linebotserver.service.cpc.CPCOilPriceService;
import io.github.junhaoshih.linebotserver.service.cwb.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
@Slf4j
public class MessageController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private DiceService diceService;

    @Autowired
    private CPCOilPriceService cpcOilPriceService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private DeveloperInfoService developerInfoService;

    @Autowired
    private TaiwanRegionService taiwanRegionService;

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: " + event);
        String originalMessageText = event.getMessage().getText();

        String[] args = originalMessageText.split(" ");

        if (args.length == 0)
            return null;

        // 發現在手機上要去輸入全形或半形的驚嘆號頗麻煩，在這間檢查
        if (args[0].charAt(0) != '！' && args[0].charAt(0) != '!')
            return null;

        args[0] = args[0].substring(1);

        switch (args[0]) {
            case "指令":
                return commandService.getCommandMessage(args);
            case "發票":
                return invoiceService.getInvoiceMessage();
            case "roll":
                return diceService.getDiceMessage(event, args);
            case "油價":
                return cpcOilPriceService.getLatestOilPriceHistoryMessage(args, event);
            case "天氣":
                return weatherService.getWeatherMessage(args, event);
            case "作者":
                return developerInfoService.getDeveloperMessage();
            case "縣市":
                return taiwanRegionService.getTaiwanRegionMessage(args, event);
        }
        return null;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
