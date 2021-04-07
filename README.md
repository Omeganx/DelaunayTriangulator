# DelaunayTriangulator
Java implementation of a dynamic Delaunay triangulation. Provides add and remove point functions.

---
## Usage

First an instance of a delaunay handler need to be created with the width and height desired.
```java
DelaunayHandler delaunayhandler = new DelaunayHandler(width, height);
```
Then we can either add or remove a point
```
Point point = new Point(x, y);

delaunayhandler.add(point);
delaunayhandler.remove(point);
```

The code in ```Main.java``` shows an example using the processing core library, left click add a point and right click remove the last added point.

<p align="center">
<img src="https://github.com/Omeganx/DelaunayTriangulator/blob/master/ressources/example.png?raw=true" width="350" height="350">
</p>


This code was made as a rework of an old code for a future project.


---

## Delaunay Triangulation
A delaunay triangulation is a non-overlapping tilling such that the maximun angle of each triangle is minimsed.
In other words, a delaunay triangulation is a triangulation that favors likewise angles in a triangle, that is to say pretty triangle over long and flat ones.
Equivalently, a triangulation has the Dellaunay property if and only if no point belong to the circumcircle of any triangle.

<p align="center">
<img src="https://github.com/Omeganx/DelaunayTriangulator/blob/master/ressources/delaunay_triangulation.png?raw=true" width="350" height="350" title="Example of a delaunay triangulation">
</p>

### Point Insertion
The method used to add a point to an existing triangulation is the Bowyer–Watson algorithm
This consists of removing all the triangles that are no longer valid due to insertion of the new point. Then, triangles are added between the hull created by
the no longer existing triangles and the new added point.

<p align="center">
<img src="https://github.com/Omeganx/DelaunayTriangulator/blob/master/ressources/add.gif?raw=true" width="350" height="350" title="Example of a delaunay triangulation">
</p>
A property of the Delaunay Triangulation is the locality of the pertubation, are incorrect triangles are adjacent to each other.

### Point Removal

This is a little trickier, because points cannot be just linked together in order to complete the hole caused by the removal of a point. The property has to be conversed.
As a matter of fact, for a given set of points, its Delaunay Triangulation is unique.

For the removal, triangles adjacent to the point are removed. Then an edge is chose and points are browsed one by one until the triangle formed by the edge and the point is valid 
in the triangulation. This is added to the triangulation but also divides the hull in two different hull. In each hull the same process is applied recursively until the hull only contains three points, those latter are creating triangle which is also added to the triangulation.
<p align="center">
<img src="https://github.com/Omeganx/DelaunayTriangulator/blob/master/ressources/remove.gif?raw=true" width="350" height="350" title="Example of a delaunay triangulation">
</p>

