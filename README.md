# Physics-Engine-Library

I constructed this library in July 2013 as a personal project during summer vacation. I had taken a physics course in high school approximately as advanced as a first year physics course in college. I figured I could have some fun applying my knowledge as well as learning some new material about 3D rigidbodies.

I had guidance from a book, "Game Physics Engine Development" by Ian Millington. I understood most of the 2D physics and implemented them successfully. As the physics progressed into three dimensions, the mathematics became increasingly difficult and development slowed down. My timebox ended on August 1, 2013, and so I stopped programming then.

The library supports

* 2D Particles (sizeless objects)
  * Movement (position, velocity, acceleration)
  * Collisions
  * Forces
    * Drag
    * Gravity
    * Anchored Bungee
    * Unanchored Bungee
    * Buoyancy
    * Anchored Spring
    * Unanchored Spring
    * Stiff Spring (for faking ground/hard surfaces)
* 3D Rigidbodies (objects with size) 
  * Movement (position, velocity, acceleration)
  * Collisions (buggy and incomplete)
    * Bounding boxes
    * Bounding spheres
    * Bounding volume trees
  * Forces
    * Gravity
    * Torque
    * Anchored Spring
    * Unanchored Spring
* Standalone tests for particles and rigidbodies - just to make sure the library seems to be working
