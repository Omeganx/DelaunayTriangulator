package com.omeganx.delaunay;



public class Triangle {
    
    public Point[] points = new Point[3];
    Triangle[] neighbours = new Triangle[3];
    
    public Point inscribed;
    public Point circumcenter;

    double radiusSq;
    double radius;
    public boolean colinear;

    final static float GAP = 1.0f;

    public Triangle(Point p1, Point p2, Point p3)
    {
        this.points[0] = p1; this.points[1] = p2; this.points[2] = p3;
        this.computeCircumscribedPoint(p1, p2, p3);
        this.computeInscribedPoint(p1, p2, p3);
    }

    public boolean isColinear()
    {
        return this.colinear;
    }
    public void addNeighbour(Triangle neighbour, Point p1, Point p2)
    {
        float x = p1.x+p2.x,y = p1.y+p2.y;
        if(x == points[0].x+points[1].x && y == points[0].y+points[1].y) this.neighbours[0] = neighbour;
        if(x == points[1].x+points[2].x && y == points[1].y+points[2].y) this.neighbours[1] = neighbour;
        if(x == points[2].x+points[0].x && y == points[2].y+points[0].y) this.neighbours[2] = neighbour;
    }

    public float area()
    {
        Point p1 = this.points[0], p2 = this.points[1], p3 = this.points[2];
        float s = p1.x*(p2.y-p3.y)+p2.x*(p3.y-p1.y)+p3.x*(p1.y-p2.y);
        return (float)Math.abs(s)*0.5f;
    }

    void computeInscribedPoint(Point p1, Point p2, Point p3)
    {
        double a = Math.sqrt(p1.distSq(p2));
        double b = Math.sqrt(p2.distSq(p3));
        double c = Math.sqrt(p3.distSq(p1));
        double s = (a+b+c);
        this.inscribed = new Point((float)((a*p3.x+b*p1.x+c*p2.x)/s), (float)((a*p3.y+b*p1.y+c*p2.y)/s));
    }

    void computeCircumscribedPoint(Point p1, Point p2, Point p3)
    {
        double d = 2*(p1.x*(p2.y - p3.y)+p2.x*(p3.y-p1.y)+p3.x*(p1.y-p2.y));
        this.colinear = d==0;

        double ux = ((p1.x*p1.x+p1.y*p1.y)*(p2.y-p3.y)+(p2.x*p2.x+p2.y*p2.y)*(p3.y-p1.y)+(p3.x*p3.x+p3.y*p3.y)*(p1.y-p2.y))/d ;
        double uy = -((p1.x*p1.x+p1.y*p1.y)*(p2.x-p3.x)+(p2.x*p2.x+p2.y*p2.y)*(p3.x-p1.x)+(p3.x*p3.x+p3.y*p3.y)*(p1.x-p2.x))/d ;

        this.radiusSq = (ux-p1.x)*(ux-p1.x)+(uy-p1.y)*(uy-p1.y);
        this.radius = Math.sqrt(this.radiusSq)+GAP;
        this.circumcenter = new Point((float)ux, (float)uy);
    }

    @Override
    public boolean equals(Object o)
    {
        return( (o instanceof  Triangle) && ((Triangle) o).circumcenter.equals(this.circumcenter));
    }

    @Override
    public String toString()
    {
        return "Triangle{"+this.points[0].toString()+","+this.points[1].toString()+","+this.points[2]+"}";
    }

    public boolean isPointVertexOfTriangle(Point p )
    {
        return (this.points[0].equals(p) || this.points[1].equals(p) || this.points[2].equals(p));
    }

    public boolean isPointInCircumcircle(Point point)
    {
        return this.circumcenter.distSq(point) <= this.radiusSq;
    }

    float sign(Point p1, Point p2, Point p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public boolean isPointInside(Point pt)
    {
        Point v1 = this.points[0], v2 = this.points[1], v3 = this.points[2];
        float d1 = sign(pt, v1, v2), d2 = sign(pt, v2, v3), d3 = sign(pt, v3, v1);
        return !(((d1 > 0) || (d2 > 0) || (d3 > 0)) && ((d1 < 0) || (d2 < 0) || (d3 < 0)));
    }
}
