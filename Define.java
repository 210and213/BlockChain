package p2pPeer;

import java.util.ArrayList;

public class Define {
    public final static String PUBLIC_ID = "everyone";
    public final static String PUBLIC_PASSWORD = "very-secret-password";
    public final static String PUBLIC_PIN = "secret-pin";
    public final static int least_exit=10;
    public final static int exit_all_time=1000;
    public final static int exit_only_time=1000;
    public final static ArrayList<Integer> block_Fragmentationdelete_us=new ArrayList<Integer>();//哪个区块正在进行删除操作 这个需要在节点创造的同时定义一个相同的 然后给此变量赋节点变量的值
    public final static int port_Fragmentation_socket=8000;//分片socket通信端口号
    public final static ArrayList<String> ip_list=new ArrayList<String>();//所有节点的ip地址
    public final static int blockchain_high=1;//区块总高度
}