package com.omeganx.delaunay;

public class Point {

    public float x;
    public float y;

    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float distSq(Point p)
    {
        float dx = this.x - p.x;
        float dy = this.y - p.y;
        return dx*dx+dy*dy;
    }

    private float sign(Point v1, Point v2, Point v3 )
    {
        return (v1.x - v3.x)* (v2.y - v3.y)- (v2.x - v3.x)*(v1.y-v3.y);
    }

    public boolean isInTriangle(Triangle t)
    {
        float d1 =sign(this, t.points[0], t.points[1]), d2 = sign(this, t.points[1], t.points[2]), d3 = sign(this, t.points[2], t.points[0]);
        return !(((d1 < 0) || (d2 < 0) || (d3 < 0))&& ((d1 > 0) || (d2 > 0) || (d3 > 0)));
    }

    @Override
    public boolean equals(Object o)
    {
        return(o instanceof Point && ((Point)o).x==this.x && ((Point)o).y==this.y);
    }

    @Override
    public int hashCode()
    {
        return ((int)this.x)+12521*((int)this.y);
    }

    @Override
    public String toString()
    {
        return "("+this.x+","+this.y+")";
    }

}
