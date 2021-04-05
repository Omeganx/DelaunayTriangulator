package com.omeganx.delaunay;

import java.util.ArrayList;

public class PointQuadTree
{
    PointQuadTree[] subTress = new PointQuadTree[4];
    boolean isLeaf;

    ArrayList<Point> content = new ArrayList<Point>();

    Point center;

    float width;
    float height;

    //Sign table for the four corners parts of the QuadTree.
    float s[][] = new float[][]{{-0.5f, -0.5f}, {-0.5f, 0.5f}, {0.5f, -0.5f}, {0.5f,0.5f}};

    public PointQuadTree(int width, int height)
    {
        this.isLeaf = true;

        this.width = (float)width;
        this.height = (float)height;

        this.center = new Point(width/2f, height/2f);
    }

    public ArrayList<Point> toList()
    {
        ArrayList<Point> list = new ArrayList<>();
        if(this.isLeaf) list = this.content;
        else
        {
            for(PointQuadTree pqt : this.subTress) list.addAll(pqt.toList());
        }
        return list;
    }

    public PointQuadTree(float width, float height, Point center)
    {
        this.isLeaf = true;
        this.width = width;
        this.height = height;
        this.center = center;
    }

    public boolean contains(Point p)
    {
        if(this.isLeaf) return this.content.contains(p);
        else
        {
            int index = 0;
            if(this.center.x > p.x) index +=2;
            if(this.center.y > p.y) index +=1;
            return  this.subTress[index].contains(p);
        }
    }
    public void add(Point p)
    {
        if(this.isLeaf)
        {
            this.content.add(p);
            if(this.content.size()>10 && this.width<4 && this.height<4) this.split();
        }
        else
        {
            int index = 0;
            if(this.center.x > p.x) index +=2;
            if(this.center.y > p.y) index +=1;
            this.subTress[index].add(p);
        }
    }

    public void split()
    {
        int index;
        float w = this.width/2f, h = this.height/2f;
        for(int i = 0; i<4; i++) this.subTress[i] = new PointQuadTree(w, h, new Point(this.center.x + s[i][0]*w, this.center.y+ s[i][1]*h));

        for(Point p : this.content)
        {
            index = 0;
            if(this.center.x < p.x) index +=2;
            if(this.center.y < p.y) index +=1;
            this.subTress[index].add(p);
        }

        this.isLeaf = false;
        this.content.clear();
    }

    public void remove(Point p)
    {
        if(this.isLeaf) this.content.remove(p);
        else
        {
            int index = 0;
            if(this.center.x > p.x) index +=2;
            if(this.center.y > p.y) index +=1;
            this.subTress[index].remove(p);
        }
    }

    public ArrayList<Point> search(Triangle t)
    {
        ArrayList<Point> points = new ArrayList<>();
        if(t.radius+t.circumcenter.x<this.center.x-this.width/2 || t.circumcenter.x-t.radius>this.center.x+this.width/2
        || t.radius+t.circumcenter.y<this.center.y-this.height/2 || t.circumcenter.y-t.radius>this.center.y+this.height/2) return points;

        if(this.isLeaf) return this.content;

        for(PointQuadTree tree : this.subTress)
        {
            points.addAll(tree.search(t));
        }

        return points;
    }


}