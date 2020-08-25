package com.littlebear.learnhanzi;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LittleBearUtils {
    private static Context theAppContext;
    private static volatile LittleBearUtils INSTANCE;

    public final String booksFile = "books/books.json";
    public final String lessonsFile = "lessons.json";

    private LittleBearUtils(final Context context) {
        theAppContext = context.getApplicationContext();
    }

    public static LittleBearUtils geUtils(final Context context) {
        if (INSTANCE == null) {
            synchronized (LittleBearUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LittleBearUtils(context);
                }
            }
        }
        return INSTANCE;
    }

    //从Asset中打开文件创建String
    public String buildStringFromAssetFile (String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager assetManager = theAppContext.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName), "UTF-8"); //使用IO流读取文件内容
            BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
            inputStreamReader.close();

        } catch (
                IOException ex) {
            ex.printStackTrace();
        }

        return stringBuilder.toString();
    }



    public void SaveToAssetFile (String fileName, String text) {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            String content = "This is the content to write into file";

            File file = new File("fileName");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
