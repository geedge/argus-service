#!/bin/sh

log_file=/home/dongxiaooyan/apache-tomcat-7.0.59/logs/tomcat_restart_job.log
tomcat_home=/home/dongxiaooyan/apache-tomcat-7.0.59
echo "/home/dongxiaooyan/apache-tomcat-7.0.59/bin/shouhu.sh Start At:" >> $log_file
date >> $log_file
#count=1
while [ 1 -eq 1 ]
do
	sleep 86400
	#sleep 30
	if [ `ps -ef | grep apache-tomcat-7.0.59 | grep -v grep | wc -l` -lt 1 ]
		then
			echo -n "Down at:" >> $log_file
			date >> $log_file
			
      # ---------  start apache-tomcat-7.0.59
		  $tomcat_home/bin/startup.sh
			echo "$tomcat_home Start...." >>$log_file
	fi
	if [ `ps -ef | grep apache-tomcat-7.0.59 | grep -v grep | wc -l` -eq 1 ]
		then
			echo -n "apache-tomcat-7.0.59 is running at:" >> $log_file
			date >> $log_file			
			echo "Now will restart apache-tomcat-7.0.59 ...." >> $log_file
      # ---------  Now will to shutdowd the apache-tomcat-7.0.59
			a=`ps axf|grep 'apache-tomcat-7.0.59'|grep -v grep|awk '{print \$1}'`;
			echo "tomcat process id: $a " >> $log_file
			kill -9 $a;
			echo -n "kille tomcat at:" >> $log_file
			date >> $log_file	
			echo "check apache-tomcat-7.0.59 stat " >> $log_file
			if [ `ps -ef | grep apache-tomcat-7.0.59 | grep -v grep | wc -l` -lt 1 ]
				then
					echo "apache-tomcat-7.0.59 has been killed! " >> $log_file
		      # ---------  Now will to start apache-tomcat-7.0.59
		      echo "Now will to start apache-tomcat-7.0.59 ...." >> $log_file
				  $tomcat_home/bin/startup.sh &
					echo "$tomcat_home Start...." >>$log_file
					if [ `ps -ef | grep apache-tomcat-7.0.59 | grep -v grep | wc -l` -lt 1 ]
						then
							echo -n "Start apache-tomcat-7.0.59 faild!" >> $log_file
							date >> $log_file	
					fi
			fi
	fi
	sleep 1
	
	
done