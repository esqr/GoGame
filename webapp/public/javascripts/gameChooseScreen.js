$(function()
{
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var socket = new WS("ws://localhost:9000/game");

    var sendMessage = function(message)
    {
        socket.send(JSON.stringify(
        {
            'message': message
        }))
    }

    var sayHello = function()
    {
        sendMessage('HELLO');
    }

    var receiveEvent = function(event)
    {
        var data = JSON.parse(event.data);
        console.log(data);

        if (data.error)
        {
            socket.close();
            $("#onError span").text(data.error);
            $("#onError").show();
            return;
        }
        else if (data.message == "HELLO")
        {
            sendMessage('GET_BOARDS ALL');
        }

//         $("#onChat").show()
//         var el = $('<div class="message"><p style="font-size:16px"></p></div>')
//         $("p", el).text(data.message)
//         $(el).addClass('me')
//         $('#messages').append(el)
    }

    socket.onmessage = receiveEvent;
    socket.onopen = sayHello;
})
