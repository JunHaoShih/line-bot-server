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
public class LineBotServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LineBotServerApplication.class, args);
	}

}
