package janemiro;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTest {
    @Test
    @DisplayName("Загрузка файла по относительному пути (рекомендуется!)")
    void filenameIsVisibleAfterUploadAction() {
        open("http://file.karelia.ru/");
        $("input[type='file']").uploadFromClasspath("img.png");
        $("#file_submit").click();
        $(".filelist-wrapper").shouldHave(text("Всего: 1 файл общим размером 0 байт\n" + "Загрузка завершена"));

    }

    @Test
    @DisplayName("Работа с PDF и проверка количества страниц")
    void pdfFileIsDownloadedAndAmountOfPagesIsChecked() throws IOException {
        open("https://digma.ru/support/cert/");
        File pdf = $(byText("№ ТС С-GB.АЛ16.В.24384")).download();
        PDF parsedPdf = new PDF(pdf);
        assertEquals(2, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Работа с PDF и проверка автора")
    void pdfFileIsDownloadedAndAuthorIsChecked() throws IOException {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdf = $(byText("PDF download")).download();
        PDF parsedPdf = new PDF(pdf);
        assertEquals("Stefan Bechtold, Sam Brannen, Johannes Link, Matthias Merdes, Marc Philipp, Juliette de Rancourt, Christian Stein",
                parsedPdf.author);
    }

    @Test
    @DisplayName("Работа с XLS и проверка значения")
    void xlsFileIsDownloadedAndValueIsChecked() throws IOException {
        open("https://ckmt.ru/price-download.html");
        File file = $$("a[href*='TehresursPrice']")
                .find(text("Скачать"))
                .download();

        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(296)
                .getCell(0)
                .getStringCellValue()
                .contains("Г В О З Д И ");

        assertTrue(checkPassed);

    }

    @Test
    @DisplayName("Работа с CSV, проверка количества строк")
    void csvFileIsDownloadedAndSmthIsChecked() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("csv.csv");
             Reader reader = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(reader);

            List<String[]> strings = csvReader.readAll();
            assertEquals(3, strings.size());

        }
    }


}
