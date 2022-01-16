# WerewolfKiller

安卓app，支持版本Android6.0+

后端仓库地址：https://github.com/Extrwave/SpringBoot-Socialgame

使用技术：RecyclerView，ViewModle，Databing，MediaPlayer，Okhttp3，Gson，百度地图sdk开发

用于狼人杀面杀游戏辅助，有同城组队功能，可以发布和查看组队信息，目前已完成狼人杀游戏辅助模块和同城组队模块的大致开发，后续将对其分别进行完善并优化项目结构

***

截图：

<img src="https://user-images.githubusercontent.com/79448699/149645959-37f398bb-106b-4fff-85eb-c00a3a3d1a97.jpg" width="300"/> <img src="https://user-images.githubusercontent.com/79448699/149646001-48a6bd2f-3b53-433f-9c1c-95debf0af68c.jpg" width="300"/>  <img src="https://user-images.githubusercontent.com/79448699/149646363-5a0161ea-a1df-4dca-8404-a68e58aff342.jpg" width="300"/>
<img src="https://user-images.githubusercontent.com/79448699/149646370-4dafd8fd-9caa-48c1-beac-84f4948573a5.jpg" width="300"/> <img src="https://user-images.githubusercontent.com/79448699/149646372-14945bc6-13a3-46c3-9f64-803909f79f84.jpg" width="300"/>  <img src="https://user-images.githubusercontent.com/79448699/149646374-56cefeda-db8f-4b1f-9dbd-5930293cbb6d.jpg" width="300"/>
<img src="https://user-images.githubusercontent.com/79448699/149646375-f53dd454-5ed1-4a25-910a-354e4467fd1b.jpg" width="300"/>  <img src="https://user-images.githubusercontent.com/79448699/149646376-842fff3a-cbf9-4760-8f38-e37e7b5ac4d7.jpg" width="300"/>  <img src="https://user-images.githubusercontent.com/79448699/149646382-b236ed0d-cc0f-41dd-8a2d-bb8f2e6126ef.jpg" width="300"/>
<img src="https://user-images.githubusercontent.com/79448699/149646383-71daf0ee-db00-4696-bd8b-45c773dd062e.jpg" width="300"/>  <img src="https://user-images.githubusercontent.com/79448699/149646380-cdb8a6a1-ccbd-468c-810b-5355619f34c7.jpg" width="300"/>
<br/>

***

# 详细进度及功能介绍

### 用户模块：

登录：目前只支持账号登录，已预留QQ和微信登录接口，待申请API和完善快捷登录方式。

注册：账号密码注册，头像为预设头像，待完善：本地头像选择和上传，注册邮箱或短信验证

修改：修改更新用户信息。

<br/>

## 狼人杀面杀辅助模块：

初始化游戏配置：选择游戏角色身份，待完善：修改房间配置入口

创建房间：目前游戏数据不能实时推送到客户端。

加入房间：通过房间号进入房间

选择座位：选择空位入座，待完善：二次选座。

### 房主操作：

公布结果：当投票开启时公布结果会关闭投票并获取其他玩家的投票信息，汇总公布，投票未开启时会公布当夜的死亡信息

发起投票:开启投票

重新发牌：重新发牌，游戏天数归0；

进入天黑：新的一天开始。房主手机充当主持人，播放游戏指令。

### 玩家操作：

表决投票：选择玩家，投票。

使用技能：根据玩家身份，夜晚时使用技能和白天使用技能应有不同的判断条件，且夜晚只能在对应阶段使用技能。

查看身份:查看身份。

### 同城组队模块

编辑及发布组队信息：选择活动类型，活动手机，编辑活动地点，调用百度地图选择位置，编辑活动标题，详细内容，发布。

地图定位：根据描述搜索定位或根据点击获取位置经纬度和描述。

组队信息查看：客户端应显示距离较近的组队信息，根据经纬度计算出距离。
