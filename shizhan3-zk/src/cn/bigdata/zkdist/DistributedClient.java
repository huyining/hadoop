package cn.bigdata.zkdist;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DistributedClient {
	private static final String connectString = "mini1:2181,mini2:2181,mini3:2181";
	private static final int sessionTimeout = 3000;
	private static final String parentNode = "/servers";

	// volatile:
	private volatile List<String> serverList;
	private ZooKeeper zk = null;

	/**
	 * 创建zk客户端的链接
	 * 
	 * @throws Exception
	 */
	public void getConnection() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数(应该是我们自己的时间处理逻辑)
				try {
					
					getServiceList();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void getServiceList() throws Exception {
		// 获取服务器子节点信息,并且对父节点进行监听
		List<String> children = zk.getChildren(parentNode, true);
		List<String> servers = new ArrayList<String>();
		for (String child : children) {
			// child只是子节点的节点名
			byte[] data = zk.getData(parentNode + "/" + child, false, null);
			servers.add(new String(data));
		}
		// 将service赋值给成员变量
		serverList = servers;
		
		//打印服务器列表
		System.out.println(serverList);
	}

	/**
	 * 业务功能
	 * @throws Exception 
	 */
	public void handleBusiness() throws Exception {
		
		System.out.println("client start working...");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		// 获取zk链接
		DistributedClient client = new DistributedClient();
		client.getConnection();
		// 获取server的子节点信息(并监听),从中获取服务器信息列表
		client.getServiceList();

		// 业务线程使用
		client.handleBusiness();
	}
}
