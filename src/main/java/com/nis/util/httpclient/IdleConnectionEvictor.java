/**
 *@Title: IdleConnectionEvictor.java
 *@Package com.nis.util.httpclient 
 *@Description TODO
 *@author dell
 *@date 2016年11月7日 下午2:20:35
 *@version  版本号
 */
package com.nis.util.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * @ClassName: IdleConnectionEvictor.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年11月7日 下午2:20:35
 * @version V1.0
 */
public class IdleConnectionEvictor extends Thread{
	private final HttpClientConnectionManager connMgr;  
    
    private Integer waitTime;  
  
    private volatile boolean shutdown;  
  
    public IdleConnectionEvictor(HttpClientConnectionManager connMgr,Integer waitTime) {  
        this.connMgr = connMgr;  
        this.waitTime = waitTime;  
        this.start();  
    }  
  
    public void run() {  
        try {  
            while (!shutdown) {  
                synchronized (this) {  
                    wait(waitTime);  
                    // 关闭失效的连接  
                    connMgr.closeExpiredConnections();  
                }  
            }  
        } catch (InterruptedException ex) {  
            // 结束  
        }  
    }  
  
    /** 
     * 销毁释放资源 
     */  
    public void shutdown() {  
        shutdown = true;  
        synchronized (this) {  
            notifyAll();  
        }  
    }
}
