package cn.bigdata.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class clientTest {
	 public static void main(String[] args) throws Exception {
		  
		  
		  ZooKeeper zk = new ZooKeeper("192.168.2.62:2181", 1000, null);
		  
		  System.out.println(zk.getClass());
		    if(zk.exists("/test", false) == null)
		    {
		      zk.create("/test", "znode1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		    }
		    System.out.println(new String(zk.getData("/test", false, null)));
		 }
}
