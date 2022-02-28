package com.walgreens.auto;

import java.io.File;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHConnectionManager {
	
	private JSch jsch;
	
	
	public Session getSession(SSHConnectionBean argConInfo) {
		
		Session session = null;
		try {
			jsch = new JSch();
	    	jsch.addIdentity(argConInfo.getPrivateKeyFile(),argConInfo.getPassPhrase());
	    	session = jsch.getSession(argConInfo.getUserName(),argConInfo.getHostName(),argConInfo.getPort());
	    	session.setConfig(argConInfo.getConfig());
	    	session.connect(argConInfo.getTimeOut());
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				//close(session);
			}
		return session;
		
	}
	
	public void close(Session argSession) {
		try {
	        argSession.disconnect();	        
		}catch (Throwable t) {
			t.printStackTrace();
		}		
	}
	
	public void close(Channel argChannel) {
		try {
	        argChannel.disconnect();	        
		}catch (Throwable t) {
			t.printStackTrace();
		}		
	}

}
