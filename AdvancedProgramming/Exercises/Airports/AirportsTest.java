package AdvancedProgramming.Exercises.Airports;

import java.util.*;
import java.util.stream.IntStream;

class Airport
{
    String name;
    String country;
    String code;
    int passenger;

    public Airport(String name, String country, String code, int passenger) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passenger = passenger;
    }
}
class Flight
{
    String from;
    String to;
    int time;
    int duration;
    //String code;

    public Flight(String from,String to,int time, int duration) {
        //this.code=code;
        this.from=from;
        this.to=to;
        this.time = time;
        this.duration = duration;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        int end = time + duration;
        int plus = end / (24 * 60);
        end %= (24 * 60);
        return String.format("%s-%s %02d:%02d-%02d:%02d%s %dh%02dm", from, to, time / 60, time % 60,
                end / 60, end % 60, plus > 0 ? " +1d" : "", duration / 60, duration % 60);
    }
}
class Airports
{
    Map<String,Airport> airports;
    Map<String,Set<Flight>> flightsByA;
    Map<String,Set<Flight>> flightsFromTo;
    Map<String,Set<Flight>> flightsTo;

    public Airports() {
        airports=new HashMap<>();
        flightsByA=new TreeMap<>();
        flightsFromTo=new HashMap<>();
        flightsTo=new HashMap<>();

    }
    public void addAirport(String name, String country, String code, int passengers)
    {
        airports.putIfAbsent(code,new Airport(name,country,code,passengers));
    }
    public void addFlights(String from, String to, int time, int duration)
    {
        Comparator<Flight> comparator=Comparator.comparing(Flight::getTime);
        Flight flight=new Flight(from,to,time,duration);
        flightsByA.putIfAbsent(from,new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime)));
        flightsByA.get(from).add(flight);
        String fromTo=String.format("%s-%s",from,to);
        flightsFromTo.putIfAbsent(fromTo,new TreeSet<>(comparator));
        flightsFromTo.get(fromTo).add(flight);

        flightsTo.putIfAbsent(to,new TreeSet<>(comparator));
        flightsTo.get(to).add(flight);

    }
    public void showFlightsFromAirport(String code)
    {
        Airport airport=airports.get(code);
        System.out.println(String.format("%s (%s)\n%s\n%s",airport.name,airport.code,airport.country,airport.passenger));
        List<Flight> flightList=new ArrayList<>(flightsByA.get(code));
        IntStream.range(1,flightList.size()+1).forEach(i->
        {
            System.out.println(String.format("%d. %s",i,flightList.get(i-1)));
        });


    }
    public void showDirectFlightsFromTo(String from, String to)
    {
        String fromTo=String.format("%s-%s",from,to);
        if(!flightsFromTo.containsKey(fromTo))
        {
            System.out.println(String.format("No flights from %s to %s",from,to));
        }
        else
        {
            List<Flight> flightList=new ArrayList<>(flightsFromTo.get(fromTo));
            IntStream.range(1,flightList.size()+1).forEach(i->
            {
                System.out.println(String.format("%s",flightList.get(i-1)));
            });
        }
    }
    public void showDirectFlightsTo(String to)
    {
        List<Flight> flightList=new ArrayList<>(flightsTo.get(to));
        IntStream.range(1,flightList.size()+1).forEach(i->
        {
            System.out.println(String.format("%s",flightList.get(i-1)));
        });
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
       airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

