package BusinessLayer;

import java.io.Serializable;

public class Validator implements Serializable {

    //the name of  base product should contain only letters
    public boolean isBaseProductNameValid(String name) {
        return name.matches("^[ A-Za-z]+$");
    }

    //a composite product cam also contain besides letters "+" and ","
    public boolean isCompositeProductNameValid(String name) {
        return name.matches("^[ A-Za-z+,]+$");
    }

    //the price should contain only digits and "," in case of floating point numbers
    public boolean isPriceValid(String price) {
        return price.matches("^[0-9.]+$");
    }

    public String getSimpleString(String s) {
        s = s.replaceAll("\\s+", " "); //eliminates extra spaces
        if (s.startsWith(" ")) {
            s = s.substring(1); //eliminates the space in the begining
        }
        if (s.endsWith(" ")) {
            s = s.substring(0,s.length() - 1); //eliminates the space at the ending
        }
        return s;
    }
}
