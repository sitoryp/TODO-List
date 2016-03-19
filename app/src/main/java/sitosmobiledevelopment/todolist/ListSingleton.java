package sitosmobiledevelopment.todolist;

/**
 * Created by Darran on 3/19/2016.
 */
public class ListSingleton {

    private static ListSingleton mInstance;

    public ListSingleton () {}

    public static ListSingleton getInstance () {

        if (mInstance == null){
            mInstance = new ListSingleton();
        }

        return mInstance;
    }

}
