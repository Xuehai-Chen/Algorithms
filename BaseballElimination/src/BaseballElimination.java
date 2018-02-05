import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {

    private int N;
    private HashMap<String, ArrayList<Integer>> info;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        setInfo(in);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        StdOut.println(division.info.toString());
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void setInfo(In in) {
        this.N = Integer.parseInt(in.readLine());
        info = new HashMap<>();
        int count = 0;
        while (in.hasNextLine()) {
            String[] newline = in.readLine().split("\\s+");
            ArrayList<Integer> newarray = new ArrayList<>();
            newarray.add(count);
            int sign = 0;
            while (newline[sign].hashCode() == 0) {
                sign++;
            }
            for (int i = sign + 1; i < newline.length; i++) {
                newarray.add(Integer.parseInt(newline[i]));
            }
            info.put(newline[sign], newarray);
            count++;
        }
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
        int n = this.N - 1;
        for (String t : teams()) {
            if (!t.equals(team) && this.wins(t) > (wins(team) + remaining(team))) return true;
        }
        FlowNetwork net = new FlowNetwork(n * (n - 1) / 2 + n + 2);
        setFlowNet(net, team);
        FordFulkerson ff = new FordFulkerson(net, n * (n + 1) / 2 + 1, 0);
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <=n; j++) {
               if(ff.inCut(i * n - i * (i + 1) / 2 + j)) return true;
            }
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return this.info.keySet();
    }

    private void setFlowNet(FlowNetwork net, String team) {
        int n = this.N - 1;
        int s = n * (n + 1) / 2 + 1;
        int count = 0;
        int teamTotal = wins(team) + remaining(team);
        String[] index = new String[n];
        for (String t : this.teams()) {
            if (!t.equals(team)) {
                index[count] = t;
                if(count!=n-1){
                    count++;
                }else{
                    break;
                }

            }
        }
        for (int i = 1; i <= n; i++) {
            FlowEdge e = new FlowEdge(i, 0, teamTotal - wins(index[i - 1]));
            net.addEdge(e);
        }
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <=n; j++) {
                FlowEdge ei = new FlowEdge(i * n - i * (i + 1) / 2 + j, i, Double.POSITIVE_INFINITY);
                net.addEdge(ei);
                FlowEdge ej = new FlowEdge(i * n - i * (i + 1) / 2 + j, j, Double.POSITIVE_INFINITY);
                net.addEdge(ej);
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                FlowEdge e = new FlowEdge(s, i * n - i * (i + 1) / 2 + j, against(index[i - 1], index[j - 1]));
                net.addEdge(e);
            }
        }
    }
}
