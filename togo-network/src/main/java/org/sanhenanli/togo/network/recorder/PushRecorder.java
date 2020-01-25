package org.sanhenanli.togo.network.recorder;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.tunnel.TunnelTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/18 20:41
 * 全局推送记录器
 *
 * @author zhouwenxiang
 */
public interface PushRecorder {

    /**
     * 最近第n条成功推送的时间
     * @param number n
     * @param receiver 消息接收者或接收者组
     * @param biz 消息所属业务或业务组
     * @param tunnel 消息使用通道或通道组
     * @return 推送结束时间
     */
    LocalDateTime lastSuccessTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel);

    /**
     * 最近第n条推送的时间
     * @param number n
     * @param receiver 消息接收者或接收者组
     * @param biz 消息所属业务或业务组
     * @param tunnel 消息使用通道或通道组
     * @return 推送结束时间
     */
    LocalDateTime lastAttemptTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel);

    /**
     * 最近第n条失败推送的时间
     * @param number n
     * @param receiver 消息接收者或接收者组
     * @param biz 消息所属业务或业务组
     * @param tunnel 消息使用通道或通道组
     * @return 推送结束时间
     */
    LocalDateTime lastErrorTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel);

    /**
     * 记录消息推送失败(正在重试中的消息不算失败)
     * @param message 消息
     * @param tip 推送结果
     */
    void recordError(Message message, TunnelTip tip);

    /**
     * 记录消息推送成功
     * @param message 消息
     */
    void recordSuccess(Message message);

    /**
     * 记录消息重试
     * @param message 消息
     * @param tip 此次的推送结果
     */
    void recordRetry(Message message, TunnelTip tip);
}
