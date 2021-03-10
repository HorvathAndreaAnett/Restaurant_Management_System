package BusinessLayer;

import java.io.Serializable;

public abstract class MenuItem implements Serializable {

    //common method to both base and composite product
    public abstract float computePrice();
}
