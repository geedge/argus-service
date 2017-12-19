#!/bin/sh

log_file=/home/dongxiaooyan/apache-tomcat-7.0.59/logs/tomcat_job.log
echo "/home/dongxiaooyan/apache-tomcat-7.0.59/bin/shouhu.sh Start At:" >> $log_file
date >> $log_file
#count=1
while [ 1 -eq 1 ]
do
	sleep 300
	if [ `ps -ef | grep apache-tomcat-7.0.59 | grep -v grep | wc -l` -lt 1 ]
		then
			echo -n "Down at:" >> $log_file
			date >> $log_file
			
      # ---------  start apache-tomcat-7.0.59
		  /home/dongxiaooyan/apache-tomcat-7.0.59/bin/startup.sh
			echo "/home/dongxiaooyan/apache-tomcat-7.0.59 Start...." >>$log_file
	fi
	sleep 1
	
	
done