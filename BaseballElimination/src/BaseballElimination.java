import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.HashMap;
import java.util.ArrayList;

public class BaseballElimination {

    private int N;
    private HashMap<String,ArrayList<Integer>> info;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        setInfo(in);
    }

    private void setInfo(In in){
        this.N = Integer.parseInt(in.readLine());
        info = new HashMap<>();
        int count =0;
        while(in.hasNextLine()){
            String[] newline = in.readLine().split("\\s+");
            ArrayList<Integer> newarray = new ArrayList<>();
            newarray.add(count);
            int sign = 0;
            while(newline[sign].hashCode()==0){
                sign++;
            }
            //StdOut.println(newline[sign].hashCode());
            for(int i =sign+1;i<newline.length;i++){
                newarray.add(Integer.parseInt(newline[i]));
            }
            info.put(newline[sign], newarray);
            count++;
        }
    }

    private void setFlowNet(FlowNetwork net, String team){
        FordFulkerson ff = new FordFulkerson(net, 0, 0);
        StdOut.print(ff.toString());
    }

    // number of teams
    public int numberOfTeams() {
        return this.info.size();
    }

    // all teams
    public Iterable<String> teams() {
        return this.info.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return this.info.get(team).get(1);
    }

    // number of losses for given team
    public int losses(String team) {
        return this.info.get(team).get(2);
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return this.info.get(team).get(3);
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int indexOfTeam2 = this.info.get(team2).get(0);
        return this.info.get(team1).get(4 + indexOfTeam2);
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int n = this.N -1;
        FlowNetwork net = new FlowNetwork(n*(n-1)/2 + n+2);
        //setFlowNet(net, team);
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return this.info.keySet();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        StdOut.println(division.info.toString());
        StdOut.println(division.numberOfTeams());
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
