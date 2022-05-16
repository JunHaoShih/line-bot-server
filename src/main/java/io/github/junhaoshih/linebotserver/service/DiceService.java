package io.github.junhaoshih.linebotserver.service;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
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
import io.github.junhaoshih.linebotserver.data.DiceResult;
import io.github.junhaoshih.linebotserver.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Service
public class DiceService {
    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Autowired
    private LineRepository lineRepository;

    /**
     * 取得擲骰子LINE訊息
     * @param event 訊息事件
     * @param args 投擲參數(只有args[1]才是投擲參數)
     * @return 擲骰子LINE訊息
     */
    public Message getDiceMessage(MessageEvent<TextMessageContent> event, String[] args) {
        if (args.length  <= 1)
            return  null;

        int diceCount;
        int diceSides;
        try {
            int[] diceParams = getDiceParams(args[1]);
            diceCount = diceParams[0];
            diceSides = diceParams[1];
        } catch (NumberFormatException e) {
            return new TextMessage("骰子指令不正確");
        }

        if (diceCount < 1 || diceCount > 20) {
            return new TextMessage("骰子數量需介於1~20之間的正整數");
        }
        if (diceSides < 4 || diceSides > 100) {
            return new TextMessage("骰子面數需介於4~100之間的正整數");
        }

        String userName = "Not Found";
        try {
            userName = lineRepository.getUserProfile(event).getDisplayName();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        DiceResult diceResult = getDiceResult(diceCount, diceSides);
        diceResult.setRollerName(userName);
        return toMessage(diceResult);
    }

    /**
     * 將傳入參數轉換成骰子數與骰子面數
     * @param diceStr 骰子參數
     * @return 一個長度為2的int陣列，第一個是骰子數，第二個是骰子面數
     */
    private int[] getDiceParams(String diceStr) {
        diceStr = diceStr.toLowerCase(Locale.ROOT);
        String[] diceParams = diceStr.split("d");
        if (diceParams.length <= 1)
            throw new NumberFormatException("diceParams的長度小於等於1");
        int diceCount = Integer.parseInt(diceParams[0]);
        int diceSides = Integer.parseInt(diceParams[1]);
        return new int[] { diceCount, diceSides };
    }

    /**
     * 投擲骰子並取得結果
     * @param diceCount 骰子數
     * @param diceSides 骰子面數
     * @return 投擲骰子的結果
     */
    private DiceResult getDiceResult(int diceCount, int diceSides) {
        Random random = new Random();
        int[] points = random.ints(diceCount, 1, diceSides + 1).toArray();
        return new DiceResult(points);
    }

    /**
     * 將擲骰子的結果轉換成LINE的Message
     * @param diceResult 擲骰子的結果
     * @return LINE的Message
     */
    private Message toMessage(DiceResult diceResult) {
        FlexMessage flexMessage = FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Bubble.builder()
                                .body(getMainBodyBox(diceResult))
                                .build()
                ).build();
        return flexMessage;
    }

    /**
     * 取得main body的Box元件
     * @param diceResult 擲骰子的結果
     * @return Box元件
     */
    private Box getMainBodyBox(DiceResult diceResult) {
        Box mainBodyBox = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("骰子模擬器")
                                .size("sm")
                                .color("#1DB446")
                                .weight(Text.TextWeight.BOLD)
                                .build());
                        add(Text.builder()
                                .text("投擲結果")
                                .size("xxl")
                                .margin("md")
                                .weight(Text.TextWeight.BOLD)
                                .build());
                        // 投擲者面板
                        /*add(Box.builder()
                                .layout(FlexLayout.HORIZONTAL)
                                .contents(getRollerComponents(diceResult.getRollerName()))
                                .build());*/
                        add(Separator.builder()
                                .margin("xxl")
                                .build());
                        // 投擲點數
                        add(Box.builder()
                                .layout(FlexLayout.VERTICAL)
                                .contents(getPointsComponents(diceResult))
                                .build());
                    }
                })
                .build();
        return mainBodyBox;
    }

    /**
     * 取得投擲者面板的所有元件
     * @param rollerName 投擲者名稱
     * @return 投擲者面板的所有元件
     */
    private List<FlexComponent> getRollerComponents(String rollerName) {
        List<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text("投擲者")
                .size("xs")
                .color("#aaaaaa")
                .build());
        flexComponents.add(Text.builder()
                .text(rollerName)
                .size("xs")
                .color("#aaaaaa")
                .align(FlexAlign.END)
                .build());
        return flexComponents;
    }

    /**
     * 取得擲骰子點數的所有面板元件
     * @param diceResult 投擲結果
     * @return 擲骰子點數的所有面板元件
     */
    private List<FlexComponent> getPointsComponents(DiceResult diceResult) {
        List<FlexComponent> flexComponents = new ArrayList<>();
        // 顯示所有投擲結果
        for (int i = 0; i < diceResult.getDiceCount(); i++) {
            flexComponents.add(Box.builder()
                    .layout(FlexLayout.HORIZONTAL)
                    .contents(getPoints(i + 1, diceResult.getPoints().get(i)))
                    .build());
        }
        flexComponents.add(Separator.builder()
                .margin("xxl").build());
        // 顯示統計
        flexComponents.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("骰子數")
                                .flex(0)
                                .size("sm")
                                .color("#555555")
                                .build());
                        add(Text.builder()
                                .text(String.valueOf(diceResult.getDiceCount()))
                                .size("sm")
                                .color("#111111")
                                .align(FlexAlign.END)
                                .build());
                    }
                })
                .build());
        int sum = diceResult.getPoints().stream().mapToInt(Integer::intValue).sum();
        flexComponents.add(Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("總計")
                                .flex(0)
                                .size("sm")
                                .color("#555555")
                                .build());
                        add(Text.builder()
                                .text(String.valueOf(sum))
                                .size("sm")
                                .color("#111111")
                                .align(FlexAlign.END)
                                .build());
                    }
                })
                .build());
        return flexComponents;
    }

    /**
     * 動態產生投擲點數component
     * @param nth 第幾個骰子
     * @param point 點數
     * @return 擲點數component
     */
    private List<FlexComponent> getPoints(int nth, int point) {
        List<FlexComponent> flexComponents = new ArrayList<>();
        flexComponents.add(Text.builder()
                .text("dice" + nth)
                .flex(0)
                .size("sm")
                .color("#555555")
                .build());
        flexComponents.add(Text.builder()
                .text(String.valueOf(point))
                .color("#111111")
                .size("sm")
                .align(FlexAlign.END)
                .build());
        return flexComponents;
    }
}
