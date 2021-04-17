package ie.tud.msc.disertation.domain;

import lombok.Data;

/**
 * Can represent a position as well as a velocity.
 */
@Data
public class Vector {

    private double x, y, z;
    private double limit = Double.MAX_VALUE;

    public Vector () {
        this(0, 0, 0);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set (double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    private void setX (double x) {
        this.x = x;
    }

    private void setY (double y) {
        this.y = y;
    }

    private void setZ (double z) {
        this.z = z;
    }

    public void add (Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    public void sub (Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    public void mul (double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    public void div (double s) {
        x /= s;
        y /= s;
        z /= s;
        limit();
    }

    public void normalize () {
        double m = mag();
        if (m > 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    private double mag () {
        return Math.sqrt(x*x + y*y);
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    private void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            x /= ratio;
            y /= ratio;
        }
    }

    public Vector clone () {
        return new Vector(x, y, z);
    }

    public String toString () {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
