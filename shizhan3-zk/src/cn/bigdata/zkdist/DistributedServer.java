package cn.bigdata.zkdist;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class DistributedServer {
	private static final String connectString = "mini1:2181,mini2:2181,mini3:2181";
	private static final int sessionTimeout = 3000;
	private static final String parentNode = "/servers";
	private ZooKeeper zk = null;

	
	/**
	 * 创建zk客户端的链接
	 * @throws Exception
	 */
	public void getConnection() throws Exception {
		zk  = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 收到时间后的回调函数(应该是我们自己的时间处理逻辑)
				System.out.println(event.getType() + "--" + event.getPath());
				try {
					zk .getChildren("/", true);
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		});
	}

	  /**
	   * 向zk集群注册服务器信息
	   * @param hostname
	   * @throws Exception
	   */
	public void  registerService(String hostname) throws Exception {
		String create = zk.create(parentNode+"/server", hostname.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL );
		System.out.println(hostname+"is on line.."+create);
	}
	
	/**
	 * 业务功能
	 * @throws Exception 
	 */
	public void handleBusiness(String hostname) throws Exception {
		
		System.out.println(hostname+"start working...");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		// 获取zk链接
		DistributedServer server = new DistributedServer();
		server.getConnection();
		
		//利用zk链接注册服务器信息
		server.registerService(args[0]);
		
		//启动业务功能
		server.handleBusiness(args[0]);
		
		
	}
}
