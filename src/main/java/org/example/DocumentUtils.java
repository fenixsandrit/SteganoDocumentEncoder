package org.example;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocumentUtils
{
    public static final String SOURCE_FILE = "test.docx";
    public static final String OUTPUT_FILE = "copy.docx";

    public static final String COLOR = "F10D0C"; //Красный

    public static XWPFDocument getDocumentFromFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            FileInputStream fis =  new FileInputStream(file.getAbsolutePath());
            System.out.println("Документ успешно получен из файла = " + fileName);

            return new XWPFDocument(fis);
        }
        catch (IOException e)
        {
            System.out.println("Ошибка при получение документа из файла = " + fileName);
            throw new RuntimeException(e);
        }
    }

    public static void saveDocumentToFile(String fileName, XWPFDocument document)
    {
        try (FileOutputStream out = new FileOutputStream(fileName))
        {
            document.write(out);
            document.close();
            System.out.println("Документ успешно сохранен в файл = " + fileName);
        }
        catch (IOException e)
        {
            System.out.println("Ошибка при сохранение документа в файл = " + fileName);
            throw new RuntimeException(e);
        }

    }
}
