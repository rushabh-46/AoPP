import java.util.Scanner;

public class Quiz1 {

  public static void main(String[] args) {
    int x = solve();
    System.out.print(x);
  }

  private static int solve() {
    // TODO Auto-generated method stub
    int x = 0;
    final Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    if (n > 10) {
      x = 2;
    }
    return x;
  }

}
