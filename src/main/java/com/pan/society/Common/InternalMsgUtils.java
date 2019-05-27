package com.pan.society.Common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.iuap.base.utils.RestUtils;
import com.yonyou.iuap.generic.utils.PropertiesUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 站内信工具类。
 *
 * @author wangruiv
 * @date 2018-08-31 16:47:19
 */
public final class InternalMsgUtils {
    private final static Logger log = LoggerFactory.getLogger(InternalMsgUtils.class);

    /**
     * 发送站内信。
     *
     * @param userIds 收信人用户主键集合。
     * @param subject 站内信主题。
     * @param content 站内信内容。
     */
    public static void send(String[] userIds, String subject, String content) {
        log.debug("开始发送站内信：userIds={}，subject={}，content={}", Arrays.toString(userIds), subject, content);

        if (ArrayUtils.isEmpty(userIds)) {
            log.error("消息接收人为空，发送站内信失败。");
        }

        JSONObject msg = new JSONObject();
        msg.put("subject", subject);
        msg.put("content", content);

        JSONArray users = new JSONArray();
        for (String userId : userIds) {
            JSONObject user = new JSONObject();
            user.put("id", userId);
            users.add(user);
        }

        JSONObject internalMsg = new JSONObject();
        internalMsg.put("msg", msg);
        internalMsg.put("users", users);

        try {
            String prefix = PropertiesUtils.getCustomerProperty("busi.base.url");
            String url = prefix + PropertiesUtils.getCustomerProperty("internalmsg.create.url");
            JsonResponse jsonResponse = RestUtils.getInstance().doPost(url, internalMsg, JsonResponse.class);
            if (jsonResponse == null || !Objects.equals(jsonResponse.get("status"), 1)) {
                log.error("发送站内信失败。");
                return;
            }

            log.debug("发送站内信成功。");
        } catch (IOException e) {
            log.error("发送站内信失败：无法获取配置busi.base.url和internalmsg.create.url的值。");
        } catch (Exception e) {
            log.error("发送站内信失败。", e);
        }
    }
}
