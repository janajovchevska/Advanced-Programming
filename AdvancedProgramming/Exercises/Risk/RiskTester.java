package AdvancedProgramming.Exercises.Risk;


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
    public int successfulAttack()
    {
        attacker=attacker.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        defender=defender.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        for(int i=0;i<attacker.size();i++)
        {
            if(attacker.get(i)<=defender.get(i))
                return 0;
        }
        return 1;

    }
}
class Risk{


    List<Attack> attacks=new ArrayList<>();
    public  int processAttacksData (InputStream is)
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
            if(attack.successfulAttack()==1)
                attacks.add(attack);



        });
        return attacks.size();

    }

}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}