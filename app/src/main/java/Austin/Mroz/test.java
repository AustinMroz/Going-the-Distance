import java.util.Scanner;
import java.lang.Math;
public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		do {
			double gen = Math.random();
			if(gen<.25)
				gen = -8*(gen-.25)*(gen-.25)+.5;
			else
				gen = 8.0/9*(gen-.25)*(gen-.25)+.5;
			System.out.println(String.format("%f\t%f",(1-gen)*12450,gen));
		} while(!s.nextLine().equals("q"));
	}
}
