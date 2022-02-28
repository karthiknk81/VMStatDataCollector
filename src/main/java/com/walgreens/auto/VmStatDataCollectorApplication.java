package com.walgreens.auto;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@SpringBootApplication
public class VmStatDataCollectorApplication {
	private static final String START_VMSTART = "chmod 700 collect_vmstat.sh;stdbuf -oL nohup ~/collect_vmstat.sh 30";
	private static final String STOP_VMSTART = "chmod 700 clean_vmstat.sh;~/clean_vmstat.sh";

	public static void main(String[] args) throws Exception {
		// SpringApplication.run(VmStatDataCollectorApplication.class, args);
		Session session = null;
		try {
			SSHConnectionBean conInfo = new SSHConnectionBean();
			conInfo.setHostName("aamead1eu2aa04");
			conInfo.setUserName("kkrishyc");
			conInfo.setPort(22);
			conInfo.setConfig("StrictHostKeyChecking", "no");
			conInfo.setPrivateKeyFile("C:\\Home\\tmp\\pk");
			conInfo.setPassPhrase("Welcome#01");
			conInfo.setTimeOut(60000);

			SSHConnectionManager mgr = new SSHConnectionManager();
			VmStatDataCollectorApplication thisObject= new VmStatDataCollectorApplication();

			session = mgr.getSession(conInfo);
			//thisObject.startVMStat(session);
			thisObject.stopVMStat(session);


		} finally {
			try {
				session.disconnect();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public boolean stopVMStat(Session session) {
		boolean retVal = false;
		System.out.println("Start....");
		try {
		
		copyFile(session,"C:\\Home\\tmp\\vmstat2\\clean_vmstat.sh","clean_vmstat.sh", true);
		copyFile(session,"/tmp/vmstat_data_col/*.csv","C:\\Home\\tmp\\vmstat2\\", false);
		executeScript(session,STOP_VMSTART);
		
		retVal = true;
		
		}catch (Exception e){
			e.printStackTrace();
			return retVal;
		}
		System.out.println("End....");
		return retVal;
	}
	public boolean startVMStat(Session session) {
		boolean retVal = false;
		try {
			
		copyFile(session,"C:\\Home\\tmp\\vmstat2\\collect_vmstat.sh","collect_vmstat.sh",true);
		executeScript(session,START_VMSTART);
		
		retVal = true;
		
		}catch (Exception e){
			e.printStackTrace();
			return retVal;
		}
		
		return retVal;
	}

	public void copyFile(Session argSession, String argSrc, String argDest, boolean isCopyTo) throws Exception {
		Channel channel = null;
		ChannelSftp sftpChannel = null;
		try {
			System.out.println("File copy started!");
			// Copy vmstat script to target server
			channel = argSession.openChannel("sftp");
			channel.connect();

			sftpChannel = (ChannelSftp) channel;
			if(isCopyTo)
				sftpChannel.put(argSrc, argDest);
			else 
				sftpChannel.get(argSrc, argDest);

			System.out.println("File copy completed...");
		} finally {
			try {
				sftpChannel.disconnect();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

	}



	public  void executeScript(Session argSession, String commands) throws Exception {

		try {
			Channel channel = argSession.openChannel("exec");

			((ChannelExec) channel).setCommand(commands);

			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}

			channel.disconnect();
		} finally {

		}

	}

	//Try outs Methods
	public static void executeScript() {
		String host = "aamead1eu2aa04";
		String user = "kkrishyc";
		String command2 = "chmod 700 collect_vmstat.sh; ./collect_vmstat.sh 1";
		try {
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			// jsch.addIdentity(new
			// File("C:\\Users\\kkrishyc\\.ssh\\id_rsa").getAbsolutePath(),"Welcome#01");
			jsch.addIdentity(new File("C:\\Home\\tmp\\pk").getAbsolutePath(), "Welcome#01");
			Session session = jsch.getSession(user, host, 22);
			session.setConfig(config);
			session.connect(60000);

			Channel channel = session.openChannel("exec");

			((ChannelExec) channel).setCommand(command2);

			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}

			channel.disconnect();
			session.disconnect();
			System.out.println("DONE");

//	        

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public static void copyFile() throws Exception {

		String USER = "kkrishyc";
		String HOST = "aamead1eu2aa04";
		int PORT = 22;
		String PKEY_FILE = "C:\\Home\\tmp\\pk";
		String PASSPHRASE = "Welcome#01";
		ChannelSftp sftpChannel = null;
		Channel channel = null;
		Session session = null;

		System.out.println("File copy started!");
		JSch jsch = new JSch();
		try {
			Properties sessionConfig = new Properties();
			sessionConfig.put("StrictHostKeyChecking", "no");

			jsch.addIdentity(new File(PKEY_FILE).getAbsolutePath(), PASSPHRASE);
			session = jsch.getSession(USER, HOST, PORT);
			session.setConfig(sessionConfig);
			session.connect(60000);

			// Copy vmstat script to target server
			channel = session.openChannel("sftp");
			channel.connect();

			sftpChannel = (ChannelSftp) channel;
			sftpChannel.put("C:\\Home\\tmp\\vmstat\\collect_vmstat.sh", "collect_vmstat.sh");

			System.out.println("File copy completed...");

			//executeScript(session, "");

		} finally {
			try {
				sftpChannel.disconnect();
				session.disconnect();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
//		executeScript();
	}
	
}
