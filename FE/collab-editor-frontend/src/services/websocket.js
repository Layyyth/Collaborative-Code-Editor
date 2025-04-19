import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const socketUrl = 'http://localhost:8080/ws-sock'; // Must match Spring Boot SockJS endpoint

const stompClient = new Client({
  webSocketFactory: () => new SockJS(socketUrl),
  reconnectDelay: 5000,
  debug: (str) => console.log('STOMP: ' + str)
});

export default stompClient;
