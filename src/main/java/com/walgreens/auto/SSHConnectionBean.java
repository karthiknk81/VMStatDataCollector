package com.walgreens.auto;

import java.util.Properties;

public class SSHConnectionBean {

	private String userName;
	private String hostName;
	private int port;
	private int timeOut;
	private String privateKeyFile;
	private Properties config;
	private String passPhrase;
	
	public SSHConnectionBean() {
	 this.config = new Properties();
	 this.port=22;
	}

	
	public int getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}


	public String getPassPhrase() {
		return passPhrase;
	}

	public void setPassPhrase(String passPhrase) {
		this.passPhrase = passPhrase;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPrivateKeyFile() {
		return privateKeyFile;
	}
	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}
	public Properties getConfig() {
		return config;
	}
	public String getConfig(String argKey) {
		return (String) config.get(argKey);
	}
	public void setConfig(String argKey, String argVal) {
		this.config.put(argKey, argVal);
	}
	
	
	
}
