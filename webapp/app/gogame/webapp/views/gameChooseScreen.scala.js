$(function()
{
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var socket = new WS("@routes.Application.game().webSocketURL(request)")

    var sendMessage = function(message)
    {
        socket.send(JSON.stringify(
        {
            'message': message
        }))
    }

    var receiveEvent = function(event)
    {
        var data = JSON.parse(event.data);

        if (data.error)
        {
            socket.close();
            $("#onError span").text(data.error);
            $("#onError").show();
            return;
        }

        console.log(data);
//         $("#onChat").show()
//         var el = $('<div class="message"><p style="font-size:16px"></p></div>')
//         $("p", el).text(data.message)
//         $(el).addClass('me')
//         $('#messages').append(el)
    }

    socket.onmessage = receiveEvent;
    socket.send(JSON.stringify( {
        message: $("#nr").val()
    }));
})
