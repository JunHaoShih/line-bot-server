package io.github.junhaoshih.linebotserver.repository;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.profile.UserProfileResponse;

import java.util.concurrent.ExecutionException;

/**
 * LINE Dao介面
 */
public interface LineRepository {

    /**
     * 用使用者ID取得profile，用於一對一聊天
     * @param userId 使用者ID
     * @return 使用者profile
     * @throws ExecutionException line的錯誤
     * @throws InterruptedException line的錯誤
     */
    UserProfileResponse getUserProfile(String userId) throws ExecutionException, InterruptedException;

    /**
     * 使用群組ID和使用者ID取得profile，用於群組聊天
     * @param groupId 群組ID
     * @param userId 使用者ID
     * @return 使用者profile
     * @throws ExecutionException line的錯誤
     * @throws InterruptedException line的錯誤
     */
    UserProfileResponse getGroupMemberProfile(String groupId, String userId) throws ExecutionException, InterruptedException;

    /**
     * 自動判斷傳入event的source類型，並取得使用者profile
     * @param event 聊天訊息事件
     * @return 使用者profile
     * @throws ExecutionException line的錯誤
     * @throws InterruptedException line的錯誤
     */
    UserProfileResponse getUserProfile(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException;
}
