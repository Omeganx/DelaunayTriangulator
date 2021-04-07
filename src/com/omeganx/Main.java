package com.omeganx;

import com.omeganx.delaunay.DelaunayHandler;
import com.omeganx.delaunay.Point;
import com.omeganx.delaunay.Triangle;
import processing.core.PApplet;
import java.util.ArrayList;

public class Main extends PApplet{

    int width = 500;
    int height = 500;
    DelaunayHandler del;

    public void settings(){

        size(width, height);
        smooth(8);

        del = new DelaunayHandler(width, height);
    }

    public void draw(){

        background(255);
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
