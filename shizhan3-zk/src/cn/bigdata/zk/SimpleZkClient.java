package cn.bigdata.zk;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class SimpleZkClient {
	private static final String connectString = "mini1:2181,mini2:2181,mini3:2181";
	private static final int sessionTimeout = 3000;
	ZooKeeper zkClient = null;

	@Before
	public void init() throws Exception {
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 收到时间后的回调函数(应该是我们自己的时间处理逻辑)
				System.out.println(event.getType() + "--" + event.getPath());
				try {
					zkClient.getChildren("/", true);
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		});
	}

	/**
	 * 增删改查
	 * 
	 * @throws Exception
	 * @throws KeeperException
	 */
	@Test // 创建数据节点到zk中
	public void testCreate() throws KeeperException, Exception {
		// 1.节点路径 2.节点的数据 3.节点的权限 4.节点的类型
		String nodeCreate = zkClient.create("/eclipse", "hellozk".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		//上传的数据可以是任何类型,但是都要转成byte
	}

	// 判断znode是否存在
	@Test
	public void testExist() throws Exception {
		Stat stat = zkClient.exists("/eclipse", false);
		System.out.println(stat == null ? "not exist" : "exist");
	}

	// 获取子节点
	@Test
	public void getChildren() throws Exception, Exception {
		List<String> children = zkClient.getChildren("/", true);
		for (String child : children) {
			System.out.println(child);
		}
		Thread.sleep(Long.MAX_VALUE);
	}
	
	//获取znode的数据
	@Test
	public void getDate() throws Exception {
		byte[] data = zkClient.getData("/eclipse", false, null);
		System.out.println(new String(data));
	}
   
	//删除znode
	@Test
	public void deleteZnode() throws Exception {
		zkClient.delete("/eclipse", -1);
	}
	
	//添加znode
	@Test
	public void setZnode() throws Exception {
		zkClient.setData("/app1", "imissyou".getBytes(), -1);
		byte[] data = zkClient.getData("/app1", false, null);
		System.out.println(new String(data));
	}
	
	
}
