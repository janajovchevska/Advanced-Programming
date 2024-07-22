package AdvancedProgramming.Exercises.TaskManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


class DeadlineNotValidException extends Exception
{
    public DeadlineNotValidException(LocalDateTime time) {
        super(String.format("The deadline "+time+" has already passed"));
    }
}
class Task
{
    String name;
    String description;
    LocalDateTime deadline;
    int priority;
    String category;

    public Duration vreme(){
        return Duration.between(LocalDateTime.now(),deadline);
    }

    public int getPriority() {
        return priority;
    }

    public Task(String line) throws DeadlineNotValidException {
        //School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1
        String[] parts=line.split(",");
        category=parts[0];
        name=parts[1];
        description=parts[2];
        LocalDateTime time = LocalDateTime.parse("3111-06-23T23:59:59");
        priority = 100;
        if (parts.length == 4 && parts[3].length() > 7) {
            time = LocalDateTime.parse(parts[3]);

        }
        if (parts.length == 4 && parts[3].length() < 7) {

            priority = Integer.parseInt(parts[3]);
        }

        if (parts.length == 5) {
            time = LocalDateTime.parse(parts[3]);
            priority = Integer.parseInt(parts[4]);
        }
        LocalDateTime dateTime1 = LocalDateTime.of(2020, 6, 2, 0, 0);
        if (time.isBefore(dateTime1) ){
            throw new DeadlineNotValidException(time);
        }
        deadline=time;

    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }

    public String toStringPriority()
    {
        LocalDateTime time = LocalDateTime.parse("3111-06-23T23:59:59");
       if(priority==100 && deadline.equals(time))
       {
            return "Task{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
       }
       else if(priority==100)
       {
           return "Task{" +
                   "name='" + name + '\'' +
                   ", description='" + description + '\'' +
                   ", deadline=" + deadline +

                   '}';
       } else if (deadline.equals(time)) {

           return "Task{" +
                   "name='" + name + '\'' +
                   ", description='" + description + '\'' +
                   ", priority=" + priority +
                   '}';
       }
       else
       {
           return "Task{" +
                   "name='" + name + '\'' +
                   ", description='" + description + '\'' +
                   ", deadline=" + deadline +
                   ", priority=" + priority +
                   '}';
       }

    }
}
class TaskManager
{
    Map<String, List<Task>> tasksByCategory;
    List<Task> tasks;

    public TaskManager() {
        tasksByCategory=new HashMap<>();
        tasks=new ArrayList<>();
    }

    public void readTasks (InputStream inputStream)
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(line->
        {
            try {
                Task task=new Task(line);
                tasks.add(task);
                tasksByCategory.putIfAbsent(task.category, new ArrayList<>());
                tasksByCategory.get(task.category).add(task);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
            }


        });

    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw=new PrintWriter(os);
        if(includeCategory)
        {
           tasksByCategory.entrySet().forEach(entry->
           {
               String cat=entry.getKey();
               List<Task> taskList=entry.getValue();

               pw.println(cat.toUpperCase());
               if(includePriority)
               {
                   taskList.stream().sorted(Comparator.comparing(Task::getPriority).thenComparing(Task::vreme)).forEach(task ->
                   {
                       pw.println(task.toStringPriority());
                   });

               }else
               {
                   taskList.stream().sorted(Comparator.comparing(Task::vreme)).forEach(task ->
                   {
                       pw.println(task.toStringPriority());
                   });

               }
           });
        }
        else
        {
            if(includePriority)
            {
                tasks.stream().sorted(Comparator.comparing(Task::getPriority).thenComparing(Task::vreme)).forEach(task ->
                {
                    pw.println(task.toStringPriority());
                });

            }else
            {
                tasks.stream().sorted(Comparator.comparing(Task::vreme)).forEach(task ->
                {
                    pw.println(task.toStringPriority());
                });

            }
        }
        pw.flush();

    }
}
public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
       System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
