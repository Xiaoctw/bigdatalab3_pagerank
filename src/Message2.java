public class Message2{
    private VertexRank dest;//代表消息发往的目的地
    private double val;//代表传入的值

    Message2(VertexRank dest) {
        this.dest = dest;
    }

    double getVal() {
        return val;
    }

    void setVal(double val) {
        this.val = val;
    }

    VertexRank getDest() {
        return dest;
    }
}