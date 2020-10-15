# 消息推送服务
指定端推送, 多端推送, 饱和式推送, 有序, 定时, 重试, 目标分组, 业务分组, 针对各分组的时间窗口控频和时间间隔控频

- 消息推送的持久化
- 指定端推送, 多端推送, 饱和式推送
- 无序推送, 有序推送, 定时推送, 重试推送
- 单目标推送, 目标组推送, 单业务推送, 业务组推送
- 针对目标, 目标组, 业务, 业务组, 推送通道, 通道组的推送频度控制
- 手动触发推送, api调度推送, 微服务调度推送
- 消息推送的全方位报表

### 有序推送
- 有序推送的每条消息都需要回执
- 由于回执有超时时间, 有序消息的推送可能重复
- 仅设置为有序的消息可以使用有序推送
- 如果对定时消息有序推送, 会优先根据消息顺序推送, 定时时间可能失效

### 定时推送
- `SimpleExecutor`的定时任务会因服务重启而丢失
- 若需要可靠的或分布式的定时推送, 自行实现任务调度系统替换`SimpleExecutor`

### 频度控制
- todo 多线程同时推送的情况下, 存在已推送而推送记录未入库, 使得频度控制失效的情况

### 自定义业务组
- 实现`BusinessRepository`替代`SimpleBusinessRepository`

### 自定义接收目标组
- 实现`ReceiverRepository`替代`SimpleReceiverRepository`

### 自定义通道组
- 实现`TunnelRepository`替代`InMemoryTunnelRepository`

### 取消推送
- 由于自定义了业务组目标组通道组, 一条业务消息的提交可能对应多条待发送, 故不设置取消推送功能

### 多实例扩展
- 实现分布式锁`PushLocker`替代`InMemoryPushLocker`
- 按需实现通道repo`TunnelRepository`替代`InMemoryTunnelRepository`
- todo 测试