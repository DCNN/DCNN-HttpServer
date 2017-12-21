package cn.alexchao.dcnn_httpserver;

import android.util.Log;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;
    private static  final String REQUEST_ROOT = "/";

    private LinkedList<File> fileList;  // shared file list

    public MyServer(LinkedList<File> fileList) throws IOException {
        super(PORT);
        this.fileList = fileList;
        start();
        Log.d("MyServer", "My Server is running, sharing " + this.fileList.size() + " files");
    }

    public MyServer() throws IOException {
        super(PORT);
        start();
        Log.d("MyServer", "My Server is running");
    }

    @Override
    public Response handle(IHTTPSession session) {
        if(REQUEST_ROOT.equals(session.getUri())||session.getUri().equals("")) {
            return responseRootPage();
        }
        return responseFile(session);
    }

    //  '/': return the home page
    private Response responseRootPage() {
        // Provide Vue SPA home page
        try {
            FileInputStream fis = new FileInputStream(Util.ROOT_PATH + "/dcnn-data/dist/index.html");
            return Response.newFixedLengthResponse(Status.OK, "text/html", fis, fis.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response404();
    }

    //  serve files
    private Response responseFile(IHTTPSession session) {
        try {
            String uri = session.getUri();

            // Vue SPA requests for static res
            if (uri.startsWith("/static/")) {
                uri = Util.ROOT_PATH + uri.replaceFirst("/static/", "/dcnn-data/dist/static/");
            }

            Log.d("MyServer", "Asking for" + uri);

            //  load file from sdCard
            FileInputStream fis = new FileInputStream(uri);

            // find the correct MIME type
            String mimeType = "text/plain";
            if (uri.endsWith(".css")) {
                mimeType = "text/css";
            } else if (uri.endsWith(".js")) {
                mimeType = "application/x-javascript";
            }

            return Response.newFixedLengthResponse(Status.OK, mimeType, fis, fis.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response404();
    }

    //  404 page
    private Response response404() {
        String builder = "<!DOCTYPE html><html><body>404 Not Found</body></html>\n";
        return Response.newFixedLengthResponse(builder);
    }
}