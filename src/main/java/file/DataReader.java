package file;

import data.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataReader implements FileReaderInterface<Object> {

    @Override
    public Object[] readAll(String file) {
        List<String> fileLines = null;
        List<Data> dataList = new ArrayList<Data>();

        try {
            fileLines = Files.readAllLines(Paths.get(file));
        } catch (IOException ex) {
            ex.printStackTrace();
            return new Data[0];
        }

        JSONArray array = new JSONArray(String.join("", fileLines));

        for (Object object: array) {
            JSONObject jsonObject = (JSONObject) object;

            dataList.add(new Data(
                    jsonObject.getString("title"),
                    jsonObject.getDouble("price"),
                    jsonObject.getInt("quantity")
            ));
        }

        return dataList.toArray();
    }
}
