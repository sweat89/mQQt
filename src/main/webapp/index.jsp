<html>
<body>
<h2>Hello World!</h2>

	<div id="console-container">
		<div id="console" />
	</div>
	<input id="msg" type="text" />
	<button type="submit" id="sendButton" onClick="postToServer()">Send!</button>
	<button type="submit" id="sendButton" onClick="closeConnect()">End</button>


<script type="text/javascript">
 	var ws = new WebSocket("ws://localhost:8081/mQQt/websocket/chat");
	ws.onopen = function() {
	};
	ws.onmessage = function(message) {
		document.getElementById("console").textContent += message.data ;
	} 
	function postToServer() {
		alert('a');
		var vv= document.getElementById("msg").value;
		ws.send(vv);
		document.getElementById("msg").value = "";
	}
	function closeConnect() {
		ws.close();
	}
</script>
    <style type="text/css"><![CDATA[
        input#chat {
            width: 410px
        }

        #console-container {
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
    ]]></style>
</body>
</html>
