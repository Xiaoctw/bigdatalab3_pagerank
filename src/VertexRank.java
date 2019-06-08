import java.util.ArrayList;
import java.util.List;

class VertexRank{
    int id;
    double weight;//权值,代表当前页面的重要程度
    private double d;//代表阻尼系数
    private Worker worker;
    private List<VertexRank> outVertexes;
    VertexRank(int id) {
        this.id=id;
        d=0.85;
        weight=10;//初始化变量
        outVertexes=new ArrayList<>();
    }

    /**
     * 核心代码,完成每一轮中每个计算节点的运算
     * @param message2s 收到的信息
     */
    void compute(List<Message2> message2s){
        int numVer=outVertexes.size();
        if(message2s==null||message2s.isEmpty()){//第一次
            double mes=weight/numVer;
            for (VertexRank vertex : outVertexes) {
                sendMessage(vertex,mes);
            }
        }else {
            weight=1-d;
            double sum=0;
            for (Message2 message2 : message2s) {
                sum+=message2.getVal();
            }
            weight+=sum*d;
            double mes=weight/numVer;
            for (VertexRank vertex : outVertexes) {
                sendMessage(vertex,mes);
            }
        }
    }
    private void sendMessage(VertexRank vertexRank, double val){
        Message2 message2=new Message2(vertexRank);
        message2.setVal(val);
        worker.message2sFromVec.add(message2);
    }

    void setWorker(Worker worker) {
        this.worker = worker;
    }

    void addVertex(VertexRank vertex){
        this.outVertexes.add(vertex);
    }
}
