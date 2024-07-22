package AdvancedProgramming.Exercises.Risk2;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Attack
{
    List<Integer> attacker;
    List<Integer> defender;

    public Attack(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }
    public String survived()
    {
        attacker=attacker.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        defender=defender.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int countA=0;
        int countD=0;
        for(int i=0;i<attacker.size();i++)
        {
            if(attacker.get(i)>defender.get(i))
            {countA+=1;}
            else
            countD+=1;
        }
        return String.format("%d %d",countA,countD);
    }
}
class Risk{


    List<Attack> attacks=new ArrayList<>();
    public  void processAttacksData (InputStream is)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        //int count=0;
        br.lines().forEach(line->
        {
            //X1 X2 X3;Y1 Y2 Y3,
            String[] parts=line.split(";" );
            String[] attacker=parts[0].split("\\s+");
            String[] defender=parts[1].split("\\s+");
            List<Integer> a=new ArrayList<>();
            List<Integer> d=new ArrayList<>();

            for(int i=0;i<attacker.length;i++)
            {
                a.add(Integer.parseInt(attacker[i]));
                d.add(Integer.parseInt(defender[i]));
            }
            Attack attack=new Attack(a,d);
           // Attack attack2=new Attack(d,a);
            System.out.println(String.format("%s",attack.survived()));




        });


    }

}
public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
