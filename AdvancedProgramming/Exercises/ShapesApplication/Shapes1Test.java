package AdvancedProgramming.Exercises.ShapesApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

class Canvas
{
    String id;
    List<Integer> sides;

    public Canvas(String id, List<Integer> sides) {
        this.id = id;
        this.sides = sides;
    }
    public int totalPerimeter()
    {
      return   sides.stream().mapToInt(side->side*4).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,sides.size(),totalPerimeter());
    }
}
class ShapesApplication{


    List<Canvas> canvases;
    public ShapesApplication()
    {
      canvases=new ArrayList<>();
    }
    int readCanvases (InputStream inputStream)
    {
       //canvas_id size_1 size_2 size_3 …. size_n
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
       // int count=0;
        br.lines().forEach(line->
        {
            String[] parts=line.split("\\s+");
            String id=parts[0];
            List<Integer> sides=new ArrayList<>();
            IntStream.range(1, parts.length).forEach(i->
            {
                sides.add(Integer.parseInt(parts[i]));

            });
           // count+=sides.size();
            Canvas canvas=new Canvas(id,sides);
            canvases.add(canvas);

        });

        return canvases.stream().mapToInt(canvas->canvas.sides.size()).sum();


    }
    void printLargestCanvasTo (OutputStream outputStream)
    {
        System.out.println(canvases.stream().max(Comparator.comparing(Canvas::totalPerimeter)).get());
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}