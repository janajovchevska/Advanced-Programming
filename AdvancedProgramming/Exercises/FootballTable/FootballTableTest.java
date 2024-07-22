package AdvancedProgramming.Exercises.FootballTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
class Team
{
    String name;
    int numberGames;
    int wins;
    int draws;
    int loses;
    int goalsScored;
    int goalsConceded;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(int numberGames) {
        this.numberGames = numberGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }
    public int points()
    {
        // број_на_победи x 3 + број_на_нерешени x 1
        return wins*3+draws;
    }
    public int goalDifference()
    {
        return goalsScored-goalsConceded;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d\n",name,numberGames,wins,draws,loses,points());
    }
}
class FootballTable{

    Map<String,Team> teams;

    public FootballTable() {
        teams=new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {

        Team home;
        Team away;
        if(!teams.containsKey(homeTeam))
        {

            home=new Team(homeTeam);
            teams.put(homeTeam,home);
        }else
            home=teams.get(homeTeam);

        if(!teams.containsKey(awayTeam))
        {
            away=new Team(awayTeam);
            teams.put(awayTeam,away);
        }
        else
            away=teams.get(awayTeam);


        home.setNumberGames(home.numberGames+1);
        away.setNumberGames(away.numberGames+1);

        home.setGoalsScored(home.goalsScored+homeGoals);
        home.setGoalsConceded(home.goalsConceded+awayGoals);

        away.setGoalsScored(away.goalsScored+awayGoals);
        away.setGoalsConceded(away.goalsConceded+homeGoals);

        if(homeGoals>awayGoals)
        {
            home.setWins(home.wins+1);
            away.setLoses(away.loses+1);
        }else
        if(homeGoals<awayGoals)
        {
            away.setWins(away.wins+1);
            home.setLoses(home.loses+1);
        }
        else
        {
            home.setDraws(home.draws+1);
            away.setDraws(away.draws+1);
        }






    }

    public void printTable() {
        Comparator<Team> comparator=Comparator.comparing(Team::points).thenComparing(Team::goalDifference).reversed().thenComparing(Team::getName);
        //teams.entrySet().stream().sorted()
        List<Team> teamList= teams.values().stream().sorted(comparator).collect(Collectors.toList());
        IntStream.range(1,teamList.size()+1).forEach(i->
                {
                    System.out.print(String.format("%2d. %s",i,teamList.get(i-1)));
                }
        );
        //teamList.forEach(team -> System.out.print(team));

    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
