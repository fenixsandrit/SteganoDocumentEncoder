package org.example;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.util.List;

import static org.example.DocumentUtils.COLOR;

public class SteganoEncoder
{
    public void encode(String text, String sourceFile, String outputFile)
    {
        String toEncode = text.replace(" ", "");
        XWPFDocument document = DocumentUtils.getDocumentFromFile(sourceFile);

        if (checkEnoughChars(toEncode, document))
        {
            try
            {
                encodeTextToXWPFDocument(toEncode, document);
                System.out.println("Текст успешно зашифрован");
            }
            catch (Throwable e)
            {
                System.out.println("Произошла ошибка во время шифрования шифрования...");
                throw new RuntimeException(e);
            }

            DocumentUtils.saveDocumentToFile(outputFile, document);
        }
        else
        {
            System.out.println("В исходном файле не достаточно символов");
        }
    }

    public boolean checkEnoughChars(String toEncode, XWPFDocument document)
    {
        int i = 0;

        for (XWPFParagraph par: document.getParagraphs())
        {
            for (XWPFRun run : par.getRuns())
            {
                String runText = run.text();

                for (int j = 0; j < runText.length(); j++)
                {
                    if (Character.toLowerCase(runText.charAt(j)) == Character.toLowerCase(toEncode.charAt(i)))
                    {
                        i++;
                        if (i == toEncode.length())
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void encodeTextToXWPFDocument(String toEncode, XWPFDocument document)
    {
        int index = 0;
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (int i = 0; i < paragraphs.size() && index < toEncode.length(); i++)
        {
            XWPFParagraph currentPar = paragraphs.get(i);
            List<XWPFRun> runs = currentPar.getRuns();

            for (int j = 0; j < runs.size() && index < toEncode.length(); j++)
            {
                XWPFRun currentRun = runs.get(j);
                String runText = currentRun.text();

                for (int k = 0; k < runText.length() && index < toEncode.length(); k++)
                {
                    if (Character.toLowerCase(runText.charAt(k)) == Character.toLowerCase(toEncode.charAt(index)))
                    {
                        if (runText.length() == 1)
                        {
                            XWPFRun run = paragraphs.get(i).insertNewRun(j);
                            run.setText(String.valueOf(runText.charAt(k)));
                            copyRun(run, currentRun);
                            run.setColor(COLOR);
                            currentPar.removeRun(j + 1);
                        }
                        else if (toEncode.charAt(index) == runText.charAt(0))
                        {
                            XWPFRun postfix = paragraphs.get(i).insertNewRun(j);
                            postfix.setText(runText.substring(k + 1));

                            XWPFRun run = paragraphs.get(i).insertNewRun(j);
                            run.setText(String.valueOf(runText.charAt(k)));

                            copyRun(run, currentRun);
                            copyRun(postfix, currentRun);
                            run.setColor(COLOR);
                            currentPar.removeRun(j + 2);
                        }
                        else if (toEncode.charAt(index) == runText.charAt(runText.length() - 1))
                        {
                            XWPFRun run = paragraphs.get(i).insertNewRun(j);
                            run.setText(String.valueOf(runText.charAt(k)));

                            XWPFRun prefix = paragraphs.get(i).insertNewRun(j);
                            prefix.setText(runText.substring(0, k));

                            copyRun(prefix, currentRun);
                            copyRun(run, currentRun);
                            run.setColor(COLOR);
                            currentPar.removeRun(j + 2);
                            j++;
                        }
                        else
                        {
                            XWPFRun postfix = paragraphs.get(i).insertNewRun(j);
                            postfix.setText(runText.substring(k + 1));

                            XWPFRun run = paragraphs.get(i).insertNewRun(j);
                            run.setText(String.valueOf(runText.charAt(k)));

                            XWPFRun prefix = paragraphs.get(i).insertNewRun(j);
                            prefix.setText(runText.substring(0, k));

                            copyRun(prefix, currentRun);
                            copyRun(run, currentRun);
                            copyRun(postfix, currentRun);
                            run.setColor(COLOR);
                            currentPar.removeRun(j + 3);
                            j++;
                        }
                        index++;
                        break;
                    }
                }
            }
        }
    }

    public void copyRun(XWPFRun copy, XWPFRun original)
    {
        CTRPr rPr = copy.getCTR().isSetRPr()
                ? copy.getCTR().getRPr()
                : copy.getCTR().addNewRPr();

        rPr.set(original.getCTR().getRPr());
    }
}
