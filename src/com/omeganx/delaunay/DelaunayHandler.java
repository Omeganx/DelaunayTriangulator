package com.omeganx.delaunay;

import java.util.ArrayList;
import java.util.HashMap;


public class DelaunayHandler {

    public PointQuadTree points;
    public TriangleQuadTreeBB triangles;

    HashMap<TriangleEdge, Triangle> edges;
    ArrayList<Triangle> trianglesToAdd = new ArrayList<Triangle>();
    HashMap<TriangleEdge, Triangle> innerEdges = new HashMap<TriangleEdge, Triangle>();
    public DelaunayHandler(int width,int height)
    {

        this.points = new PointQuadTree(width, height);
        this.triangles = new TriangleQuadTreeBB(width, height);
        this.edges = new HashMap<TriangleEdge, Triangle>();

        Point p0 = new Point(0, 0);
        Point p1 = new Point(width, 0);
        Point p2 = new Point(0, height);
        Point p3 = new Point(width, height);

        this.points.add(p0);
        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);

        Triangle t1 = new Triangle(p0, p1, p2);
        Triangle t2 = new Triangle(p1, p2, p3);

        t1.addNeighbour(t2, p1, p2);
        t2.addNeighbour(t1, p1, p2);

        this.triangles.add(t1);
        this.triangles.add(t2);
    }

    public void add(Point p)
    {
        if(this.points.contains(p))
            return;

        ArrayList<Triangle> invalid= this.triangles.searchInvalidTriangles(p);
        this.getEdges(invalid);

        if(this.checkColinearity(p))
        {
            this.edges.clear();
            return;
        }
        else{
            invalid.forEach(t->this.triangles.remove(t));
            this.trianglesToAdd.forEach(t -> addAndUpdate(t));
            this.points.add(p);
        }
    }
    
    private boolean checkColinearity(Point p)
    {
        this.trianglesToAdd.clear();
        for(TriangleEdge edge : this.edges.keySet())
        {
            Triangle triangle = new Triangle(edge.p1, edge.p2, p);
            this.trianglesToAdd.add(triangle);

            if(triangle.isColinear())
            {
                this.trianglesToAdd.clear();
                return true;
            }
        }
        return false;
    }

    ArrayList<Point> edgesToPoints()
    {
        int s = this.edges.keySet().size();
        TriangleEdge[] triangleEdges = new TriangleEdge[s];

        this.edges.keySet().toArray(triangleEdges);
        ArrayList<Point> points = new ArrayList<Point>();

        points.add(triangleEdges[0].p1);
        points.add(triangleEdges[0].p2);

        Point point = triangleEdges[0].p2;
        TriangleEdge lastEdge = triangleEdges[0];

        for(int i = 2; i<s; i++) for(TriangleEdge edge : this.edges.keySet())
            if((edge.p1 == point || edge.p2 == point) && !edge.equals(lastEdge))
            {
                if(edge.p1 == point) point = edge.p2;
                else point = edge.p1;

                lastEdge = edge;
                points.add(point);
                break;
            }
        return points;
    }


    public void triangulate(ArrayList<Point> points, Point toRemove)
    {
        if(points.size()==3)
        {
            this.addAndUpdate(new Triangle(points.get(0), points.get(1), points.get(2)));
            return;
        }

        Point p1 = points.get(0);
        Point p2 = points.get(1);

        for(int i = 2; i< points.size(); i++)
        {
            Triangle triangleToTest = new Triangle(p1, p2, points.get(i));
            if(isTriangleValid(triangleToTest))
            {
                this.addAndUpdate(triangleToTest);

                ArrayList<Point> points1 = new ArrayList<>();
                ArrayList<Point> points2 = new ArrayList<>();

                for(int j=1; j<=i; j++) points1.add(points.get(j));
                for(int j=i; j<=points.size(); j++) points2.add(points.get(j%points.size()));

                if(i!=2)triangulate(points1, toRemove);
                if(i!=points.size()-1) triangulate(points2,toRemove);
                return;
            }
        }
    }

    void addAndUpdate(Triangle t)
    {
        TriangleEdge[] triangleEdge = {new TriangleEdge(t.points[0], t.points[1]), new TriangleEdge(t.points[1], t.points[2]),new TriangleEdge(t.points[2], t.points[0])};
        for (TriangleEdge edg : triangleEdge) {
            if (this.edges.containsKey(edg)) {
                Triangle neighbour = this.edges.get(edg);
                if(neighbour!=null) neighbour.addNeighbour(t, edg.p1, edg.p2);
                t.addNeighbour(neighbour, edg.p1, edg.p2);
                this.edges.remove(edg);
            } else this.edges.put(edg, t);
        }
        this.triangles.add(t);
    }

    public void remove(Point p)
    {
        ArrayList<Triangle> toRemove = this.triangles.searchTrianglesWithGivenVertex(p);

        this.getEdges(toRemove);
        this.points.remove(p);
        ArrayList<Point> pointList = this.edgesToPoints();
        toRemove.forEach(t -> this.triangles.remove(t));
        triangulate(pointList, p);
    }

    private void getEdges(ArrayList<Triangle> triangleList)
    {
        this.edges.clear();
        for(Triangle triangle : triangleList) {
            for (int i = 0; i < 3; i++) {
                TriangleEdge triangleEdge = new TriangleEdge(triangle.points[i], triangle.points[(i + 1) % 3]);
                if (this.edges.containsKey(triangleEdge))  this.edges.remove(triangleEdge);
                else this.edges.put(triangleEdge, triangle.neighbours[i]);
            }
        }
    }

    public ArrayList<Triangle> getTriangles()
    {
        return this.triangles.toList();
    }

    boolean isTriangleValid(Triangle triangleToTest)
    {
        if(triangleToTest.isColinear()) return false;

        ArrayList<Point> pointCandidates = this.points.search(triangleToTest);
        for (Point p : pointCandidates) if (!p.equals(triangleToTest.points[0]) && !p.equals(triangleToTest.points[1]) && !p.equals(triangleToTest.points[2]) && triangleToTest.isPointInCircumcircle(p)) return false;

        ArrayList<Triangle> invalidTriangle = this.triangles.searchInvalidTriangles(triangleToTest.inscribed); //If inscribed point in existing triangle then the triangles overlap.
        for (Triangle t : invalidTriangle) if (t.isPointInside(triangleToTest.inscribed)) return false;

        return true;
    }
}
