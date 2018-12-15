操作系统运行环境linux7.0
文件摘要工具安装步骤：
1.将MESA.zip 拷贝opt目录下，解压,执行update_env.sh
2.可将digest_gen拷贝到任何目录（ldd digest_gen查看依赖包）
执行命令如下：
./digest_gen -f /home/a.txt 结果如下：
/home/a.txt 21 1.154222 



如果报错
vim /etc/ld.so.conf.d/MESA.conf
/opt/MESA/lib/


如果还不行在执行
source /etc/profile.d/MESA.sh


如果开机自启动或者无法执行的话在咱们的tomcat守护脚本里面加入
source ~/.bash_profile
