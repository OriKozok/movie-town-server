# movie-town-server-wip
Server side of my system simulating a cinema website


##Getting started
In order to run the project, please clone the repository:

$ git clone https://github.com/OriKozok/movie-town-server-wip

After downloading the project, please check the application's properties.
The project is using MySQL driver with the username "root" and password "12345678".
Make sure you are using the same driver and credentials, and if not, change them.
Check Maven's settings and change Maven home path to Bundled if it's not already using it.
If you changed the home path, please right click on the project, choose the Maven tab and and click on reload project.
After that, you can run the project.

##Technologies
-Spring: used for defining beans, services and controllers
-Hibernate: used for creating the beans' tables and communicating with the DB
-REST: used for defining the methods and paths of the controllers
-JWT: used for filtering requests and generating a unique token for each client
