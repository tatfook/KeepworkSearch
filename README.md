# 1.��Ŀ���

�ṩ�ٿ�֪ʶ�⡢keepwork��������
�ṩ�ٿ�֪ʶ�⡢keepwork�������ݷ���


# 2.��Ŀ��װ

## 2.1 jdk��װ
	����1.8�汾��/etc/profile����������ݣ�
	JAVA_HOME=/usr/java/jdk1.8.0_91
	PATH=$JAVA_HOME/bin:$PATH
	CLASSPATH=$JAVA_HOME/jre/lib/ext:$JAVA_HOME/lib/tools.jar
	export PATH JAVA_HOME CLASSPATH


## 2.2 play��װ
   ����1.4.2�汾��/etc/profile����������ݣ�
   PLAY_HOME=/home/essearch/play-1.4.2/
   PATH=$PATH:$PLAY_HOME 
   
## 2.3 �˿��޸ĵ�ַ
./play-1.4.2/KeepworkSearch/conf/application.conf:32:http.port=19001

# 3.��Ŀ����

[root@la essearch]# cat start.sh 
#!/bin/sh

play run KeepworkSearch & > /home/essearch/log/jest.log > /dev/null 2>&1

