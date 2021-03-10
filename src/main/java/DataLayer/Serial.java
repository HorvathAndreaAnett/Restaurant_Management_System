package DataLayer;

import BusinessLayer.Restaurant;

import java.io.*;

public class Serial {

    private static String filename; //the name of the file for serialization .ser

    public Serial() {
    }

    public Serial(String filename) {
        this.filename = filename;
    }

    public void serialize(Restaurant r) {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            //writing the state of the restaurant to the file
            out.writeObject(r);
            out.flush();

            out.close();
            file.close();
        } catch (Exception e) {
        }
    }

    public Restaurant deserialize() {
        Restaurant r = new Restaurant();
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            //put in r the state of the restaurant in the .ser file
            r = (Restaurant)in.readObject();

            in.close();
            file.close();

        } catch (Exception e) {
        }
        return r;
    }

}
