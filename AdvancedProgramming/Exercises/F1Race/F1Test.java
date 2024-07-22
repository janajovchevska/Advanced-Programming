package AdvancedProgramming.Exercises.F1Race;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Lap
{
    int minutes;
    int seconds;
    int milliseconds;

    public Lap(int minutes, int seconds, int milliseconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }
    public double timeTransform()
    {
        return minutes*60+seconds+milliseconds/(double)1000;
    }

    @Override
    public String toString() {
        return String.format("%d:%d:%d",minutes,seconds,milliseconds);
    }
}
class Driver
{
    String name;
    List<Lap> laps;

    public Driver(String name, List<Lap> laps) {
        this.name = name;
        this.laps = laps;
    }
    public double shortestTime()
    {
       return laps.stream().min(Comparator.comparing(Lap::timeTransform)).get().timeTransform();
    }
    public Lap shortestTimeLap()
    {
        return laps.stream().min(Comparator.comparing(Lap::timeTransform)).get();
    }
}
class F1Race
{
    List<Driver> drivers;

    public F1Race() {
        drivers=new ArrayList<>();
    }
    public void readResults(InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(line->
                {

                   // Webber 2:32:103 2:49:182 2:18:132
                    String[] parts=line.split("\\s+");
                    String name=parts[0];
                    List<Lap> laps=new ArrayList<>();
                    for(int i=1;i< parts.length;i++)
                    {
                        String[] timeParts=parts[i].split(":");
                        int minutes=Integer.parseInt(timeParts[0]);
                        int seconds=Integer.parseInt(timeParts[1]);
                        int milliseconds=Integer.parseInt(timeParts[2]);
                        Lap lap=new Lap(minutes,seconds,milliseconds);
                        laps.add(lap);

                    }
                    Driver driver=new Driver(name,laps);
                    drivers.add(driver);

                }
                );
    }
   public void printSorted(OutputStream outputStream)
   {
       PrintWriter pw=new PrintWriter(outputStream);
       drivers=drivers.stream().sorted(Comparator.comparing(Driver::shortestTime)).collect(Collectors.toList());
      IntStream.range(1,drivers.size()+1).forEach(i->
      {
         pw.println(String.format("%d. %-10.10s %s",i,drivers.get(i-1).name,drivers.get(i-1).shortestTimeLap()));
      });
       pw.flush();
   }
}
public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

