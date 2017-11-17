# 大腿时刻 - 来看看你的哪个队友又 Carry 全场了吧

## 简介

大腿时刻是一个能让你知道最近哪个队友又在游戏中 Carry 了的小工具，暂时仅支持 Dota2。界面长这样

![display](https://github.com/orangex/AmazingFellow/blob/master/display.jpeg)

## 下载

[From GitHub](https://github.com/orangex/AmazingFellow/releases/download/v0.03/app-release.apk)

## 更新日志

v0.03      2017/11/14	引入 Bugly ，一键上滑至顶部

v0.02	2017/11/13 	增加通知栏提醒（非常驻，30分钟拉一次），引入 GreenDao ，下次进来不用网络也能曾经的数据。

v0.01	2017/11/12	基本确定下来了刷新加载逻辑，可以自己玩玩的东西。

v-1        ~ 2017/11/11  混沌的 coding

## Todo

- [ ] 微信分享战绩图片 （喵的微信把我的申请给驳回了）
- [x] 引入 Bugly 异常上报
- [x] 自动升级
- [ ] 反馈通道 （好累啊暂时就用邮箱吧）
- [x] 持久化
- [x] 通知栏提醒
- [x] Http 重试
- [ ] rxlifecircle
- [x] stetho
- [ ] Dagger2?
- [ ] 支持吃鸡和屁股(far away)
- [ ] 后端(far away)

## 注意

源码是无法运行的，因为 ignore 了个 config.java， 是一些 SDK 的 APPKEY 之类的不能外泄的东西，如有需要可以自行申请。