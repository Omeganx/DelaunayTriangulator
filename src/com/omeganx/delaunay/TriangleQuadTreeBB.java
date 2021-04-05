package com.omeganx.delaunay;

import java.util.ArrayList;

public class TriangleQuadTreeBB {
    boolean isLeaf;
    float w;
    float h;

    Point center;

    TriangleQuadTreeBB[] subTrees = new TriangleQuadTreeBB[4];

    ArrayList<Triangle> content = new ArrayList<>();

    //Used for spliting the quadtree. this gives the new coordinates of the 4 quadrants.
    float s[][] = new float[][]{{-0.5f, -0.5f}, {-0.5f, 0.5f}, {0.5f, -0.5f}, {0.5f, 0.5f}};

    public TriangleQuadTreeBB(int w, int h)
    {
        this.isLeaf = true;
        this.w = w;
        this.h = h;
        center = new Point(w/2f, h/2f);
    }

    public TriangleQuadTreeBB(float w, float h, Point center)
    {
        this.isLeaf = true;
        this.w = w;
        this.h = h;
        this.center = center;
    }

    public void add(Triangle t)
    {

        if(isCircumcircleInBoundingBox(t))return;

        if(this.isLeaf)
        {
            this.content.add(t);
            if(this.content.size()>20 && w>3 && h>3) this.split();
        }
        else for (TriangleQuadTreeBB tress : subTrees) tress.add(t);

    }

    public void remove(Triangle t)
    {
        if(isCircumcircleInBoundingBox(t)) return;
        if(this.isLeaf) this.content.remove(t);
        else for (TriangleQuadTreeBB tress : subTrees) tress.remove(t);

    }

    boolean isCircumcircleInBoundingBox(Triangle t)
    {
        return (t.circumcenter.x+t.radius<this.center.x-this.w/2f || t.circumcenter.x- t.radius>this.center.x+this.w/2f
                || t.circumcenter.y+t.radius<this.center.y-this.h/2f || t.circumcenter.y- t.radius>this.center.y+this.h/2f);
    }

    public ArrayList<Triangle> searchTrianglesWithGivenVertex(Point p)
    {
        ArrayList<Triangle> invalid = new ArrayList<>();

        if(this.isLeaf)
        {
            for(Triangle t : this.content) if(t.isPointVertexOfTriangle(p)) invalid.add(t);
            return invalid;
        }
        else
        {
            int index = ((this.center.x < p.x) ? 2 : 0) + ((this.center.y < p.y) ? 1 : 0);
            return this.subTrees[index].searchTrianglesWithGivenVertex(p);
        }
    }

    public ArrayList<Triangle> searchInvalidTriangles(Point p)
    {
        ArrayList<Triangle> invalid = new ArrayList<>();

        if(this.isLeaf)
        {
            for(Triangle t : this.content) if(t.isPointInCircumcircle(p)) invalid.add(t);
            return invalid;
        }
        else
        {
            int index = ((this.center.x < p.x) ? 2 : 0) + ((this.center.y < p.y) ? 1 : 0);
            return this.subTrees[index].searchInvalidTriangles(p);
        }
    }

    void split()
    {

        this.isLeaf = false;
        float w=this.w/2f, h=this.h/2f;

        for(int i = 0; i<4; i++) this.subTrees[i] = new TriangleQuadTreeBB(w, h, new Point(this.center.x + s[i][0]*w, this.center.y + s[i][1]*h));


        this.content.forEach(t -> this.add(t));
        this.content.clear();
    }

    public ArrayList<Triangle> toList()
    {
        ArrayList<Triangle> triangles = new ArrayList<>();
        if(this.isLeaf) {

            for (Triangle t : this.content) {
                if (isPointInBoundingBox(t.inscribed)) {
                    triangles.add(t);
                }
            }
        }
        else
            for (TriangleQuadTreeBB trees : this.subTrees) triangles.addAll(trees.toList());

        return triangles;
    }

    public boolean isPointInBoundingBox(Point p)
    {
        return this.center.x+this.w/2>p.x && this.center.x-this.w/2<p.x && this.center.y+this.h/2>p.y && this.center.y-this.h/2<p.y;
    }

    public float totalArea()
    {
        float area = 0;

        for(Triangle t : this.toList()) area += t.area();

        return area;
    }
}
