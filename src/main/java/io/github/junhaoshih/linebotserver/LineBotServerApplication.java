package io.github.junhaoshih.linebotserver;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import io.github.junhaoshih.linebotserver.service.CommandService;
import io.github.junhaoshih.linebotserver.service.DiceService;
import io.github.junhaoshih.linebotserver.service.InvoiceService;
import io.github.junhaoshih.linebotserver.service.cpc.CPCOilPriceService;
import io.github.junhaoshih.linebotserver.service.cwb.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.util.*;

@SpringBootApplication
@LineMessageHandler
@Slf4j
public class LineBotServerApplication {

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

	public static void main(String[] args) {
		SpringApplication.run(LineBotServerApplication.class, args);
	}

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
		}
		return null;
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

}
