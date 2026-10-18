public class Account {
  private final Object lock = new Object();
  private int balance;

  public void deposit(int amount) {
    synchronized (lock) {
      balance += amount;
      lock.notifyAll();
    }
  }

  public int withdraw(int amount) throws InterruptedException {
    synchronized (lock) {
      while (balance < amount) {
        lock.wait();
      }
      balance -= amount;
      return balance;
    }
  }

  public int balance() {
    return balance;
  }

  public static void main(String[] args) throws InterruptedException {
    var account = new Account();
    var t1 = Thread.ofPlatform().start(() -> {
      try {
        IO.println("t1 : " + account.withdraw(50));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    var t2 = Thread.ofPlatform().start(() -> {
      account.deposit(30);
      account.deposit(30);
    });
    t2.join();
    IO.println("main : " + account.balance());
    t1.join();
  }
}