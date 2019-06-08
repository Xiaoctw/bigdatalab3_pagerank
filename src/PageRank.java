import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class PageRank {
    private Map<Integer,VertexRank> valToVec;

    public PageRank(String inputFile,String outputFile,int step) throws FileNotFoundException {
        valToVec=new HashMap<>();
        Master master=new Master();
        List<Worker> workers=new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            workers.add(new Worker(master));
        }
        master.workers.addAll(workers);
        Scanner in=new Scanner(new File(inputFile));
        PrintStream stream=new PrintStream(new File(outputFile));
        while (in.hasNext()){
            int id1=in.nextInt();
            int id2=in.nextInt();
            if (!valToVec.containsKey(id1)){
                VertexRank vertexRank=new VertexRank(id1);
                valToVec.put(id1,vertexRank);
            }
            if (!valToVec.containsKey(id2)) {
                VertexRank vertexRank=new VertexRank(id2);
                valToVec.put(id2,vertexRank);
            }
            valToVec.get(id1).addVertex(valToVec.get(id2));
//            valToVec.get(id1).outVertexes.add(valToVec.get(id2));
        }
        System.out.println("导入节点完成");
        for (Integer i : valToVec.keySet()) {
            var vertex=valToVec.get(i);
            Worker worker=workers.get((int) (Math.random()*workers.size()));
            vertex.setWorker(worker);
            worker.addVectex(vertex);
            master.vertexWorkerMap.put(vertex,worker);
        }
        System.out.println("分配计算节点完成");
        for (int i = 0; i < step; i++) {//进行迭代运算
            master.execute();
            System.out.println("当前共有"+master.message1sRec.size()+"条信息");
            System.out.println("第"+(i+1)+"轮迭代完成");
        }
        System.out.println("进行权重排序");
        Set<VertexRank> vertexRanks=new TreeSet<>((vertexRank, t1) -> Double.compare(t1.weight,vertexRank.weight));
//        for (VertexRank value : valToVec.values()) {
//            System.out.println("节点"+value.id+"的权重为:");
//        }
        vertexRanks.addAll(valToVec.values());
        System.out.println("输出结果");
        for (VertexRank vertexRank : vertexRanks) {
            stream.print("节点"+vertexRank.id+"的权重为");
            stream.printf("%.2f\n",vertexRank.weight);
        }
        System.out.println("完成");
    }

    public static void main(String[] args) {
        try {
            new PageRank("/home/xiao/文档/大数据分析/实验3数据/图数据","/home/xiao/文档/大数据分析/实验3数据/计算pageRank结果",15);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
