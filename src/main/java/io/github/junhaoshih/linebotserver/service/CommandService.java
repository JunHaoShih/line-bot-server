package io.github.junhaoshih.linebotserver.service;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Service;

@Service
public class CommandService {
    public Message getCommandMessage(String[] args) {
        if (args.length == 1)
            return getDefaultMessage();

        if ("roll".equals(args[1])) {
            return getRollCommandMessage();
        } else  if ("油價".equals(args[1])) {
            return getOilCommandMessage();
        }
        return getDefaultMessage();
    }

    private Message getDefaultMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("可用指令:").append(System.lineSeparator());
        builder.append("!指令: 顯示所有可用指令").append(System.lineSeparator());
        builder.append("!發票: 顯示當期發票兌獎資訊").append(System.lineSeparator());
        builder.append("!roll: 投擲骰子，請輸入「!指令 roll」取得更詳細之說明").append(System.lineSeparator());
        builder.append("!油價: 取得中油油價歷史，請輸入「!指令 油價」取得更詳細之說明").append(System.lineSeparator());

        return new TextMessage(builder.toString());
    }

    /**
     * 取得擲骰子詳細資訊
     * @return 擲骰子詳細資訊
     */
    private Message getRollCommandMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("擲骰子指令為 !roll xdy").append(System.lineSeparator());
        builder.append("其中x為骰子數，y為骰子面數").append(System.lineSeparator());
        builder.append("1 <= x <= 20, 4 <= y <= 100").append(System.lineSeparator());
        builder.append("以下為合法的擲骰子指令範例").append(System.lineSeparator());
        builder.append("!roll 2d6").append(System.lineSeparator());
        builder.append("上方指令將投擲2顆6面的骰子").append(System.lineSeparator());

        return new TextMessage(builder.toString());
    }

    private Message getOilCommandMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("油價指令為 !油價 x").append(System.lineSeparator());
        builder.append("其中x為油類").append(System.lineSeparator());
        builder.append("92、95、98").append(System.lineSeparator());
        builder.append("以下為合法的油價指令範例").append(System.lineSeparator());
        builder.append("!油價 95").append(System.lineSeparator());
        builder.append("上方指令將95無鉛汽油近五周油價").append(System.lineSeparator());

        return new TextMessage(builder.toString());
    }
}
