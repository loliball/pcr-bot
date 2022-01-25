# PCR自动报刀机器人

本项目直接使用mirai-core进行开发，未来会改成基于mirai-console开发的插件

# 使用

下载右侧releases中的最新版本并解压  
与jar同目录新建config.txt并输入以下内容   
qq.id = 机器人qq  
qq.pwd = 机器人密码  
qq.owner = 管理者qq  
BF_Client_Data = bigfun数据  
输入如下启动命令  
java -Dfile.encoding=UTF-8 -jar mirai-bot.main.jar

# 构建

下载源码后使用idea打开  
新建artificial JAR，选择JavaMain.java作为MainClass  
打包编译后需要手动删除jar包中的如下文件才能启动  
BC1024KE.DSA  
BC1024KE.SF  
BC2048KE.DSA  
BC2048KE.SF  