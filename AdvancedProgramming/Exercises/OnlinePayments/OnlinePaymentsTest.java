package AdvancedProgramming.Exercises.OnlinePayments;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Item
{
    String name;
    int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s %d",name,price);
    }
}

class OnlinePayments
{
    Map<String,List<Item>> items;

    public OnlinePayments() {
        items=new HashMap<>();
    }

    public void readItems(InputStream in) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        br.lines().forEach(line->
        {
            //151020;Административно-материјални трошоци и осигурување;750
            String[] parts=line.split(";");

            items.putIfAbsent(parts[0],new ArrayList<>());
            items.get(parts[0]).add(new Item(parts[1],Integer.parseInt(parts[2])));
        });
        //System.out.println(items);

    }

    public void printStudentReport(String id, PrintStream out) {
        if(!items.containsKey(id))
        {
            System.out.println(String.format("Student %s not found!",id));
        }else {
            //Student: 151020 Net: 13050 Fee: 149 Total: 13199
            PrintWriter pw = new PrintWriter(out);
            int net = items.get(id).stream().mapToInt(item -> item.price).sum();
            int fee = (int) Math.round(net * (1.14 / 100));
            if(fee<3)
                fee=3;
            if(fee>300)
                fee=300;
            int total = net + fee;

            Comparator<Item> comparator=Comparator.comparing(Item::getPrice).reversed();
            pw.print(String.format("Student: %s Net: %d Fee: %d Total: %d\nItems:\n", id, net, fee, total));

            List<Item> listItems=items.get(id).stream().sorted(comparator).collect(Collectors.toList());
            IntStream.range(1,listItems.size()+1).forEach(i->
                    {
                        pw.println(String.format("%d. %s",i,listItems.get(i-1)));
                    }
                    );

            pw.flush();
        }

    }
}
public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}