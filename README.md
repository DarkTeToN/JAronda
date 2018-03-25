# JAronda
A Java application to play the board game Aronda.

An external interface is now available to start JAronda as a game server 
(in order to be able to use it for training an AI for instance).
This is the user manual to do so:

1°/ Start JAronda with the following command

java -DajpMode=true -jar <pathToJArondaJarFile>

The following mesage should appear in your console:
Starting JAronda in Aronda JSON Protocol communication mode...
Listening on port: 11815

2°/ Available commands

All commands are HTTP requests. If the command was executed successfully, a HTTP
200 response is returned in the header, and a JSON object is present in the body.
Examples of the JSON to send and the received JSON are given in the example folder, 
at the root of this repository. The root context of the server is http://localhost:11815/jaronda/
The follwing contexts are to append to the root context.

    * startNewGame
        
        This command resets the game controller to a new game status. At the start
        of the server, there's no need to send this command for the server is already
        in an empty board status. This is a GET method context.

    * getBoard

        This command returns a JSON object containing all necessary information
        on the current board, and also extra information such as score, whose
        player turn it is, etc. This is a GET method context.

    * playMove

        This command gives the game controller which move to play. If the move is
        valid, this method returns a JSON object containing the game board status
        after the move is played (the result would be the same as sending a getBoard
        request right after the move). 
        If the played move is invalid, the method returns an HTTP 520 response.
        This is a POST method context where the example data to send can be found in
        the examples.