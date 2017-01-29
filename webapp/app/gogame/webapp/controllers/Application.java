package gogame.webapp.controllers;

import gogame.webapp.models.*;

import play.Logger;
import play.mvc.*;

import com.fasterxml.jackson.databind.JsonNode;

import gogame.webapp.views.html.*;
import models.msgs.Info;

import akka.actor.*;

import java.util.*;
import play.libs.*;

public class Application extends Controller {
    static List<ActorRef> actors = new ArrayList<ActorRef>();

    /**
     * Display the home page.
     */
    public static Result index() {
        return ok(index.render());
    }

    /**
     * Display the chat room.
     */
//     public static Result chatRoom(String username) {
//         if (username == null || username.trim().equals("")) {
//             flash("error", "Please choose a valid username.");
//             return redirect(routes.Application.index());
//         }
//         return ok(chatRoom.render(username));
//     }

    public static Result gameChooseScreen() {
        return ok(views.js.gameChooseScreen.render());
    }

    /**
     * Handle the chat websocket.
     */
    public static WebSocket<JsonNode> game() {
        return new WebSocket<JsonNode>() {
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
//                 Table.notifyAll(new Info("I've got a WebSocket", username));

                try {
                    actors.add(Akka.system().actorOf(Props.create(WebAppDoorkeeper.class, in, out)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
