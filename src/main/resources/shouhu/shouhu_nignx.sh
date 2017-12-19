#!/bin/sh

log_file=/usr/local/nignx/nignx_job.log
echo "/usr/local/nginx/sbin/shouhu_nignx.sh Start At:" >> $log_file
date >> $log_file
#count=1
while [ 1 -eq 1 ]
do
	sleep 30
	if [ `ps -ef | grep nignx | grep -v grep | wc -l` -lt 1 ]
		then
			echo -n "Down at:" >> $log_file
			date >> $log_file
			
      # ---------  start /usr/local/nginx/sbin/nginx
		  /usr/local/nginx/sbin/nginx
			echo "/usr/local/nginx/sbin/nginx Start...." >>$log_file
	fi
	sleep 1
	
	
done