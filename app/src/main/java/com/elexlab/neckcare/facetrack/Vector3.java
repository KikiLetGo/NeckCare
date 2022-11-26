package com.elexlab.neckcare.facetrack;

public class Vector3 {
    private float x;
    private float y;
    private float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vector3 crossProduct(Vector3 vector3){
        float ax = getX();
        float ay = getY();
        float az = getZ();

        float bx = vector3.getX();
        float by = vector3.getY();
        float bz = vector3.getZ();

        float x = ay*bz - az*by;
        float y = az*bx - ax*bz;
        float z = ax*by - ay*bx;
        return new Vector3(x,y,z);
    }

    public Vector3 add(Vector3 vector3){
        return new Vector3(x+vector3.getX(), y+vector3.getY(), z+vector3.getZ());
    }
    public Vector3 sub(Vector3 vector3){
        return new Vector3(x-vector3.getX(), y-vector3.getY(), z-vector3.getZ());
    }
    public float length() {
        return (float) Math.sqrt( this.x * this.x + this.y * this.y + this.z * this.z );
    }
    public Vector3 normalize(){
        float len = length();
        return  new Vector3(x/len, y/len, z/len);
    }
    public Vector3 multiplyScalar(float scalar ) {
        return new Vector3(x*scalar, y*scalar, z*scalar);
    }

    public float lengthSq() {

        return this.x * this.x + this.y * this.y + this.z * this.z;

    }

    public float dot( Vector3 v ) {

        return this.x * v.x + this.y * v.y + this.z * v.z;

    }

    public double angleTo(Vector3 v){
        double denominator = Math.sqrt( this.lengthSq() * v.lengthSq() );

        if ( denominator == 0 ) return Math.PI / 2;

		double theta = this.dot( v ) / denominator;

        // clamp, to handle numerical problems
        return Math.acos( clamp( theta, - 1, 1 ) );
    }

    public double clamp( double value, double min, double max ) {

        return Math.max( min, Math.min( max, value ) );

    }
}
