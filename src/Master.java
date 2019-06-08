import java.util.*;

class Master extends Node{
    int step;//超级轮的个数
    List<Message2> message1sRec;
    Map<VertexRank,Worker> vertexWorkerMap;//保留每个图节点位于哪一个工作节点当中
    Set<Worker> workers;
    Master() {
        workers=new HashSet<>();
        vertexWorkerMap =new HashMap<>();
        step=0;
        message1sRec=new ArrayList<>();
    }

    /**
     * 这个相当于是进行一步执行操作,超级轮的个数会递增,收到上一次来的消息,进行
     * 消息发送,把对应的消息发送到对应的节点中
     */
    void execute(){
        if (step==0){
            //Set<Worker> workers= (Set<Worker>) vertexWorkerMap.values();
            for (Worker worker : workers) {
                worker.execute();
            }
            step++;
            return;
        }
        //初始操作,什么也不用做
        Map<Worker, List<Message2>> map = new HashMap<>();
        for (Message2 message2 : message1sRec) {//对从上一轮收到的信息进行处理操作
            Worker worker = vertexWorkerMap.get(message2.getDest());
            if (!map.containsKey(worker)) {
                map.put(worker, new ArrayList<>());
            }
            map.get(worker).add(message2);
        }

        for (Worker worker : map.keySet()) {//接受操作
            worker.receive(map.get(worker));
        }
        message1sRec.clear();//消息及时清空,注意消息何时清空
        for (Worker worker : map.keySet()) {//执行操作,这个与接受操作分开进行
            worker.execute();
        }
        step++;
    }
}
