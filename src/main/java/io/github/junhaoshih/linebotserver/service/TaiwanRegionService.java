package io.github.junhaoshih.linebotserver.service;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import io.github.junhaoshih.linebotserver.data.InvoicePrizeResult;
import io.github.junhaoshih.linebotserver.data.roc.RocCounty;
import io.github.junhaoshih.linebotserver.data.roc.RocRegion;
import io.github.junhaoshih.linebotserver.enums.cwb.County;
import io.github.junhaoshih.linebotserver.repository.RocCountyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaiwanRegionService {

    @Autowired
    private RocCountyRepository rocCountyRepository;

    public Message getTaiwanRegionMessage(String[] args, MessageEvent<TextMessageContent> event) {
        try {
            if (args.length == 1) {
                // get county message
                return getCountyMessage();
            } else if (args.length == 2) {
                // get region message
                String countyStr = args[1];
                County county = County.getByName(countyStr);
                return getRegionMessage(county);
            } else {
                throw new IllegalArgumentException("請輸入正確格式「!縣市」或「!縣市 地區」");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new TextMessage("發生錯誤: " + e.getMessage());
        }
    }

    private Message getCountyMessage() throws IOException, InterruptedException {
        List<RocCounty> rocCounties = rocCountyRepository.getRocCounties();
        return toCountyMessage(rocCounties);
    }

    private Message toCountyMessage(List<RocCounty> rocCounties) {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder().body(
                                Box.builder().contents(new ArrayList<>(){
                                    {
                                        add(Text.builder()
                                                .text("區域清單")
                                                .size("sm")
                                                .color("#1DB446")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Text.builder()
                                                .text("台灣縣市")
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
                                                .contents(getCountyButtons(rocCounties))
                                                .build());
                                    }
                                }).layout(FlexLayout.VERTICAL).build()
                        ).build()
                ).build();
        return flexMessage;
    }

    private List<FlexComponent> getCountyButtons(List<RocCounty> rocCounties) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        for (RocCounty rocCounty: rocCounties) {
            flexComponents.add(Button.builder()
                    .action(new PostbackAction(
                            rocCounty.getCityName(),
                            rocCounty.getCityEngName(),
                            "!縣市 " + rocCounty.getCityName()))
                    .build()
            );
        }
        return flexComponents;
    }

    private Message getRegionMessage(County county) throws IOException, InterruptedException {
        List<RocRegion> rocRegions = rocCountyRepository.getRocRegions(county);
        return toRegionMessage(county, rocRegions);
    }

    private Message toRegionMessage(County county, List<RocRegion> rocRegions) {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder().body(
                                Box.builder().contents(new ArrayList<>(){
                                    {
                                        add(Text.builder()
                                                .text("區域清單")
                                                .size("sm")
                                                .color("#1DB446")
                                                .weight(Text.TextWeight.BOLD)
                                                .build());
                                        add(Text.builder()
                                                .text(county.getCountyName() + "鄉鎮市區")
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
                                                .contents(getRegionButtons(county, rocRegions))
                                                .build());
                                    }
                                }).layout(FlexLayout.VERTICAL).build()
                        ).build()
                ).build();
        return flexMessage;
    }

    private List<FlexComponent> getRegionButtons(County county, List<RocRegion> rocRegions) {
        ArrayList<FlexComponent> flexComponents = new ArrayList<>();
        for (RocRegion rocRegion: rocRegions) {
            flexComponents.add(Button.builder()
                    .action(new PostbackAction(
                            rocRegion.getAreaName(),
                            rocRegion.getAreaEngName(),
                            "!天氣 " + county.getCountyName() + " " + rocRegion.getAreaName()))
                    .build()
            );
        }
        return flexComponents;
    }
}
