# Pente
 Ultimate Pente
CS329: Software Development
Chase, Parker, Ross, Tyler, Christian


Game Design:
The Ultimate Pente Program is based on the traditional Pente Game. Additionally, it allows users to be able to play over the network and communicate. The program focuses on the implementation of client-server interaction, multi-threading, variations of pente, and artificial intelligence. Other goals include more practice with the software development process, UI design, and effective teamwork.


Understanding:
When we started working on the program, logic was our first priority. We decided on using Chase’s code, as we thought that it would be easiest to implement network connectivity using PenteUtil. Chase’s group also had a solid foundation for adding the new features to the game. (e.g. AI, multithreading…) Here is a basic outline of the Client/Server interaction: Player 1 will be Client A, Player 2 will be Client B. Players open Pente program, select to play over the network. Client A and Client B are in game, server waits for Client A’s SYN. Both Clients are sleeping, until Client A sends SYN to server. Client A makes first move, sends SYN to server indicating the move, Client A is awake, Client B is sleeping (to avoid players placing stones when it is not their turn). Server sends an acknowledgement back to Client A, indicating it received its SYN. Client A’s move is made and shown on the pente board. Server calls a process move method which places the stone onto the board and shows it on the GUI. The process move method contains calls to the game logic (to make sure that the move is legal), all the variables to update the backend, the GUI, actionListeners, and taking turns methods. Any error checking is done in this process. Server then sends a message to both Client A and Client B which updates the board. After this message has been delivered and the board has updated, Client A is now sleeping, and Client B is awake so it can place a stone onto the board. This process repeats until one of the players win. If a player’s connection has timed out or they haven’t made a move in a certain amount of time, the game will end. If Client B times out, after 30 seconds Client A automatically wins the game.


Game Rules:
Pente is a strategic, two-player puzzle game that evolved from “Go”. The board’s dimensions usually are 15*15 (15 lines by 15 columns), a total of 225 intersections. The board starts completely devoid of stones. The first player (chosen by chance) begins the game by playing one stone on the center point. After the first move, players take turns placing their stones one at a time on any empty intersection. When you place a stone, a move is completed. Whenever your opponent has two stones which are adjacent, those stones are vulnerable for the opposing player to capture. The pair can be captured by placing stones on either sides. Captures can be made along diagonal, horizontal, and vertical lines. All four stones involved must be consecutive and in a straight line. Whoever wins the first five will be the winner. Again, the winning line may run horizontally, vertically, or diagonally. 


User Goals:
The Ultimate Pente Program should support the standard 2-player version of Pente on a GUI over a network. User would be able to play the pente game with an competitor that server paired for this user. User is able to choose the game mode they want why they first enter the game and there are two game mode: local-play and play-online. The user should also be able to run multiple programs on one computer. Additionally, the user should be able to play against an artificial intelligence robot player.

Features:
Our pente game supports multiple game variations: a player should be able to select a whole set of rules by name (e.g. "Pente", "gomoku") or a combination of rules à la carte. The game supports both human and computer players. The game supports both networked play (client-server, with the server coordinating the game play and the clients handling only the user interface) and local play, which should not require the player to start up separate client and server program. This is selected when the user first enters the game. We have a server which supports simultaneous games, and does not crash or stop accepting connections if there's a problem with a game.

GUI Design:
	
Network Protocol:
Our network protocol is showing below, we have two protocols on both client and server. From serve to client we send number 0-5 to include all the information that we need to send, from client to server we have protocols number 0-2. And the list below explains what text message and value it contains. 
FROM SERVER TO CLIENT:
0-BoardUpdate(FROM SERVER TO CLIENT)
    int numCaptures
    ArrayList<points> captures
    turnstatus
    blackCaptures
    whiteCaptures
1-IllegalMove(FROM SERVER TO CLIENT)
2-PlayerOne wins(FROM SERVER TO CLIENT)
3-PlayerTwo wins(FROM SERVER TO CLIENT)
4-Lobby Update
5-challenged(auto accept)
    your turn
FROM CLIENT TO SERVER:
0-NewGame
1-BoardUpdate
    String "x,y"
2-ChooseOpponent
    String "Opponent Name"

