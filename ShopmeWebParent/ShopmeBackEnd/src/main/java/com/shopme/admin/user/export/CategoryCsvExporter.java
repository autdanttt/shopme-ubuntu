package com.shopme.admin.user.export;

import com.shopme.common.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter{
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv","categories_");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category ID", "Category Name"};
        String[] fieldMapping = {"id", "name"};
        csvWriter.writeHeader(csvHeader);
        for(Category category : listCategories){
            category.setName(category.getName().replace("--", "--"));
            csvWriter.write(category, fieldMapping);
        }
        csvWriter.close();
    }
}
