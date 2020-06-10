package com.io.day01.test02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHnadler implements Runnable{

    private Socket socket;

    TimeServerHnadler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if(body==null){
                    break;
                }
                System.out.println("The time server receive order:"+body);
                currentTime = "query time order".equals(body)?new Date(System.currentTimeMillis()).toString() :"BAD ORDER";
                out.println(currentTime);
            }
        } catch (IOException e) {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
                out=null;
            }
            if(this.socket!=null){
                try {
                    this.socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}
