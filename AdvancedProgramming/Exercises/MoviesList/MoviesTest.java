package AdvancedProgramming.Exercises.MoviesList;

import java.util.*;
import java.util.stream.Collectors;

class Movie
{
    String title;
    List<Integer> ratings;
    double ratingCoef;

    public Movie(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public double averageRating()
    {
        return ratings.stream().mapToInt(i->i).average().orElse(0.00);
    }

    public void setRatingCoef(double ratingCoef) {
        this.ratingCoef = ratingCoef;
    }

    public double getRatingCoef() {
        return ratingCoef;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,averageRating(),ratings.size());
    }
}
class MoviesList
{
    List<Movie> movies;

    public MoviesList() {
        movies=new ArrayList<>();
    }

    public int maxRatings()
    {
        return movies.stream().mapToInt(movie->movie.ratings.size()).sum();
    }


    public void addMovie(String title, int[] ratings)
    {
        List<Integer> ratingsList=new ArrayList<>();
        ratingsList=Arrays.stream(ratings).boxed().collect(Collectors.toList());
        Movie movie=new Movie(title,ratingsList);
        movies.add(movie);
    }
    public List<Movie> top10ByAvgRating()
    {
        Comparator<Movie> comparator=Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle);
        return movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef()
    {
        movies.forEach(movie -> movie.setRatingCoef((movie.averageRating()*movie.ratings.size())/maxRatings()));
        Comparator<Movie> comparator=Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getTitle);
        //просечен ретјтинг на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
        return movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());

    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde