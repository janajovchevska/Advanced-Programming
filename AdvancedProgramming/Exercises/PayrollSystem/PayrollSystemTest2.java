package AdvancedProgramming.Exercises.PayrollSystem;

import java.util.*;
import java.util.stream.Collectors;

class BonusNotAllowedException extends Exception
{
    public BonusNotAllowedException(String message) {
        super(message);
    }
}
abstract class Employee
{
    String id;
    String level;
    double rate;
    String bonus;

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }
    abstract double salary();
   public double bonus(){
       String bonus2;
       if(bonus.contains("%"))
       {
           bonus2=bonus.replace("%","");
           return salary()*Double.parseDouble(bonus2)/100;
       }
       else
       {
           bonus2=bonus;
           return Double.parseDouble(bonus2);
       }
   }
   abstract double overtimeSalary();

    public String getLevel() {
        return level;
    }
}
class HourlyEmployee extends Employee
{

    double hours;

    public HourlyEmployee(String id, String level,double hours) {
        super(id, level);
        this.hours=hours;
    }

    @Override
    double salary() {
        if(hours>=40)
            return 40*rate+(hours-40)*rate*1.5;
        else
            return
                    hours*rate;
    }

    @Override
    double overtimeSalary() {
       // System.out.println(rate);
        if(hours>40)
        {
            return (hours-40)*rate*1.5;
        }
        else
            return 0;
    }


    @Override
    public String toString() {
        if(bonus.equals("0.00"))
            return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",id,level,salary()+bonus(),hours>=40 ? 40.00 : hours,hours>=40 ? hours-40.00 : 0.00);


        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f Bonus: %.2f",id,level,salary()+bonus(),hours>=40 ? 40.00 : hours,hours>=40 ? hours-40.00 : 0.00,bonus());
    }
}
class FreelanceEmployee extends Employee
{
    List<Integer> ticketPoints;

    public FreelanceEmployee(String id, String level,List<Integer> ticketPoints) {
        super(id, level);
        this.ticketPoints=ticketPoints;
    }

    @Override
    double salary() {
        return sumPoints()*rate;
    }

    @Override
    double overtimeSalary() {
        return -1;
    }

    public int sumPoints()
    {
        return ticketPoints.stream().mapToInt(i->i).sum();
    }

    @Override
    public String toString() {
        if(bonus.equals("0.00"))
            return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",id,level,salary()+bonus(),ticketPoints.size(),sumPoints());

        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d Bonus: %.2f",id,level,salary()+bonus(),ticketPoints.size(),sumPoints(),bonus());
    }
}
class PayrollSystem
{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    List<Employee> employees=new ArrayList<>();
    List<FreelanceEmployee> freelanceEmployees=new ArrayList<>();
    List<HourlyEmployee> hourlyEmployees=new ArrayList<>();

    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel)
    {
        this.hourlyRateByLevel=hourlyRateByLevel;
        this.ticketRateByLevel=ticketRateByLevel;
    }
    public Employee createEmployee (String line) throws BonusNotAllowedException {
        //H;ID;level;hours; 100
        //F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN; 10%
        //F;Gjorgji;level2;1;5;10;6
         String[] parts=line.split("\\s+");
         String bonus="0.00";
        if(parts.length==2)
         {
             if(parts[1].contains("%"))
             {

               // parts[1]=parts[1].replace("%","");
                if(Double.parseDouble(parts[1].replace("%",""))>20.00)
                    throw new BonusNotAllowedException(String.format("Bonus of %s is not allowed",parts[1]));
                 bonus= parts[1];
             }
             else
             {
                 if(Integer.parseInt(parts[1])>1000)
                     throw new BonusNotAllowedException(String.format("Bonus of %s$ is not allowed",parts[1]));

                 bonus=parts[1];
             }

         }

        String[] delovi=parts[0].split(";");
        String id=delovi[1];
        String level=delovi[2];

        if(delovi[0].equals("H"))
        {
            double hours=Double.parseDouble(delovi[3]);
            HourlyEmployee he=new HourlyEmployee(id,level,hours);
            he.rate=hourlyRateByLevel.get(level);
            he.bonus=bonus;
            employees.add(he);
            hourlyEmployees.add(he);

            //System.out.println(he);
            return he;

        }
        else
        {
            List<Integer> points=new ArrayList<>();
            for(int i=3;i< delovi.length;i++)
            {
                points.add(Integer.parseInt(delovi[i]));
            }
            FreelanceEmployee fe=new FreelanceEmployee(id,level,points);
            fe.rate=ticketRateByLevel.get(level);
            fe.bonus=bonus;
            employees.add(fe);
            freelanceEmployees.add(fe);
            //System.out.println(fe);
            return  fe;


        }

    }
    public Map<String, Double> getOvertimeSalaryForLevels ()
    {
       Map<String,Double> result=employees.stream().collect(Collectors.groupingBy(Employee::getLevel,Collectors.summingDouble(Employee::overtimeSalary)));
        List<String> keysWithZeros = result.keySet().stream().filter(key -> result.get(key) == -1).collect(Collectors.toList());

        keysWithZeros.forEach(result::remove);
        return result;

    }
    public void printStatisticsForOvertimeSalary ()
    {
       DoubleSummaryStatistics dss=hourlyEmployees.stream().mapToDouble(HourlyEmployee::overtimeSalary).summaryStatistics();
        System.out.println(String.format("Statistics for overtime salary: Min: %.2f Average: %.2f Max: %.2f Sum: %.2f",dss.getMin(),dss.getAverage(),dss.getMax(),dss.getSum()));
    }
    public Map<String, Integer> ticketsDoneByLevel()
    {
       return freelanceEmployees.stream().collect(Collectors.groupingBy(freelanceEmployees->freelanceEmployees.level,Collectors.summingInt(freelanceEmployees->freelanceEmployees.ticketPoints.size())));

    }
    public Collection<Employee> getFirstNEmployeesByBonus (int n)
    {
        return employees.stream().sorted(Comparator.comparing(Employee::bonus).reversed()).limit(n).collect(Collectors.toList());
        //return null;
    }
}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
               ps.printStatisticsForOvertimeSalary();
               break;
           case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                   System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
               break;
           case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
       }

    }
}