# Diary
日记/便签app
### 前言
> 这是一个日记app（和便签很像），第一次上传代码到GitHub，以及当练习练练手。参考的是这个 APP 是这个老哥的项目[developerHaoz]（https://github.com/developerHaoz）， app地址 [WatermelonDiaryNew](https://github.com/developerHaoz/WatermelonDiaryNew) 


#### 1、界面
界面和developerHaoz的几乎一样，删减了一些控件，布局更简单些


#### 2、第三方库
没有用到原作者用到的库

像butterknife很简单方便，但是练手的话就想自己写View的绑定Click事件等

#### 3、日记增删改的实现
这点和原作者有些不一样

原作者写了DatabaseHelper类作为数据库操作的帮助类，然后在需要进行增删查改的地方再写相关操作

本人这里写了一个DiaryList类（单例），里面有数据库的操作方法，在需要用到的时候调用

### 最后
本人是个懒得写文档的人，具体内容自己克隆项目研究

### 新增功能
使用Service、AlarmManager和Notification实现新功能

#### 添加通知栏提醒功能
删除日记后，通知栏发送通知


#### 添加到点提醒功能
1.到了设定的时间后，开启服务，通知栏发送通知提醒

2.部分机型关闭apk后定时器（AlarmManager）失效，谷歌原生系统有效

  可能和第三方厂家修改有关，也许是自身使用了错误的AlarmManager相关方法
