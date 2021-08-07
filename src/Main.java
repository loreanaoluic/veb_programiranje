import com.google.gson.Gson;

import java.io.File;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws Exception {
        port(9090);
        Gson g = new Gson();

        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        after((req, res) -> res.type("application/json"));
        get("/test", (req, res) -> {
            return "Works";
        });
    }
}
