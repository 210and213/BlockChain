package org.hive2hive.examples;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.api.interfaces.IUserManager;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Peer {

    private IH2HNode node;
    private FileAgent fileAgent;
    int port_Fragmentation_socket;//分片socket通信端口号
    ArrayList<Integer> block_Fragmentationdelete_us;//哪个区块正在进行删除操作 这个需要在节点创造的同时定义一个相同的 然后给此变量赋节点变量的值
    ArrayList<String> ip_list;//所有节点的ip地址
    int blockchain_high;//区块总高度
	int blockchain_range=500;//定义一个检查删除范围，比如现在区块高度为1000，我们只检查1000-500的区块删除，因为0-500的大概率已经删除到稳定节点个数
	 
    public Peer(String directory){
        fileAgent = new FileAgent(new File(directory));
        node = H2HNode.createNode(FileConfiguration.createDefault());
        node.connect(NetworkConfiguration.createInitial());

        IUserManager userManager = node.getUserManager();
        UserCredentials everyone = new UserCredentials(Define.PUBLIC_ID, Define.PUBLIC_PASSWORD,Define.PUBLIC_PIN);
       
		
        try{
            IProcessComponent<Void> register = userManager.createRegisterProcess(everyone);
            register.execute();

            IProcessComponent<Void> login = userManager.createLoginProcess(everyone, fileAgent);
            login.execute();

            node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Peer(String ipAddr, String directory){
    	
        fileAgent = new FileAgent(new File(directory));
        node = H2HNode.createNode(FileConfiguration.createDefault());
        try{
            NetworkConfiguration nodeConf = NetworkConfiguration.create(InetAddress.getByName(ipAddr));
            node.connect(nodeConf);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }

        IUserManager userManager = node.getUserManager();
        UserCredentials everyone = new UserCredentials(Define.PUBLIC_ID, Define.PUBLIC_PASSWORD,Define.PUBLIC_PIN);

        try {
            IProcessComponent<Void> login = userManager.createLoginProcess(everyone, fileAgent);
            login.execute();
            if(userManager.isLoggedIn()){//
                System.out.println("login success!");//
            }//

            node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public IH2HNode getNode(){
        return node;
    }

    public FileAgent getFileAgent() {
        return fileAgent;
    }
    
    public void delete_run(){//循环一轮删除节点
    	 for( int count=blockchain_high;count>(blockchain_high-blockchain_range);count--){
			 block_Fragmentationdelete_us.set(count, 1);
			 Thread DeletetThread=new Thread(new FileFragmentation_delete(count, port_Fragmentation_socket, ip_list, block_Fragmentationdelete_us));
			 DeletetThread.start();
			 block_Fragmentationdelete_us.set(count, 0);
		 }
    }

    public static void main(String args[]){
        Peer peer1 = new Peer("C:\\Users\\new\\Desktop\\tmp1");

        try{
            Peer peer2 = new Peer("127.0.0.1","C:\\Users\\new\\Desktop\\tmp2");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            File fileAtPeer1 = new File(peer1.getFileAgent().getRoot(), "test-file.txt");
            FileUtils.write(fileAtPeer1, "Hello"); // add some content
            IFileManager fileManager1 = peer1.getNode().getFileManager();
            fileManager1.createAddProcess(fileAtPeer1).execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
        

    }

}
