package AdvancedProgramming.Exercises.DailyTemperatures;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
class Temperature
{
    List<String> temperatures;


    public Temperature(List<String> temperatures) {
        this.temperatures = temperatures;
    }

    public List<Double> transformToCelsius()
    {
        List<Double> transformedTemperatures=new ArrayList<>();
        List<String> temperatures2=new ArrayList<>();
        if(temperatures.get(0).contains("F")) {

            temperatures2 = temperatures.stream().map(t ->
            {
                t = t.replace("F", "");
                return t;
            }).collect(Collectors.toList());


            temperatures2.forEach(t ->
            {
                double parsed = Double.parseDouble(t);
                parsed = (parsed - 32) * 5 / 9;
                transformedTemperatures.add(parsed);
            });
        }
        else
        {
            temperatures2= temperatures.stream().map(t ->
            {
                t = t.replace("C", "");
                return t;
            }).collect(Collectors.toList());
            temperatures2.forEach(t ->
            {
                double parsed = Double.parseDouble(t);
                //parsed = (parsed - 32) * 5 / 9;
                transformedTemperatures.add(parsed);
            });

        }
        return transformedTemperatures;

    }
    public List<Double> transformToFahrenheit()
    {
        List<Double> transformedTemperatures=new ArrayList<>();
        List<String> temperatures2=new ArrayList<>();
        if(temperatures.get(0).contains("C")) {

            temperatures2 = temperatures.stream().map(t ->
            {
                t = t.replace("C", "");
                return t;
            }).collect(Collectors.toList());


            temperatures2.forEach(t ->
            {
                double parsed = Double.parseDouble(t);
                parsed = (parsed*9/5)+32;
                transformedTemperatures.add(parsed);
            });
        }
        else
        {
            temperatures2 = temperatures.stream().map(t ->
            {
                t = t.replace("F", "");
                return t;
            }).collect(Collectors.toList());
            temperatures2.forEach(t ->
            {
                double parsed = Double.parseDouble(t);
                //parsed = (parsed - 32) * 5 / 9;
                transformedTemperatures.add(parsed);
            });

        }
        return transformedTemperatures;

    }

}
class DailyTemperatures
{

    Map<Integer,Temperature> dailyTemperatures;
    public DailyTemperatures() {
        dailyTemperatures=new TreeMap<>();
    }
    public void readTemperatures(InputStream inputStream)
    {

        //137 23C 15C 28C.
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(line->
                {
                    String[] parts=line.split("\\s+");
                    int day=Integer.parseInt(parts[0]);
                    List<String> temperatures=new ArrayList<>();
                    for(int i=1;i<parts.length;i++)
                    {
                        temperatures.add(parts[i]);
                    }
                    Temperature temperature=new Temperature(temperatures);
                    dailyTemperatures.put(day,temperature);
                }

        );

        // System.out.println(dailyTemperatures);

    }
    void writeDailyStats(OutputStream outputStream, char scale)
    {
        PrintWriter pw=new PrintWriter(outputStream);
        dailyTemperatures.entrySet().forEach(entry->
                {
                    // 11: Count:   7 Min:  38.33C Max:  40.56C Avg:  39.44C
                    int day=entry.getKey();
                    if(scale=='C')
                    {
                        DoubleSummaryStatistics dss=entry.getValue().transformToCelsius().stream().mapToDouble(t->t).summaryStatistics();
                        pw.println(String.format("%3d: Count: %3d Min:%7.2fC Max:%7.2fC Avg:%7.2fC",day,dss.getCount(),dss.getMin(),dss.getMax(),dss.getAverage()));

                    }
                    if(scale=='F')
                    {
                        DoubleSummaryStatistics dss=entry.getValue().transformToFahrenheit().stream().mapToDouble(t->t).summaryStatistics();
                        pw.println(String.format("%3d: Count: %3d Min:%7.2fF Max:%7.2fF Avg:%7.2fF",day,dss.getCount(),dss.getMin(),dss.getMax(),dss.getAverage()));

                    }
                }

        );
        pw.flush();
    }
}
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

// Vashiot kod ovde
