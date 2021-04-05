package com.omeganx.delaunay;

public class TriangleEdge {

    Point p1;
    Point p2;

    Point center;

    public TriangleEdge(Point p1, Point p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.center = new Point((p1.x+p2.x), (p1.y+p2.y));
    }

    @Override
    public boolean equals(Object edge)
    {
        return (edge instanceof TriangleEdge) && ((TriangleEdge)edge).center.x==this.center.x && ((TriangleEdge)edge).center.y==this.center.y;
    }

    @Override
    public int hashCode()
    {
        return (int)(this.center.x)*11253+(int)(this.center.y);
    }

    public String toString()
    {
        return "TriangleEdge{"+this.p1+";"+this.p2+"}";
    }
}

