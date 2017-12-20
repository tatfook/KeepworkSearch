# 1.项目简介

提供百科知识库、keepwork检索服务；
提供百科知识库、keepwork新增数据服务。


# 2.项目安装

## 2.1 jdk安装
	建议1.8版本，/etc/profile添加如下内容：
	JAVA_HOME=/usr/java/jdk1.8.0_91
	PATH=$JAVA_HOME/bin:$PATH
	CLASSPATH=$JAVA_HOME/jre/lib/ext:$JAVA_HOME/lib/tools.jar
	export PATH JAVA_HOME CLASSPATH


## 2.2 play安装
   建议1.4.2版本，/etc/profile添加如下内容：
   PLAY_HOME=/home/essearch/play-1.4.2/
   PATH=$PATH:$PLAY_HOME 
   
## 2.3 端口修改地址
./play-1.4.2/KeepworkSearch/conf/application.conf:32:http.port=19001

# 3.项目运行

[root@la essearch]# cat start.sh 
#!/bin/sh

play run KeepworkSearch & > /home/essearch/log/jest.log > /dev/null 2>&1

