# movie-town-server-wip
Server side of my system simulating a cinema website


## Getting started
In order to run the project, please clone the repository:

$ git clone https://github.com/OriKozok/movie-town-server-wip

After downloading the project, please check the application's properties.<br/>
The project is using MySQL driver with the username "root" and password "12345678".<br/>
Make sure you are using the same driver and credentials, and if not, change them.<br/>
Check Maven's settings and change Maven home path to Bundled if it's not already using it.<br/>
If you changed the home path, please right click on the project, choose the Maven tab and and click on reload project.<br/>
After that, you can run the project.

## Technologies
-Spring: Used for defining beans, services and controllers<br/>
-Hibernate: Used for creating the beans' tables and communicating with the DB<br/>
-REST: Used for defining the methods and paths of the controllers<br/>
-JWT: Used for filtering requests and generating a unique token for each client<br/>
