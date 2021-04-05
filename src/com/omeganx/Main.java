package com.omeganx;

import com.omeganx.delaunay.DelaunayHandler;
import com.omeganx.delaunay.Point;
import com.omeganx.delaunay.Triangle;
import com.omeganx.delaunay.TriangleEdge;
import processing.core.PApplet;
import com.omeganx.image.*;
import java.util.ArrayList;

public class Main extends PApplet{

    int n = 500;
    DelaunayHandler del;
    ArrayList<Point> points;
    public void settings(){
        size(500, 500);
        Image res = new Image(500, 500);
        points = res.genPoints(n, 7);

        del = new DelaunayHandler(500, 500);

    }

    public void draw(){
        background(200);
        ArrayList<Triangle> triangles = del.getTriangles();

        for(Triangle t : triangles) for(int i = 0 ; i<3; i++)
                line(t.points[i].x, t.points[i].y, t.points[(i+1)%3].x, t.points[(i+1)%3].y);
            
    }

    public void mousePressed(){
        if(mouseButton == LEFT)
        {
            Point p = new Point(mouseX, mouseY);
            del.add(p);
        }
        if(mouseButton == RIGHT)
        {
            if(del.points.toList().size()>5)
            {
                del.remove(del.points.toList().get(del.points.toList().size()-1));
            }
        }
        if(mouseButton == CENTER)
        {
            del = new DelaunayHandler(width, height);
        }
    }

    public static void main(String[] args){
        String[] processingArgs = {"MySketch"};
        Main mySketch = new Main();
        PApplet.runSketch(processingArgs, mySketch);
    }
}
