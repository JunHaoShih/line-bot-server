package io.github.junhaoshih.linebotserver.service;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;

@Service
public class DeveloperInfoService {
    public Message getDeveloperMessage() {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder().body(
                                Box.builder().contents(new ArrayList<>(){
                                    {
                                        add(Text.builder()
                                                .text("開發者")
                                                .size("sm")
                                                .color("#1DB446")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Text.builder()
                                                .text("施俊浩")
                                                .size("xxl")
                                                .margin("md")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Text.builder()
                                                .text("victor5517@gmail.com")
                                                .size("xs")
                                                .color("#aaaaaa")
                                                .wrap(true)
                                                .action(new URIAction("victor5517@gmail.com", URI.create("mailto:victor5517@gmail.com"), null))
                                                .build());
                                        add(Separator.builder()
                                                .margin("xxl")
                                                .build());
                                        // Github按鈕
                                        add(Button.builder()
                                                .action(new URIAction("Github", URI.create("https://github.com/JunHaoShih"), null))
                                                .build());
                                        // LINE Bot Server Repository按鈕
                                        add(Button.builder()
                                                .action(new URIAction("LINE Bot Server Repository", URI.create("https://github.com/JunHaoShih/line-bot-server"), null))
                                                .build());
                                    }
                                }).layout(FlexLayout.VERTICAL).build()
                        ).build()
                ).build();
        return flexMessage;
    }
}
