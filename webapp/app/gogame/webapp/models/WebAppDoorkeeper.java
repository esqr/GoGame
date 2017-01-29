package gogame.webapp.models;

import gogame.common.*;
import gogame.common.bot.BotMoveGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import play.Logger;
import play.mvc.*;

import com.fasterxml.jackson.databind.JsonNode;

import views.html.*;
import models.*;
import models.msgs.Info;

import akka.actor.*;

import models.msgs.*;
import play.libs.*;
import play.libs.F.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WebAppDoorkeeper extends UntypedActor {
    private WebSocket.Out<JsonNode> out;

    public void WebAppDoorkeeper(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
        this.out = out;

        in.onMessage(new Callback<JsonNode>() {
            @Override
            public void invoke(JsonNode event) {
                String inputLine = event.get("message").asText();

                SimpleLogger.log("recv command: ", inputLine);

                Scanner scanner = new Scanner(inputLine);
                String command = scanner.next();
                String answer = "";

                if (command.equals(CommunicationConstants.HELLO)) {
                    answer = (CommunicationConstants.HELLO);
                } else if (command.equals(CommunicationConstants.GET_BOARDS)) {
                    StringBuilder sb = new StringBuilder();
                    String filterString = scanner.next();
                    GameServer.Filter filter = GameServer.Filter.valueOf(filterString);

                    sb.append(CommunicationConstants.BOARD_LIST);
                    List<Room> rooms = GameServer.getInstance().getRoomList(filter);

                    for (Room room : rooms) {
                        sb.append(" ").append(room.getName())
                                .append(" ").append(room.getBoard().getSize())
                                .append(" ").append(room.getBoard().getPlayerNumber());
                    }

                    answer = (sb.toString());
                } else if (command.equals(CommunicationConstants.JOIN_BOARD)) {
//                     String roomName = scanner.next();
//                     NetMoveGenerator generator = new NetMoveGenerator(writer, reader);
//
//                     try {
//                         GameServer.getInstance().addPlayerToBoard(generator, roomName);
//                         answer = (CommunicationConstants.JOINED + " " + generator.getBoard().getSize());
//                         generator.start();
//                     } catch (BoardFullException e) {
//                         answer = (CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_FULL);
//                     }

                } else if (command.equals(CommunicationConstants.NEW_BOARD)) {
                    String roomName = scanner.next();
                    int boardSize = scanner.nextInt();

                    if (boardSize < 3) {
                        answer = (CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_SIZE);
//                         continue;
                    } else {
//                         try {
// //                             GameServer.getInstance().newBoard(roomName, boardSize);
// //                             NetMoveGenerator generator = new NetMoveGenerator(writer, reader);
// //
// //                             GameServer.getInstance().addPlayerToBoard(generator, roomName);
// //                             answer = (CommunicationConstants.JOINED + " " + generator.getBoard().getSize());
// //                             generator.start();
//                         } catch (BoardExistsException e) {
//                             answer = (CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_EXISTS);
//                         } catch (BoardFullException shouldNeverHappen) {
//                             shouldNeverHappen.printStackTrace();
//                         }
                    }
                } else if (command.equals(CommunicationConstants.PLAY_WITH_BOT)) {
                    int boardSize = scanner.nextInt();

                    if (boardSize < 3) {
                        answer = (CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_SIZE);
                    } else {
                        String roomName = "bot-" ;//+ getId();

//                         try {
    //                         GameServer.getInstance().newBoard("bot-" + getId(), boardSize);
    //                         NetMoveGenerator generator = new NetMoveGenerator(writer, reader);
    //                         BotMoveGenerator bot = new BotMoveGenerator();
    //                         GameServer.getInstance().addPlayerToBoard(generator, roomName);
    //                         GameServer.getInstance().addPlayerToBoard(bot, roomName);
    //                         answer = (CommunicationConstants.JOINED + " " + generator.getBoard().getSize());
    //                         generator.start();
//                         } catch (BoardExistsException | BoardFullException shouldNeverHappen) {
//                             shouldNeverHappen.printStackTrace();
//                         }
                    }
                } else if (inputLine.equals(CommunicationConstants.BYE)) {
//                     break;
                }

                ObjectNode node = Json.newObject();
                node.put("message", answer);

                out.write(node);
            }
        });
    }
    public void onReceive(Object message) throws Exception {
    }
}
