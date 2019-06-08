import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker extends Node {
    Master master;//从属的主节点
    int numVectex;//保存的图节点的个数
    List<VertexRank> vertexList;//保存当前节点中存储的图节点
    List<Message2> message2sFromVec;//保存每一轮计算过程当中,从计算节点返回来的message值
    List<Double> times;//记录每个超级轮过程中计算的时间
    private Map<VertexRank,List<Message2>> map=new HashMap<>();//保留收到的每个消息对应的图节点
    public Worker(Master master,List<VertexRank> vertexList) {
        times=new ArrayList<>();
        this.master=master;
        this.vertexList = vertexList;
        numVectex= vertexList.size();
        message2sFromVec =new ArrayList<>();
    }

    public Worker(Master master) {
        times=new ArrayList<>();
        this.master=master;
        vertexList =new ArrayList<>();
        numVectex=0;
        message2sFromVec =new ArrayList<>();
    }

    /**
     * 向一个
     * @param vertex 节点
     */
    void addVectex(VertexRank vertex){
        if (vertexList ==null){
            vertexList =new ArrayList<>();
        }
        vertexList.add(vertex);
        numVectex++;
    }

    /**
     * 收到了信息,将信息传输到自己的所有图节点当中
     * 这些信息都是从主节点传过来的
     * @param list message信息
     */
    void receive(List<Message2> list){
        for (Message2 message2 : list) {
            if(map.containsKey(message2.getDest())){
                map.get(message2.getDest()).add(message2);
            }else {
                map.put(message2.getDest(),new ArrayList<>());
                map.get(message2.getDest()).add(message2);
            }
        }
    }

    void execute(){
        long startTime=System.currentTimeMillis();
        if (master.step==0){
            for (VertexRank vertexRank : vertexList) {
                vertexRank.compute(new ArrayList<>());
            }
        }
        for (VertexRank vertex : map.keySet()) {
            // vertex=(ShortestPathVertex)vertex;
            var lis=map.get(vertex);
            vertex.compute(lis);//进行执行操作
        }
        map.clear();//消息要及时清空
        send();//一定要及时发送消息,用于下一轮计算
        long endTime=System.currentTimeMillis();
        times.add((double)(endTime-startTime));
    }

    /**
     * 这里是把从图中节点计算得来的消息发送出去
     * 把消息添加到总的链表里
     */
    private void send(){
        master.message1sRec.addAll(message2sFromVec);
        message2sFromVec.clear();
    }
}