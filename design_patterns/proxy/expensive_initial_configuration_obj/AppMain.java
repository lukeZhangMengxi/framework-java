package expensive_initial_configuration_obj;

public class AppMain {

    public static void main(String[] args) {
        ExpensiveObject object = new ExpensiveObjectProxy();
        object.process();
        object.process();
    }
}
