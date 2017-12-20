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

    //  '/': return shared file list
    private Response responseRootPage() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("<ol>");
        for(int i = 0 , len = fileList.size(); i < len ; i++) {
            File file = new File(fileList.get(i).getPath());
            if(file.exists()) {
                builder.append("<li> <a href=\"")
                    .append(file.getPath())
                    .append("\">")
                    .append(file.getName())
                    .append("</a></li>");
            }
        }
        builder.append("</ol>");
        builder.append("</body></html>\n");

        return Response.newFixedLengthResponse(String.valueOf(builder));
    }

    //  serve files
    private Response responseFile(IHTTPSession session){
        try {
            //  uri：用于标示文件资源的字符串，这里即是文件路径
            String uri = session.getUri();
            Log.d("MyServer", "Asking for" + uri);
            //  文件输入流
            FileInputStream fis = new FileInputStream(uri);
            //  application/octet-stream (old mime)
            return Response.newFixedLengthResponse(Status.OK, "text/plain", fis, fis.available());
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