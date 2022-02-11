package janemiro;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class FilesTest {

    @Test
    void ZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files.zip");

        //Check pdf

        ZipEntry pdfEntry = zipFile.getEntry("PDF doc.pdf");
        try (InputStream stream = zipFile.getInputStream(pdfEntry)) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.text).contains("Предложение Фабрики");
        }

        //Check xls

        ZipEntry xlsEntry = zipFile.getEntry("XLSX doc.xls");
        try (InputStream stream = zipFile.getInputStream(xlsEntry)) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0)
                    .getRow(298)
                    .getCell(0)
                    .getStringCellValue())
                    .isEqualTo("Г В О З Д И ");
        }

        //Check csv

        ZipEntry csvEntry = zipFile.getEntry("CSV doc.csv");
        try (InputStream stream = zipFile.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list).hasSize(3)
                    .contains(
                            new String[]{"Cat", "Kitty"}
                    );
        }
    }
}