Use Case Diagram:






Use Case Diagram:
We simulate two scenarios in two condition LOCAL and NETWORK, this will be the most common cases when users start playing our game, that they are enter enter in local or network version.
		   LOCAL:						  NETWORK:

















Sequence Diagram(s):


 

Team Assignments:
Chase: Networking, Client-Server, etc.
Ross: Network Protocol: Client-Server interaction
Parker: GUI design and write-up, AI, computer player
Tyler: Rules/Logic, AI integration, some networking





Timeline/Meeting Times:

11.16.18 | 7:00 PM-12AM - Ross, Chase, Tyler, Parker
Meeting Content: Chase’s Version, we decided which version of Pente you will work with (or what code you will keep/modify)


11.17.18 | 5:00-7:00PM - Ross, Chase, Tyler, Christian, Parker
Meeting Content: We completed the basic design phase for Chase’s version of Pente. We figured out the requirements specification,

11.20.18 | 8:00-9:00PM - Ross, Chase, Tyler, Parker
Meeting Content: We started the write-up and make it as a recording document through the entire project working time. 


11.26.18 | 4:30PM-5:30PM - Tyler, Ross, Chase
Goals of meeting:
Create a plan regarding communication between GUI, Clients/Servers, and Logic
Discuss parameters needed for local and network play
Improve UI design
Discuss game variants to begin logic implementation
What was accomplished:
New additions to the GUI
Gomoku, Keryo-Pente, Traditional, Connect6 variants
Partial understanding of client/server programming for initial C grade requirements 

11.26.18 | 6:30PM-11:30PM - Parker, Ross, Chase
Goals:
Implement game variants into logic
What was accomplished:
Gomoku and Keryo-Pente variants completed
Encountered some issues after pulling out a fresh copy regarding failure to switch players and place corresponding stones
Other variants will be implemented after fixing the issue

11.30.18 | 5:00PM-8:00PM - Parker, Ross, Chase
Goals: 
Start client/server interaction
What was accomplished: 
Outlined client/server messages, also outlined network protocols
BoardUpdate, newGame, connection open/close, lobby messsages

12.2.18 | 10:00PM-1:00AM - Parker, Ross, Chase
Goals:
Implement Server Client interaction
What was accomplished:
Able to send one piece, any others could not be sent 

12.6.18 | 5:00PM-8:00PM - Tyler, Ross, Chase, Parker
Goals:
Get to C Level
What was accomplished:
Network play was completed
Finish enter screen, choose game mode


12.7.18 | 6:00PM-12:00PM - Tyler, Ross, Chase, Parker
Goals:
Get to C Level
What was accomplished:
The first version of the Ultimate Pente is complete
It works at a very good C-level condition with a few bugs

12.8.18 - 2.9.18 | 10:00PM-10:00AM - Parker, Ross, Chase, Tyler
Goals:
Implement and pass necessary objects related to lobbyPanel into lobbyPanel and penteClient
Set up JRadioButtons for players and current games in the lobby
Ensure correct Panels are being replaced and updated when needed
Improve implementation between local and networked versions of Ultimate Pente
Plan out extensibility frameworks for rulesets
What was accomplished:
Improved the integration of local and networked versions of Ultimate Pente
Improvements to GUI including creating instances of the correct gamePente depending on the users desire to play local vs networked
Creation of ButtonGroup for rulesets
Components and information needed from lobbyPanel extended to the correct pente client

12.9.18 | 6:00PM-12:00AM - Parker, Ross, Chase, Tyler
Goals:
Finalize project
Get to B/A-Level
What was accomplished:
Finished class/user diagrams
Complete AI

12.9.18 - 2.10.18 | 12:00AM - 10:00AM - Tyler, Parker, Ross, Chase
Goals:
Begin incorporating B level requirements
What was accomplished:
Completed integration of AI
Can be accessed by choosing local play and AI opponent JRadioButtons combination
Pop up menus that prevent options from going unchosen
If user tries to select AI as opponent on the local network, they are notified and JRadioButtons are reset to pick new play combination
Issues regarding multiple instances of the same class in different packages prevented the ability to extend frameworks for instances of other rulesets



