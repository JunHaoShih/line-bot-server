package io.github.junhaoshih.linebotserver.repository.api;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.profile.UserProfileResponse;
import io.github.junhaoshih.linebotserver.repository.LineRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

/**
 * 從LINE API取得LINE資訊
 */
@Repository
public class ApiLineRepository implements LineRepository {

    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Override
    public UserProfileResponse getUserProfile(String userId) throws ExecutionException, InterruptedException {
        LineMessagingClient client = LineMessagingClient.builder(channelToken).build();
        UserProfileResponse profileResponse = client.getProfile(userId).get();
        return profileResponse;
    }

    @Override
    public UserProfileResponse getGroupMemberProfile(String groupId, String userId) throws ExecutionException, InterruptedException {
        LineMessagingClient client = LineMessagingClient.builder(channelToken).build();
        UserProfileResponse profileResponse = client.getGroupMemberProfile(groupId, userId).get();
        return profileResponse;
    }

    @Override
    public UserProfileResponse getUserProfile(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException {
        System.out.println("Source type: " + event.getSource().getClass().getName());
        if (event.getSource().getClass().getName().equals(UserSource.class.getName()))
            return getUserProfile(event.getSource().getUserId());
        else
            return getGroupMemberProfile(event.getSource().getSenderId(), event.getSource().getUserId());
    }
}
