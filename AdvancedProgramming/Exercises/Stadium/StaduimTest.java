package AdvancedProgramming.Exercises.Stadium;

import java.util.*;
import java.util.stream.IntStream;

class SeatNotAllowedException extends Exception
{
    public SeatNotAllowedException(String message) {
        super(message);
    }
}
class SeatTakenException extends Exception
{
    public SeatTakenException(String message) {
        super(message);
    }
}
class Sector
{
    String code;
    int numSeats;
    int takenSeats;

    List<Integer> typesTickets=new ArrayList<>();
    Map<Integer,Boolean> seatsTaken=new HashMap<>();

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;

        IntStream.range(1,numSeats+1).forEach(i->
        {
            seatsTaken.put(i,true);
        });
    }
    public double procent()
    {
       return  (numSeats - takenSeats) * 100 / (double)numSeats;
    }
    public int availableSeats()
    {
        return numSeats-takenSeats;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",code,numSeats-takenSeats,numSeats,100-procent());
    }
}
class Stadium
{

    String name;
    Map<String,Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors=new HashMap<>();
    }
    void createSectors(String[] sectorNames, int[] sizes)
    {
        for(int i=0;i<sectorNames.length;i++)
        {
            Sector sector=new Sector(sectorNames[i],sizes[i]);
            sectors.put(sector.code,sector);
        }

    }
    void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector sector=sectors.get(sectorName);

        if(!sector.seatsTaken.get(seat))
            throw new SeatTakenException("SeatTakenException");

        if(type==1) {
            if (sector.typesTickets.contains(2))
                throw new SeatNotAllowedException("SeatNotAllowedException");
        }
        if(type==2) {
            if (sector.typesTickets.contains(1))
                throw new SeatNotAllowedException("SeatNotAllowedException");
        }


        sector.seatsTaken.put(seat,false);
        sector.takenSeats++;
        sector.typesTickets.add(type);


    }
    void showSectors()
    {
       sectors.values().stream().sorted(Comparator.comparing(Sector::availableSeats).reversed()).forEach(System.out::println);
    }


}
public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
