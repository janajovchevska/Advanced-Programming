package AdvancedProgramming.Exercises.Names;

import java.util.*;
import java.util.stream.Collectors;

class Names
{
    Map<String,Integer> names;
    Set<String> unikatni;

    public Names() {
        names=new TreeMap<>();
        unikatni=new TreeSet<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name,0);
        int value=names.get(name);
        value++;
        names.put(name,value);
        unikatni.add(name);
    }

    public int uniqueChar(String name)
    {
        //int count=0;
        List<Character> chars=new ArrayList<>();
        for(char c: name.toCharArray())
        {
            if(!chars.contains(Character.toLowerCase(c)))
            {
                chars.add(Character.toLowerCase(c));
            }

        }
        return chars.size();
    }

    public void printN(int n) {
       names.entrySet().forEach(entry->
       {
           if(entry.getValue()>=n)
           System.out.println(String.format("%s (%d) %d",entry.getKey(),entry.getValue(),uniqueChar(entry.getKey())));
       });
    }
    public String findName(int len, int x)
    {
       List<String> deleteNames= unikatni.stream().filter(key->key.length()>=len).collect(Collectors.toList());
        //System.out.println(deleteNames);
        deleteNames.forEach(deleted->unikatni.remove(deleted));
        List<String> find=new ArrayList<>(unikatni);


        return find.get(x%unikatni.size());
    }
}
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
       System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde